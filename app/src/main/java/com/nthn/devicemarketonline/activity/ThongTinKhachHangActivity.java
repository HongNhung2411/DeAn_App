package com.nthn.devicemarketonline.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.util.CheckConnection;
import com.nthn.devicemarketonline.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ThongTinKhachHangActivity extends AppCompatActivity {
    EditText editTenKhachHang, editEmail, editSoDienThoai;
    Button btnXacNhan, btnTroVe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);
        AnhXa();
        btnTroVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            EventButton();
        } else {
            CheckConnection.showToast_short(getApplicationContext(), "Hãy kiểm tra lại kết nối!");
        }
    }

    private void AnhXa() {
        editTenKhachHang = (EditText) findViewById(R.id.edittextTenKhachHang);
        editEmail = (EditText) findViewById(R.id.edittextEmailKhachHang);
        editSoDienThoai = (EditText) findViewById(R.id.edittextSDTKhachHang);
        btnXacNhan = findViewById(R.id.buttonXacNhan);
        btnTroVe = findViewById(R.id.buttonTroVe);
    }

    private void EventButton() {
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String ten = editTenKhachHang.getText().toString().trim();
                final String sdt = editSoDienThoai.getText().toString().trim();
                final String email = editEmail.getText().toString().trim();

                if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.duongDanDonHang, new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
                            Log.d("madonhang", madonhang);
                            if (Integer.parseInt(madonhang) > 0) {
                                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                StringRequest request = new StringRequest(Request.Method.POST, Server.duongDanChiTietDonHang, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("1")) {
                                            MainActivity.mangGioHang.clear();
                                            CheckConnection.showToast_short(getApplicationContext(), "Thêm dữ liệu giỏ hàng thành công!");
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            CheckConnection.showToast_short(getApplicationContext(), "Mời bạn tiếp tục mua hàng!");
                                        } else {
                                            CheckConnection.showToast_short(getApplicationContext(), "Dữ liệu giỏ hàng bị lỗi!");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < MainActivity.mangGioHang.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("madonhang", madonhang);
                                                jsonObject.put("masanpham", MainActivity.mangGioHang.get(i).getIdSp());
                                                jsonObject.put("tensanpham", MainActivity.mangGioHang.get(i).getTenSp());
                                                jsonObject.put("giasanpham", MainActivity.mangGioHang.get(i).getGiaSp());
                                                jsonObject.put("soluongsanpham", MainActivity.mangGioHang.get(i).getSoLuongSp());


                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("json", jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                queue.add(request);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("tenkhachhang", ten);
                            hashMap.put("sodienthoai", sdt);
                            hashMap.put("email", email);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else {
                    CheckConnection.showToast_short(getApplicationContext(), "Hãy kiểm tra lại dữ liệu vừa nhập!");
                }
            }
        });
    }
}
