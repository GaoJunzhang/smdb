package com.zgj.mps.tool;

import cn.hutool.core.util.StrUtil;
import org.apache.commons.collections.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Constructor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alexis on 2019/8/27.
 */
public class CommonUtil {
    public static Map<String, Object> defaultResponse(int errCode, String errMsg) {
        Map<String, Object> res = new HashMap<>();
        res.put("errCode", errCode);
        res.put("errMsg", errMsg);
        return res;
    }

    public static String generateNewCode(int len, String code) {
        String defaultCode = "";
        for (int i = 0; i < len; i++) {
            defaultCode += "0";
        }
        if (StrUtil.isEmpty(code)) {
            return defaultCode;
        }
        String oldCode = code.substring(code.length() - len);
        boolean carry = true;
        String newCode = "";
        for (int i = len - 1; i >= 0; i--) {
            char c = oldCode.charAt(i);
            if (carry) {
                carry = false;
                if (c == 'Z') {
                    c = '0';
                    carry = true;
                } else if (c == '9') {
                    c = 'A';
                } else {
                    c += 1;
                }
            }
            newCode = c + newCode;
        }
        if (newCode.equals(defaultCode)) {
            return null;
        }
        return newCode;
    }

    public static String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            String[] ips = ip.split(",");
            if (ips != null && ips.length > 0) {
                ip = ips[0];
            }
        }
        return ip;
    }
    public static <T> List<T> castEntity(List<Object[]> list, Class<T> clazz) throws Exception {
        List<T> returnList = new ArrayList<T>();
        if(CollectionUtils.isEmpty(list)){
            return returnList;
        }
        Object[] co = list.get(0);
        Class[] c2 = new Class[co.length];
        //确定构造方法
        for (int i = 0; i < co.length; i++) {
            if(co[i]!=null){
                c2[i] = co[i].getClass();
            }else {
                c2[i]=String.class;
            }
        }
        for (Object[] o : list) {
            Constructor<T> constructor = clazz.getConstructor(c2);
            returnList.add(constructor.newInstance(o));
        }
        return returnList;
    }
}
