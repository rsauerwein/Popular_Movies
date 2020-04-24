package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.net.Uri;
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


        setSupportActionBar(mActivityBinding.detailActivityToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String json = intent.getStringExtra(Intent.EXTRA_TEXT);
        Movie movie = new Gson().fromJson(json, Movie.class);

        // Avoid unnecessary api calls
        if (savedInstanceState == null) {
            mViewModel.setMovie(movie);
        }

        // Query the Room database
        // If the query contains a result, the movie is a favorite movie.
        LiveData<Movie> queryresult = mViewModel.fetchMovie(movie.getId());
        queryresult.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie == null) {
                    mViewModel.setmBtnFavoriteText(getString(R.string.mark_as_favorite));
                } else {
                    mViewModel.setmBtnFavoriteText("Remove from favorites");
                    mViewModel.getMovie().setFavorite(true);
                }
            }
        });
    }

    public void favorite(View view) {
        mViewModel.favoriteButtonTap();
        Log.d(LOG_TAG, "click favorite");
    }

    public void playVideo(View view) {
        String key = mViewModel.getMovie().getVideos().get(Integer.valueOf(view.getTag().toString())).getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
        startActivity(intent);
    }
}
