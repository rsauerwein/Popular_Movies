package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import cc.sauerwein.popularmovies.databinding.ActivityDetailBinding;
import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.model.Video;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

public class DetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private MovieDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreate");
        super.onCreate(savedInstanceState);
        ActivityDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        setTitle(getString(R.string.movie_detail));

        mViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setViewModel(mViewModel);


        setSupportActionBar(binding.detailActivityToolbar);
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
        queryresult.observe(this, movie1 -> {
            if (movie1 == null) {
                mViewModel.setmBtnFavoriteText(getString(R.string.mark_as_favorite));
            } else {
                mViewModel.setmBtnFavoriteText(getString(R.string.remove_from_favorites));
                mViewModel.getMovie().setFavorite(true);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                share();
                break;
            default:
                Log.wtf(LOG_TAG, "onOptionsItemSelected called with unknown item");
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void favorite(View view) {
        mViewModel.favoriteButtonTap();
        Log.d(LOG_TAG, "click favorite");
    }

    public void playVideo(View view) {
        Video video = mViewModel.getMovie().getVideos().get(Integer.parseInt(view.getTag().toString()));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(video.getYoutubeUrl()));
        startActivity(intent);
    }

    public void share() {
        if (mViewModel.getMovie().getVideos().size() > 0) {
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,
                    getString(R.string.share_message) + mViewModel.getMovie().getVideos().get(0).getYoutubeUrl());

            try {
                startActivity(Intent.createChooser(intent, getString(R.string.share_prompt)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(this, getString(R.string.share_error), Toast.LENGTH_LONG);
            toast.show();
        }

    }
}
