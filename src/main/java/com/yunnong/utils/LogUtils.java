package com.yunnong.utils;


import org.slf4j.Logger;

/**
 * Created by joker on 2016/3/23.
 */
public class LogUtils {

    public static void LogInfo(Logger logger, String message){
        logger.info("[msg]:"+message);
    }

    public static void LogError(Logger logger, String message, Throwable e){
        logger.error("[msg]:"+message, e);
    }
}
