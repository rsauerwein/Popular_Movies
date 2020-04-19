package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MovieDetailViewModel extends AndroidViewModel {
    private static final String TAG = MovieDetailViewModel.class.getSimpleName();
    final private Repository mRepository;
    private Movie mMovie;
    private MutableLiveData<String> mBtnFavoriteText;
    private Context mContext;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mBtnFavoriteText = new MutableLiveData<>();
        mContext = application.getApplicationContext();
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
            mBtnFavoriteText.postValue(mContext.getString(R.string.mark_as_favorite));
        } else {
            mRepository.insertMovie(mMovie);
            mBtnFavoriteText.postValue("Remove from favorites");
        }
    }

    public MutableLiveData<String> getmBtnFavoriteText() {
        return mBtnFavoriteText;
    }

    public void setmBtnFavoriteText(String txt) {
        mBtnFavoriteText.postValue(txt);
    }
}
