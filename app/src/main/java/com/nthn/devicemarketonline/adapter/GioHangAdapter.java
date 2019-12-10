package com.nthn.devicemarketonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.activity.GioHangActivity;
import com.nthn.devicemarketonline.activity.MainActivity;
import com.nthn.devicemarketonline.model.GioHang;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GioHangAdapter extends BaseAdapter {
    Context context;
    ArrayList<GioHang> arrayGioHang;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    public GioHangAdapter(Context context, ArrayList<GioHang> arrayGioHang) {
        this.context = context;
        this.arrayGioHang = arrayGioHang;
    }

    @Override
    public int getCount() {
        return arrayGioHang.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayGioHang.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txtTenGioHang, txtGiaGioHang;
        public ImageView imgGioHang;
        public Button btnMinus, btnValues, btnPlus;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_giohang, null);
            viewHolder.txtTenGioHang = (TextView) view.findViewById(R.id.textviewTenGioHang);
            viewHolder.txtGiaGioHang = (TextView) view.findViewById(R.id.textviewGiaGioHang);
            viewHolder.imgGioHang = (ImageView) view.findViewById(R.id.imageGioHang);
            viewHolder.btnMinus = (Button) view.findViewById(R.id.buttonMinus);
            viewHolder.btnValues = (Button) view.findViewById(R.id.buttonValues);
            viewHolder.btnPlus = (Button) view.findViewById(R.id.buttonPlus);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        GioHang gioHang = (GioHang) getItem(i);
        viewHolder.txtTenGioHang.setText(gioHang.getTenSp());
        viewHolder.txtGiaGioHang.setText(decimalFormat.format(gioHang.getGiaSp()) + " Đ");
        Picasso.with(context).load(gioHang.getHinhAnhSp())
                .placeholder(R.drawable.noimage)
                .error(R.drawable.images)
                .into(viewHolder.imgGioHang);

        viewHolder.btnValues.setText(gioHang.getSoLuongSp() + "");
        int s1 = Integer.parseInt(viewHolder.btnValues.getText().toString());
        if (s1 >= 10) {
            viewHolder.btnPlus.setVisibility(View.INVISIBLE);
            viewHolder.btnMinus.setVisibility(View.VISIBLE);
        } else if (s1 <= 1) {
            viewHolder.btnMinus.setVisibility(View.INVISIBLE);
        } else if (s1 >= 1) {
            viewHolder.btnMinus.setVisibility(View.VISIBLE);
            viewHolder.btnPlus.setVisibility(View.VISIBLE);
        }

        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soLuongMoiNhat = Integer.parseInt(finalViewHolder.btnValues.getText().toString()) + 1;
                int slht = MainActivity.mangGioHang.get(i).getSoLuongSp();
                long giaHt = MainActivity.mangGioHang.get(i).getGiaSp();
                MainActivity.mangGioHang.get(i).setSoLuongSp(soLuongMoiNhat);
                long giaMoiNhat = (giaHt * soLuongMoiNhat) / slht;
                MainActivity.mangGioHang.get(i).setGiaSp(giaMoiNhat);
                finalViewHolder.txtGiaGioHang.setText(decimalFormat.format(giaMoiNhat) + " Đ");
                GioHangActivity.EventUltil();
                if (soLuongMoiNhat > 9) {
                    finalViewHolder.btnPlus.setVisibility(View.INVISIBLE);
                    finalViewHolder.btnMinus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnValues.setText(String.valueOf(soLuongMoiNhat));
                } else {
                    finalViewHolder.btnMinus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnPlus.setVisibility(View.VISIBLE);
                    finalViewHolder.btnValues.setText(String.valueOf(soLuongMoiNhat));
                }
            }
        });
        final ViewHolder finalViewHolder1 = viewHolder;
        viewHolder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soLuongMoiNhat = Integer.parseInt(finalViewHolder1.btnValues.getText().toString()) - 1;
                int slht = MainActivity.mangGioHang.get(i).getSoLuongSp();
                long giaHt = MainActivity.mangGioHang.get(i).getGiaSp();
                MainActivity.mangGioHang.get(i).setSoLuongSp(soLuongMoiNhat);
                long giaMoiNhat = (giaHt * soLuongMoiNhat) / slht;
                MainActivity.mangGioHang.get(i).setGiaSp(giaMoiNhat);
                finalViewHolder1.txtGiaGioHang.setText(decimalFormat.format(giaMoiNhat) + " Đ");
                GioHangActivity.EventUltil();
                if (soLuongMoiNhat < 2) {
                    finalViewHolder1.btnMinus.setVisibility(View.INVISIBLE);
                    finalViewHolder1.btnPlus.setVisibility(View.VISIBLE);
                    finalViewHolder1.btnValues.setText(String.valueOf(soLuongMoiNhat));
                } else {
                    finalViewHolder1.btnMinus.setVisibility(View.VISIBLE);
                    finalViewHolder1.btnPlus.setVisibility(View.VISIBLE);
                    finalViewHolder1.btnValues.setText(String.valueOf(soLuongMoiNhat));
                }
            }
        });

        return view;
    }
}
