package cc.sauerwein.popularmovies.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.io.IOException;
import java.util.List;

import cc.sauerwein.popularmovies.data.db.AppDatabase;
import cc.sauerwein.popularmovies.data.network.Api;
import cc.sauerwein.popularmovies.model.Movie;

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
    public static LiveData<List<Movie>> mMovies;

    private Repository(Application application) {
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
        sAppExecutors.diskIO().execute(() -> mDb.movieDao().deleteMovie(movie));
    }

    public void insertMovie(Movie movie) {
        sAppExecutors.diskIO().execute(() -> mDb.movieDao().insertMovie(movie));
    }

    public LiveData<List<Movie>> fetchMovies(String option) {
        return Api.fetchMovies(option);
    }


    /**
     * Checks if the movie is already stored in room (favorite)
     * and fetches more details from the API
     *
     * The returned Mutable Live Data contains either an update Movie object or null if something
     * goes wrong (no connection)
     *
     * @param movie Movie object
     * @return MutableLiveData Movie
     */
    public MutableLiveData<Movie> getMovieDetails(Movie movie) {
        final MutableLiveData<Movie> movieDetails = new MutableLiveData<>();
        Api.ApiInterface api = Api.getApi();
        String movieId = String.valueOf(movie.getId());
        String apiKey = Api.getApiKey();

        LiveData<Movie> queryresult = getMovieById(movie.getId());
        queryresult.observeForever(new Observer<Movie>() {
            @Override
            public void onChanged(Movie result) {
                queryresult.removeObserver(this);
                if (result != null) movie.setFavorite(true);
            }
        });


        sAppExecutors.networkIO().execute(() -> {
            try {
                movie.setReviews(api.fetchReviews(movieId, apiKey).execute().body().getReviews());
                movie.setVideos(api.fetchVideos(movieId, apiKey).execute().body().getVideos());
                movieDetails.postValue(movie);
            } catch (IOException e) {
                movieDetails.postValue(null);
                e.printStackTrace();
            }
        });
        return movieDetails;
    }
}
