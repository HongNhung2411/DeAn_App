package com.nthn.devicemarketonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.model.LoaiSp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class LoaispAdapter extends BaseAdapter {
    ArrayList<LoaiSp> arrayListLoaiSp;
    Context context;

    public LoaispAdapter(ArrayList<LoaiSp> arrayListLoaiSp, Context context) {
        this.arrayListLoaiSp = arrayListLoaiSp;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayListLoaiSp.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayListLoaiSp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        TextView txtTenLoaiSp;
        ImageView imgLoaiSp;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_listview_loaisp, null);
            viewHolder.txtTenLoaiSp = view.findViewById(R.id.textviewloaisp);
            viewHolder.imgLoaiSp = view.findViewById(R.id.imageviewloaiSp);
            view.setTag(viewHolder);

        } else {
            viewHolder= (ViewHolder) view.getTag();

        }
        LoaiSp loaiSp= (LoaiSp) getItem(i);
        viewHolder.txtTenLoaiSp.setText(loaiSp.getTenloaisp());
        Picasso.with(context).load(loaiSp.getHinhanhloaisp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.images)
                .into(viewHolder.imgLoaiSp);

        return view;
    }
}
