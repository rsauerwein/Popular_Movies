package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.databinding.ActivityMainBinding;
import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mMovieAdapter;

    private MainActivityViewModel mViewModel;
    private final String POPULAR_MOVIES = "popular-movies";
    private final String TOP_RATED_MOVIES = "top-rated-movies";
    private final String MY_FAVORITES = "my-favorites";
    private ActivityMainBinding mMainBinding;
    private MenuItem mMostPopular;
    private MenuItem mTopRated;
    private MenuItem mMyFavorites;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Setup ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainBinding.setViewModel(mViewModel);

        // bind RecyclerView
        GridLayoutManager layoutManager = new GridLayoutManager(getApplication().getApplicationContext(), 2);
        RecyclerView recyclerView = mMainBinding.rvMovieOverview;
        mMovieAdapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mMovieAdapter);

        // Setup Toolbar
        mToolbar = mMainBinding.mainActivityToolbar;
        setSupportActionBar(mToolbar);

        listUpdate(POPULAR_MOVIES);
    }

    private void listUpdate(String option) {
        Log.d(LOG_TAG, "Perform MainActivity listUpdate");
        mMovieAdapter.resetMovieData();
        mViewModel.setLoadingVisibility(View.VISIBLE);

        LiveData<List<Movie>> result;

        switch (option) {
            case TOP_RATED_MOVIES:
                result = mViewModel.getTopRatedMovies();
                break;
            case POPULAR_MOVIES:
                result = mViewModel.getPopularMovies();
                break;
            case MY_FAVORITES:
                result = mViewModel.getFavoriteMovies();
                break;
            default:
                Log.wtf(LOG_TAG, "Passed illegal argument to listUpdate");
                throw new IllegalArgumentException();
        }
        result.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mViewModel.setLoadingVisibility(View.GONE);
                if (movies != null) {
                    mMovieAdapter.setMovieData(movies);
                } else {
                    mViewModel.setErrorMessageVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mMostPopular = menu.findItem(R.id.action_most_popular);
        mTopRated = menu.findItem(R.id.action_top_rated);
        mMyFavorites = menu.findItem(R.id.action_my_favorites);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_top_rated:
                mToolbar.setTitle(getString(R.string.top_rated));
                listUpdate(TOP_RATED_MOVIES);
                mTopRated.setVisible(false);
                mMostPopular.setVisible(true);
                mMyFavorites.setVisible(true);
                break;
            case R.id.action_most_popular:
                mToolbar.setTitle(R.string.popular);
                listUpdate(POPULAR_MOVIES);
                mMostPopular.setVisible(false);
                mTopRated.setVisible(true);
                mMyFavorites.setVisible(true);
                break;
            case R.id.action_my_favorites:
                mToolbar.setTitle(getString(R.string.my_favorites));
                listUpdate(MY_FAVORITES);
                mTopRated.setVisible(true);
                mMostPopular.setVisible(true);
                mMyFavorites.setVisible(false);
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showMovies() {
//        mErrorMessageTv.setVisibility(View.INVISIBLE);
//        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
//        mRecyclerView.setVisibility(View.INVISIBLE);
//        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, cc.sauerwein.popularmovies.DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(movie));
        startActivity(intent);
    }
}
