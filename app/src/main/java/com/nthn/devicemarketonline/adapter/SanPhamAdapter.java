package com.nthn.devicemarketonline.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.activity.ChiTietSanPhamActivity;
import com.nthn.devicemarketonline.model.SanPham;
import com.nthn.devicemarketonline.util.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.ItemHolder> {
    Context context;
    ArrayList<SanPham> arraySanPham;

    public SanPhamAdapter(Context context, ArrayList<SanPham> arraySanPham) {
        this.context = context;
        this.arraySanPham = arraySanPham;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dong_sanphammoinhat, null);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        SanPham sanPham = arraySanPham.get(position);
        holder.txtTenSanPham.setText(sanPham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtGiaSanPham.setText("Giá: " + decimalFormat.format(sanPham.getGiasp()) + "Đ");
        Picasso.with(context).load(sanPham.getHinhanhsp())
                .placeholder(R.drawable.noimage)
                .into(holder.imgHinhSanPham);

    }

    @Override
    public int getItemCount() {

        return arraySanPham.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        public ImageView imgHinhSanPham;
        public TextView txtTenSanPham, txtGiaSanPham;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            imgHinhSanPham = (ImageView) itemView.findViewById(R.id.imagesanpham);
            txtGiaSanPham = (TextView) itemView.findViewById(R.id.textviewgiasanpham);
            txtTenSanPham = (TextView) itemView.findViewById(R.id.textviewtensanpham);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, ChiTietSanPhamActivity.class);
                    intent.putExtra("thongtinsanpham",arraySanPham.get(getAdapterPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CheckConnection.showToast_short(context,arraySanPham.get(getAdapterPosition()).getTensp());
                    context.startActivity(intent);
                }
            });

        }
    }
}
