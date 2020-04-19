package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MovieDetailViewModel extends AndroidViewModel {
    private static final String TAG = MovieDetailViewModel.class.getSimpleName();
    final private Repository mRepository;
    private Movie mMovie;
    private MutableLiveData<String> mBtnFavoriteText;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mBtnFavoriteText = new MutableLiveData<>();
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
        if (mMovie.isFavorite()) {
            mRepository.deleteMovie(mMovie);
            mBtnFavoriteText.postValue("Mark as favorite");
        } else {
            mRepository.insertMovie(mMovie);
            mBtnFavoriteText.postValue("Remove from favorites");
        }
        mRepository.updateMovie(mMovie);
    }

    public MutableLiveData<String> getmBtnFavoriteText() {
        return mBtnFavoriteText;
    }

    public void setmBtnFavoriteText(MutableLiveData<String> mBtnFavoriteText) {
        this.mBtnFavoriteText = mBtnFavoriteText;
    }
}
