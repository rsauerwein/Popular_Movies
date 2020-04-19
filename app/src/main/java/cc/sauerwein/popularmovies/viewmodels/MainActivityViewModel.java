package cc.sauerwein.popularmovies.viewmodels;

import android.app.Application;
import android.content.Context;
import android.util.Log;
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
    private Repository mRepository;

    // Layout data
    private ObservableInt mLoadingVisibility;
    private ObservableInt mErrorMessageVisibility;
    private ObservableInt mRecyclerViewVisibility;
    private MutableLiveData<String> mActionBarTitle;

    // Recyclerview
    private MovieAdapter mMovieAdapter;
    private List<Movie> mMovieList;

    // Options for listUpdate
    private final String POPULAR_MOVIES = "popular-movies";
    private final String TOP_RATED_MOVIES = "top-rated-movies";
    private final String MY_FAVORITES = "my-favorites";


    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepository = Repository.getInstance(application);

        mLoadingVisibility = new ObservableInt();
        mErrorMessageVisibility = new ObservableInt();
        mRecyclerViewVisibility = new ObservableInt();
        mActionBarTitle = new MutableLiveData<>();

        mLoadingVisibility.set(View.GONE);
        mErrorMessageVisibility.set(View.GONE);
        mRecyclerViewVisibility.set(View.GONE);
        mActionBarTitle.postValue(application.getString(R.string.popular));

        Log.d(LOG_TAG, "Setup MainActivityViewModel");
    }

    public void setupRecyclerView(RecyclerView recyclerView, MovieAdapter movieAdapter) {
        this.mMovieAdapter = movieAdapter;
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication().getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(movieAdapter);
    }

    public LiveData<List<Movie>> getPopularMovies() {
        return mRepository.fetchMovies(Movie.OPTION_IS_POPULAR);
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        return mRepository.fetchMovies(Movie.OPTION_IS_TOP_RATED);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mRepository.getFavoriteMovies();
    }

    public ObservableInt getLoadingVisibility() {
        return this.mLoadingVisibility;
    }

    public void setLoadingVisibility(int visibility) {
        this.mLoadingVisibility.set(visibility);
    }

    public ObservableInt getErrorMessageVisibility() {
        return this.mErrorMessageVisibility;
    }

    public void setErrorMessageVisibility(int visibility) {
        this.mErrorMessageVisibility.set(visibility);
    }

    public ObservableInt getRecyclerViewVisibility() {
        return mRecyclerViewVisibility;
    }

    public void setRecyclerViewVisibility(int visibility) {
        this.mRecyclerViewVisibility.set(visibility);
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public void setMovieList(List<Movie> movieList) {
        if (movieList != null) {
            this.mMovieList = movieList;
            mMovieAdapter.notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("movieData must contain movies");
        }
    }

    public void setMovieAdapter(MovieAdapter mMovieAdapter) {
        this.mMovieAdapter = mMovieAdapter;
    }

    public MovieAdapter getmMovieAdapter() {
        return mMovieAdapter;
    }

    public void resetMovieData() {
        this.mMovieList = null;
    }

    public MutableLiveData<String> getActionBarTitle() {
        return mActionBarTitle;
    }

    public void setActionBarTitle(String title) {
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
                if (movies != null) {
                    setRecyclerViewVisibility(View.VISIBLE);
                    setMovieList(movies);
                } else {
                    listUpdate(MY_FAVORITES, context);
                    Toast toast = Toast.makeText(context, context.getString(R.string.error), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }
}
