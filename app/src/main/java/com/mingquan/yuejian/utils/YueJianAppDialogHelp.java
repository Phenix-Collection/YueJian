package com.mingquan.yuejian.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.adapter.YueJianAppGiftGridViewAdapter;
import com.mingquan.yuejian.adapter.YueJianAppViewPageGridViewAdapter;
import com.mingquan.yuejian.auth.YueJianAppWheelAreaPicker;
import com.mingquan.yuejian.interf.YueJianAppIBottomDialog;
import com.mingquan.yuejian.interf.YueJianAppICallBack;
import com.mingquan.yuejian.interf.YueJianAppINomalDialog;
import com.mingquan.yuejian.proto.YueJianAppApiProtoHelper;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPrivateInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserPublicInfoModel;
import com.mingquan.yuejian.proto.model.YueJianAppACUserTagMetaDataModel;
import com.mingquan.yuejian.proto.model.YueJianAppACVideoInfoModel;
import com.mingquan.yuejian.ui.dialog.YueJianAppBottomDialog;
import com.mingquan.yuejian.ui.dialog.YueJianAppDialogHelper;
import com.mingquan.yuejian.ui.view.YueJianAppMyRatingBar;
import com.mingquan.yuejian.vchat.YueJianAppLabelGroup;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 对话框辅助类
 */
public class YueJianAppDialogHelp {
    /***
     * 获取一个dialog
     *
     * @param context
     * @return
     */
    public static AlertDialog.Builder getDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        return builder;
    }

    /***
     * 获取一个耗时等待对话框
     *
     * @param context
     * @param message
     * @return
     */
    public static ProgressDialog getWaitDialog(Context context, String message) {
        ProgressDialog waitDialog =
                new ProgressDialog(context, ProgressDialog.THEME_DEVICE_DEFAULT_DARK);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /***
     * 获取一个信息对话框，注意需要自己手动调用show方法显示
     *
     * @param context
     * @param message
     * @param onClickListener
     * @return
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String message,
                                                       android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onClickListener);
        return builder;
    }

    public static AlertDialog.Builder getMessageDialog(Context context, String message) {
        return getMessageDialog(context, message, null);
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message,
                                                       android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(Html.fromHtml(message));
        builder.setPositiveButton("确定", onClickListener);
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getConfirmDialog(Context context, String message,
                                                       android.content.DialogInterface.OnClickListener onOkClickListener,
                                                       android.content.DialogInterface.OnClickListener onCancleClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setMessage(message);
        builder.setPositiveButton("确定", onOkClickListener);
        builder.setNegativeButton("取消", onCancleClickListener);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String title, String[] arrays,
                                                      android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setPositiveButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays,
                                                      android.content.DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, "", arrays, onClickListener);
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title,
                                                            String[] arrays, int selectIndex,
                                                            android.content.DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        builder.setNegativeButton("取消", null);
        return builder;
    }

    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays,
                                                            int selectIndex, android.content.DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, "", arrays, selectIndex, onClickListener);
    }

    public static void showDialog(
            LayoutInflater inflater, Context context, String msg, final YueJianAppINomalDialog dialogInface) {
        View v = inflater.inflate(R.layout.yue_jian_app_dialog_show_onback_view, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(v);
        dialog.show();
        TextView mContent = (TextView) v.findViewById(R.id.tv_dialog_msg);
        Button mCancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
        Button mDetermine = (Button) v.findViewById(R.id.btn_dialog_determine);
        mContent.setText(msg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.cancelDialog(v, dialog);
            }
        });
        mDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.determineDialog(v, dialog);
            }
        });
    }

    public static void showSingleConfirmDialog(
            LayoutInflater inflater, Context context, String msg, final YueJianAppINomalDialog dialogInface) {
        View v = inflater.inflate(R.layout.yue_jian_app_dialog_single_confirm, null);
        final Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(v);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        TextView mContent = (TextView) v.findViewById(R.id.tv_dialog_msg);
        Button mCancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
        Button mDetermine = (Button) v.findViewById(R.id.btn_dialog_determine);
        mContent.setText(msg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.cancelDialog(v, dialog);
            }
        });
        mDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.determineDialog(v, dialog);
                dialog.dismiss();
            }
        });
    }

    public static void showDialog(Context context, String msg, final YueJianAppINomalDialog dialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_show_onback_view, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_vChat);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.dialog_show_exit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.show();
        TextView mContent = (TextView) v.findViewById(R.id.tv_dialog_msg);
        Button mCancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
        Button mDetermine = (Button) v.findViewById(R.id.btn_dialog_determine);
        mContent.setText(msg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.cancelDialog(v, dialog);
            }
        });
        mDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.determineDialog(v, dialog);
            }
        });
    }

    /**
     * 显示设置星座对话框
     *
     * @param context
     */
    public static void showSignDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_sign, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        WheelPicker wheelPicker = (WheelPicker) v.findViewById(R.id.wheel_picker);
        List<String> signList = Arrays.asList(context.getResources().getStringArray(R.array.sign));
        final int[] index = {0};
        wheelPicker.setData(signList);
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                index[0] = position;
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.determineDialog(dialog, index[0]);
            }
        });

        dialog.show();
    }

    /**
     * 显示设置身高对话框
     *
     * @param context
     */
    public static void showHeightDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_height, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        final WheelPicker leftPicker = (WheelPicker) v.findViewById(R.id.main_wheel_left);
        WheelPicker centerPicker = (WheelPicker) v.findViewById(R.id.main_wheel_center);
        WheelPicker rightPicker = (WheelPicker) v.findViewById(R.id.main_wheel_right);

        final ArrayList<String> leftData = new ArrayList<>();
        ArrayList<String> centerData = new ArrayList<>();
        ArrayList<String> rightData = new ArrayList<>();
        leftData.add("1");
        leftData.add("2");
        rightData.add("CM");
        for (int i = 0; i < 100; i++) {
            String num = (i < 10 ? "0" + i : String.valueOf(i));
            centerData.add(num);
        }

        leftPicker.setData(leftData);
        centerPicker.setData(centerData);
        centerPicker.setSelectedItemPosition(60);
        rightPicker.setData(rightData);

        final String[] leftValue = {leftData.get(0)};
        final String[] centerValue = {centerData.get(60)};
        final String[] rightValue = {rightData.get(0)};

        leftPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {

            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                leftValue[0] = String.valueOf(data);
            }
        });

        centerPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {

            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                centerValue[0] = String.valueOf(data);
            }
        });

        rightPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {

            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                rightValue[0] = String.valueOf(data);
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.determineDialog(dialog, leftValue[0] + centerValue[0] + rightValue[0]);
            }
        });

        dialog.show();
    }

    /**
     * 显示设置体重对话框
     *
     * @param context
     */
    public static void showWeightDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_weight, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        final WheelPicker leftPicker = (WheelPicker) v.findViewById(R.id.main_wheel_left);
        final WheelPicker centerPicker = (WheelPicker) v.findViewById(R.id.main_wheel_center);
        WheelPicker rightPicker = (WheelPicker) v.findViewById(R.id.main_wheel_right);

        final ArrayList<String> leftData = new ArrayList<>();
        final ArrayList<String> centerData = new ArrayList<>();
        ArrayList<String> rightData = new ArrayList<>();
        leftData.add("0");
        leftData.add("1");
        leftData.add("2");
        rightData.add("KG");
        for (int i = 0; i < 100; i++) {
            String num = (i < 10 ? "0" + i : String.valueOf(i));
            centerData.add(num);
        }

        leftPicker.setData(leftData);
        centerPicker.setData(centerData.subList(30, centerData.size()));
        centerPicker.setSelectedItemPosition(30);
        rightPicker.setData(rightData);

        final String[] leftValue = {leftData.get(0)};
        final String[] centerValue = {centerData.get(60)};
        final String[] rightValue = {rightData.get(0)};

        leftPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {

            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                leftValue[0] = String.valueOf(data);
                if (position == 0) {
                    centerPicker.setData(centerData.subList(30, centerData.size()));
                    centerPicker.setSelectedItemPosition(30);
                } else {
                    centerPicker.setData(centerData);
                    centerPicker.setSelectedItemPosition(60);
                }
            }
        });

        centerPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {

            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                centerValue[0] = String.valueOf(data);
            }
        });

        rightPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {

            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                rightValue[0] = String.valueOf(data);
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = Integer.parseInt(leftValue[0]) == 0 ? centerValue[0] + rightValue[0] : leftValue[0] + centerValue[0] + rightValue[0];
                bottomDialogInterface.determineDialog(dialog, value);
            }
        });

        dialog.show();
    }

    /**
     * 显示设置昵称对话框
     *
     * @param context
     */
    public static void showNameDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_name, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        final EditText editText = (EditText) v.findViewById(R.id.edit_text);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString().trim();
                bottomDialogInterface.determineDialog(dialog, value);
            }
        });

        dialog.show();
    }

    /**
     * 显示设置设置身份证号
     *
     * @param context
     */
    public static void showIdCard(final Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_id_card, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        final EditText etName = (EditText) v.findViewById(R.id.et_name);
        final EditText etIdCard = (EditText) v.findViewById(R.id.et_id_card);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YueJianAppUtils.isFastClick()) {
                    return;
                }
                String name = etName.getText().toString().trim();
                String icCard = etIdCard.getText().toString().trim();
                if (new YueJianAppIdCardUtil(icCard).isCorrect() == 0) {
                    bottomDialogInterface.determineDialog(dialog, name, icCard);
                } else {
                    Toast.makeText(context, "请输入正确的身份证号码！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    /**
     * 显示设置城市对话框
     *
     * @param context
     */
    public static void showCityDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_city, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        final YueJianAppWheelAreaPicker wheelPicker = (YueJianAppWheelAreaPicker) v.findViewById(R.id.area_picker);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = String.format("%s %s %s", wheelPicker.getCountry(), wheelPicker.getProvince(), wheelPicker.getCity());
                bottomDialogInterface.determineDialog(dialog, value);
            }
        });

        dialog.show();
    }

    /**
     * 显示个人介绍对话框
     * 或 个性签名对话框
     *
     * @param context
     */
    public static void showMyIntroDialog(Context context, String title, String hintText, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_my_intro, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
        final EditText editText = (EditText) v.findViewById(R.id.edit_text);
        final TextView tvCharNum = (TextView) v.findViewById(R.id.tv_char_num);

        tvTitle.setText(title);
        editText.setHint(hintText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int charNum = s.toString().trim().length();
                tvCharNum.setText(String.format("%s/15", charNum));
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = editText.getText().toString();
                bottomDialogInterface.determineDialog(dialog, value);
            }
        });

        dialog.show();
    }

    /**
     * 显示形象标签
     *
     * @param context
     */
    public static void showMyLabelsDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_my_labels, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(true);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        final YueJianAppLabelGroup labelContainer = (YueJianAppLabelGroup) v.findViewById(R.id.label_container);
        labelContainer.setLabels(YueJianAppAppContext.getInstance().getMyTagMetaData());
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<YueJianAppACUserTagMetaDataModel> list = labelContainer.getSelectLabelDatas();
                bottomDialogInterface.determineDialog(dialog, list);
            }
        });

        dialog.show();
    }

    /**
     * 显示下载更新对话框
     *
     * @param context
     * @param msg
     * @param dialogInface
     * @param flag
     */
    public static void showUpdateDialog(
            Context context, String msg, final YueJianAppINomalDialog dialogInface, boolean flag) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_show, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.show();
        TextView mContent = (TextView) v.findViewById(R.id.tv_dialog_msg);
        Button mCancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
        Button mDetermine = (Button) v.findViewById(R.id.btn_dialog_determine);
        mContent.setText(msg);
        mCancel.setVisibility(flag ? View.GONE : View.VISIBLE);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.cancelDialog(v, dialog);
            }
        });
        mDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.determineDialog(v, dialog);
            }
        });
    }

    /**
     * 确认对话框
     *
     * @param inflater
     * @param context
     * @param msg
     * @param dialogInface
     */
    public static void showConfirmDialog(
            LayoutInflater inflater, Context context, String msg, final YueJianAppINomalDialog dialogInface) {
        View v = inflater.inflate(R.layout.yue_jian_app_dialog_show_onback_view, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_confirm);
        dialog.setContentView(v);
        dialog.show();
        TextView mContent = (TextView) v.findViewById(R.id.tv_dialog_msg);
        Button mCancel = (Button) v.findViewById(R.id.btn_dialog_cancel);
        Button mDetermine = (Button) v.findViewById(R.id.btn_dialog_determine);
        mContent.setText(msg);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.cancelDialog(v, dialog);
            }
        });
        mDetermine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.determineDialog(v, dialog);
            }
        });
    }

    /**
     * VChat消费者结算对话框
     *
     * @param inflater
     * @param context
     * @param dialogInface
     */
    public static void showUserVChatResultDialog(LayoutInflater inflater,
                                                 final Context context,
                                                 final YueJianAppACUserPublicInfoModel userPublicInfoModel,
                                                 int broadcastSeconds,
                                                 int costedDiamond,
                                                 int experience,
                                                 final YueJianAppINomalDialog dialogInface) {
        View v = inflater.inflate(R.layout.yue_jian_app_dialog_show_vchat_result, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_vChat);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.dialog_show_exit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(v);
        dialog.setCancelable(false); // false：点击外部或返回键不能关闭
        dialog.show();

        ImageView iv_Close = (ImageView) v.findViewById(R.id.iv_close);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tvTime = (TextView) v.findViewById(R.id.tv_time);
        TextView tvDiamond = (TextView) v.findViewById(R.id.tv_diamond);
        TextView tvLoveNum = (TextView) v.findViewById(R.id.tv_love_num);
        YueJianAppMyRatingBar ratingBar = (YueJianAppMyRatingBar) v.findViewById(R.id.rating_bar);
        final YueJianAppLabelGroup labelContainer = (YueJianAppLabelGroup) v.findViewById(R.id.label_container);
        labelContainer.setLabels(YueJianAppAppContext.getInstance().getUserTagMetaData());

        ((YueJianAppAvatarView) v.findViewById(R.id.avatar)).setAvatarUrl(userPublicInfoModel.getAvatarUrl());
        ((TextView) v.findViewById(R.id.tv_name)).setText(userPublicInfoModel.getName());
        tvTime.setText(String.format("通话：%s 秒", broadcastSeconds));
        tvDiamond.setText(String.format("消费：%s 钻石", costedDiamond));
        tvLoveNum.setText(String.format("本次亲密值：%s", experience));
        ratingBar.setStar(userPublicInfoModel.getStar());
        iv_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.cancelDialog(v, dialog);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogInface.determineDialog(v, dialog);
                final List<YueJianAppACUserTagMetaDataModel> list = labelContainer.getSelectLabelDatas();
                if (list.size() != 1) {
                    return;
                }
                final int tag_id = list.get(0).getTagId();
                YueJianAppApiProtoHelper.sendACTagUserReq(
                        (Activity) context,
                        YueJianAppAppContext.getInstance().getLoginUid(),
                        YueJianAppAppContext.getInstance().getToken(),
                        userPublicInfoModel.getUid(),
                        tag_id,
                        new YueJianAppApiProtoHelper.ACTagUserReqCallback() {
                            @Override
                            public void onError(int errCode, String errMessage) {
                                YueJianAppTLog.error("tag user error");
                            }

                            @Override
                            public void onResponse() {
                                YueJianAppTLog.info("tag user success :%s", list.get(0).getName());
                            }
                        }
                );
            }
        });

        YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
        privateInfoModel.setDiamond(privateInfoModel.getDiamond() - costedDiamond);
        YueJianAppAppContext.getInstance().setPrivateInfo(privateInfoModel);
    }

    /**
     * VChat主播结算对话框
     *
     * @param inflater
     * @param context
     * @param dialogInface
     */
    public static void showBroadcasterVChatResultDialog(LayoutInflater inflater,
                                                        final Context context,
                                                        final YueJianAppACUserPublicInfoModel userPublicInfoModel,
                                                        int broadcastSeconds,
                                                        int costedDiamond,
                                                        int experience,
                                                        final YueJianAppINomalDialog dialogInface) {
        View v = inflater.inflate(R.layout.yue_jian_app_dialog_show_broadcaster_vchat_result, null);
        final Dialog dialog = new Dialog(context, R.style.dialog_vChat);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.dialog_show_exit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(v);
        dialog.setCancelable(false); // false：点击外部或返回键不能关闭
        dialog.show();

        ImageView iv_Close = (ImageView) v.findViewById(R.id.iv_close);
        TextView tvConfirm = (TextView) v.findViewById(R.id.tv_confirm);
        TextView tvTime = (TextView) v.findViewById(R.id.tv_time);
        TextView tvDiamond = (TextView) v.findViewById(R.id.tv_diamond);
        TextView tvLoveNum = (TextView) v.findViewById(R.id.tv_love_num);
        YueJianAppMyRatingBar ratingBar = (YueJianAppMyRatingBar) v.findViewById(R.id.rating_bar);

        ((YueJianAppAvatarView) v.findViewById(R.id.avatar)).setAvatarUrl(userPublicInfoModel.getAvatarUrl());
        ((TextView) v.findViewById(R.id.tv_name)).setText(userPublicInfoModel.getName());
        tvTime.setText(String.format("通话：%s 秒", broadcastSeconds));
        tvLoveNum.setText(String.format("本次亲密值：%s", experience));
        ratingBar.setStar(userPublicInfoModel.getStar());
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(String.format("收益：+%s 趣票", costedDiamond));
        stringBuilder.setSpan(
                new ForegroundColorSpan(Color.parseColor("#ec6997")),
                3,
                3 + 1 + String.valueOf(costedDiamond).length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvDiamond.setText(stringBuilder);
        iv_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInface.cancelDialog(v, dialog);
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialogInface.determineDialog(v, dialog);
            }
        });
    }

    /**
     * VChat预约对话框
     *
     * @param inflater
     * @param context
     */
    public static void showVChatAppointmentDialog(LayoutInflater inflater,
                                                  Context context, final YueJianAppINomalDialog dialogInterface) {
        View v = inflater.inflate(R.layout.yue_jian_app_vchat_dialog_appointment, null);
        ImageView appointment = (ImageView) v.findViewById(R.id.iv_appointment);
        ImageView close = (ImageView) v.findViewById(R.id.iv_close);
        YueJianAppAvatarView avatar = (YueJianAppAvatarView) v.findViewById(R.id.avatar);
        final Dialog dialog = new Dialog(context, R.style.dialog_vChat);
        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.determineDialog(v, dialog);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.cancelDialog(v, dialog);
            }
        });

        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.dialog_show_exit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(v);
        dialog.show();
    }

    /**
     * 显示礼物列表对话框
     *
     * @param context
     */
    public static void showGiftListDialog(
            final Activity context,
            ArrayList<YueJianAppACGiftModel> giftModels,
            YueJianAppACUserPublicInfoModel targetModel,
            int videoId,
            final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_gift_list, null);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        TextView cancel = (TextView) v.findViewById(R.id.tv_cancel);
        TextView diamond = (TextView) v.findViewById(R.id.tv_diamond);
        Button btnRecharge = (Button) v.findViewById(R.id.recharge);
        ViewPager mVpGiftView = (ViewPager) v.findViewById(R.id.vp_gift_page);
        YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
        String diamondNum = privateInfoModel == null ? "" : String.valueOf(privateInfoModel.getDiamond());
        diamond.setText(diamondNum);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.cancelDialog(dialog);
            }
        });
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.determineDialog(dialog);
            }
        });

        createGiftUI(context, dialog, giftModels, targetModel, videoId, diamond, mVpGiftView);
        dialog.setCancelable(true);
        dialog.show();
    }

    private static void createGiftUI(
            final Activity context,
            final YueJianAppBottomDialog bottomDialog,
            ArrayList<YueJianAppACGiftModel> mDataList,
            final YueJianAppACUserPublicInfoModel targetModel,
            final int videoId,
            final TextView tvDiamond,
            ViewPager viewPager) {
        // 礼物item填充
        final List<View> mGiftViewList = new ArrayList<>();
        int index = 0;
        int countSize;
        final boolean[] canSend = {true}; // 判断是否可送礼物
        final List<YueJianAppGiftGridViewAdapter> mAdapterList = new ArrayList<>();
        final List<GridView> mGiftViews = new ArrayList<>();
        if (mDataList.size() % 8 == 0) {
            countSize = mDataList.size() / 8;
        } else {
            countSize = mDataList.size() / 8 + 1;
        }
        for (int i = 0; i < countSize; i++) { //设置每一个页面中GridView的数据
            View v = context.getLayoutInflater().inflate(R.layout.yue_jian_app_view_show_gifts_gv, null);
            mGiftViewList.add(v);
            final List<YueJianAppACGiftModel> giftModelList = new ArrayList<>();
            for (int j = 0; j < 8; j++) {
                if (index >= mDataList.size()) {
                    break;
                }
                giftModelList.add(mDataList.get(index));
                index++;
            }
            GridView gridView = (GridView) v.findViewById(R.id.gv_gift_list);
            mGiftViews.add(gridView);
            final YueJianAppGiftGridViewAdapter mAdapter = new YueJianAppGiftGridViewAdapter(giftModelList);
            gridView.setAdapter(mAdapter);
            mAdapterList.add(mAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (!canSend[0]) {
                        YueJianAppTLog.error("can not send!!");
                        return;
                    }
                    for (int i = 0; i < mAdapterList.size(); i++) {
                        mAdapterList.get(i).setChosenId(giftModelList.get(position).getGiftId());
                    }

                    for (int i = 0; i < mGiftViews.size(); i++) {
                        for (int j = 0; j < mGiftViews.get(i).getChildCount(); j++) {
                            mGiftViews.get(i)
                                    .getChildAt(j)
                                    .findViewById(R.id.iv_show_gift_selected_border)
                                    .setBackgroundResource(0);
                        }
                    }
                    view.findViewById(R.id.iv_show_gift_selected_border).setBackgroundResource(R.drawable.yue_jian_app_item_gift_selected_bg);
                    YueJianAppApiProtoHelper.sendACSendGiftReq(
                            context,
                            YueJianAppAppContext.getInstance().getLoginUid(),
                            YueJianAppAppContext.getInstance().getToken(),
                            targetModel.getUid(),
                            giftModelList.get(position).getGiftId(),
                            1,
                            videoId,
                            new YueJianAppApiProtoHelper.ACSendGiftReqCallback() {
                                @Override
                                public void onError(int errCode, String errMessage) {
                                    if (errCode == YueJianAppApiProtoHelper.ERR_NO_DIAMOND) { // 钻石不足
                                        bottomDialog.dismiss();
                                        YueJianAppDialogHelp.showDialog(context, "钻石不足，是否立即充值", new YueJianAppINomalDialog() {
                                            @Override
                                            public void cancelDialog(View v, Dialog d) {
                                                d.dismiss();
                                            }

                                            @Override
                                            public void determineDialog(View v, Dialog d) {
                                                YueJianAppDialogHelper.showRechargeDialogFragment(((FragmentActivity) context).getSupportFragmentManager());
                                                d.dismiss();
                                            }
                                        });
                                        return;
                                    }

                                    Toast mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                                    mToast.setGravity(Gravity.CENTER, 0, 0);
                                    mToast.setText(errMessage);
                                    mToast.show();
                                    canSend[0] = true;
                                }

                                @Override
                                public void onResponse(YueJianAppACGiftModel gift) {
                                    YueJianAppACUserPrivateInfoModel privateInfoModel = YueJianAppAppContext.getInstance().getPrivateInfoModel();
                                    privateInfoModel.setDiamond(privateInfoModel.getDiamond() - gift.getPrice());
                                    tvDiamond.setText(String.valueOf(privateInfoModel.getDiamond()));
                                    YueJianAppAppContext.getInstance().setPrivateInfo(privateInfoModel);
                                    canSend[0] = true;
                                }
                            }
                    );
                }
            });
        }
        viewPager.setAdapter(new YueJianAppViewPageGridViewAdapter(mGiftViewList));
    }

    /**
     * 显示分享短视频对话框
     */
    public static void showShareVideoDialog(final Activity context, final YueJianAppACVideoInfoModel videoInfoModel, final YueJianAppICallBack callBackInteger) {
        View v = View.inflate(context, R.layout.yue_jian_app_pop_view_share_video, null);
        YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        LinearLayout wechat = (LinearLayout) v.findViewById(R.id.ll_live_shar_wechat);
        LinearLayout qq = (LinearLayout) v.findViewById(R.id.ll_live_shar_qq);
        LinearLayout qqZone = (LinearLayout) v.findViewById(R.id.ll_live_shar_qqzone);
        LinearLayout pyq = (LinearLayout) v.findViewById(R.id.ll_live_shar_pyq);

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new YueJianAppShareUtils().shareVideo(context, v.getId(), videoInfoModel, callBackInteger);
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new YueJianAppShareUtils().shareVideo(context, v.getId(), videoInfoModel, callBackInteger);
            }
        });

        qqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new YueJianAppShareUtils().shareVideo(context, v.getId(), videoInfoModel, callBackInteger);
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new YueJianAppShareUtils().shareVideo(context, v.getId(), videoInfoModel, callBackInteger);
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 显示分享推广对话框
     */
    public static void showShareInvitationDialog(final Activity context) {
        View v = View.inflate(context, R.layout.yue_jian_app_pop_view_share, null);
        final YueJianAppShareUtils shareUtils = new YueJianAppShareUtils();
        YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        LinearLayout sinna = (LinearLayout) v.findViewById(R.id.ll_live_shar_sinna);
        LinearLayout wechat = (LinearLayout) v.findViewById(R.id.ll_live_shar_wechat);
        LinearLayout qq = (LinearLayout) v.findViewById(R.id.ll_live_shar_qq);
        LinearLayout qqZone = (LinearLayout) v.findViewById(R.id.ll_live_shar_qqzone);
        LinearLayout pyq = (LinearLayout) v.findViewById(R.id.ll_live_shar_pyq);

        sinna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareInvitation(context, v.getId());
            }
        });

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareInvitation(context, v.getId());
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareInvitation(context, v.getId());
            }
        });

        qqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareInvitation(context, v.getId());
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareInvitation(context, v.getId());
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 显示分享主播对话框
     */
    public static void showShareBroadCastDialog(final Activity context) {
        View v = View.inflate(context, R.layout.yue_jian_app_pop_view_share, null);
        final YueJianAppShareUtils shareUtils = new YueJianAppShareUtils();
        YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        dialog.setContentView(v);
        LinearLayout sinna = (LinearLayout) v.findViewById(R.id.ll_live_shar_sinna);
        LinearLayout wechat = (LinearLayout) v.findViewById(R.id.ll_live_shar_wechat);
        LinearLayout qq = (LinearLayout) v.findViewById(R.id.ll_live_shar_qq);
        LinearLayout qqZone = (LinearLayout) v.findViewById(R.id.ll_live_shar_qqzone);
        LinearLayout pyq = (LinearLayout) v.findViewById(R.id.ll_live_shar_pyq);

        sinna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareBroadcaster(context, v.getId());
            }
        });

        wechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareBroadcaster(context, v.getId());
            }
        });

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareBroadcaster(context, v.getId());
            }
        });

        qqZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareBroadcaster(context, v.getId());
            }
        });

        pyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareUtils.shareBroadcaster(context, v.getId());
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 弹出免费体验视频的对话框
     */
    public static void showToVideoDialog(Context context, final YueJianAppIBottomDialog bottomDialogInterface) {
        View v = View.inflate(context, R.layout.yue_jian_app_dialog_to_video, null);
        LinearLayout llRootView = (LinearLayout) v.findViewById(R.id.ll_dialog_out);
        RelativeLayout rlBackground = (RelativeLayout) v.findViewById(R.id.rl_dialog_background);
        ImageView ivClose = (ImageView) v.findViewById(R.id.iv_dialog_close);
        ImageView ivToVideo = (ImageView) v.findViewById(R.id.iv_dialog_to_video);
        final YueJianAppBottomDialog dialog = new YueJianAppBottomDialog(context, R.style.dialog_vChat);
        int randomIndex = (int) (Math.random() * 2);
        rlBackground.setBackgroundResource(randomIndex == 0 ? R.drawable.yue_jian_app_icon_16002 : R.drawable.yue_jian_app_icon_16003);
        llRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.cancelDialog(dialog);
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.cancelDialog(dialog);
            }
        });
        ivToVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialogInterface.determineDialog(dialog);
            }
        });
        dialog.setContentView(v);
        dialog.setCancelable(true);
        dialog.show();
    }

    public static void showEditReplyItemDialog(final Activity context, final YueJianAppINomalDialog dialogInterface) {
        View v = context.getLayoutInflater().inflate(R.layout.dialog_edit_reply_item, null);
        final EditText etReply = v.findViewById(R.id.et_reply_item);
        etReply.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                YueJianAppTLog.info("text changed :%s, count:%s", s, count);
                if (count >= 20) {
                    Toast.makeText(context, "短语限制20字以内！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextView tvCancel = v.findViewById(R.id.tv_cancel);
        TextView tvSave = v.findViewById(R.id.tv_save);
        final Dialog dialog = new Dialog(context, R.style.dialog_vChat);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.dialog_show_exit);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.dimAmount = 0.5f;
        dialogWindow.setAttributes(lp);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.show();
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogInterface.cancelDialog(v, dialog);
            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YueJianAppStringUtil.isEmpty(etReply.getText().toString().trim())) {
                    Toast.makeText(context, "短语内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogInterface.determineDialog(etReply, dialog);
            }
        });
    }
}
