package com.aige.loveproduction.ui.dialogin;

import android.content.Context;
import android.view.View;

import com.aige.loveproduction.R;
import com.aige.loveproduction.base.BaseDialog;

/**
 * 加载等待弹窗
 */
public class LoadingDialog {
    public static class Builder extends CommonDialog.Builder<Builder> {
        private OnListener mListener;
        public Builder(Context context) {
            super(context);
            setCustomView(R.layout.loading_dialog);
        }

        public Builder setListener(OnListener listener) {
            mListener = listener;
            return this;
        }
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_ui_confirm) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onConfirm(getDialog());
                }
            } else if (id == R.id.tv_ui_cancel) {
                autoDismiss();
                if (mListener != null) {
                    mListener.onCancel(getDialog());
                }
            }
        }
    }
    public interface OnListener {
        /**
         * 点击确定时回调
         */
        void onConfirm(BaseDialog dialog);

        /**
         * 点击取消时回调
         */
        default void onCancel(BaseDialog dialog) {}
    }
}
