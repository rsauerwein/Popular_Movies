package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import cc.sauerwein.popularmovies.adapter.MovieAdapter;
import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageTv;

    private MenuItem mMostPopular;
    private MenuItem mTopRated;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie_overview);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessageTv = findViewById(R.id.tv_error_message);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        // Todo
//        mRetrofit = new Retrofit.Builder()
//                .baseUrl(NetworkUtils.API_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//
//        mRetrofitService = mRetrofit.create(GetDataService.class);
//
//        loadMovieData(NetworkUtils.OPTION_POPULAR_MOVIES);
        setupViewModel();
    }

    private void setupViewModel() {
        ViewModel viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }
// Todo
//    private void loadMovieData(final String option) {
//        mLoadingIndicator.setVisibility(View.VISIBLE);
//        mRecyclerView.setVisibility(View.INVISIBLE);
//        mErrorMessageTv.setVisibility(View.INVISIBLE);
//
//        new InternetCheck(internet -> {
//            if (internet) {
//                if (option.equals(NetworkUtils.OPTION_POPULAR_MOVIES)) {
//                    setTitle(getString(R.string.popular));
//                    getPopularMoviesFromApi();
//                } else {
//                    getTopRatedMoviesFromApi();
//                    setTitle(getString(R.string.top_rated));
//                }
//            } else {
//                showErrorMessage();
//                mLoadingIndicator.setVisibility(View.INVISIBLE);
//            }
//        });
//
//    }

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
// Todo
//    public void getPopularMoviesFromApi() {
//        Call<MovieList> task = mRetrofitService.getPopularMovies(API_KEY);
//        new CallApi().execute(task);
//    }
//
//    public void getTopRatedMoviesFromApi() {
//        Call<MovieList> task = mRetrofitService.getTopRatedMovies(API_KEY);
//        new CallApi().execute(task);
//    }
//
//    public interface GetDataService {
//        @GET("movie/popular")
//        Call<MovieList> getPopularMovies(@Query("api_key") String api_key);
//
//        @GET("movie/top_rated")
//        Call<MovieList> getTopRatedMovies(@Query("api_key") String api_key);
//    }
//
//    private class CallApi extends AsyncTask<Call<MovieList>, Void, MovieList> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected MovieList doInBackground(Call<MovieList>... calls) {
//            try {
//                return calls[0].execute().body();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(MovieList movieList) {
//            super.onPostExecute(movieList);
//            mLoadingIndicator.setVisibility(View.INVISIBLE);
//
//            try {
//                mMovieAdapter.setMovieData(movieList.getMovies());
//                showMovies();
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                showErrorMessage();
//            }
//        }
//    }
}
