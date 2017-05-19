package com.moetutu.acg12.fragment;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.moetutu.acg12.R;
import com.moetutu.acg12.util.SnackbarUtils;
import com.moetutu.acg12.util.ToastUtils;


/**
 * Description
 * Company Beijing guokeyuzhou
 * author  youxuan  E-mail:xuanyouwu@163.com
 * date createTime：16/7/5
 * version
 */
public class BasePermissionFragment extends BaseFragment {
    private AlertDialog mAlertDialog;

    @Override
    public void onStop() {
        super.onStop();
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
    }


    /**
     * 检查权限
     *
     * @param permission
     * @return
     */
    protected boolean checkPermission(String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)// Permission was added in API Level 16
        {
            return ContextCompat.checkSelfPermission(getActivity(), permission)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    /**
     * 检查权限
     *
     * @param permissions
     * @return
     */
    protected boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)// Permission was added in API Level 16
        {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(getActivity(), permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Requests given permission.
     * If the permission has been denied previously, a Dialog will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    protected void requestPermission(final String permission, String rationale, final int requestCode) {
        if (shouldShowRequestPermissionRationale(permission)) {
            showAlertDialog(getString(R.string.permission_title_rationale), rationale,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    }, getString(R.string.label_ok), null, getString(R.string.label_cancel));
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }


    /**
     * This method shows dialog with given title & message.
     * Also there is an option to pass onClickListener for positive & negative button.
     *
     * @param title                         - dialog title
     * @param message                       - dialog message
     * @param onPositiveButtonClickListener - listener for positive button
     * @param positiveText                  - positive button text
     * @param onNegativeButtonClickListener - listener for negative button
     * @param negativeText                  - negative button text
     */
    protected void showAlertDialog(@Nullable String title, @Nullable String message,
                                   @Nullable DialogInterface.OnClickListener onPositiveButtonClickListener,
                                   @NonNull String positiveText,
                                   @Nullable DialogInterface.OnClickListener onNegativeButtonClickListener,
                                   @NonNull String negativeText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveText, onPositiveButtonClickListener);
        builder.setNegativeButton(negativeText, onNegativeButtonClickListener);
        mAlertDialog = builder.show();
    }

    public void showSnackBar(String msg) {
        SnackbarUtils.showSnack(getActivity(), msg);
    }

    public void showLongSnackBar(String msg) {
        SnackbarUtils.showLongSnack(getActivity(), msg);
    }

    public void showFillToast(CharSequence msg) {
        ToastUtils.showFillToast(msg);
    }

    public void showRoundRectToast(CharSequence msg) {
        ToastUtils.showRoundRectToast(msg);
    }

}
