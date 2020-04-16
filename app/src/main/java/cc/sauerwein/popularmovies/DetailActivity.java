package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.databinding.ActivityDetailBinding;
import cc.sauerwein.popularmovies.utilities.NetworkUtils;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

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
        mActivityBinding.setViewModel(mViewModel);

        Intent intent = getIntent();
        int json = intent.getIntExtra(Intent.EXTRA_TEXT, -1);

        mViewModel.getMovie(json).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                mActivityBinding.tvMovieTitle.setText(movie.getTitle());
                mActivityBinding.tvDescription.setText(movie.getOverview());
                mActivityBinding.tvUserRating.setText(getString(R.string.movie_rating, movie.getUserRating()));

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(movie.getReleaseDate());
                mActivityBinding.tvReleaseDate.setText(String.format("%1d", calendar.get(Calendar.YEAR)));

                String posterPath = movie.getPosterPath();
                Uri posterUri = NetworkUtils.createPosterUri(posterPath);
                Picasso.get().load(posterUri).into(mActivityBinding.ivMoviePosterThumbnail);
                mActivityBinding.ivMoviePosterThumbnail.setContentDescription(movie.getTitle());
            }
        });
    }

    public void favorite(View view) {

//        Repository repository = Repository.getInstance(this);
//        AppExecutors.getInstance().diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                Movie movie = repository.getMovieById(mMovie.getId());
//
//                if (movie == null) {
//                    repository.insertMovie(mMovie);
//                    Log.d(LOG_TAG, "Add movie to favorites");
//                } else {
//                    repository.deleteMovie(movie);
//                    Log.d(LOG_TAG, "Remove movie from favorites");
//                }
//            }
//        });

    }
}
