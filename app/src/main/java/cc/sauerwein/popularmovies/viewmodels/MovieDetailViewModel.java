package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MovieDetailViewModel extends AndroidViewModel {
    private static final String TAG = MovieDetailViewModel.class.getSimpleName();
    final private Repository mRepository;
    private Movie mMovie;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        Log.d(TAG, "Call MovieDetailViewModel constructor with arguments");
    }

    public LiveData<Movie> fetchMovie(int id) {
        return mRepository.getMovieById(id);
    }

    public Movie getMovie() {
        return mMovie;
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
    }

    public void favoriteButtonTap() {
//        if (mMovie.isFavorite()) {
//            mMovie.setFavorite(false);
//        } else {
//            mMovie.setFavorite(true);
//        }
        mRepository.updateMovie(mMovie);
    }
}
