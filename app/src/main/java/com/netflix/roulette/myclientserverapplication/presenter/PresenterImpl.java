package com.netflix.roulette.myclientserverapplication.presenter;

import com.netflix.roulette.myclientserverapplication.pojo.Movie;
import com.netflix.roulette.myclientserverapplication.model.Model;
import com.netflix.roulette.myclientserverapplication.model.ModelImpl;
import com.netflix.roulette.myclientserverapplication.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.subscriptions.Subscriptions;

public class PresenterImpl implements Presenter {

    private Model model = new ModelImpl();
    private Subscription subscription = Subscriptions.empty();
    private View view;

    public PresenterImpl(View view) {
        this.view = view;
    }

    @Override
    public void onTextChanged(String name) {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }

        Action1<Throwable> onErrorAction = throwable -> view.showError(throwable.getMessage());
        Action0 onCompletedAction = () -> view.onComplete();

        Map<String, String> request = new HashMap<>();
        if (!view.getType()) {
            request.put("director", name);
            subscription = model.getMovieList(request)
                    .subscribe(movies -> {
                        if (movies != null && !movies.isEmpty())
                            view.showMovie(movies);
                        else
                            view.showEmptyList();
                    }, onErrorAction, onCompletedAction);
        }
        else {
            request.put("title", name);
            subscription = model.getMovie(request)
                    .subscribe(movie -> {
                        if (movie != null) {
                            List<Movie> arr = new ArrayList<>();
                            arr.add(movie);
                            view.showMovie(arr);
                        } else
                            view.showEmptyList();
                    }, onErrorAction, onCompletedAction);
        }
    }

    @Override
    public void onStop() {
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
