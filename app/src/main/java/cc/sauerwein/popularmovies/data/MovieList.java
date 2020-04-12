package cc.sauerwein.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {
    @SerializedName("results")
    private List<Movie> mMovies;

    public List<Movie> getMovies() {
        return mMovies;
    }

    public void setMovies(List<Movie> mMovies) {
        this.mMovies = mMovies;
    }
}
