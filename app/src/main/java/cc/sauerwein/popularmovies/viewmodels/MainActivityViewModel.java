package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import cc.sauerwein.popularmovies.data.MovieList;
import cc.sauerwein.popularmovies.data.Repository;

public class MainActivityViewModel extends AndroidViewModel {

    private Repository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = Repository.getInstance(application);
    }

    public MutableLiveData<MovieList> getPopularMovies() {
        return repository.getPopularMovies();
    }

    public MutableLiveData<MovieList> getTopRatedMovies() {
        return repository.getTopRatedMovies();
    }
}
