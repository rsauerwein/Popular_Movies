package cc.sauerwein.popularmovies;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.data.MovieList;
import cc.sauerwein.popularmovies.databinding.ActivityMainBinding;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MenuItem mMostPopular;
    private MenuItem mTopRated;

    private MainActivityViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(mViewModel);

        mViewModel.setupRecyclerview(binding.rvMovieOverview);

        listUpdate();
    }

    private void listUpdate() {
        mViewModel.setLoadingVisibility(View.VISIBLE);
        mViewModel.getPopularMovies().observe(this, new Observer<MovieList>() {
            @Override
            public void onChanged(MovieList movieList) {
                mViewModel.setLoadingVisibility(View.GONE);
                mViewModel.setMovieListInAdapter(movieList);
                Log.d(LOG_TAG, "Call onChanged");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main, menu);
//        mMostPopular = menu.findItem(R.id.action_most_popular);
//        mTopRated = menu.findItem(R.id.action_top_rated);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        mMovieAdapter.resetMovieData();
//
//        switch (item.getItemId()) {
//            case R.id.action_top_rated:
//// Todo
//                //                loadMovieData(NetworkUtils.OPTION_TOP_RATED_MOVIES_MOVIES);
//                mTopRated.setVisible(false);
//                mMostPopular.setVisible(true);
//                break;
//            case R.id.action_most_popular:
//                // Todo
//                //loadMovieData(NetworkUtils.OPTION_POPULAR_MOVIES);
//                mMostPopular.setVisible(false);
//                mTopRated.setVisible(true);
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
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
//        Intent intent = new Intent(this, cc.sauerwein.popularmovies.DetailActivity.class);
//        intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(movie));
//        startActivity(intent);
    }
}
