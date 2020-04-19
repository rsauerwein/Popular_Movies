package cc.sauerwein.popularmovies.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
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

    // Options
    public final static String OPTION_IS_FAVORITE = "is-favorite";
    public final static String OPTION_IS_TOP_RATED = "is-top-rated";
    public final static String OPTION_IS_POPULAR = "is-popular";

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

    public String getFormatedReleaseDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.releaseDate);
        return String.format("%1d", calendar.get(Calendar.YEAR));
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

    public String getFormatedUserRating() {
        return this.userRating + "/10";
    }

    @BindingAdapter("moviePoster")
    public static void loadImage(ImageView view, String url) {
        Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(view, new Callback() {
                    // Fix the issue that the application stops working after deleting application data
                    // https://stackoverflow.com/questions/23978828/how-do-i-use-disk-caching-in-picasso
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(url)
                                .into(view);
                    }
                });
    }

    public String getMoviePosterUrl() {
        return "https://image.tmdb.org/t/p/w185/" + posterPath;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
