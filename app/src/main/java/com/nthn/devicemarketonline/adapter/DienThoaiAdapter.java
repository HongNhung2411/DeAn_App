package com.nthn.devicemarketonline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DienThoaiAdapter extends BaseAdapter {
    Context context;
    ArrayList<SanPham> arrayDienThoai;

    public DienThoaiAdapter(Context context, ArrayList<SanPham> arrayDienThoai) {
        this.context = context;
        this.arrayDienThoai = arrayDienThoai;
    }

    @Override
    public int getCount() {
        return arrayDienThoai.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayDienThoai.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txtTenDienThoai, txtGiaDienThoai, txtMoTaDienThoai;
        public ImageView imgDienThoai;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_dienthoai, null);
            viewHolder.txtTenDienThoai = (TextView) view.findViewById(R.id.textviewDienThoai);
            viewHolder.txtGiaDienThoai = (TextView) view.findViewById(R.id.textviewGiaDienThoai);
            viewHolder.txtMoTaDienThoai = (TextView) view.findViewById(R.id.textviewMoTaDienThoai);
            viewHolder.imgDienThoai = (ImageView) view.findViewById(R.id.imageviewDienThoai);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SanPham sanPham = (SanPham) getItem(i);
        viewHolder.txtTenDienThoai.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiaDienThoai.setText("Giá: " + decimalFormat.format(sanPham.getGiasp()) + "Đ");
        viewHolder.txtMoTaDienThoai.setMaxLines(2);
        viewHolder.txtMoTaDienThoai.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMoTaDienThoai.setText(sanPham.getMotasp());
        Picasso.with(context).load(sanPham.getHinhanhsp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.images)
                .into(viewHolder.imgDienThoai);

        return view;
    }
}
