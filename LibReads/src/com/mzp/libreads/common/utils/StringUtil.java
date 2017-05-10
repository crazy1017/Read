package com.mzp.libreads.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mzp.libreads.common.log.OperLog;

import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

// TODO: Auto-generated Javadoc
/**
 * 字符串辅助类.
 */
public final class StringUtil {

    /** The Constant TAG. */
    private static final String TAG = StringUtil.class.getName();

    /**
     * Checks if is number.
     * 
     * @param number
     *            the number
     * @return true, if is number
     */
    public static boolean isNumber(String number) {

        if (number == null || number.trim().equals("")) {
            return false;
        }

        String strPattern = "^\\d+$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(number);
        return m.matches();
    }

    /**
     * 字符串转int
     * 
     * @param number
     *            the number
     * @param defaultInt
     *            the default int
     * @return the int
     */
    public static int string2Int(String number, int defaultInt) {
        String strPattern = "^\\d+$";
        if (number == null || number.trim().equals("") || !number.matches(strPattern)) {
            return defaultInt;
        }

        int result = Integer.valueOf(number);
        return result;
    }

    /**
     * Gets the last index string.
     * 
     * @param flag
     *            the flag
     * @param content
     *            the content
     * @return the last index string
     */
    public static String getLastIndexString(String flag, String content) {
        int i = content.lastIndexOf(flag);
        return content.substring(i);

    }

    /**
     * Gets the uUID.
     * 
     * @return the uUID
     */
    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * Checks if is ip.
     * 
     * @param strIp
     *            the str ip
     * @return true, if is ip
     */
    public static boolean isIp(String strIp) {
        if (strIp == null || strIp.trim().equals("")) {
            return false;
        }
        String strPattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)"
                + "\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strIp);
        return m.matches();
    }

    /**
     * 检测字符串是否为空.
     * 
     * @param str
     *            the str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.replace(" ", "").length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 获得后缀
     * 
     * @param flag
     *            the flag
     * @param content
     *            the content
     * @return the last index string
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 
     * 检查是否分享类型
     * 
     * @return
     */
    public static boolean checkShareType(String content, Pattern url_pattern) {

        boolean checkurl = false;
        if (StringUtil.isEmpty(content)) {
            return checkurl;
        }
        String infolist[] = content.split(" ");
        if (infolist.length == 3) {
            Matcher matchr = url_pattern.matcher(infolist[1]);

            if (!matchr.find()) {
                return checkurl;
            }
            matchr = url_pattern.matcher(infolist[2]);
            if (!matchr.find()) {
                return checkurl;
            }
            checkurl = true;
            return checkurl;
        }

        return checkurl;
    }

    /**
     * 
     * 检查是否聚吧类型
     * 
     * @return
     */
    public static boolean checkPartiesType(String content, Pattern url_pattern) {

        boolean checkurl = false;
        if (StringUtil.isEmpty(content)) {
            return checkurl;
        }
        String infolist[] = content.split(" ");
        if (infolist.length == 4) {
            Matcher matchr = url_pattern.matcher(infolist[1]);

            if (!matchr.find()) {
                return checkurl;
            }
            matchr = url_pattern.matcher(infolist[2]);
            if (!matchr.find()) {
                return checkurl;
            }
            checkurl = true;
            return checkurl;
        }

        return checkurl;
    }

    /**
     * 返回字符串长度，后面需要判断ASCLL码.
     * 
     * @param input
     *            the input
     * @return the int
     */
    public static int strLength(String input) {
        if (!isEmpty(input)) {

            int length = 0;
            for (int i = 0; i < input.length(); i++) {
                int ascii = Character.codePointAt(input, i);
                if (ascii >= 0 && ascii <= 255)
                    length++;
                else
                    length += 3;

            }
            return length;
        }
        return 0;
    }

    /**
     * 返回 UTF-8编码的字符串长度.
     * 
     * @param input
     *            the input
     * @return the int
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException
     */
    public static int length(String input) throws UnsupportedEncodingException {
        if (input == null) {
            return 0;
        }
        return input.getBytes("UTF-8").length;
    }

    /**
     * Location url.
     * 
     * @param Longitude
     *            the longitude
     * @param Latitude
     *            the latitude
     * @return the string
     */
    public static String locationUrl(String Longitude, String Latitude) {

        return baselocationUrl(Longitude, Latitude, "150", "120");
    }

    /**
     * Location url.
     * 
     * @param Longitude
     *            the longitude
     * @param Latitude
     *            the latitude
     * @return the string
     */
    public static String baselocationUrl(String Longitude, String Latitude, String width, String height) {
        String url = "http://api.map.baidu.com/staticimage?width=" + width + "&height=" + height + "&markers="
                + Longitude + "," + Latitude + "&zoom=17&markerStyles=m,,0xff0000";
        return url;
    }

    /**
     * Query replace all. 查询时，对查询条件的字符串进行特殊字符替换操作
     * 
     * @param str
     *            the str
     * @return the string
     */
    public static String queryReplaceAll(String str) {
        str = str.replace("?", "\\?").replace("!", "\\!").replace("+", "\\+").replace("^", "\\^").replace("$", "\\$")
                .replace("*", "\\*").replace(".", "\\.").replace("{", "\\{").replace("}", "\\}").replace("(", "\\(")
                .replace(")", "\\)").replace("[", "\\[").replace("]", "\\]").replace("|", "\\|").replace("-", "\\-")
                .replace("=", "\\=").replace("\n", "");
        return str;
    }

    /**
     * 对超过规定的最大字符长度的字符串进行裁剪.
     * 
     * @param editText
     *            the edit text
     * @param oldStr
     *            the old str
     * @param maxLen
     *            the max len
     */
    public static void maxLengthWatcher(EditText editText, String oldStr, int maxLen) {
        Editable editable = editText.getText();
        String inputStr = editText.getText().toString();
        int len = 0;
        try {
            len = StringUtil.length(editable.toString());
        } catch (UnsupportedEncodingException e) {
            OperLog.error(TAG, "StringUtil.length() Exception =" + e);
        }
        if (len > maxLen) {
            String tempStr = "";
            boolean flag = true;

            // 取规定的最大字符长度的字符串
            while (flag) {
                tempStr = inputStr.substring(0, oldStr.length() + 1);
                try {
                    len = StringUtil.length(tempStr.toString());
                } catch (UnsupportedEncodingException e) {
                    OperLog.error(TAG, "StringUtil.length() Exception =" + e);
                }
                if (len <= maxLen) {
                    oldStr = tempStr;
                } else {
                    flag = false;
                }
            }

            int selEndIndex = Selection.getSelectionEnd(editable);

            editText.setText(oldStr);
            editable = editText.getText();

            // 新字符串的长度
            int newLen = editable.length();
            // 旧光标位置超过字符串长度
            if (selEndIndex > newLen) {
                selEndIndex = editable.length();
            }
            // 设置新光标所在的位置
            Selection.setSelection(editable, selEndIndex);
        }
    }
}
