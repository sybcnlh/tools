package cn.gjing.tools.common.util;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Gjing
 * param util
 **/
public final class ParamUtils {
    private ParamUtils() {

    }

    /**
     * 检查参数是否为空
     *
     * @param param 参数
     * @param <T> 泛型参数
     * @return True 为空,false不为空
     */
    public static <T> boolean isEmpty(T param) {
        if (param == null || "".equals(param)) {
            return true;
        } else if (param instanceof Collection) {
            return ((Collection<?>) param).isEmpty();
        } else if (param instanceof Map) {
            return ((Map<?, ?>) param).isEmpty();
        } else if (param.getClass().isArray()) {
            return Array.getLength(param) == 0;
        }
        return false;
    }

    /**
     * 不为空返回原对象值,为空跑出NPE
     *
     * @param param   参数
     * @param <T> 泛型
     * @return 原参数
     */
    public static <T> T requireNotNull(T param) {
        if (param == null) {
            throw new NullPointerException("Parameter cannot be null");
        }
        return param;
    }

    /**
     * 不为空返回原对象值,为空跑出NPE
     *
     * @param param     参数
     * @param error 错误信息
     * @param <T>   泛型
     * @return 原参数
     */
    public static <T> T requireNotNull(T param, String error) {
        if (param == null) {
            throw new NullPointerException(error);
        }
        return param;
    }

    /**
     * 判断集合里是否含有空值
     *
     * @param list 参数集合
     * @param <T>  泛型
     * @return true为包含
     */
    public static <T> boolean listHasEmpty(Collection<? extends T> list) {
        return list.stream().anyMatch(ParamUtils::isEmpty);
    }

    /**
     * 检查多参数里面是否有空值
     *
     * @param params 多个参数集合
     * @return true为包括, false不包括
     */
    public static boolean multiEmpty(Object... params) {
        return Arrays.stream(params).anyMatch(ParamUtils::isEmpty);
    }

    /**
     * 参数不为空或者size=0或者isEmpty
     *
     * @param param 参数
     * @param <T> 泛型参数
     * @return true为不含有, false为含有
     */
    public static <T> boolean isNotEmpty(T param) {
        return !isEmpty(param);
    }

    /**
     * 判断两个参数是否相等
     *
     * @param param1 参数1
     * @param param2 参数2
     * @return true为相等
     */
    public static boolean equals(Object param1, Object param2) {
        return param1 == param2 || (requireNotNull(param1).equals(param2));
    }

    /**
     * 去除字符串的空格
     *
     * @param str 字符串
     * @return 去除后
     */
    public static String trim(String str) {
        return isEmpty(str) ? null : str.trim();
    }

    /**
     * 去除集合中的空元素
     *
     * @param list 集合
     * @return 不包含空值的集合
     */
    public static List<String> trim(List<String> list) {
        List<String> listNonNull = list.stream().filter(ParamUtils::isNotEmpty).collect(Collectors.toList());
        return listNonNull.size() <= 0 ? null : listNonNull.stream().map(ParamUtils::trim).collect(Collectors.toList());
    }

    /**
     * 移除字符串的符号
     *
     * @param str    字符串
     * @param symbol 符号
     * @return 移除后
     */
    public static String removeSymbol(String str, String symbol) {
        if (isEmpty(str)) {
            return null;
        } else {
            str = removeStartSymbol(str, symbol);
            return removeEndSymbol(str, symbol);
        }
    }

    /**
     * 移除字符串开始的符号
     *
     * @param str    字符串
     * @param symbol 符号
     * @return 移除后的
     */
    public static String removeStartSymbol(String str, String symbol) {
        int strLen;
        if (isNotEmpty(str) && (strLen = str.length()) != 0) {
            int start = 0;
            if (isEmpty(symbol)) {
                return trim(str);
            } else {
                while (start != strLen && symbol.indexOf(str.charAt(start)) != -1) {
                    start++;
                }
            }
            return str.substring(start);
        }
        return trim(str);
    }

    /**
     * 移除字符串末尾的符号
     *
     * @param str    需要处理的字符串
     * @param symbol symbol 符号
     * @return 移除后的文本
     */
    public static String removeEndSymbol(String str, String symbol) {
        int end;
        if (isNotEmpty(str) && (end = str.length()) != 0) {
            if (isEmpty(symbol)) {
                return trim(str);
            }
            while (end != 0 && symbol.indexOf(str.charAt(end - 1)) != -1) {
                end--;
            }
            return str.substring(0, end);
        }
        return trim(str);
    }

    /**
     * 字符串根据符号截取
     *
     * @param str    需要截取的字符串
     * @param symbol 符号
     * @return 截取完的文本数组
     */
    public static String[] split(String str, String symbol) {
        if (isEmpty(str) || symbol.length() != 1) {
            return new String[]{};
        } else {
            List<String> list = new ArrayList<>();
            int i = 0;
            int start = 0;
            while (i < str.length()) {
                if (String.valueOf(str.charAt(i)).equals(symbol)) {
                    list.add(str.substring(start, i));
                    i++;
                    start = i;
                } else {
                    i++;
                }
            }
            list.add(str.substring(start));
            return list.toArray(new String[0]);
        }
    }

    /**
     * 参数转map
     *
     * @param paramStr 参数字符串
     * @return map
     */
    public static Map<String, String> paramToMap(String paramStr) {
        if (paramStr == null) {
            return new HashMap<>(16);
        }
        String[] params = paramStr.split("&");
        Map<String, String> resMap = new HashMap<>(16);
        for (String s : params) {
            String[] param = s.split("=");
            if (param.length >= 2) {
                String key = param[0];
                StringBuilder value = new StringBuilder(param[1]);
                for (int j = 2; j < param.length; j++) {
                    value.append("=").append(param[j]);
                }
                resMap.put(key, value.toString());
            }
        }
        return resMap;
    }

    /**
     * 删除字符串里的符号
     *
     * @param str    字符串
     * @param symbol 符号,仅可使用一个符号
     * @return 删除符号后的字符串
     */
    public static String removeAllSymbol(String str, String symbol) {
        if (isEmpty(str) || symbol.length() > 1) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        String[] strings = split(str, symbol);
        if (isNotEmpty(strings)) {
            for (String s : strings) {
                builder.append(s);
            }
            return builder.toString();
        }
        return null;
    }

    /**
     * 判断数组里是否包含指定值
     *
     * @param arr 目标数组
     * @param val 值
     * @return 返回true为包含该值
     */
    public static boolean contains(String[] arr, String val) {
        if (arr == null) {
            return false;
        }
        for (String s : arr) {
            if (equals(s, val)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证是不是合法email
     *
     * @param email 验证的email
     * @return true/false
     */
    public static boolean isEmail(String email) {
        String regex = "^([a-z0-9_.-]+)@([\\da-z.-]+)\\.([a-z.]{2,6})$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    /**
     * 验证是不是合法的手机号
     *
     * @param phone 验证的手机号
     * @return boolean
     */
    public static boolean isMobileNumber(String phone) {
        String regex = "^1([3-8])\\d{9}$";
        return Pattern.compile(regex).matcher(phone).matches();
    }

    /**
     * 验证是不是合法的固话
     *
     * @param tel 验证的固话
     * @return boolean
     */
    public static boolean isTelPhone(String tel) {
        String regex = "^(0[0-9]{2,3}-)?([2-9][0-9]{6,7})+(-[0-9]{1,4})?$";
        return Pattern.compile(regex).matcher(tel).matches();
    }

    /**
     * 验证是不是合法的邮编
     *
     * @param postCode 验证的邮编
     * @return boolean
     */
    public static boolean isPostCode(String postCode) {
        String regex = "^\\d{6}$";
        return Pattern.compile(regex).matcher(postCode).matches();
    }

    /**
     * Whether it's numeric
     *
     * @param param param
     * @return true or false
     */
    public static boolean isNumber(String param) {
        String regex = "^(-?\\d+)(\\.\\d+)?$";
        return Pattern.compile(regex).matcher(param).matches();
    }
}
