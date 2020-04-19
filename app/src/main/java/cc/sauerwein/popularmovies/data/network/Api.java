package cc.sauerwein.popularmovies.data.network;

import android.util.Log;

import java.io.IOException;

import cc.sauerwein.popularmovies.model.Movie;
import cc.sauerwein.popularmovies.model.MovieList;
import cc.sauerwein.popularmovies.preferences.ApiKey;
import cc.sauerwein.popularmovies.utilities.InternetCheck;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Api {
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = ApiKey.API_KEY; //Insert your API key here
    private static ApiInterface api;
    private static final String LOG_TAG = Api.class.getSimpleName();

    public static ApiInterface getApi() {
        if (api == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Api.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            api = retrofit.create(ApiInterface.class);
        }

        return api;
    }

    public interface ApiInterface {
        @GET("movie/popular")
        Call<MovieList> fetchPopularMovies(@Query("api_key") String api_key);

        @GET("movie/top_rated")
        Call<MovieList> fetchTopRatedMovies(@Query("api_key") String api_key);
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static Response<MovieList> fetchMovies(String option) {
        ApiInterface api = Api.getApi();
        Call<MovieList> call;

        // Todo howto interrupt the request in case internet=false?
        new InternetCheck(internet -> {
            if (internet) {
                Log.d(LOG_TAG, "Device online");
            } else {
                Log.d(LOG_TAG, "Device offline");
            }
        });

        switch (option) {
            case Movie.OPTION_IS_POPULAR:
                call = api.fetchPopularMovies(API_KEY);
                break;
            case Movie.OPTION_IS_TOP_RATED:
                call = api.fetchTopRatedMovies(API_KEY);
                break;
            default:
                throw new IllegalArgumentException("Method call with invalid option");
        }

        try {
            return call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
