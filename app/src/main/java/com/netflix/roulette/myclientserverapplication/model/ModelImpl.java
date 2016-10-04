package com.netflix.roulette.myclientserverapplication.model;

import com.google.gson.GsonBuilder;
import com.netflix.roulette.myclientserverapplication.pojo.Movie;

import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ModelImpl implements Model{

    private ApiInterface apiInterface;

    public ModelImpl() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()))
                .baseUrl("http://netflixroulette.net/api/")
                .client(client)
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    @Override
    public Observable<List<Movie>> getMovieList(Map<String, String> request) {
        return apiInterface.getMovieList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Movie> getMovie(Map<String, String> request) {
        return apiInterface.getMovie(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
