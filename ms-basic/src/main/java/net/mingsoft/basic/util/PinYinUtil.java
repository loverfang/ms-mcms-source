package net.mingsoft.basic.util;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.apache.commons.lang3.StringUtils;

/**
 * 中文转拼音工具类
 * @author by 铭飞开源团队
 * @Description TODO
 * @date 2020/6/16 14:40
 */
public class PinYinUtil {


    /**
     * 将字符串中的中文转化为拼音,其他字符不变
     *
     * @param inputString
     * @return
     */
    public static String getPingYin(String inputString)  {
        if (!StringUtils.isBlank(inputString)) {
            try {
                return  PinyinHelper.convertToPinyinString(inputString,"", PinyinFormat.WITHOUT_TONE);
            } catch (PinyinException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
