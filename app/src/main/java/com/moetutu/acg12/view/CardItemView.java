package com.moetutu.acg12.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.entity.ArticleEntity;
import com.moetutu.acg12.util.GlideUtils;


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

    public ImageView imageView;
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
        imageView = (ImageView) findViewById(R.id.card_image_view);
        userNameTv = (TextView) findViewById(R.id.card_user_name);
        imageNumTv = (TextView) findViewById(R.id.card_pic_num);
        likeNumTv = (TextView) findViewById(R.id.card_like);
        commentTv = (TextView) findViewById(R.id.card_comment);
    }

    public void fillData(ArticleEntity itemData) {
//        Uri uri = Uri.parse(itemData.thumbnail.url);
        GlideUtils.loadDetails(imageView.getContext(),itemData.thumbnail.url,imageView);
//        imageView.setImageURI(uri);

        userNameTv.setText(itemData.title);
        imageNumTv.setText(itemData.date.human);
        commentTv.setText(String.valueOf(itemData.comment));
    }

}
