package com.yunnong.utils;

import net.sf.json.JSONObject;

/**
 * Created by joker on 2016/3/22.
 */
public class ErrorsCallbackUtils {
    private final static int REQUEST_BODY_EMPTY = 406;

    public static String requestbodyEmpty(String api){
        JSONObject callback = new JSONObject();
        callback.put("api", api);
        callback.put("result", REQUEST_BODY_EMPTY);
        return callback.toString();
    }
}
