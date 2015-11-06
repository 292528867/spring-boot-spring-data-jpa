package com.wonders.xlab.framework.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangqiang on 15/10/9.
 */
public class ValidationUtils {

    public static boolean isMobiles(String mobiles) {
        if (StringUtils.isNotBlank(mobiles)) {
            Pattern p = Pattern.compile("^((13[0-9])|(177)|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
            Matcher m = p.matcher(mobiles);
            return m.matches();
        }
        return false;
    }

}
