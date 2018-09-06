package com.moetutu.acg12.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.github.florent37.arclayout.ArcLayout;
import com.moetutu.acg12.R;
import com.moetutu.acg12.activity.LoginAcitivity;
import com.moetutu.acg12.dialog.BottomActionDialog;
import com.moetutu.acg12.entity.UserEntity;
import com.moetutu.acg12.entity.eventmodel.ImageEvtivity;
import com.moetutu.acg12.entity.eventmodel.UserEvent;
import com.moetutu.acg12.presenter.UserDbPresenter;
import com.moetutu.acg12.util.GlideUtils;
import com.moetutu.acg12.util.LogUtils;
import com.moetutu.acg12.util.PicCropUtil;
import com.moetutu.acg12.util.SpUtils;
import com.moetutu.acg12.util.SystemUtils;
import com.moetutu.acg12.view.widget.BaseRecyclerAdapter;
import com.yalantis.ucrop.UCrop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Description
 * Company guokeyuzhou
 * Created by chengwanying on 16/7/4.
 * version
 * 个人中心Fragement
 */
public class MainUserFragement extends LazyBaseFragment {

    @BindView(R.id.image_portrait)
    ImageView imagePortrait;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.no_login)
    LinearLayout noLogin;
    @BindView(R.id.yes_login)
    LinearLayout yesLogin;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_miao)
    TextView tvMiao;
    @BindView(R.id.image_bg)
    ImageView imageBg;
    @BindView(R.id.arclayout)
    ArcLayout arclayout;
    private UserDbPresenter presenter;

    private static final int REQ_ALBUM = 1001;
    private static final int REQ_CMERA = 1002;

    public static final int REQUEST_CAMERA_STORAGE = 2001;

    Uri groupIconPathUri;
    private Uri mDestinationUri;
    private Uri mSingleDesUri;
    private String mSinglePath;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage_%s.jpeg";

    public static MainUserFragement newInstance() {
        MainUserFragement fragment = new MainUserFragement();
        return fragment;
    }

    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_user, container, false);
            ButterKnife.bind(this, rootView);
            initView(rootView);
        }
        if (rootView.getParent() != null) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        return rootView;
    }

    @Override
    public void onLazyLoad() {
        super.onLazyLoad();
    }

    public void initView(View view) {
        EventBus.getDefault().register(this);
        String bg = SpUtils.getInstance().getStringData("userBg","");
        if (!TextUtils.isEmpty(bg)){
            Uri bgUri = Uri.parse(bg);
            GlideUtils.loadUserBG(getContext(), bgUri, imageBg);
        }
        GlideUtils.loadUser(getContext(), R.mipmap.icon_defaulthead, imagePortrait);
        loadUser();
    }


    @Subscribe
    public void onEvent(UserEvent event) {
        if (event == null) return;
        loadUser();
    }

    @Subscribe
    public void onEvent(ImageEvtivity event) {
        if (event == null) return;
        SpUtils.getInstance().putData("userBg",event.uri.toString());
        GlideUtils.loadUserBG(getContext(), event.uri, imageBg);
    }

    public void loadUser() {
        presenter = new UserDbPresenter(getActivity());
        if (presenter == null) return;
        try {
            UserEntity userEntity = presenter.getLoginUser();
            if (userEntity == null) {
                noLogin.setVisibility(View.VISIBLE);
                yesLogin.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(userEntity.getName())) {
                    noLogin.setVisibility(View.VISIBLE);
                    yesLogin.setVisibility(View.GONE);
                } else {
                    noLogin.setVisibility(View.GONE);
                    yesLogin.setVisibility(View.VISIBLE);
                    tvMiao.setText("猫爪:" + userEntity.getPoint());
                    tvName.setText(userEntity.getName());
                    GlideUtils.loadUser(getContext(), presenter.getLoginUser().getAvatarUrl(), imagePortrait);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (presenter != null) {
            try {
                presenter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick({R.id.arclayout, R.id.tv_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arclayout:
                String[] permisions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                if (checkPermissions(permisions)) {
                    showPicMenu();
                } else {
                    requestPermissions(permisions, REQUEST_CAMERA_STORAGE);
                }
                break;
            case R.id.tv_login:
                LoginAcitivity.launch(getContext());
                break;
        }
    }

    private void showPicMenu() {
        new BottomActionDialog(
                getContext(),
                null,
                Arrays.asList("拍照", "相册"),
                new BottomActionDialog.OnActionItemClickListener() {
                    @Override
                    public void onItemClick(BottomActionDialog dialog, BottomActionDialog.ActionItemAdapter adapter, BaseRecyclerAdapter.ViewHolder holder, View view, int p) {
                        if (p == 0) {
                            PicCropUtil.cropFromCamera(getActivity());
                        } else if (p == 1) {
                            PicCropUtil.cropFromGallery(getActivity());
                        }
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    showPicMenu();
                } else {
                    showFillToast("权限被拒绝,请到设置中心打开拍照与文件读写权限!");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private Uri createRandomTempFile() {
        File file = new File(getActivity().getCacheDir(), String.format(SAMPLE_CROPPED_IMAGE_NAME, SystemClock.elapsedRealtime()));
        mSinglePath = file.getAbsolutePath();
        return Uri.fromFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode != Activity.RESULT_OK) return;
        PicCropUtil.onActivityResult(requestCode, resultCode, data, getActivity(), new PicCropUtil.CropHandler() {
            @Override
            public void handleCropResult(Uri uri, int tag) {
                LogUtils.d("---------->Uri"+uri);
            }

            @Override
            public void handleCropError(Intent data) {

            }
        });

//        switch (requestCode) {
//            case REQ_CMERA:
//                if (groupIconPathUri != null) {
//                    mDestinationUri = createRandomTempFile();
//                    UCrop.of(groupIconPathUri, mDestinationUri)
//                            .withAspectRatio(16, 9)
//                            .withMaxResultSize(800, 450)
//                            .withOptions(new UCrop.Options())
//                            .start(getActivity());
//                }
//                break;
//            case REQ_ALBUM:
//                if (data.getData() != null) {
//                    groupIconPathUri = data.getData();
//                    mDestinationUri = createRandomTempFile();
//                    UCrop.Options options = new UCrop.Options();
//                    UCrop.of(groupIconPathUri, mDestinationUri)
//                            .withAspectRatio(16, 9)
//                            .withMaxResultSize(800, 450)
//                            .withOptions(options)
//                            .start(getActivity());
//                }
//                break;
//            case UCrop.REQUEST_CROP:
//                if (mDestinationUri != null) {
//                    mSingleDesUri = mDestinationUri;
//                    dispImage();
//                }
//                break;
//            case UCrop.RESULT_ERROR:
//                handleCropError(data);
//                break;
//        }
    }


    private void dispImage() {
        if (TextUtils.isEmpty(mSinglePath)) return;
        File file = new File(mSinglePath);
        if (file == null || !file.exists()) return;

        Glide.with(getContext())
                .load(mSingleDesUri)
                .placeholder(R.mipmap.cat_loging)
                .error(R.mipmap.cat_loging)
                .into(imageBg);

//        GlideUtils.loadDetails(getContext(),mSinglePath,imageBg);
//        //去重
//        Set<String> urlSets = new HashSet<>();
//        urlSets.addAll(picAdapter.getData());
//        final ArrayList<String> data = new ArrayList<String>();
//        data.add(mSinglePath);
//        data.addAll(urlSets);
//        picAdapter.clearData();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                picAdapter.bindData(true, data);
//            }
//        }, 500);
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            showSnackBar(cropError.getMessage());
            Toast.makeText(getActivity(), cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            showFillToast("未知异常");
        }
    }
}
