package com.nthn.devicemarketonline.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.nthn.devicemarketonline.R;
import com.nthn.devicemarketonline.adapter.LoaispAdapter;
import com.nthn.devicemarketonline.adapter.SanPhamAdapter;
import com.nthn.devicemarketonline.model.GioHang;
import com.nthn.devicemarketonline.model.LoaiSp;
import com.nthn.devicemarketonline.model.SanPham;
import com.nthn.devicemarketonline.server.ApiService;
import com.nthn.devicemarketonline.util.CheckConnection;
import com.nthn.devicemarketonline.util.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewFlipper viewFlipper;
    private RecyclerView recyclerViewManHiinhChinh;
    private RecyclerView rcvResult;
    private NavigationView navigationView;
    private ListView listViewManHinhChinh;
    private DrawerLayout drawerLayout;
    private ImageView imgeHeader;
    private LinearLayout lnlMain;
    ArrayList<LoaiSp> mangLoaiSp;
    LoaispAdapter loaispAdapter;
    int id = 0;
    String tenLoaiSp = "";
    String hinhAnhLoaiSp = "";
    ArrayList<SanPham> mangSanPham;
    SanPhamAdapter sanPhamAdapter;
    private SanPhamAdapter searchAdapter;
    private ApiService apiService;
    private ProgressBar progressBar;
    public static ArrayList<GioHang> mangGioHang;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            ActionBar();
            ActionViewFlipper();
            progressBar.setVisibility(View.VISIBLE);
            //      getLoaiSanPham();
            GetDuLieuLoaiSp();
            GetDuLieuSanPhamMoiNhat();

            catchOnItemListView();
        } else {
            CheckConnection.showToast_short(getApplicationContext(), "Hãy kiểm tra lại kết nối!");
            finish();
        }

    }

    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                rcvResult.setVisibility(View.GONE);
                lnlMain.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Toast like print
                searchByName(query);
                progressBar.setVisibility(View.VISIBLE);
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // UserFeedback.show( "SearchOnQueryTextChanged: " + s);
                return false;
            }
        });
        return true;


//        getMenuInflater().inflate(R.menu.menu, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuGioHang:
                Intent intent = new Intent(getApplicationContext(), GioHangActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void catchOnItemListView() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            CheckConnection.showToast_short(getApplicationContext(), "Hãy kiểm tra lại kết nối!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, DienThoaiActivity.class);
                            intent.putExtra("idLoaiSanPham", mangLoaiSp.get(i).getId());
                            startActivity(intent);
                        } else {
                            CheckConnection.showToast_short(getApplicationContext(), "Không có mạng!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, LapTopActivity.class);
                            intent.putExtra("idLoaiSanPham", mangLoaiSp.get(i).getId());
                            startActivity(intent);
                        } else {
                            CheckConnection.showToast_short(getApplicationContext(), "Không có mạng!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, LienHeActivity.class);
                            startActivity(intent);
                        } else {
                            CheckConnection.showToast_short(getApplicationContext(), "Không có mạng!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 4:
                        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                            Intent intent = new Intent(MainActivity.this, ThongTinActivity.class);
                            startActivity(intent);
                        } else {
                            CheckConnection.showToast_short(getApplicationContext(), "Không có mạng!");
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        });
    }

    private void getLoaiSanPham() {
        apiService.getLaptop(2).enqueue(new Callback<SanPham>() {
            @Override
            public void onResponse(Call<SanPham> call, retrofit2.Response<SanPham> response) {

            }

            @Override
            public void onFailure(Call<SanPham> call, Throwable t) {

            }
        });
    }

    private ArrayList<SanPham> arrResult = new ArrayList<>();

    private void searchByName(final String str) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String duongDan = Server.searchByName;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, duongDan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("AMBE1203", "aaa: " + response);
                int id = 0;
                String tenDt = "";
                int giaDt = 0;
                String hinhAnhDt = "";
                String moTa = "";
                int idSpDt = 0;
                if (response != null && response.length() != 2) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenDt = jsonObject.getString("tensp");
                            giaDt = jsonObject.getInt("giasp");
                            hinhAnhDt = jsonObject.getString("hinhanhsp");
                            moTa = jsonObject.getString("motasp");
                            idSpDt = jsonObject.getInt("idsanpham");
                            arrResult.add(new SanPham(id, tenDt, giaDt, hinhAnhDt, moTa, idSpDt));
                            searchAdapter.notifyDataSetChanged();
                        }

                        rcvResult.setVisibility(View.VISIBLE);
                        lnlMain.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("str", String.valueOf(str));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void GetDuLieuSanPhamMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.duongDanSpMoiNhat, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response != null) {
                    progressBar.setVisibility(View.GONE);

                    int ID = 0;
                    String tenSanPham = "";
                    Integer giaSanPham = 0;
                    String hinhAnhSanPham = "";
                    String moTaSanPham = "";
                    int IDSanPham = 0;

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            ID = jsonObject.getInt("id");
                            tenSanPham = jsonObject.getString("tensp");
                            giaSanPham = jsonObject.getInt("giasp");
                            hinhAnhSanPham = jsonObject.getString("hinhanhsp");
                            moTaSanPham = jsonObject.getString("motasp");
                            IDSanPham = jsonObject.getInt("idsanpham");
                            mangSanPham.add(new SanPham(ID, tenSanPham, giaSanPham, hinhAnhSanPham, moTaSanPham, IDSanPham));
                            sanPhamAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    private void GetDuLieuLoaiSp() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.duongDanLoaiSp, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            tenLoaiSp = jsonObject.getString("tenloaisp");
                            hinhAnhLoaiSp = jsonObject.getString("hinhanhloaisp");
                            mangLoaiSp.add(new LoaiSp(id, tenLoaiSp, hinhAnhLoaiSp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mangLoaiSp.add(new LoaiSp(response.length() + 1, "Liên hệ", "https://voip24h.vn/wp-content/uploads/2019/03/phone-png-mb-phone-icon-256.png"));
                    mangLoaiSp.add(new LoaiSp(response.length() + 2, "Thông tin", "https://support.casio.com/global/vi/wat/manual/5413_vi/fig/App_icon_02_VPCVILcirwnbhj.png"));
                    loaispAdapter.notifyDataSetChanged();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.showToast_short(getApplicationContext(), error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangQuangCao = new ArrayList<>();
        mangQuangCao.add("https://cdn.tgdd.vn/Files/2017/03/09/958798/son-tung-oppo-f3_800x450.jpg");
        mangQuangCao.add("http://genknews.genkcdn.vn/thumb_w/640/2018/7/27/photo1532663263511-15326632635121211751050.jpg");
        mangQuangCao.add("http://i.imgur.com/8xmLoTE.jpg");
        mangQuangCao.add("http://mobifone.vteen.com.vn/upload/image/sao/viet-thao/7-1-2016/nhung-ong-hoang-ba-chua-cua-quang-cao-viet/12.jpg");
        mangQuangCao.add("https://a.ipricegroup.com/trends-article/4-cach-chup-anh-selfie-dep-hon-bang-oppo-f3-ghi-lai-khoanh-khac-day-dam-me-cua-tuoi-tre-medium.jpg");
        mangQuangCao.add("http://www.techrum.vn/chevereto/images/2018/09/09/mUU3G.jpg");
        for (int i = 0; i < mangQuangCao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangQuangCao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("RestrictedApi")
    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Glide.with(getApplicationContext())
                .load(R.drawable.meo)
                .apply(RequestOptions.circleCropTransform())
                .into(imgeHeader);
    }


    private void anhXa() {
        toolbar = findViewById(R.id.toolbarManHinhChinh);
        lnlMain = findViewById(R.id.main);
        imgeHeader = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progress_bar);
        viewFlipper = findViewById(R.id.viewflipper);
        recyclerViewManHiinhChinh = findViewById(R.id.recyclerview);
        rcvResult = findViewById(R.id.rcvMain);
        navigationView = findViewById(R.id.navigationview);
        listViewManHinhChinh = findViewById(R.id.listviewManHinhChinh);
        drawerLayout = findViewById(R.id.drawerlayout);

        //    apiService = Server.getApiService();
        mangLoaiSp = new ArrayList<>();
        mangLoaiSp.add(new LoaiSp(0, "Trang chính", "https://khangkhang.sagohano.com/Statics/Images/icon-trang-chu-xanh.png"));
        loaispAdapter = new LoaispAdapter(mangLoaiSp, getApplicationContext());
        listViewManHinhChinh.setAdapter(loaispAdapter);
        mangSanPham = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getApplicationContext(), mangSanPham);
        recyclerViewManHiinhChinh.setHasFixedSize(true);
        recyclerViewManHiinhChinh.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        recyclerViewManHiinhChinh.setAdapter(sanPhamAdapter);

        searchAdapter = new SanPhamAdapter(getApplicationContext(), arrResult);
        rcvResult.setHasFixedSize(true);
        rcvResult.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rcvResult.setAdapter(searchAdapter);


        if (mangGioHang == null) {
            mangGioHang = new ArrayList<>();
        }
    }
}
