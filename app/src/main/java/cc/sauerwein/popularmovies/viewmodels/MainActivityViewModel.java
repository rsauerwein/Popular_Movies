package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();
    private final Repository mRepository;
    private Context mContext;

    // Layout data
    private final ObservableInt mLoadingVisibility;
    private final ObservableInt mErrorMessageVisibility;
    private final ObservableInt mRecyclerViewVisibility;
    private final MutableLiveData<String> mActionBarTitle;
    // Recyclerview
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList;
    private final MutableLiveData<Movie> mClickedItem;

    private boolean mFavoritesDisplayed;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mContext = getApplication().getApplicationContext();
        mRepository = Repository.getInstance(application);

        mLoadingVisibility = new ObservableInt();
        mErrorMessageVisibility = new ObservableInt();
        mRecyclerViewVisibility = new ObservableInt();
        mActionBarTitle = new MutableLiveData<>();
        mClickedItem = new MutableLiveData<>();

        mLoadingVisibility.set(View.GONE);
        mErrorMessageVisibility.set(View.GONE);
        mRecyclerViewVisibility.set(View.GONE);
        mActionBarTitle.postValue(application.getString(R.string.popular));

        Log.d(LOG_TAG, "Setup MainActivityViewModel");
    }

    public void setupRecyclerView(RecyclerView recyclerView, int spanCount) {
        this.mMovieAdapter = new MovieAdapter(this);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication().getApplicationContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(this.mMovieAdapter);
    }

    private LiveData<List<Movie>> getPopularMovies() {
        return mRepository.fetchMovies(Movie.OPTION_IS_POPULAR);
    }

    private LiveData<List<Movie>> getTopRatedMovies() {
        return mRepository.fetchMovies(Movie.OPTION_IS_TOP_RATED);
    }

    private LiveData<List<Movie>> getFavoriteMovies() {
        return mRepository.getFavoriteMovies();
    }

    public ObservableInt getLoadingVisibility() {
        return this.mLoadingVisibility;
    }

    private void setLoadingVisibility(int visibility) {
        this.mLoadingVisibility.set(visibility);
    }

    public ObservableInt getErrorMessageVisibility() {
        return this.mErrorMessageVisibility;
    }

    private void setErrorMessageVisibility(int visibility) {
        this.mErrorMessageVisibility.set(visibility);
    }

    public ObservableInt getRecyclerViewVisibility() {
        return mRecyclerViewVisibility;
    }

    private void setRecyclerViewVisibility(int visibility) {
        this.mRecyclerViewVisibility.set(visibility);
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    private void setMovieList(List<Movie> movieList) {
        if (movieList != null) {
            this.mMovieList = movieList;
            mMovieAdapter.notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("movieData must contain movies");
        }
    }

    private void resetMovieData() {
        this.mMovieList = null;
    }

    public MutableLiveData<String> getActionBarTitle() {
        return mActionBarTitle;
    }

    private void setActionBarTitle(String title) {
        this.mActionBarTitle.postValue(title);
    }

    /**
     * Performs a main activity list update
     *
     * @param option  Use the options provided by the Movie class
     */
    public void listUpdate(String option) {
        Log.d(LOG_TAG, "Perform MainActivity listUpdate");
        this.resetMovieData();
        this.setRecyclerViewVisibility(View.GONE);
        this.setLoadingVisibility(View.VISIBLE);

        LiveData<List<Movie>> result;

        switch (option) {
            case Movie.OPTION_IS_TOP_RATED:
                this.setActionBarTitle(mContext.getString(R.string.top_rated));
                result = this.getTopRatedMovies();
                mFavoritesDisplayed = false;
                break;
            case Movie.OPTION_IS_POPULAR:
                this.setActionBarTitle(mContext.getString(R.string.popular));
                result = this.getPopularMovies();
                mFavoritesDisplayed = false;
                break;
            case Movie.OPTION_IS_FAVORITE:
                this.setActionBarTitle(mContext.getString(R.string.my_favorites));
                result = this.getFavoriteMovies();
                mFavoritesDisplayed = true;
                break;
            default:
                Log.wtf(LOG_TAG, "Passed illegal argument to listUpdate");
                throw new IllegalArgumentException();
        }
        result.observeForever(new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                result.removeObserver(this);
                setLoadingVisibility(View.GONE);
                setErrorMessageVisibility(View.GONE);

                // Check if the result contains data
                if (movies != null && movies.size() != 0) {
                    setRecyclerViewVisibility(View.VISIBLE);
                    setMovieList(movies);
                } else {
                    // Result contains no data and local favorite db is empty
                    if (Objects.equals(option, Movie.OPTION_IS_FAVORITE)) {
                        setErrorMessageVisibility(View.VISIBLE);
                    } else {
                        // Device seems to be offline but locally stored favorites are available
                        listUpdate(Movie.OPTION_IS_FAVORITE);
                        Toast toast = Toast.makeText(mContext, mContext.getString(R.string.error), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
    }

    public void activityResumed() {
        // Refresh the favorites list as the user may have removed the movie from favorites
        if (mFavoritesDisplayed) listUpdate(Movie.OPTION_IS_FAVORITE);
    }

    public void posterClick(int position) {
        mClickedItem.postValue(mMovieList.get(position));
    }

    public MutableLiveData<Movie> getClickedItem() {
        return mClickedItem;
    }

    public void resetClickedItem() {
        mClickedItem.postValue(null);
    }

    public void toggleSort(MenuItem item) {
        Application application = getApplication();

        if (item.getTitle().toString().equals(application.getString(R.string.most_popular_menu))) {
            listUpdate(Movie.OPTION_IS_POPULAR);
            item.setTitle(application.getString(R.string.top_rated_menu));
        } else if (item.getTitle().toString().equals(application.getString(R.string.top_rated_menu))) {
            listUpdate(Movie.OPTION_IS_TOP_RATED);
            item.setTitle(application.getString(R.string.most_popular_menu));
        }
    }
}
