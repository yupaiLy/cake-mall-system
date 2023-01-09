package cn.twopair.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {
    /**
     * @Param: request
     * @Param: name
     * @Description: 根据名字获取cookie
     * @Return: javax.servlet.http.Cookie
     * @Author: 李佳骏
     * @Date: 2022/11/19 08:39
     */
    public static Cookie getCookieByName(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        }
        return null;
    }

    /**
     * @Param: request
     * @Description: 将request域中cookikes封装至Map中
     * @Return: java.util.Map<java.lang.String, javax.servlet.http.Cookie>
     * @Author: 李佳骏
     * @Date: 2022/11/19 08:39
     */
    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        HashMap<String, Cookie> cookieHashMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieHashMap.put(cookie.getName(), cookie);
            }
        }
        return cookieHashMap;
    }

    public  static  void addCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name.trim(), value.trim());
        cookie.setMaxAge(30 * 60);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public  static  void addCookie(HttpServletResponse response, String name, String value, int maxAge){
        Cookie cookie = new Cookie(name.trim(), value.trim());
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    public  static  String getCookieValueByName(HttpServletRequest request, String name){
        for (Cookie cookie : request.getCookies()) {
            if(name.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return "";
    }
}
