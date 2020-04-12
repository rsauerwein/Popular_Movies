package cc.sauerwein.popularmovies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
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
    private final Api.ApiInterface mRetrofitService;
    private Callback<MovieList> callback = new Callback<MovieList>() {
        @Override
        public void onResponse(Call<MovieList> call, Response<MovieList> response) {

        }

        @Override
        public void onFailure(Call<MovieList> call, Throwable t) {

        }
    };

    public static Repository getInstance(Application application) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new repository instance");
                sInstance = new Repository(application);
            }
        }
        Log.d(LOG_TAG, "Getting the repository instance");
        return sInstance;
    }

    public Repository(Application application) {
        this.mDb = AppDatabase.getInstance(application);
        this.mRetrofitService = Api.getApi();
    }

    public LiveData<Movie> getMovieById(int id) {
        return mDb.movieDao().loadMovieById(id);
    }

    public void deleteMovie(Movie movie) {
        mDb.movieDao().deleteMovie(movie);
    }

    public void insertMovie(Movie movie) {
        mDb.movieDao().insertMovie(movie);
    }

    // Todo methods renaming
    public LiveData<List<Movie>> getAllMovies() {
        return mDb.movieDao().loadAllMovies();
    }

    // Todo - is it a good idea to share the same callback objects in different methods
    public void getPopularMovies() {
        mRetrofitService.getPopularMovies(Api.getApiKey()).enqueue(callback);
    }

    public void getTopRatedMovies() {
        mRetrofitService.getTopRatedMovies(Api.getApiKey()).enqueue(callback);
    }
}
