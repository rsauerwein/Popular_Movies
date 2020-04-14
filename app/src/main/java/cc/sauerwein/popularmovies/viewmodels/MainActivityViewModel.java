package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.MovieList;
import cc.sauerwein.popularmovies.data.Repository;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private Repository repository;
    private RecyclerView mRecyclerView;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance(application);
        mMovieAdapter = new MovieAdapter();

        Log.d(LOG_TAG, "Call MainActivityViewModel constructor");
    }

    public MutableLiveData<MovieList> getPopularMovies() {
        return repository.getPopularMovies();
    }

    public MutableLiveData<MovieList> getTopRatedMovies() {
        return repository.getTopRatedMovies();
    }

    public void setMovieListInAdapter(MovieList movieList) {
        mMovieAdapter.setMovieData(movieList.getMovies());
    }

    // Todo solve it in a more elegant way
    public void setupRecyclerview(RecyclerView recyclerView, Context context) {
        GridLayoutManager layoutManager = new GridLayoutManager(context, 2);

        mRecyclerView = recyclerView;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);
    }
}
