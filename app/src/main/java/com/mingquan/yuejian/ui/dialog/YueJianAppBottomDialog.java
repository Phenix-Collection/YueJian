package com.mingquan.yuejian.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.mingquan.yuejian.R;

/**
 * Created by Administrator on 2018/10/18
 */

public class YueJianAppBottomDialog extends Dialog {
    public YueJianAppBottomDialog(@NonNull Context context) {
        super(context);
        initLayout();
    }

    public YueJianAppBottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initLayout();
    }

    protected YueJianAppBottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initLayout() {
        Window dialogWindow = this.getWindow();
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setWindowAnimations(R.style.dialog_bottom_enter_bottom_exit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.3f;
        dialogWindow.setAttributes(lp);
    }
}
