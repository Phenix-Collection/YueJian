package com.mingquan.yuejian.agora;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mingquan.yuejian.R;

public class YueJianAppEffectAndFilterItemView extends LinearLayout {

    private ImageView mItemIcon;
    private TextView mItemText;

    private int mItemType = YueJianAppEffectAndFilterSelectAdapter.RECYCLEVIEW_TYPE_EFFECT;//effect or filter

    public YueJianAppEffectAndFilterItemView(Context context) {
        super(context);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        View viewRoot = LayoutInflater.from(context).inflate(R.layout.yue_jian_app_effect_and_filter_item_view,
                this, true);
        mItemIcon = (ImageView) viewRoot.findViewById(R.id.item_icon);
        mItemText = (TextView) viewRoot.findViewById(R.id.item_text);
        init();
    }

    private void init() {
        if (mItemType == YueJianAppEffectAndFilterSelectAdapter.RECYCLEVIEW_TYPE_EFFECT) {
            mItemText.setVisibility(GONE);
        } else {
            mItemText.setVisibility(VISIBLE);
        }
    }

    public void setUnselectedBackground() {
        if (mItemType == YueJianAppEffectAndFilterSelectAdapter.RECYCLEVIEW_TYPE_EFFECT) {
            mItemIcon.setBackground(getResources().getDrawable(R.drawable.yue_jian_app_effect_item_circle_unselected));
        } else {
            mItemIcon.setBackgroundColor(Color.parseColor("#00000000"));
        }
    }

    public void setSelectedBackground() {
        if (mItemType == YueJianAppEffectAndFilterSelectAdapter.RECYCLEVIEW_TYPE_EFFECT) {
            mItemIcon.setBackground(getResources().getDrawable(R.drawable.yue_jian_app_effect_item_circle_selected));
        } else {
            mItemIcon.setBackground(getResources().getDrawable(R.drawable.yue_jian_app_effect_item_square_selected));
        }
    }

    public void setItemIcon(int resourceId) {
        mItemIcon.setImageDrawable(getResources().getDrawable(resourceId));
    }

    public void setItemText(String text) {
        mItemText.setText(text);
    }

    public void setItemText(int resid) {
        mItemText.setText(resid);
    }

    public void setItemType(int itemType) {
        mItemType = itemType;
        init();
    }
}
