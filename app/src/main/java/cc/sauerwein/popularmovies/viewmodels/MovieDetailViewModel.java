package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    private final MutableLiveData<String> mBtnFavoriteText;
    private final MutableLiveData<Integer> mNoReviewsNoticeVisibility;
    private final MutableLiveData<Integer> mNoTrailersNoticeVisibility;
    private final Context mContext;
    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;


    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);

        mBtnFavoriteText = new MutableLiveData<>();

        mNoReviewsNoticeVisibility = new MutableLiveData<>();
        mNoReviewsNoticeVisibility.postValue(View.GONE);

        mNoTrailersNoticeVisibility = new MutableLiveData<>();
        mNoTrailersNoticeVisibility.postValue(View.GONE);

        mContext = application.getApplicationContext();
        Log.d(TAG, "Call MovieDetailViewModel constructor with arguments");
    }

    /**
     * @return Movie object which the ViewModel holds
     */
    public Movie getMovie() {
        return mMovie;
    }

    /**
     * As we pass the Movie object from the Main to Detail Activity we need this method to allow the
     * activity to pass the Movie object to the ViewModel
     *
     * @param movie Movie object passed by the MainActivity to the DetailAcitvity
     */
    public void setMovie(Movie movie) {
        this.mMovie = movie;

        checkIfFavorite();
        fetchMovieDetails();
    }

    /**
     * Fetch trailers and reviews
     */
    private void fetchMovieDetails() {
        MutableLiveData<Movie> details = mRepository.getMovieDetails(mMovie);
        details.observeForever(new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                details.removeObserver(this);

                if (movie == null) {
                    Toast toast = Toast.makeText(mContext, mContext.getString(R.string.detail_error), Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    mVideoAdapter.notifyDataSetChanged();
                    mReviewAdapter.notifyDataSetChanged();

                    if (movie.getReviews().size() == 0) {
                        mNoReviewsNoticeVisibility.postValue(View.VISIBLE);
                    }

                    if (movie.getVideos().size() == 0) {
                        mNoTrailersNoticeVisibility.postValue(View.VISIBLE);
                    }
                }
            }
        });
    }

    /**
     * Checks if mMovie is already stored in the room database
     * If yes, the movie is a favorite
     */
    private void checkIfFavorite() {
        LiveData<Movie> queryresult = mRepository.getMovieById(mMovie.getId());

        queryresult.observeForever(new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                queryresult.removeObserver(this);
                if (movie != null) {
                    mMovie.setFavorite(true);
                    mBtnFavoriteText.postValue(mContext.getString(R.string.remove_from_favorites));
                } else {
                    mBtnFavoriteText.postValue(mContext.getString(R.string.mark_as_favorite));
                }
            }
        });
    }

    public void favoriteButtonTap() {
        if (mMovie.isFavorite()) {
            mRepository.deleteMovie(mMovie);
            mMovie.setFavorite(false);
            mBtnFavoriteText.postValue(mContext.getString(R.string.mark_as_favorite));
        } else {
            mMovie.setFavorite(true);
            mRepository.insertMovie(mMovie);
            mBtnFavoriteText.postValue(mContext.getString(R.string.remove_from_favorites));
        }
    }

    public MutableLiveData<String> getmBtnFavoriteText() {
        return mBtnFavoriteText;
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

    public MutableLiveData<Integer> getNoReviewsNoticeVisibility() {
        return mNoReviewsNoticeVisibility;
    }

    public MutableLiveData<Integer> getNoTrailersNoticeVisibility() {
        return mNoTrailersNoticeVisibility;
    }
}
