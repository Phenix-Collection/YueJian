package com.mingquan.yuejian.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mingquan.yuejian.YueJianAppAppContext;
import com.mingquan.yuejian.R;
import com.mingquan.yuejian.proto.model.YueJianAppACGiftModel;
import com.mingquan.yuejian.widget.YueJianAppLoadUrlImageView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2016/3/28.
 */
public class YueJianAppGiftGridViewAdapter extends BaseAdapter {
    private List<YueJianAppACGiftModel> giftList;
    private int chosenId = -1;

    public YueJianAppGiftGridViewAdapter(List<YueJianAppACGiftModel> giftList) {
        this.giftList = giftList;
    }

    /**
     * 设置礼物选中的位置
     */
    public void setChosenId(int mGiftId) {
        this.chosenId = mGiftId;
    }

    @Override
    public int getCount() {
        return giftList.size();
    }

    @Override
    public Object getItem(int position) {
        return giftList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(YueJianAppAppContext.getInstance(), R.layout.yue_jian_app_item_show_gift, null);
            viewHolder = new ViewHolder();
            viewHolder.mBackground = (ImageView) convertView.findViewById(R.id.iv_bg);
            viewHolder.mGiftViewImg = (YueJianAppLoadUrlImageView) convertView.findViewById(R.id.iv_show_gift_img);
            viewHolder.mGiftPrice = (TextView) convertView.findViewById(R.id.tv_show_gift_price);
            viewHolder.mGiftName = (TextView) convertView.findViewById(R.id.tv_gift_name);
            viewHolder.border = (ImageView) convertView.findViewById(R.id.iv_show_gift_selected_border);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setGiftData(viewHolder, position);
        return convertView;
    }

    private void setGiftData(ViewHolder viewHolder, int position) {
        List list = Arrays.asList(new int[]{0, 2, 5, 7});
        viewHolder.mBackground.setAlpha(list.contains(position) ? 0.7f : 0.75f);
        YueJianAppACGiftModel g = giftList.get(position);
        viewHolder.mGiftViewImg.setGiftLoadUrl(g.getIcon());
        viewHolder.mGiftPrice.setText(String.valueOf(g.getPrice()));
        if (chosenId == g.getGiftId()) {
            viewHolder.border.setBackgroundResource(R.drawable.yue_jian_app_item_gift_selected_bg);
        } else {
            viewHolder.border.setBackgroundResource(0);
        }
        viewHolder.mGiftName.setText(g.getName());
    }

    private static class ViewHolder {
        ImageView mBackground;
        YueJianAppLoadUrlImageView mGiftViewImg;
        TextView mGiftPrice;
        TextView mGiftName;
        ImageView border;
    }

    public void setVisibility(View view, int visibleTag) {
        if (null != view && view.getVisibility() != visibleTag) {
            view.setVisibility(visibleTag);
        }
    }
}
