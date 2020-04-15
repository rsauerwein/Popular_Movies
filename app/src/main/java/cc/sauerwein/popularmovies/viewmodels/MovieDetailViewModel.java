package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.data.Repository;

public class MovieDetailViewModel extends AndroidViewModel {
    private static final String TAG = MovieDetailViewModel.class.getSimpleName();
    private LiveData<Movie> movie;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "Call MovieDetailViewModel constructor with arguments");
    }


    public LiveData<Movie> getMovie(int id) {
        Repository repository = Repository.getInstance(this.getApplication());
        this.movie = repository.getMovieById(id);
        return this.movie;
    }

//    private void loadMovie(Movie movie) {
//        Repository repository = Repository.getInstance(this.getApplication());
//        this.movie = repository.getMovieById(movie.getId());
//        Log.d(TAG, "loadMovie by id from database");
//    }
}
