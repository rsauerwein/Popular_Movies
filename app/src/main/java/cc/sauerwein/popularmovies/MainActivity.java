package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.data.MovieList;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageTv;

    private MenuItem mMostPopular;
    private MenuItem mTopRated;

    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_overview);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTv = findViewById(R.id.tv_error_message);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.setupRecyclerview(mRecyclerView, this);

        listUpdate();
    }

    private void listUpdate() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mViewModel.getPopularMovies().observe(this, new Observer<MovieList>() {
            @Override
            public void onChanged(MovieList movieList) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                mViewModel.setMovieListInAdapter(movieList);
                Log.d(LOG_TAG, "Call onChanged");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        mMostPopular = menu.findItem(R.id.action_most_popular);
        mTopRated = menu.findItem(R.id.action_top_rated);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        mMovieAdapter.resetMovieData();

        switch (item.getItemId()) {
            case R.id.action_top_rated:
// Todo
                //                loadMovieData(NetworkUtils.OPTION_TOP_RATED_MOVIES_MOVIES);
                mTopRated.setVisible(false);
                mMostPopular.setVisible(true);
                break;
            case R.id.action_most_popular:
                // Todo
                //loadMovieData(NetworkUtils.OPTION_POPULAR_MOVIES);
                mMostPopular.setVisible(false);
                mTopRated.setVisible(true);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showMovies() {
        mErrorMessageTv.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie movie) {
        Intent intent = new Intent(this, cc.sauerwein.popularmovies.DetailActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(movie));
        startActivity(intent);
    }
}
