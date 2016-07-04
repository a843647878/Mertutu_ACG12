package com.moetutu.acg12.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.TestMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 卡片View项
 * @author xmuSistone
 */
@SuppressLint("NewApi")
public class CardItemView extends LinearLayout {

    public SimpleDraweeView imageView;
    private TextView userNameTv;
    private TextView imageNumTv;
    private TextView likeNumTv;
    private TextView commentTv;

    public CardItemView(Context context) {
        this(context, null);
    }

    public CardItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate(context, R.layout.card_item, this);
        imageView = (SimpleDraweeView) findViewById(R.id.card_image_view);
        userNameTv = (TextView) findViewById(R.id.card_user_name);
        imageNumTv = (TextView) findViewById(R.id.card_pic_num);
        likeNumTv = (TextView) findViewById(R.id.card_like);
        commentTv = (TextView) findViewById(R.id.card_comment);
    }

    public void fillData(TestMode.PostsBean itemData) {
        Uri uri = Uri.parse(itemData.getThumbnail().getMedium());
        imageView.setImageURI(uri);

        userNameTv.setText(itemData.getPost_title());
        imageNumTv.setText(quChu(getImgStr(itemData.getPost_content())).size() + "");
        commentTv.setText(itemData.getComment_count());
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


    public List<String> quChu(List<String> l) {
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
}
