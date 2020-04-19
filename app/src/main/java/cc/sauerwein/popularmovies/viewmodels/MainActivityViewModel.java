package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();
    private Repository mRepository;

    private ObservableInt mLoadingVisibility;
    private ObservableInt mErrorMessageVisibility;
    private ObservableInt mRecyclerViewVisibility;

    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);

        mLoadingVisibility = new ObservableInt();
        mErrorMessageVisibility = new ObservableInt();
        mRecyclerViewVisibility = new ObservableInt();

        mLoadingVisibility.set(View.GONE);
        mErrorMessageVisibility.set(View.GONE);
        mRecyclerViewVisibility.set(View.GONE);

        Log.d(LOG_TAG, "Setup MainActivityViewModel");
    }

    public void setupRecyclerView(RecyclerView recyclerView, MovieAdapter movieAdapter) {
        this.mMovieAdapter = movieAdapter;
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movieAdapter);
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return mRepository.fetchMovies(Movie.OPTION_IS_POPULAR);
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return mRepository.fetchMovies(Movie.OPTION_IS_TOP_RATED);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mRepository.getFavoriteMovies();
    }

    public ObservableInt getLoadingVisibility() {
        return this.mLoadingVisibility;
    }

    public void setLoadingVisibility(int visibility) {
        this.mLoadingVisibility.set(visibility);
    }

    public ObservableInt getErrorMessageVisibility() {
        return this.mErrorMessageVisibility;
    }

    public void setErrorMessageVisibility(int visibility) {
        this.mErrorMessageVisibility.set(visibility);
    }

    public ObservableInt getRecyclerViewVisibility() {
        return mRecyclerViewVisibility;
    }

    public void setRecyclerViewVisibility(int visibility) {
        this.mRecyclerViewVisibility.set(visibility);
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(List<Movie> movieList) {
        if (movieList != null) {
            this.mMovieList = movieList;
            mMovieAdapter.notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("movieData must contain movies");
        }
    }

    public void setMovieAdapter(MovieAdapter mMovieAdapter) {
        this.mMovieAdapter = mMovieAdapter;
    }

    public MovieAdapter getmMovieAdapter() {
        return mMovieAdapter;
    }

    public void resetMovieData() {
        this.mMovieList = null;
    }
}
