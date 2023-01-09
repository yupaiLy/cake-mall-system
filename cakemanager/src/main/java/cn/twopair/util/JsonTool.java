package cn.twopair.util;

import cn.twopair.result.Code;
import cn.twopair.result.Result;
import com.alibaba.fastjson2.JSONObject;

public class JsonTool {
    public static String createJsonData(Result res) {
        JSONObject ret = new JSONObject();
        ret.put("code", res.getCode());
        ret.put("data_num", res.getData_num());
        ret.put("data", res.getData());


        return ret.toString();
    }
}
