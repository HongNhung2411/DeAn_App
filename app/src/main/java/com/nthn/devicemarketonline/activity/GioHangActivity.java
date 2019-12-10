package com.nthn.devicemarketonline.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.adapter.GioHangAdapter;
import com.nthn.devicemarketonline.model.GioHang;
import com.nthn.devicemarketonline.util.CheckConnection;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {

    ListView lvGioHang;
    TextView txtThongBao;
    static TextView txtTongTien;
    Button btnThanhToan, btnTiepTucMua;
    Toolbar toolbarGioHang;
    GioHangAdapter gioHangAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        anhXa();
        actionToolBar();
        checkData();
        EventUltil();
        CatchOnItemListview();
        EventButton();
    }

    private void EventButton() {
        btnTiepTucMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.mangGioHang.size()>0){
                    Intent intent=new Intent(getApplicationContext(),ThongTinKhachHangActivity.class);
                    startActivity(intent);
                }else {
                    CheckConnection.showToast_short(getApplicationContext(),"Giỏ hàng đang trống!");
                }
            }
        });
    }

    private void CatchOnItemListview() {
        lvGioHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(GioHangActivity.this);
                builder.setTitle("Xác nhận");
                builder.setMessage("Bạn có chắc chắn xóa sản phẩm?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (MainActivity.mangGioHang.size()<=0){
                            txtThongBao.setVisibility(View.VISIBLE);
                        }else {
                            MainActivity.mangGioHang.remove(position);
                            gioHangAdapter.notifyDataSetChanged();
                            EventUltil();
                            if (MainActivity.mangGioHang.size()<=0){
                                txtThongBao.setVisibility(View.VISIBLE);
                            }else {
                                txtThongBao.setVisibility(View.INVISIBLE);
                                gioHangAdapter.notifyDataSetChanged();
                                EventUltil();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gioHangAdapter.notifyDataSetChanged();
                        EventUltil();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    public static void EventUltil() {
        long tongTien = 0;
        for (int i = 0; i < MainActivity.mangGioHang.size(); i++) {
            tongTien += MainActivity.mangGioHang.get(i).getGiaSp();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongTien.setText(decimalFormat.format(tongTien) + " Đ");
    }

    private void checkData() {
        if (MainActivity.mangGioHang.size() <= 0) {
            gioHangAdapter.notifyDataSetChanged();
            txtThongBao.setVisibility(View.VISIBLE);
            lvGioHang.setVisibility(View.INVISIBLE);
        } else {
            gioHangAdapter.notifyDataSetChanged();
            txtThongBao.setVisibility(View.INVISIBLE);
            lvGioHang.setVisibility(View.VISIBLE);
        }
    }

    private void actionToolBar() {
        setSupportActionBar(toolbarGioHang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        lvGioHang = findViewById(R.id.listviewGioHang);
        txtThongBao = findViewById(R.id.textviewThongbao);
        txtTongTien = findViewById(R.id.textviewTongTien);
        btnThanhToan = findViewById(R.id.buttonThanhToanGioHang);
        btnTiepTucMua = findViewById(R.id.buttonTiepTucMuaHang);
        toolbarGioHang = findViewById(R.id.toolbarGioHang);
        gioHangAdapter = new GioHangAdapter(GioHangActivity.this, MainActivity.mangGioHang);
        lvGioHang.setAdapter(gioHangAdapter);
    }
}
