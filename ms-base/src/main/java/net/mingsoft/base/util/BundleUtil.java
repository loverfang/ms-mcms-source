package net.mingsoft.base.util;

import net.mingsoft.base.constant.Const;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 获取国际化资源工具类
 */
public class BundleUtil {

    /**
     * 获取本地化文件
     * @param key
     * @param resources 资源文件所在位置
     * @return
     */
    public static String getLocaleString(String key, String resources) {
        Locale locale = LocaleContextHolder.getLocale();
        return ResourceBundle.getBundle(resources, locale).getString(key);
    }

    /**
     * 读取国际化资源文件
     *
     * @param key
     *            键值
     * @return 返回获取到的字符串
     */
    public static String getResString(String key) {
        return Const.RESOURCES.getString(key);
    }

    /**
     * 读取国际化资源文件，优先模块对应的资源文件，如果模块资源文件找不到就会优先基础层
     *
     * @param key
     *            键值
     * @param rb
     *            模块对应资源文件
     * @return 返回获取到的字符串
     */
    public static String getResString(String key, ResourceBundle rb) {
        try {
            return rb.getString(key);
        } catch (MissingResourceException var4) {
            return Const.RESOURCES.getString(key);
        }
    }

    /**
     * 读取国际化资源文件
     *
     * @param key
     *            键值
     * @param fullStrs
     *            需填充的值
     * @return 返回获取到的字符串
     */
    public static String getResString(String key, String... fullStrs) {
        String temp = getResString(key);
        for(int i = 0; i < fullStrs.length; ++i) {
            temp = temp.replace("{" + i + "}", fullStrs[i]);
        }

        return temp;
    }

    /**
     * 读取国际化资源文件，优先模块对应的资源文件，如果模块资源文件找不到就会优先基础层
     *
     * @param key
     *            键值
     * @param rb
     *            模块对应资源文件
     * @return 返回获取到的字符串
     */
    public static String getResString(String key, ResourceBundle rb, String... fullStrs) {
        String temp = "";
        try {
            temp = rb.getString(key);
        } catch (MissingResourceException e) {
            temp = getResString(key);
        }
        for (int i = 0; i < fullStrs.length; i++) {
            temp = temp.replace("{" + i + "}", fullStrs[i]);
        }
        return temp;
    }
}
