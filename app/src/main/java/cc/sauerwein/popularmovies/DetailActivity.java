package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;

    private TextView mMovieTitleTv;
    private TextView mMovieDescriptionTv;
    private TextView mUserRating;
    private TextView mReleaseDate;
    private ImageView mMovieThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        populateUI();
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
        Uri posterUri = NetworkUtils.createPosterUri(posterPath);
        Picasso.get().load(posterUri).into(mMovieThumbnail);
        mMovieThumbnail.setContentDescription(mMovie.getTitle());
    }
}
