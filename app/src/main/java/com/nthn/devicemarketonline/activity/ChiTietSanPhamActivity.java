package com.nthn.devicemarketonline.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.model.GioHang;
import com.nthn.devicemarketonline.model.SanPham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    Toolbar toolbarChiTiet;
    ImageView imgChiTiet;
    TextView txtTen, txtGia, txtMoTa;
    Spinner spinner;
    Button btnDatMua;

    int id = 0;
    String tenChiTiet = "";
    int giaChiTiet = 0;
    String hinhAnhChiTiet = "";
    String moTaChiTiet = "";
    int idSanPham = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        anhXa();
        actionToolbar();
        getInformation();
        catchEventSpinner();
        eventButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuGioHang:
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void eventButton() {
        btnDatMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mangGioHang.size() > 0) {
                    int s1 = Integer.parseInt(spinner.getSelectedItem().toString());
                    boolean exit = false;
                    for (int i = 0; i < MainActivity.mangGioHang.size(); i++) {
                        if (MainActivity.mangGioHang.get(i).getIdSp() == id) {
                            MainActivity.mangGioHang.get(i).setSoLuongSp(MainActivity.mangGioHang.get(i).getSoLuongSp() + s1);

                            if (MainActivity.mangGioHang.get(i).getSoLuongSp() >= 10) {
                                MainActivity.mangGioHang.get(i).setSoLuongSp(10);
                                MainActivity.mangGioHang.get(i).setGiaSp(giaChiTiet * MainActivity.mangGioHang.get(i).getSoLuongSp());
                                exit = true;
                            }
                        }
                    }
                    if (exit == false) {
                        int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
                        long giaMoi = soLuong * giaChiTiet;
                        MainActivity.mangGioHang.add(new GioHang(id, tenChiTiet, giaMoi, hinhAnhChiTiet, soLuong));
                    }
                } else {
                    int soLuong = Integer.parseInt(spinner.getSelectedItem().toString());
                    long giaMoi = soLuong * giaChiTiet;
                    MainActivity.mangGioHang.add(new GioHang(id, tenChiTiet, giaMoi, hinhAnhChiTiet, soLuong));
                }
                Intent intent = new Intent(ChiTietSanPhamActivity.this, GioHangActivity.class);
                startActivity(intent);
            }
        });
    }

    private void catchEventSpinner() {
        Integer[] soLuong = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_dropdown_item, soLuong);
        spinner.setAdapter(arrayAdapter);
    }

    private void getInformation() {


        SanPham sanPham = (SanPham) getIntent().getSerializableExtra("thongtinsanpham");
        id = sanPham.getId();
        tenChiTiet = sanPham.getTensp();
        giaChiTiet = sanPham.getGiasp();
        hinhAnhChiTiet = sanPham.getHinhanhsp();
        moTaChiTiet = sanPham.getMotasp();
        idSanPham = sanPham.getIdsanpham();

        txtTen.setText(tenChiTiet);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtGia.setText("Giá: " + decimalFormat.format(giaChiTiet) + " Đ");
        txtMoTa.setText(moTaChiTiet);
        Picasso.with(getApplicationContext()).load(hinhAnhChiTiet)
                .placeholder(R.drawable.noimage)
                .error(R.drawable.images)
                .into(imgChiTiet);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarChiTiet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarChiTiet.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        toolbarChiTiet = findViewById(R.id.toolbarChiTietSanPham);
        imgChiTiet = findViewById(R.id.imageviewChiTietSanPham);
        txtTen = findViewById(R.id.textviewTenChiTietSanPham);
        txtGia = findViewById(R.id.textviewGiaChiTietSanPham);
        txtMoTa = findViewById(R.id.textviewMoTaChiTietSanPham);
        spinner = findViewById(R.id.spinner);
        btnDatMua = findViewById(R.id.buttonDatMua);
    }
}
