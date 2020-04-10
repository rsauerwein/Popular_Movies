package cc.sauerwein.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


/**
 * Stores all relevant movie metadata
 * As we create Movie objects with gson, we don't need a constructor
 */
public class Movie {
    private int id;
    private String title;
    private String overview;
    @SerializedName("release_date")
    private Date releaseDate;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("vote_average")
    private String userRating;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getUserRating() {
        return userRating;
    }
}
