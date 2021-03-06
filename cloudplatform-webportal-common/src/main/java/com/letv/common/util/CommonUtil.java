/*
 * Pprun's Public Domain.
 */
package com.letv.common.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.letv.common.exception.CommonException;

/**
 * Constant used in the utility method.
 * 
 * @author <a href="mailto:quest.run@gmail.com">pprun</a>
 */
public class CommonUtil {

	public static final String UTF8 = "UTF-8";
	public static final String GB18030 = "GB18030";
	public static final String GBK = "gbk";
	public static final String TIME_ZONE_UTC = "UTC";
	/**
	 * This is quick way to round a {@link BigDecimal#setScale(2,
	 * java.math.RoundingMode.ROUND_HALF_UP)}.
	 * <p>
	 * No roundingMode parameter was provided because, I trust, if you know how
	 * to specify the {@literal roundingMode}, you might not need this utility
	 * method.
	 * </p>
	 * 
	 * @param value
	 *            the value to round.
	 * @return the rouned value by setting the scale to {@literal 2} and
	 *         {@literal RuondingMode} to {@link BigDecimal#ROUND_HALF_UP}.
	 */
	public static BigDecimal round(BigDecimal value) {
		return value.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Make an empty {@code ""} if the given String is {@code null}.
	 * 
	 * @param src
	 * @return
	 */
	public static String nullToEmpty(String src) {
		return src == null ? "" : src;
	}

	public static String join(Collection<String> collectionOfStrings,
			String delimeter) {
		StringBuilder result = new StringBuilder();
		for (String s : collectionOfStrings) {
			result.append(s);
			result.append(delimeter);
		}
		return result.substring(0, result.length() - 1);
	}

	/**
	 * 将列名转换成属性名
	 * 
	 * @param columnName
	 * @return
	 */
	public static String convertColumnNameToAttrName(String columnName) {
		StringBuffer sb = new StringBuffer();
		String[] arr = columnName.split("_");
		boolean isFirst = true;
		for (String str : arr) {
			if (isFirst) {
				sb.append(str);
				isFirst = false;
			} else {
				sb.append(toFirstLetterUpperCase(str));
			}
		}
		return sb.toString();

	}

	/**
	 * 将属性名换成列名
	 * 
	 * @param attributeName
	 * @return
	 */
	public static String convertAttrNameToColumnName(String attributeName) {
		char[] flag = attributeName.toCharArray();
		StringBuilder sum = new StringBuilder();
		for (char a : flag) {

			String str = String.valueOf(a);
			if (str.toLowerCase().charAt(0) - a == 32) {
				str = "_" + str.toLowerCase();
			}
			sum.append(str.toUpperCase());
		}
		return sum.toString();

	}

	/**
	 * 将一个字符串的首字母改成大写
	 * 
	 * @param str
	 * @return
	 */
	public static String toFirstLetterUpperCase(String str) {
		StringBuffer sb = new StringBuffer();
		sb.append(str.substring(0, 1).toUpperCase());
		sb.append(str.substring(1));
		return sb.toString();
	}

	/**
	 * @author xufei<xufei1@letv.com> 将一个 JavaBean 对象转化为一个 Map
	 * @param bean
	 *            要转化的JavaBean 对象
	 * @return 转化出来的 Map 对象
	 */
	public static <K,V> Map<K,V> convertBeanToMap(Object bean) {
		Map<K, V> map = new HashMap<K,V>();
		try {
			map = BeanUtils.describe(bean);
		} catch (IllegalAccessException e) {
			throw new CommonException("bean转换map:实例化 JavaBean 失败", e);
		} catch (InvocationTargetException e) {
			throw new CommonException("bean转换map:实例化 JavaBean 失败", e);
		} catch (NoSuchMethodException e) {
			throw new CommonException("bean转换map:调用属性的 setter 方法失败",e);
		}
		return map;
	}

	/**
	 * 将一个 Map 对象转化为一个 JavaBean
	 * @param <T>
	 * 
	 * @param type
	 *            要转化的类型
	 * @param map
	 *            包含属性值的 map
	 * @return 转化出来的 JavaBean 对象
	 * @throws IntrospectionException
	 *             如果分析类属性失败
	 * @throws IllegalAccessException
	 *             如果实例化 JavaBean 失败
	 * @throws InstantiationException
	 *             如果实例化 JavaBean 失败
	 * @throws InvocationTargetException
	 *             如果调用属性的 setter 方法失败
	 */
	public static <T> T convertMapToBean(Class<?> type, Map<?,?> map) {
		//改造传入的map,将传入map的key值转换成 驼峰式命名规则 如  TYPE_ID 转换为 typeId
		Map<String,Object> resultMap = new HashMap<String,Object>();
		Iterator<?> iterator = map.keySet().iterator();
		while (iterator.hasNext()){
			String key = (String) iterator.next();
			String newKey = "";
			if(!isPropertyName(key)){
				newKey = convertColumnNameToAttrName(key.toLowerCase());
			}else{
				newKey = key;
			}
			resultMap.put(newKey, map.get(key));
		}
		T obj = null ;
		try{
			obj = (T) type.newInstance(); // 创建 JavaBean 对象
		} catch (IllegalAccessException e) {
			throw new CommonException("反射安全异常,没有方法的访问权限!",e);
		} catch (InstantiationException e) {
			throw new CommonException("实例化异常!",e);
		}
		
		try {
			BeanUtils.populate(obj, resultMap);
		} catch (IllegalAccessException e) {
			throw new CommonException("反射时参数不合法异常!",e);
		} catch (InvocationTargetException e) {
			throw new CommonException("通过反射执行方法异常!",e);
		}
		return obj;
	}

	/**
	 * 判断给定字符串是否符合属性命名规范
	 * 首字母小写的驼峰命名规范
	 * @param str
	 * @return
	 */
	public static boolean isPropertyName(String str){
		boolean flag = true ;
		for(int i=0;i<str.length();i++){
			char ch = str.charAt(i);
			//判断字符是否为字母
			if(!Character.isLetter(ch)){
				flag = false;
				break;
			}
			//判断首字母是否小写
			if(i == 0 && Character.isUpperCase(ch)){
				flag = false;
				break;
			}
		}
		return flag;
	}
	/**
	 * 比较model是否符合conditionModel  如果model完全符合conditionModel中条件
	 * 返回true 否则返回false
	 * 如 model_A:{proId:2,proName:"xxx",type:"CCC"}  
	 *    conditionModel_B:{proName:"xxx"}
	 *   方法返回true 
	 * @param model
	 * @param conditionModel
	 * @return
	 */
	public static <T> boolean isFitModel(T model,T conditionModel){
		boolean flag = true;
		if(!model.getClass().equals(conditionModel.getClass()))
			return false;
		//首先判断两个moudel是不是同样类型,若果不是返回false
		//获得作为条件的model的所有属性字段
		Field [] propertys = conditionModel.getClass().getDeclaredFields();
		for(int i = 0;i<propertys.length;i++){
			Field field = propertys[i];
			String propertyName = field.getName().toLowerCase();
			//获得条件model的所有方法
			Method [] methods = conditionModel.getClass().getDeclaredMethods();
			for(int j = 0;j<methods.length;j++){
				Method method = methods[j];
				String methodName = method.getName().toLowerCase();
				//如果是set方法跳出本次循环
				if(methodName.startsWith("set")){
					continue;
				}
				//判断是不是get方法   boolean的get方法是以is开头,普通的方法是以get开头,而且不带参数 ,其它方法也跳出本次循环
				if((method.getParameterTypes().length == 0)&&(methodName.indexOf(propertyName) != -1) && (methodName.startsWith("get") || methodName.startsWith("is"))){
					try {
						//得到的值为null跳出本次循环,得到的值不相等也跳出本次循环
						if((null == method.invoke(model)) || (null == method.invoke(conditionModel))){
							continue;
						}else if(! method.invoke(model).equals(method.invoke(conditionModel))){
							flag = false;
							break;
						}
					} catch (IllegalArgumentException e) {
						throw new CommonException("反射时传入参数不合法!",e);
					} catch (IllegalAccessException e) {
						throw new CommonException("反射安全异常,没有方法的访问权限!",e);
					} catch (InvocationTargetException e) {
						throw new CommonException("通过反射执行方法异常!",e);
					}
				}else{
					continue;
				}
			}
			if(! flag){
				break;
			}
		}

		return flag; 
	}
}
