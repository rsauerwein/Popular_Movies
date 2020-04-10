package cc.sauerwein.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import cc.sauerwein.popularmovies.data.Movie;

public class MovieList {
    @SerializedName("results")
    private List<Movie> mMovies;

    public List<Movie> getMovies() {
        return mMovies;
    }
}
