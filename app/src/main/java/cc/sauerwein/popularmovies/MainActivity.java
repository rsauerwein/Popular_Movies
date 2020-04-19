package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.databinding.ActivityMainBinding;
import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding mMainBinding;
    private MenuItem mMostPopular;
    private MenuItem mTopRated;
    private MenuItem mMyFavorites;

    // Options for listUpdate
    private final String POPULAR_MOVIES = "popular-movies";
    private final String TOP_RATED_MOVIES = "top-rated-movies";
    private final String MY_FAVORITES = "my-favorites";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainBinding.setViewModel(mViewModel);
        mMainBinding.setLifecycleOwner(this);

        // setup RecyclerView
        mViewModel.setupRecyclerView(mMainBinding.rvMovieOverview, new MovieAdapter(this, mViewModel));

        // Setup Toolbar
        setSupportActionBar(mMainBinding.mainActivityToolbar);

        // Avoid unnecessary API calls
        if (savedInstanceState == null) {
            mViewModel.listUpdate(POPULAR_MOVIES, this);
        }
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
                mViewModel.listUpdate(TOP_RATED_MOVIES, this);
                mTopRated.setVisible(false);
                mMostPopular.setVisible(true);
                mMyFavorites.setVisible(true);
                break;
            case R.id.action_most_popular:
                mViewModel.listUpdate(POPULAR_MOVIES, this);
                mMostPopular.setVisible(false);
                mTopRated.setVisible(true);
                mMyFavorites.setVisible(true);
                break;
            case R.id.action_my_favorites:
                mViewModel.listUpdate(MY_FAVORITES, this);
                mTopRated.setVisible(true);
                mMostPopular.setVisible(true);
                mMyFavorites.setVisible(false);
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, cc.sauerwein.popularmovies.DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(movie));
        startActivity(intent);
    }
}
