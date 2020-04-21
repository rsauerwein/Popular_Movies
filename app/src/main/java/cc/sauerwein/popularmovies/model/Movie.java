package cc.sauerwein.popularmovies.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cc.sauerwein.popularmovies.R;


/**
 * Stores all relevant movie metadata
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
    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private String voteAverage;
    @SerializedName("vote_count")
    @ColumnInfo(name = "vote_count")
    private String voteCount;
    private boolean isFavorite;
    // Todo figure out how to store videos and reviews in room
    @Ignore
    private List<Video> videos;
    @Ignore
    private List<Review> reviews;

    // Options
    public final static String OPTION_IS_FAVORITE = "is-favorite";
    public final static String OPTION_IS_TOP_RATED = "is-top-rated";
    public final static String OPTION_IS_POPULAR = "is-popular";

    public Movie(int id, String title, String overview, Date releaseDate, String posterPath, String voteAverage) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
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

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getFormatedUserRating() {
        return this.voteAverage + "/10";
    }

    @BindingAdapter("moviePoster")
    public static void loadImage(ImageView view, String url) {
        Picasso.get()
                .load(url)
                .error(R.drawable.ic_error_red_24dp)
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

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }
}
