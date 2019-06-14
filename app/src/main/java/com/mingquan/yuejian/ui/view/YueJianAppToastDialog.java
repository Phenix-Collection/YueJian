package com.mingquan.yuejian.ui.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mingquan.yuejian.R;

public class YueJianAppToastDialog extends Dialog {
  private static final int DISMISS = 1;
  private static final int DEFAULT_TIME = 3000;
  private int mTimeLong;
  private boolean cancelable = true;
  private TextView tv;
  private String text;

  @SuppressLint("HandlerLeak")
  private Handler mHandler = new Handler() {
    public void handleMessage(android.os.Message msg) {
      switch (msg.what) {
        case DISMISS:
          dismiss();
          break;

        default:
          break;
      }
    };
  };
  private ProgressBar pb_collect;

  public YueJianAppToastDialog(Context context) {
    super(context, R.style.Dialog_bocop);
    init();
  }

  public void setCustomView(int resId) {
    mTimeLong = DEFAULT_TIME;
    View contentView = View.inflate(getContext(), resId, null);
    setContentView(contentView);
    contentView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (cancelable) {
          dismiss();
        }
      }
    });
    tv = (TextView) findViewById(R.id.tv);
    pb_collect = (ProgressBar) findViewById(R.id.pb_collect);
    tv.setText(text);
    getWindow().setWindowAnimations(R.style.alpha_in);
  }

  private void init() {
    mTimeLong = DEFAULT_TIME;
    View contentView = View.inflate(getContext(), R.layout.yue_jian_app_dialog_toast, null);
    setContentView(contentView);

    contentView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (cancelable) {
          dismiss();
        }
      }
    });
    tv = (TextView) findViewById(R.id.tv);
    pb_collect = (ProgressBar) findViewById(R.id.pb_collect);
    tv.setText(text);
    getWindow().setWindowAnimations(R.style.alpha_in);
  }

  @Override
  public void show() {
    pb_collect.setVisibility(View.GONE);
    mHandler.sendEmptyMessageDelayed(DISMISS, mTimeLong);
    super.show();
  }

  public void setTimeLong(int timeLong) {
    if (timeLong < 0) {
      return;
    }
    this.mTimeLong = timeLong;
  }

  public void showCrollect(String text) {
    pb_collect.setVisibility(View.GONE);
    tv.setText(text);
    super.show();
  }

  @Override
  public void dismiss() {
    super.dismiss();
  }

  @Override
  public void setTitle(CharSequence title) {
    tv.setText(title);
  }

  @Override
  public void setTitle(int titleId) {
    setTitle(getContext().getString(titleId));
  }
}
