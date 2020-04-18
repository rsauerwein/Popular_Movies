package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();
    private Repository mRepository;

    private ObservableInt mLoadingVisibility;
    private ObservableInt mErrorMessageVisibility;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);

        mLoadingVisibility = new ObservableInt();
        mErrorMessageVisibility = new ObservableInt();

        mLoadingVisibility.set(View.GONE);
        mErrorMessageVisibility.set(View.GONE);

        Log.d(LOG_TAG, "Setup MainActivityViewModel");
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return this.mRepository.getPopularMovies();
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return this.mRepository.getTopRatedMovies();
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
}
