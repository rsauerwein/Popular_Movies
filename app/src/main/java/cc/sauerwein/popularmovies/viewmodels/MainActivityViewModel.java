package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
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
    private Repository mRepository;

    private ObservableInt mLoadingVisibility;
    private ObservableInt mErrorMessageVisibility;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mMovieAdapter = new MovieAdapter();
        mLoadingVisibility = new ObservableInt();
        mLoadingVisibility.set(View.GONE);

        Log.d(LOG_TAG, "Call MainActivityViewModel constructor");
    }

    public MutableLiveData<MovieList> getPopularMovies() {
        return mRepository.getPopularMovies();
    }

    public MutableLiveData<MovieList> getTopRatedMovies() {
        return mRepository.getTopRatedMovies();
    }

    public void setMovieListInAdapter(MovieList movieList) {
        mMovieAdapter.setMovieData(movieList.getMovies());
    }

    // Todo solve it in a more elegant way
    public void setupRecyclerview(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication().getApplicationContext(), 2);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mMovieAdapter);
    }

    public ObservableInt getLoadingVisibility() {
        return mLoadingVisibility;
    }

    public void setLoadingVisibility(int visibility) {
        mLoadingVisibility.set(visibility);
    }

    public ObservableInt getErrorMessageVisibility() {
        return mErrorMessageVisibility;
    }

    public void setErrorMessageVisibility(int visibility) {
        mErrorMessageVisibility.set(visibility);
    }
}
