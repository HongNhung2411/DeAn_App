package com.nthn.devicemarketonline.adapter;

import android.content.Context;
import android.text.TextUtils;
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

public class LaptopAdapter extends BaseAdapter {
    Context context;
    ArrayList<SanPham> arrayLaptop;

    public LaptopAdapter(Context context, ArrayList<SanPham> arrayLaptop) {
        this.context = context;
        this.arrayLaptop = arrayLaptop;
    }

    @Override
    public int getCount() {
        return arrayLaptop.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayLaptop.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txtTenLaptop,txtGiaLaptop,txtMoTaLaptop;
        public ImageView imgLaptop;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_laptop, null);
            viewHolder.txtTenLaptop = (TextView) view.findViewById(R.id.textviewTenLaptop);
            viewHolder.txtGiaLaptop = (TextView) view.findViewById(R.id.textviewGiaLaptop);
            viewHolder.txtMoTaLaptop = (TextView) view.findViewById(R.id.textviewMoTaLaptop);
            viewHolder.imgLaptop = (ImageView) view.findViewById(R.id.imageviewLaptop);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SanPham sanPham = (SanPham) getItem(i);
        viewHolder.txtTenLaptop.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtGiaLaptop.setText("Giá: " + decimalFormat.format(sanPham.getGiasp()) + "Đ");
        viewHolder.txtMoTaLaptop.setMaxLines(2);
        viewHolder.txtMoTaLaptop.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtMoTaLaptop.setText(sanPham.getMotasp());
        Picasso.with(context).load(sanPham.getHinhanhsp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.images)
                .into(viewHolder.imgLaptop);

        return view;
    }
}
