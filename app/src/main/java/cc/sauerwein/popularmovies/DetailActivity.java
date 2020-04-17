package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.databinding.ActivityDetailBinding;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private MovieDetailViewModel mViewModel;
    private ActivityDetailBinding mActivityBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getString(R.string.movie_detail));

        mViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        mActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        mActivityBinding.setLifecycleOwner(this);
        mActivityBinding.setViewModel(mViewModel);

        Intent intent = getIntent();
        int json = intent.getIntExtra(Intent.EXTRA_TEXT, -1);

        mViewModel.fetchMovie(json);

//                String posterPath = movie.getPosterPath();
//                Uri posterUri = NetworkUtils.createPosterUri(posterPath);
//                Picasso.get().load(posterUri).into(mActivityBinding.ivMoviePosterThumbnail);
    }

    public void favorite(View view) {
        mViewModel.favoriteButtonTap();
        Log.d(LOG_TAG, "click favorite");
    }
}
