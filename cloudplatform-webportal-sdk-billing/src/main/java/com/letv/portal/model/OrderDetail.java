package com.letv.portal.model;

import java.sql.Date;

import com.letv.common.model.BaseModel;
import com.letv.portal.enumeration.ChargeType;

/**Program Name: OrderDetail <br>
 * Description:  订单详细<br>
 * @author name: liuhao1 <br>
 * Written Date: 2015年7月27日 <br>
 * Modified By: <br>
 * Modified Date: <br>
 */
public class OrderDetail extends BaseModel{

	private static final long serialVersionUID = -4183461858753165213L;
	
	private Long orderId;
	private ChargeType chargeType;
	private Date startTime;
	private Date endTime;
	private float price;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public ChargeType getChargeType() {
		return chargeType;
	}
	public void setChargeType(ChargeType chargeType) {
		this.chargeType = chargeType;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	
}
