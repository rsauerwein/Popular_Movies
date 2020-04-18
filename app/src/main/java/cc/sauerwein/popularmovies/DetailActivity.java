package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import cc.sauerwein.popularmovies.databinding.ActivityDetailBinding;
import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private MovieDetailViewModel mViewModel;
    private ActivityDetailBinding mActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setTitle(getString(R.string.movie_detail));

        mViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        mActivityBinding.setLifecycleOwner(this);
        mActivityBinding.setViewModel(mViewModel);


        Intent intent = getIntent();
        String json = intent.getStringExtra(Intent.EXTRA_TEXT);
        Movie movie = new Gson().fromJson(json, Movie.class);
        mViewModel.setMovie(movie);
        mActivityBinding.setImageUrl(movie.getMoviePosterUrl());

        LiveData<Movie> queryresult = mViewModel.fetchMovie(movie.getId());
        queryresult.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie == null) return;
            }
        });
    }

    public void favorite(View view) {
        mViewModel.favoriteButtonTap();
        Log.d(LOG_TAG, "click favorite");
    }
}
