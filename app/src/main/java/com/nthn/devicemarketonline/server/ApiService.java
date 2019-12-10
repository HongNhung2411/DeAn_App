package com.nthn.devicemarketonline.server;

import com.nthn.devicemarketonline.model.LoaiSp;
import com.nthn.devicemarketonline.model.SanPham;
import com.nthn.devicemarketonline.util.Server;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    String urlLoaiSP = Server.duongDanLoaiSp;

    @GET("/server/getloaisp.php")
    Call<LoaiSp> getLoaiSanPham();

    @GET("/server/getsanphammoinhat.php")
    Call<SanPham> getSpMoiNhat();

    @POST("/server/getsanpham.php?page=1")
    Call<SanPham> getLaptop(@Field("idsanpham") int id);
}
