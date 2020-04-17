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
    final private Repository mRepository;
    private LiveData<Movie> mMovie;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        Log.d(TAG, "Call MovieDetailViewModel constructor with arguments");
    }

    public void fetchMovie(int id) {
        mMovie = mRepository.getMovieById(id);
    }

    public LiveData<Movie> getMovie() {
        return mMovie;
    }

    public void setMovie(LiveData<Movie> mMovie) {
        this.mMovie = mMovie;
    }

    public void favoriteButtonTap(Movie movie) {
        if (movie.isFavorite()) {
            movie.setFavorite(false);
        } else {
            movie.setFavorite(true);
        }
        mRepository.updateMovie(movie);
    }
}
