package com.zkwg.modelmanager.utils;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class OkHttpTest {
    public static void main(String[] args) throws IOException {
        String url = "http://172.20.10.14:31205/seldon/sklearn-model/test2-59aa6c9b-34c6-43b7-a18d-4bed3e6877f5/api/v1.0/predictions";
        OkHttpClient httpClient = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json;charset=UTF-8");
        String post = "{\"data\":{\"ndarray\":[[5.964, 4.006, 2.081, 1.031]]}}";
        RequestBody requestBody = RequestBody.create(mediaType, post);
        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();
        Response response = httpClient.newCall(request).execute();
        System.out.println(response.body().string());
    }

    public String upload(String url, String filePath, String fileName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body().toString();
    }


}
