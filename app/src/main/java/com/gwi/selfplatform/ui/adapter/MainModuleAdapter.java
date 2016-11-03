package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.ui.beans.MainModuleBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/1/12 0012.
 */
public class MainModuleAdapter extends ArrayAdapter<MainModuleBean> {
    private Context mContext;
    private LayoutInflater mInflater;

    /**
     *
     * @param context context
     * @param objects objects
     */
    public MainModuleAdapter(Context context, List<MainModuleBean> objects) {
        super(context, 0, objects);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_module, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        MainModuleBean item = getItem(position);

        holder.ivIcon.setImageResource(item.getResIcon());
        holder.ivTips.setImageResource(item.getResTips());
        holder.txtDesp.setText(item.getResDescription());
        holder.txtDesp.setTextColor(mContext.getResources().getColor(item.getResDespColor()));
        holder.lyRoot.setBackgroundColor(mContext.getResources().getColor(item.getResBgColor()));
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_module.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        public ImageView ivIcon;
        @Bind(R.id.txt_desp)
        public TextView txtDesp;
        @Bind(R.id.iv_tips)
        public ImageView ivTips;
        @Bind(R.id.ly_root)
        public LinearLayout lyRoot;

        /**
         *
         * @param view view
         */
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
