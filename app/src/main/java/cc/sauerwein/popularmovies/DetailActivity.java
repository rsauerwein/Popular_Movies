package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import java.util.Calendar;

import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;

    private TextView mMovieTitleTv;
    private TextView mMovieDescriptionTv;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private ImageView mMovieThumbnail;
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle(getString(R.string.movie_detail));

        Intent intent = getIntent();
        String json = intent.getStringExtra(Intent.EXTRA_TEXT);
        mMovie = new Gson().fromJson(json, Movie.class);

        mMovieTitleTv = findViewById(R.id.tv_movie_title);
        mMovieDescriptionTv = findViewById(R.id.tv_description);
        mUserRating = findViewById(R.id.tv_user_rating);
        mReleaseDate = findViewById(R.id.tv_release_date);

        setupViewModel();
        populateUI();
    }

    private void setupViewModel() {
        MovieDetailViewModel viewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        viewModel.getMovie(mMovie);
    }

    /**
     * Populate the Movie detail information to the UI
     */
    private void populateUI() {
        mMovieTitleTv.setText(mMovie.getTitle());
        mMovieDescriptionTv.setText(mMovie.getOverview());
        mUserRating.setText(getString(R.string.movie_rating, mMovie.getUserRating()));
        mMovieThumbnail = findViewById(R.id.iv_movie_poster_thumbnail);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mMovie.getReleaseDate());
        mReleaseDate.setText(String.format("%1d", calendar.get(Calendar.YEAR)));

        String posterPath = mMovie.getPosterPath();
        //Uri posterUri = NetworkUtils.createPosterUri(posterPath);
        //Picasso.get().load(posterUri).into(mMovieThumbnail);
        mMovieThumbnail.setContentDescription(mMovie.getTitle());
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
