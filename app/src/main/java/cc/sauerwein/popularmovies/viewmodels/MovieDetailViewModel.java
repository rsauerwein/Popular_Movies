package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.adapter.ReviewAdapter;
import cc.sauerwein.popularmovies.adapter.VideoAdapter;
import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MovieDetailViewModel extends AndroidViewModel {
    private static final String TAG = MovieDetailViewModel.class.getSimpleName();
    final private Repository mRepository;
    private Movie mMovie;
    private MutableLiveData<String> mBtnFavoriteText;
    private Context mContext;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);
        mBtnFavoriteText = new MutableLiveData<>();
        mContext = application.getApplicationContext();
        Log.d(TAG, "Call MovieDetailViewModel constructor with arguments");
    }

    // Query the room db for a specific Movie
    // That way we could check if an entry is already stored (favorite)
    public LiveData<Movie> fetchMovie(int id) {
        return mRepository.getMovieById(id);
    }

    // Get the Movie object which the ViewModel holds
    public Movie getMovie() {
        return mMovie;
    }

    // As we pass the Movie object from the Main to Detail Activity we need this method to allow the
    // activity to set the Movie object
    // As soon as this method gets called the ViewModel also adds Details (Reviews, Videos) to the Movie object
    public void setMovie(Movie movie) {
        this.mMovie = movie;
        MutableLiveData<Movie> details = mRepository.getMovieDetails(movie);
        details.observeForever(new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                notifiyRecyclerView();
                details.removeObserver(this);
            }
        });
    }

    private void notifiyRecyclerView() {
        mVideoAdapter.notifyDataSetChanged();
        mReviewAdapter.notifyDataSetChanged();
    }

    public void favoriteButtonTap() {
        if (mMovie.isFavorite()) {
            mRepository.deleteMovie(mMovie);
            mMovie.setFavorite(false);
            mBtnFavoriteText.postValue(mContext.getString(R.string.mark_as_favorite));
        } else {
            mMovie.setFavorite(true);
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

    public void setupVideoRecyclerView(RecyclerView recyclerView) {
        this.mVideoAdapter = new VideoAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.mVideoAdapter);
    }

    public void setupReviewRecyclerView(RecyclerView recyclerView) {
        this.mReviewAdapter = new ReviewAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.mReviewAdapter);
    }
}
