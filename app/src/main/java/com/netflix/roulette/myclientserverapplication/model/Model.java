package com.netflix.roulette.myclientserverapplication.model;

import com.netflix.roulette.myclientserverapplication.pojo.Movie;

import java.util.List;
import java.util.Map;

import rx.Observable;

public interface Model {
    Observable<List<Movie>> getMovieList(Map<String, String> request);
    Observable<Movie> getMovie(Map<String, String> request);
}
