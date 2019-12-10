package com.nthn.devicemarketonline.util;

import com.nthn.devicemarketonline.server.ApiService;
import com.nthn.devicemarketonline.server.RetrofitClient;

public class Server {
    public static String localhost = "172.16.94.34";
    public static String baseUrl = "http://" + localhost + ":81";
    public static String duongDanLoaiSp = baseUrl + "/server/getloaisp.php";
    public static String duongDanSpMoiNhat = baseUrl + "/server/getsanphammoinhat.php";
    public static String duongDanDienThoai = baseUrl + "/server/getsanpham.php?page=";
    public static String duongDanDonHang = baseUrl + "/server/thongtinkhachhang.php";
    public static String duongDanChiTietDonHang = baseUrl + "/server/chitietdonhang.php";
    public static String searchByName = baseUrl + "/server/searchByName.php";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(baseUrl).create(ApiService.class);
    }

}
