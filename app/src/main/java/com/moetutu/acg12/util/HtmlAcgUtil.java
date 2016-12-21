package com.moetutu.acg12.util;

import android.content.Context;

import com.moetutu.acg12.R;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description
 * Created by chengwanying on 2016/12/5.
 * Company BeiJing guokeyuzhou
 */

public class HtmlAcgUtil {

    public static String stripHtml(String content) {
        // <p>段落替换为换行
        content = content.replaceAll("<p .*?>", "\r\n");
        // <br><br/>替换为换行
        content = content.replaceAll("<br\\s*/?>", "\r\n");
        // 去掉其它的<>之间的东西
        content = content.replaceAll("\\<.*?>", "");
        // 还原HTML
        // content = HTMLDecoder.decode(content);
        content = content.replaceAll("&nbsp;", "");
        content = content.replaceAll("&#8211;", "");
        content = content.replaceAll("&#8221;", "");
        content = content.replaceAll("&#amp;", "");
        return content;
    }


    public static List<String> getImgStr(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();

        //     String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址

        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile
                (regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            // Matcher m  = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img); //匹配src

            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);

            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }


    public static List<String> quChu(List<String> l) {
        for (int i = 0; i < l.size(); i++)  //外循环是循环的次数
        {
            for (int j = l.size() - 1; j > i; j--)  //内循环是 外循环一次比较的次数
            {

                if (l.get(i).equals(l.get(j))) {
                    l.remove(j);
                }

            }
        }
        return l;
    }


    public static boolean isHttps(String url){
        return url.startsWith("http");
    }


    /*
    * 获取当前主题
    * */
    public static int getAppTheme(Context context) {
        String value = SharedPreferrenceHelper.gettheme(context);
        switch (Integer.valueOf(value)) {
            case 1:
                return R.style.AppBaseTheme;
            case 2:
                return R.style.AppBaseThemeZi;
            case 3:
                return R.style.AppBaseThemeLan;
            case 4:
                return R.style.AppBaseThemeFen;
            case 5:
                return R.style.AppBaseThemeHong;
            default:
                return R.style.AppBaseTheme;
        }
    }


    /*
    * 选择主题
    * */
    public static void switchAppTheme(Context context,String index) {
        SharedPreferrenceHelper.settheme(context, index);
    }




}
