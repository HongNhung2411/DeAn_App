package com.nthn.devicemarketonline.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.adapter.DienThoaiAdapter;
import com.nthn.devicemarketonline.adapter.LaptopAdapter;
import com.nthn.devicemarketonline.model.SanPham;
import com.nthn.devicemarketonline.util.CheckConnection;
import com.nthn.devicemarketonline.util.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LapTopActivity extends AppCompatActivity {
    Toolbar toolbarlaptop;
    ListView lvlaptop;
    LaptopAdapter laptopAdapter;
    ArrayList<SanPham> mangLaptop;
    int idLaptop;
    int page = 1;
    View footerView;
    boolean isLoading = false;
    boolean limitData = false;
    MHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lap_top);

        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            anhXa();
            getIdLoaiSp();
            actionToolbar();
            getData(page);
            loadMoreData();
        } else {
            CheckConnection.showToast_short(getApplicationContext(), "Hãy kiểm tra lại Internet!");
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuGioHang:
                Intent intent=new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadMoreData() {
        lvlaptop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ChiTietSanPhamActivity.class);
                intent.putExtra("thongtinsanpham", mangLaptop.get(i));
                startActivity(intent);
            }
        });
        lvlaptop.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstItem, int visibleItem, int totalItem) {

                if (firstItem + visibleItem == totalItem && totalItem != 0 && isLoading == false && limitData == false) {
                    isLoading = true;
                    ThreadData threadData = new ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void anhXa() {
        toolbarlaptop = findViewById(R.id.toolbarLaptop);
        lvlaptop = findViewById(R.id.listviewLaptop);
        mangLaptop = new ArrayList<>();


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerView = inflater.inflate(R.layout.progressbar, null);

        mHandler = new MHandler();
    }

    private void getIdLoaiSp() {
        idLaptop = getIntent().getIntExtra("idLoaiSanPham", -1);
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarlaptop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarlaptop.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongDan = Server.duongDanDienThoai + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongDan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                String tenLaptop = "";
                int giaLaptop = 0;
                String hinhAnhLaptop = "";
                String moTaLaptop = "";
                int idSpLaptop = 0;
                if (response != null && response.length() != 2) {
                    lvlaptop.removeFooterView(footerView);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenLaptop = jsonObject.getString("tensp");
                            giaLaptop = jsonObject.getInt("giasp");
                            hinhAnhLaptop = jsonObject.getString("hinhanhsp");
                            moTaLaptop = jsonObject.getString("motasp");
                            idSpLaptop = jsonObject.getInt("idsanpham");
                            mangLaptop.add(new SanPham(id, tenLaptop, giaLaptop, hinhAnhLaptop, moTaLaptop, idSpLaptop));
                        }

                        laptopAdapter = new LaptopAdapter(getApplicationContext(), mangLaptop);
                        lvlaptop.setAdapter(laptopAdapter);
                        laptopAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    lvlaptop.removeFooterView(footerView);
                    limitData = true;
                    CheckConnection.showToast_short(getApplicationContext(), "Đã hết dữ liệu!");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AMBE1203111", error.toString());


            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("idsanpham", String.valueOf(idLaptop));
                return param;
            }
        };
        requestQueue.add(stringRequest);

    }


    public class MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    lvlaptop.addFooterView(footerView);
                    break;
                case 1:
                    getData(++page);
                    isLoading = false;
                    break;
            }
            super.handleMessage(msg);
        }
    }


    public class ThreadData extends Thread {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
