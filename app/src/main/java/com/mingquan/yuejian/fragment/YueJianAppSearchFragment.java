package com.mingquan.yuejian.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppUserBaseInfoAdapter;
import com.mingquan.yuejian.base.YueJianAppBaseFragment;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACUserReleationInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.utils.YueJianAppInputMethodUtils;
import com.mingquan.yuejian.utils.YueJianAppUiUtils;
import com.mingquan.yuejian.utils.YueJianAppUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 搜索
 */
public class YueJianAppSearchFragment extends YueJianAppBaseFragment {
  @BindView(R.id.et_search_input) EditText mSearchKey;
  @BindView(R.id.lv_search) ListView mLvSearch;
  @BindView(R.id.ll_title) RelativeLayout llTitle;
  @BindView(R.id.txv_msg) TextView mtxvMsg;

  private List<YueJianAppACUserReleationInfoModel> mUserList = new ArrayList<>();
  private Unbinder unbinder;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.yue_jian_app_fragment_search_index, null);
    unbinder = ButterKnife.bind(this, view);
    initView(view);
    initData();
    return view;
  }

  @Override
  public void onStart() {
    super.onStart();
    if (getActivity() != null) {
      final WindowManager.LayoutParams layoutParams = getActivity().getWindow().getAttributes();
      layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
      layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
      getActivity().getWindow().setAttributes(layoutParams);
      getActivity().getWindow().getDecorView().setSystemUiVisibility(
              View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
      }
    }
  }

  @Override
  public void initView(View view) {
    mLvSearch.setDividerHeight(0);
    mLvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (YueJianAppUtils.isFastClick()){
          return;
        }
        YueJianAppDialogHelper.showVchatOtherInfoDialogFragment(
                getActivity().getSupportFragmentManager(),
                mUserList.get(position).getUser().getUid()
        );
      }
    });
    mSearchKey.setOnKeyListener(new View.OnKeyListener() {
      @Override
      public boolean onKey(View v, int keyCode, KeyEvent event) {
        // 修改回车键功能
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          // 先隐藏键盘
          InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
          ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
              .hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                  InputMethodManager.HIDE_NOT_ALWAYS);
          //搜索
          search();
        }
        return false;
      }
    });
  }

  @Override
  public void initData() {
  }

  @OnClick({R.id.tv_search_cancel})
  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.tv_search_cancel:
        if (YueJianAppInputMethodUtils.isShowKeyboard(getActivity(),mSearchKey)) {
          YueJianAppInputMethodUtils.hideSoftKeyboard(getActivity());
        } else {
          getActivity().finish();
        }
        break;
    }
  }

  private void search() {
    if (TextUtils.isEmpty(mSearchKey.getText().toString().trim())) {
      YueJianAppAppContext.showToastAppMsg(getActivity(), mSearchKey.getHint().toString().trim());
      return;
    }
    YueJianAppInputMethodUtils.hideSoftKeyboard(getActivity());
    showWaitDialog();
    String screenKey = mSearchKey.getText().toString().trim();
    YueJianAppApiProtoHelper.sendACSearchUserReq(getActivity(), YueJianAppAppContext.getInstance().getLoginUid(),
        screenKey, new YueJianAppApiProtoHelper.ACSearchUserReqCallback() {
          @Override
          public void onError(int errCode, String errMessage) {
            hideWaitDialog();
            YueJianAppAppContext.showToastAppMsg(getActivity(), errMessage);
          }

          @Override
          public void onResponse(ArrayList<YueJianAppACUserReleationInfoModel> users) {
            hideWaitDialog();
            mUserList = users;
            if (users.isEmpty()) {
              YueJianAppUiUtils.setVisibility(mtxvMsg, View.VISIBLE);
              YueJianAppUiUtils.setVisibility(mLvSearch, View.GONE);
            } else {
              YueJianAppUiUtils.setVisibility(mtxvMsg, View.GONE);
              YueJianAppUiUtils.setVisibility(mLvSearch, View.VISIBLE);
            }
            fillUI();
          }
        });
  }

  private void fillUI() {
    if (null == mLvSearch)
      return;
    mLvSearch.setAdapter(new YueJianAppUserBaseInfoAdapter(mUserList, getActivity()));
  }

  public void onResume() {
    super.onResume();
    MobclickAgent.onPageStart(
        "搜索"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
    MobclickAgent.onResume(getActivity()); //统计时长
  }

  public void onPause() {
    super.onPause();
    MobclickAgent.onPageEnd("搜索"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
    // onPageEnd 在onPause 之前调用,因为 onPause
    // 中会保存信息。"SplashScreen"为页面名称，可自定义
    MobclickAgent.onPause(getActivity());
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbinder.unbind();
  }
}
