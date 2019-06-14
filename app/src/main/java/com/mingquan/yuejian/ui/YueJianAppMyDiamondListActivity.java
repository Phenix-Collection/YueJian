package com.mingquan.yuejian.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mingquan.yuejian.R;
import com.mingquan.yuejian.base.YueJianAppBaseActivity;
import com.mingquan.yuejian.vchat.YueJianAppXTemplateTitle;
import com.mingquan.yuejian.widget.YueJianAppAvatarView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的钻石
 */
public class YueJianAppMyDiamondListActivity extends YueJianAppBaseActivity {
    @BindView(R.id.title_layout)
    YueJianAppXTemplateTitle titleLayout;
    @BindView(R.id.list_view)
    ListView listView;
    private List mDiamondList;
    private DiamondAdaper mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.yue_jian_app_fragment_my_diamond_list;
    }

    @Override
    public void initView() {
        mAdapter = new DiamondAdaper();
    }

    @Override
    public void initData() {
        mDiamondList = new ArrayList();
        requestData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 请求充值列表
     * 请求用户砖石数量
     */
    private void requestData() {
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(
                "我的钻石");
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("我的钻石");
        MobclickAgent.onPause(this);
    }

    private class DiamondAdaper extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
//            return mDiamondList.size();
        }

        @Override
        public Object getItem(int i) {
//            return mDiamondList.get(i);
            return null;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.yue_jian_app_item_my_diamond_list, null);
                viewHolder = new ViewHolder();
                viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                viewHolder.tvPayNum = (TextView) convertView.findViewById(R.id.tv_pay_num);
                viewHolder.tvPayName = (TextView) convertView.findViewById(R.id.tv_pay_name);
                viewHolder.tvBroadcastName = (TextView) convertView.findViewById(R.id.tv_broadcast_name);
                viewHolder.broadcastAvatar = (YueJianAppAvatarView) convertView.findViewById(R.id.broadcast_avatar);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvTime;
        TextView tvDate;
        TextView tvPayNum;
        TextView tvPayName;
        TextView tvBroadcastName;
        YueJianAppAvatarView broadcastAvatar;
    }
}
