package com.shrimpcolo.johnnytam.idouban.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by Johnny Tam on 2016/7/10.
 */
public class ShrimpColoManager {
    private static IShrimpColoService sShrimpService;

    public synchronized static IShrimpColoService createShrimpService() {
        if (sShrimpService  == null) {
            Retrofit retrofit = createRetrofit();
            sShrimpService = retrofit.create(IShrimpColoService.class);
        }

        return sShrimpService;
    }

    private static Retrofit createRetrofit() {
        OkHttpClient httpClient;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();

        return new Retrofit.Builder()
                .baseUrl(IShrimpColoService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .build();
    }
}
