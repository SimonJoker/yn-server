package com.yunnong.utils;

import java.util.Date;

/**
* <p>Title: UidUtil</p>
* <p>Description: 生成唯一的uid</p>
* <p>Company: LTGames</p> 
* @author xjoker
* @date 2015年7月15日
*/
public class UidUtils {

	private static Date date = new Date();
	private static StringBuilder buf = new StringBuilder();
	private static int seq = 0;
	private static final int ROTATION = 9999;
	private static final int GARDEN_ROTATION = 99;
	private static final int GOODS_ROTATION = 999;
	
	public static synchronized long next(){
		if (seq > ROTATION) 
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%2$04d", date, seq++);
		return Long.parseLong(str);
	}
	
	public static synchronized long chUIDnext(){
		if (seq > ROTATION) 
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%2$03d", date, seq++);
		String uid = new StringBuilder("1").append(str).toString();
		return Long.parseLong(uid);
	}

	public static synchronized long chPIdNext(){
		if (seq > GARDEN_ROTATION)
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%2$03d", date, seq++);
		String pid = new StringBuilder("2").append(str).toString();
		return Long.parseLong(pid);
	}

	public static synchronized long chOIdNext(){
		if (seq > GOODS_ROTATION)
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%2$03d", date, seq++);
		String gid = new StringBuilder("3").append(str).toString();
		return Long.parseLong(gid);
	}

	public static synchronized long chTIdNext(){
		if (seq > GOODS_ROTATION)
			seq = 0;
		buf.delete(0, buf.length());
		date.setTime(System.currentTimeMillis());
		String str = String.format("%1$tY%1$tm%1$td%1$tk%1$tM%2$03d", date, seq++);
		String lid = new StringBuilder("4").append(str).toString();
		return Long.parseLong(lid);
	}

	public static synchronized long chCommentIdNext(){
		if (seq > GOODS_ROTATION)
			seq = 0;
		buf.delete(0, buf.length());
		String lid = new StringBuilder("5").append(System.currentTimeMillis()).append(seq++).toString();
		return Long.parseLong(lid);
	}

}
