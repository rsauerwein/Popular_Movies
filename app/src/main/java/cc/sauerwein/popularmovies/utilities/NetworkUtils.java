package cc.sauerwein.popularmovies.utilities;

import android.net.Uri;

public class NetworkUtils {

//    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";
private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";
//
//    public static final String OPTION_POPULAR_MOVIES = "movie/popular";
//    public static final String OPTION_TOP_RATED_MOVIES_MOVIES = "movie/top_rated";
//
//    private static final String API_KEY_PARAM = "api_key";
//    private static final String API_KEY = ApiKey.API_KEY; //Insert your API key here
//
private static final String DEFAULT_IMAGE_SIZE = "w185";


    /**
     * @param posterPath Stored in the Movie object
     * @return URI to the movie poster - for example http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg
     */
    public static Uri createPosterUri(String posterPath) {

        return Uri.parse(IMAGE_BASE_URL).buildUpon()
                .appendPath(DEFAULT_IMAGE_SIZE)
                .appendEncodedPath(posterPath)
                .build();
    }
}
