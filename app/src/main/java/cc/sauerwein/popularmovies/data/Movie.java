package cc.sauerwein.popularmovies.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


/**
 * Stores all relevant movie metadata
 * As we create Movie objects with gson, we don't need a constructor
 */
@Entity(tableName = "movies")
public class Movie {
    @PrimaryKey
    private int id;
    private String title;
    private String overview;
    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private Date releaseDate;
    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private String userRating;
    private boolean isFavorite;

    public Movie(int id, String title, String overview, Date releaseDate, String posterPath, String userRating) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.userRating = userRating;
    }

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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
