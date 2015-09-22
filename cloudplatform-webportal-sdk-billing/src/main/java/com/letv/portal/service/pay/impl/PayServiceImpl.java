package com.letv.portal.service.pay.impl;

import com.letv.common.exception.ValidateException;
import com.letv.common.session.Session;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.HttpClient;
import com.letv.common.util.MD5;
import com.letv.portal.constant.Constant;
import com.letv.portal.model.order.Order;
import com.letv.portal.model.subscription.Subscription;
import com.letv.portal.service.order.IOrderService;
import com.letv.portal.service.pay.IPayService;
import com.mysql.jdbc.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("payService")
public class PayServiceImpl implements IPayService {
	
	private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

	@Autowired
	IOrderService orderService;

	@Autowired(required = false)
	private SessionServiceImpl sessionService;

	@Value("${pay.callback}")
	private String PAY_CALLBACK;

	@Value("${pay.success}")
	private String PAY_SUCCESS;

	public Map<String, Object> pay(Long orderId, Map<String, Object> map,
			HttpServletResponse response) {
		Map<String, Object> ret = new HashMap<String, Object>();
		Order order = this.orderService.selectOrderById(orderId);
		if (order == null) {
			throw new ValidateException("参数未查出数据,orderId=" + orderId);
		}
		if (order.getStatus().intValue() == 1) {
			ret.put("alert", "订单状态已失效");
			ret.put("status", 1);
		} else if (order.getStatus().intValue() == 2) {
			ret.put("alert", "订单状态已支付成功，请勿重复提交");
			ret.put("status", 2);
		} else {
			String pattern = (String) map.get("pattern");
			Map<String, String> params = new HashMap<String, String>();
			double price = getValidOrderPrice(order);
			if (price == 0.0D) {
				try {
					if (updateOrderPayInfo(order, "9999", price + "", 2)) {
						response.sendRedirect(this.PAY_SUCCESS + "?id=" + order.getId());
						return ret;
					} else {
						throw new ValidateException("更新订单状态异常");
					}
				} catch (IOException e) {
					logger.error("pay inteface sendRedirect had error, ", e);
				}
			}
			String url = getParams(order.getOrderNumber(), price, pattern,this.PAY_CALLBACK,
					this.PAY_SUCCESS + "?id=" + order.getId(), order.getSubscription().getProductName(), 
					order.getSubscription().getProductName(), null, params);

			if ("1".equals(pattern)) {//支付宝方法
				try {
					response.sendRedirect(getPayUrl(url, params));
				} catch (IOException e) {
					logger.error("pay inteface sendRedirect had error, ", e);
				}
			} else if ("2".equals(pattern)) {//微信支付
				String str = HttpClient.get(getPayUrl(url, params), 2000, 2000);
				ret = transResult(str);
				if (!updateOrderPayInfo(order, (String) ret.get("ordernumber"),
						(String) ret.get("price"), null)) {
					ret.put("alert", "微信方式支付异常");
				}
			}
		}
		return ret;
	}

	private String getParams(String number, double price, String pattern,
			String backUrl, String frontUrl, String productName,
			String productDesc, String defaultBank, Map<String, String> params) {
		try {
			params.put("corderid", number);
			params.put("userid", this.sessionService.getSession().getUserId()+ "");
			params.put("username", URLEncoder.encode(this.sessionService.getSession().getUserName(), "UTF-8"));
			params.put("companyid", "4");
			params.put("deptid", "112");
			params.put("productid", "0");
			params.put("backurl", backUrl);
			params.put("fronturl", frontUrl);
			params.put("price", price + "");
			params.put("buyType", "0");
			params.put("pid", "0");
			params.put("chargetype", "1");
			params.put("productname", URLEncoder.encode(productName, "UTF-8"));
			params.put("productdesc", URLEncoder.encode(productDesc, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			logger.error("payService getParams had error :", e);
		}

		if ("1".equals(pattern)) {//支付宝支付
			params.put("defaultbank", defaultBank);
			return Constant.PAY_URL;
		}
		if ("2".equals(pattern)) {//微信支付
			return Constant.WX_URL;
		}
		return null;
	}

	private String getPayUrl(String url, Map<String, String> params) {
		StringBuffer sb = new StringBuffer();
		sb.append(url);
		sb.append("?");
		for (Map.Entry<String, String> entry : params.entrySet()) {
			sb.append((String) entry.getKey());
			sb.append("=");
			sb.append((String) entry.getValue());
			sb.append("&");
		}
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> transResult(String result) {
		ObjectMapper resultMapper = new ObjectMapper();
		Map<String, Object> jsonResult = new HashMap<String, Object>();
		if (StringUtils.isNullOrEmpty(result))
			return jsonResult;
		try {
			jsonResult = (Map<String, Object>) resultMapper.readValue(result, Map.class);
		} catch (Exception e) {
			logger.error("transResult had error:", e);
		}
		return jsonResult;
	}

	public boolean callback(Map<String, Object> map) {
		//①验证必须字段
		if ((map.get("corderid") == null) || (map.get("stat") == null)
				|| (map.get("money") == null)
				|| (map.get("ordernumber") == null)) {
			return false;
		}

		Order order = this.orderService.selectOrderByOrderNumber((String) map.get("corderid"));

		//②支付成功已通知服务器并且order的状态为已支付
		if (Integer.parseInt((String) map.get("stat")) == 1 && order.getStatus() == 2) {
			return true;
		}
		
		//③验证请求是否合法，规则：corderid=xxx&key&money=xxx&companyid=4
		String sign = getSign("4", Constant.SIGN_KEY, order.getOrderNumber(), getValidOrderPrice(order) + "");
		if (sign != null && sign.equals(map.get("sign"))) {
			return updateOrderPayInfo(order, (String) map.get("ordernumber"),
					(String) map.get("money"), 2);
		}
		return false;
	}

	/**
	  * @Title: getValidOrderPrice
	  * @Description: 获取有效的价格(优先使用折扣价)
	  * @param order
	  * @return double   
	  * @throws 
	  * @author lisuxiao
	  * @date 2015年9月17日 下午4:23:46
	  */
	private double getValidOrderPrice(Order order) {
		if ((order.getDiscountPrice() == null)
				|| (order.getDiscountPrice() < 0.0D)) {//使用原价
			return order.getPrice();
		}
		return order.getDiscountPrice();//使用折扣价
	}

	private boolean updateOrderPayInfo(Order order, String orderNumber, String price, Integer status) {
		if (getValidOrderPrice(order) == Double.parseDouble(price)) {
			Order o = new Order();
			o.setId(order.getId());
			o.setStatus(status);
			o.setUpdateTime(new Timestamp(new Date().getTime()));
			o.setPayNumber(orderNumber);
			this.orderService.updateBySelective(o);
			return true;
		}
		return false;
	}

	public Map<String, Object> queryState(Long orderId) {
		Order order = this.orderService.selectOrderById(orderId);
		if (order==null || order.getPayNumber() == null) {
			return null;
		}
		Map<String, String> params = new HashMap<String, String>();
		params.put("companyid", "4");
		params.put("corderid", order.getPayNumber());
		params.put("sign", getSign("4", Constant.SIGN_KEY, order.getPayNumber(), null));
		String url = getPayUrl(Constant.QUERY_URL, params);
		String ret = HttpClient.get(url, 2000, 2000);
		Map<String, Object> map = transResult(ret);

		if ((map.get("status") != null) && (Integer) map.get("status")==1) {//支付成功
			if (updateOrderPayInfo(order, (String) map.get("ordernumber"), (String) map.get("money"), 2)) {
				return map;
			}
			return null;
		}

		return map;
	}

	private String getSign(String companyId, String key, String payNumber, String money) {
		if ((companyId == null) || (key == null) || (payNumber == null)) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("corderid=");
		sb.append(payNumber);
		sb.append("&");
		sb.append(key);
		if (money != null) {
			sb.append("&money=");
			sb.append(money);
		}
		sb.append("&companyid=");
		sb.append(companyId);
		MD5 m = new MD5();
		return m.getMD5ofStr(sb.toString()).toLowerCase();
	}

}