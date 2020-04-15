package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import cc.sauerwein.popularmovies.data.MovieList;
import cc.sauerwein.popularmovies.data.Repository;

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

    public MutableLiveData<MovieList> getPopularMovies() {
        return this.mRepository.getPopularMovies();
    }

    public MutableLiveData<MovieList> getTopRatedMovies() {
        return this.mRepository.getTopRatedMovies();
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
