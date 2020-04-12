package cc.sauerwein.popularmovies.data;

import cc.sauerwein.popularmovies.preferences.ApiKey;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Todo cleanup member variables
public class Api {

    public static final String OPTION_POPULAR_MOVIES = "movie/popular";
    public static final String OPTION_TOP_RATED_MOVIES_MOVIES = "movie/top_rated";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
    private static final String API_KEY_PARAM = "api_key";


    private static final String DEFAULT_IMAGE_SIZE = "w185";
    private static final String API_BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = ApiKey.API_KEY; //Insert your API key here
    private static ApiInterface api;
    private ApiInterface mRetrofitService;

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
        Call<MovieList> getPopularMovies(@Query("api_key") String api_key);

        @GET("movie/top_rated")
        Call<MovieList> getTopRatedMovies(@Query("api_key") String api_key);
    }

    // Todo where to implement the internet check?
//    private void loadMovieData(final String option) {
//        new InternetCheck(internet -> {
//            if (internet) {
//                if (option.equals(Api.OPTION_POPULAR_MOVIES)) {
//                    getPopularMovies();
//                } else {
//                    getTopRatedMovies();
//                }
//            } else {
//                // Todo
//            }
//        });
//    }

    // Todo ab ins Repo
//    public void getPopularMovies() {
//        Call<MovieList> task = mRetrofitService.getPopularMovies(API_KEY);
//        new CallApi().execute(task);
//    }
//
//    public void getTopRatedMovies() {
//        Call<MovieList> task = mRetrofitService.getTopRatedMovies(API_KEY);
//        new CallApi().execute(task);
//    }

// Todo wsl obsolete
//    private class CallApi extends AsyncTask<Call<MovieList>, Void, MovieList> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected MovieList doInBackground(Call<MovieList>... calls) {
//            try {
//                return calls[0].execute().body();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(MovieList movieList) {
//            super.onPostExecute(movieList);
//
//            try {
//                //mMovieAdapter.setMovieData(movieList.getMovies());
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//                // Todo
//            }
//        }
//    }
}
