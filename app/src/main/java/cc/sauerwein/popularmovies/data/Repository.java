package cc.sauerwein.popularmovies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import cc.sauerwein.popularmovies.utilities.AppExecutors;
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
    private MutableLiveData<MovieList> mMovieList;
    private Callback<MovieList> callback;

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
        this.mMovieList = new MutableLiveData<>();

        this.callback = new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                Log.d(LOG_TAG, "API Request - Call onResponse");
                // Values for the UI
                mMovieList.setValue(response.body());

                // Write all Movies into the DB
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        insertMovie(response.body().getMovies());
                    }
                });
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                Log.d(LOG_TAG, "API Request - Call onFailure");
            }
        };
    }

    public LiveData<Movie> getMovieById(int id) {
        return mDb.movieDao().loadMovieById(id);
    }

    public void deleteMovie(Movie movie) {
        mDb.movieDao().deleteMovie(movie);
    }

    public void insertMovie(List<Movie> movies) {
        mDb.movieDao().insertMovie(movies);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mDb.movieDao().loadAllMovies();
    }

    public MutableLiveData<MovieList> getPopularMovies() {
        mRetrofitService.getPopularMovies(Api.getApiKey()).enqueue(callback);
        return mMovieList;
    }

    public MutableLiveData<MovieList> getTopRatedMovies() {
        mRetrofitService.getTopRatedMovies(Api.getApiKey()).enqueue(callback);
        return mMovieList;
    }
}
