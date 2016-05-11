package com.yunnong.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Integer getCurrentTimeSecond(){
		 Date date = new Date();
		 long currentMills = date.getTime();
		 return (int) (currentMills/1000);
	 }

	public static Integer getCurrentTimeMinute(){
		 Date date = new Date();
		 long currentMills = date.getTime(); 
		 return (int) (currentMills/1000)/60*60;
	 }

	/**
	 * @param num 1,2......
	 * @return 获取明天，后天....的日期（yyy-mm-dd）,0表示当天
	 */
	public static String getAnyDate(int num){
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		long time = (date.getTime() / 1000) + 60*60*24*num;	//秒
		date.setTime(time * 1000);									//毫秒
		return sf.format(date).toString();
	}
	/**
	 * @return 返回yyyy-MM-dd 格式的日期字符串
	 */
	public static String getCurrentDate(){
		Date date = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
		return matter1.format(date);
	}


	/**
	 *@param date 为空则默认是今天日期、可自行设置“2013-06-03”格式的日期
	 *@return  返回1是星期日、2是星期一、3是星期二、4是星期三、5是星期四、6是星期五、7是星期六
	 */
	public static int getDayofweek(String date){
		Calendar cal = Calendar.getInstance();
//   cal.setTime(new Date(System.currentTimeMillis()));
		if (date.equals("")) {
			cal.setTime(new Date(System.currentTimeMillis()));
		}else {
			cal.setTime(new Date(getDateByStr2(date).getTime()));
		}
		return cal.get(Calendar.DAY_OF_WEEK);
	}


	public static Date getDateByStr2(String dd)
	{
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
		Date date;
		try {
			date = sd.parse(dd);
		} catch (ParseException e) {
			date = null;
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @param o sql timestamp
	 * @return string
	 */
	public static String timestamp2string(Object o){
		Timestamp ts = (Timestamp) o;
		String tsStr = "";
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		tsStr = sdf.format(ts);
		return tsStr;
	}

}
