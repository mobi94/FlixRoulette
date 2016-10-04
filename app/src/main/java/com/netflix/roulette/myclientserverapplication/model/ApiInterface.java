package com.netflix.roulette.myclientserverapplication.model;

import com.netflix.roulette.myclientserverapplication.pojo.Movie;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface ApiInterface {
    @GET("api.php")
    Observable<List<Movie>> getMovieList(@QueryMap Map<String, String> request);
    @GET("api.php")
    Observable<Movie> getMovie(@QueryMap Map<String, String> request);
}
