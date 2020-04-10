package cc.sauerwein.popularmovies.data;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Singleton class
 * Use getInstance to get a Repository object
 */
public class Repository {

    private static final String LOG_TAG = Repository.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static Repository sInstance;
    private final AppDatabase mDb;


    public Repository(Context context) {
        this.mDb = AppDatabase.getInstance(context);
    }

    public static Repository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new repository instance");
                sInstance = new Repository(context);
            }
        }
        Log.d(LOG_TAG, "Getting the repository instance");
        return sInstance;
    }

    public List<Movie> getAllMovies() {
        return mDb.movieDao().loadAllMovies();
    }

    public Movie getMovieById(int id) {
        return mDb.movieDao().loadMovieById(id);
    }

    public void deleteMovie(Movie movie) {
        mDb.movieDao().deleteMovie(movie);
    }

    public void insertMovie(Movie movie) {
        mDb.movieDao().insertMovie(movie);
    }
}
