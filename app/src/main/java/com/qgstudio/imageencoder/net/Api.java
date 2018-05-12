package com.qgstudio.imageencoder.net;

import com.qgstudio.imageencoder.image.ImageData;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Api {
    @POST("encrypt")
    Observable<int[][][]> requestEncrypt(@Body ImageData data);

    @POST("decrypt")
    Observable<int[][][]> requestDecrypt(@Body ImageData data);

    @Multipart
    @POST("uploadencrypt")
    Observable<int[][][]> uploadEncrypt(@Part MultipartBody.Part file);
    @Multipart
    @POST("uploaddecrypt")
    Observable<int[][][]> uploadDecrypt(@Part MultipartBody.Part file);
}
