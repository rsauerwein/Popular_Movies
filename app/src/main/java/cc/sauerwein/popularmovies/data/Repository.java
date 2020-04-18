package cc.sauerwein.popularmovies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import cc.sauerwein.popularmovies.data.db.AppDatabase;
import cc.sauerwein.popularmovies.data.network.Api;
import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.model.MovieList;
import cc.sauerwein.popularmovies.utilities.AppExecutors;
import retrofit2.Response;

/**
 * Singleton class
 * Use getInstance to get a Repository object
 */
public class Repository {

    private static final String LOG_TAG = Repository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final AppDatabase mDb;
    private static AppExecutors sAppExecutors;

    public Repository(Application application) {
        this.mDb = AppDatabase.getInstance(application);
    }

    public static Repository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new repository instance");
                sInstance = new Repository(application);
                sAppExecutors = AppExecutors.getInstance();
            }
        }
        Log.d(LOG_TAG, "Getting the repository instance");
        return sInstance;
    }

    public LiveData<Movie> getMovieById(int id) {
        return mDb.movieDao().loadMovieById(id);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mDb.movieDao().loadAllMovies();
    }

    public void deleteMovie(Movie movie) {
        mDb.movieDao().deleteMovie(movie);
    }

    public void insertMovie(List<Movie> movies) {
        mDb.movieDao().insertMovies(movies);
    }

    public void updateMovie(Movie movie) {
        sAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mDb.movieDao().updateMovie(movie);
            }
        });
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mDb.movieDao().loadAllMovies();
    }

    // Todo avoid unnecessary API calls
    public LiveData<List<Movie>> getPopularMovies() {
        sAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Response<MovieList> response = Api.fetchMovies(Movie.OPTION_IS_POPULAR);
                mDb.movieDao().insertMovies(response.body().getMovies());
            }
        });
        return mDb.movieDao().loadPopularMovies();
    }

    public LiveData<List<Movie>> getTopRatedMovies() {
        sAppExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Response<MovieList> response = Api.fetchMovies(Movie.OPTION_IS_TOP_RATED);
                mDb.movieDao().insertMovies(response.body().getMovies());
            }
        });
        return mDb.movieDao().loadTopRatedMovies();
    }
}
