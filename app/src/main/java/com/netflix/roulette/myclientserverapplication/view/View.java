package com.netflix.roulette.myclientserverapplication.view;

import com.netflix.roulette.myclientserverapplication.pojo.Movie;
import java.util.List;

public interface View {
    void showMovie(List<Movie> movies);
    void showError(String error);
    void showEmptyList();
    void onComplete();
    boolean getType();
}
