package com.moetutu.acg12.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.ImagePagerActivity;
import com.moetutu.acg12.entity.CommentEntity;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.ItemDecorationUtils;
import com.moetutu.acg12.view.widget.BaseArrayRecyclerAdapter;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.zzhoujay.richtext.CacheType;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;
import com.zzhoujay.richtext.callback.ImageFixCallback;
import com.zzhoujay.richtext.callback.OnImageClickListener;

import java.util.List;

/**
 * Description
 * Created by chengwanying on 2017/4/1.
 * Company BeiJing kaimijiaoyu
 */

public class ArticleAdapter extends BaseArrayRecyclerAdapter<CommentEntity> {
    @Override
    public int bindView(int viewtype) {
        return R.layout.item_comment;
    }

    @Override
    public void onBindHoder(ViewHolder holder, CommentEntity commentEntity, int position) {
        if (commentEntity == null) return;
        TextView tv_name = holder.obtainView(R.id.tv_name);
        TextView tv_time = holder.obtainView(R.id.tv_time);
        TextView tv_content = holder.obtainView(R.id.tv_content);
        ImageView image_portrait = holder.obtainView(R.id.image_portrait);
        RecyclerView children_rv = holder.obtainView(R.id.children_rv);
        tv_name.setText(commentEntity.getUser().name);
        tv_time.setText(commentEntity.getHumanDate());

        RichText
                .fromHtml(commentEntity.getContent()) // 数据源
//                                .type(RichText.TYPE_MARKDOWN) // 数据格式,不设置默认是Html,使用fromMarkdown的默认是Markdown格式
                .autoFix(false) // 是否自动修复，默认true
                //.async(true)  已取消异步的接口，交给调用者自己处理
                .fix(new ImageFixCallback() {
                    @Override
                    public void onInit(ImageHolder holder) {

                    }

                    @Override
                    public void onLoading(ImageHolder holder) {
                    }

                    @Override
                    public void onSizeReady(ImageHolder holder, int width, int height) {
                        holder.setMaxHeight(height+200);
                        holder.setMaxWidth(width+200);
                    }

                    @Override
                    public void onImageReady(ImageHolder holder, int width, int height) {
                        holder.setScaleType(ImageHolder.ScaleType.FIT_CENTER);
                        holder.setHeight(height+140);
                        holder.setWidth(width+140);
                    }

                    @Override
                    public void onFailure(ImageHolder holder, Exception e) {

                    }
                }) // 设置自定义修复图片宽高
//                                .fixLink(linkFixCallback) // 设置链接自定义回调
//                                .noImage(true) // 不显示并且不加载图片
                .resetSize(true) // 默认false，是否忽略img标签中的宽高尺寸（只在img标签中存在宽高时才有效），true：忽略标签中的尺寸并触发SIZE_READY回调，false：使用img标签中的宽高尺寸，不触发SIZE_READY回调
                .clickable(true) // 是否可点击，默认只有设置了点击监听才可点击
//                .imageClick() // 设置图片点击回调
//                                .imageLongClick(onImageLongClickListener) // 设置图片长按回调
//                                .urlClick(onURLClickListener) // 设置链接点击回调
//                                .urlLongClick(onUrlLongClickListener) // 设置链接长按回调
                .placeHolder(R.mipmap.cat_loging) // 设置加载中显示的占位图
                .error(R.mipmap.cat_loging) // 设置加载失败的错误图
                .cache(CacheType.ALL) // 缓存类型，默认为Cache.LAYOUT（不缓存图片，只缓存图片大小信息和文本样式信息）
//                                .imageGetter() // 设置图片加载器，默认为DefaultImageGetter，使用okhttp实现
                .bind(holder) // 绑定richText对象到某个object上，方便后面的清理
                .into(tv_content); // 设置目标TextView

//        tv_content.setText(commentEntity.getContent());
        GlideUtils.loadUser(image_portrait.getContext(), commentEntity.getUser().avatarUrl, image_portrait);

        if (commentEntity.children == null) {
            children_rv.setVisibility(View.GONE);
        } else {
            if (commentEntity.children.size() > 0) {
                children_rv.setVisibility(View.VISIBLE);
                children_rv.setLayoutManager(new LinearLayoutManager(children_rv.getContext()));
                children_rv.addItemDecoration(ItemDecorationUtils.getCommFull05Divider(children_rv.getContext(), true));
                CommentsAdapter commentsAdapter = new CommentsAdapter();
                children_rv.setAdapter(commentsAdapter);
                commentsAdapter.bindData(true, commentEntity.children);
            } else {
                children_rv.setVisibility(View.GONE);
            }
        }
    }
}
