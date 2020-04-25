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

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.Repository;
import cc.sauerwein.popularmovies.model.Movie;

public class MainActivityViewModel extends AndroidViewModel {

    private static final String LOG_TAG = MainActivityViewModel.class.getSimpleName();
    // Options for listUpdate
    public final String POPULAR_MOVIES = "popular-movies";
    private final String TOP_RATED_MOVIES = "top-rated-movies";
    public final String MY_FAVORITES = "my-favorites";
    private final Repository mRepository;
    // Layout data
    private final ObservableInt mLoadingVisibility;
    private final ObservableInt mErrorMessageVisibility;
    private final ObservableInt mRecyclerViewVisibility;
    private final MutableLiveData<String> mActionBarTitle;
    // Recyclerview
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList;
    private final MutableLiveData<Movie> mClickedItem;


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
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

    public void listUpdate(String option, Context context) {
        Log.d(LOG_TAG, "Perform MainActivity listUpdate");
        this.resetMovieData();
        this.setRecyclerViewVisibility(View.GONE);
        this.setLoadingVisibility(View.VISIBLE);

        LiveData<List<Movie>> result;

        switch (option) {
            case TOP_RATED_MOVIES:
                this.setActionBarTitle(context.getString(R.string.top_rated));
                result = this.getTopRatedMovies();
                break;
            case POPULAR_MOVIES:
                this.setActionBarTitle(context.getString(R.string.popular));
                result = this.getPopularMovies();
                break;
            case MY_FAVORITES:
                this.setActionBarTitle(context.getString(R.string.my_favorites));
                result = this.getFavoriteMovies();
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
                    if (option == MY_FAVORITES) {
                        mErrorMessageVisibility.set(View.VISIBLE);
                    } else {
                        // Device seems to be offline but locally stored favorites are available
                        listUpdate(MY_FAVORITES, context);
                        Toast toast = Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });
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
            listUpdate(POPULAR_MOVIES, application.getApplicationContext());
            item.setTitle(application.getString(R.string.top_rated_menu));
        } else if (item.getTitle().toString().equals(application.getString(R.string.top_rated_menu))) {
            listUpdate(TOP_RATED_MOVIES, application.getApplicationContext());
            item.setTitle(application.getString(R.string.most_popular_menu));
        }
    }
}
