package cc.sauerwein.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;

import cc.sauerwein.popularmovies.databinding.ActivityMainBinding;
import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MainActivityViewModel mViewModel;
    private ActivityMainBinding mMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup ViewModel
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainBinding.setViewModel(mViewModel);
        mMainBinding.setLifecycleOwner(this);

        // setup RecyclerView
        mViewModel.setupRecyclerView(mMainBinding.rvMovieOverview, 2);

        // Setup Toolbar
        setSupportActionBar(mMainBinding.mainActivityToolbar);

        setupClickListener();

        // Avoid unnecessary API calls
        if (savedInstanceState == null) {
            mViewModel.listUpdate(mViewModel.POPULAR_MOVIES, this);
        } else {
            // Fix the issue that the ActionBar contains the application title after rotating the device
            getSupportActionBar().setTitle(mViewModel.getActionBarTitle().getValue());
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_sort:
                mViewModel.toggleSort(item);
                break;
            case R.id.action_my_favorites:
                mViewModel.listUpdate(mViewModel.MY_FAVORITES, this);
                break;
            default:
                Log.wtf(LOG_TAG, "onOptionsItemSelected called with unknown item");
                return super.onOptionsItemSelected(item);
        }
        mMainBinding.rvMovieOverview.scrollToPosition(0);
        return true;
    }

    public void setupClickListener() {
        mViewModel.getClickedItem().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                if (movie != null) {
                    Intent intent = new Intent(getApplicationContext(), cc.sauerwein.popularmovies.DetailActivity.class);
                    intent.putExtra(Intent.EXTRA_TEXT, new Gson().toJson(movie));
                    startActivity(intent);
                    Log.d(LOG_TAG, "Call clickListener");

                    // Perhaps not the best solution..
                    // Workaround for the issue that onChanged get's triggered again when rotating device
                    // when the ViewModel already stores a clicked item
                    mViewModel.resetClickedItem();
                }
            }
        });
    }
}
