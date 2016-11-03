package com.gwi.selfplatform.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gwi.phr.hospital.R;
import com.gwi.selfplatform.db.gen.T_HealthEdu_Datum;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HealtItemsListAdapter extends BaseAdapter {

    private List<T_HealthEdu_Datum> mList = new ArrayList<T_HealthEdu_Datum>();
    private Context mContext;

    public HealtItemsListAdapter(Context context, List<T_HealthEdu_Datum> list) {
        mContext = context;
        if(list!=null&&!list.isEmpty()) {
            mList.addAll(list);
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold = null;
        if(convertView==null) {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_health_list_item, parent,false);
            viewHold.img = (ImageView) convertView.findViewById(R.id.itemIcon);
            viewHold.title = (TextView) convertView.findViewById(R.id.itemTitle);
            viewHold.content = (TextView) convertView.findViewById(R.id.itemContent);
            convertView.setTag(viewHold);
            //TODO:
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        T_HealthEdu_Datum item = mList.get(position);
        viewHold.title.setText(item.getDatumName());
        viewHold.content.setText(item.getDatumIntro());
        viewHold.url = item.getDatumPath();
        ImageLoader.getInstance().displayImage(item.getDatumLogo(), viewHold.img);
        return convertView;
    }

    public static class ViewHold {
        public ImageView img;
        public TextView title;
        public TextView content;
        public String url;       
    }
    
}
