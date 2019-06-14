package com.mingquan.yuejian.vchat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.widget.YueJianAppLoadUrlImageView;

public class YueJianAppLargerImageDialogFragment extends DialogFragment {
  private String imagePath;
  YueJianAppLoadUrlImageView mImage;

  YueJianAppLargerImageDialogFragment mFragment;
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.yue_jian_app_dialog_fragment_larger_image, null);
    mImage = (YueJianAppLoadUrlImageView) view.findViewById(R.id.image_view);
    mFragment = this;
    initView(view);
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    final WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    layoutParams.dimAmount = 0.0f; //对话框外部设置透明
    getDialog().getWindow().setAttributes(layoutParams);
    getDialog().getWindow().setBackgroundDrawable(
        new ColorDrawable(Color.TRANSPARENT)); //对话框内部的背景设为透明
  }

  public void initView(View view) {
    imagePath = getArguments().getString("imagePath");
    if (imagePath != null) {
      mImage.setImageLoadUrl(imagePath);
    }
    mImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        mFragment.dismiss();
      }
    });
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }
}
