package cc.sauerwein.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Stores data from /movie/{movie_id}/videos
 * https://developers.themoviedb.org/3/movies/get-movie-videos
 */
public class VideoList {
    @SerializedName("results")
    private List<Video> mVideos;

    public List<Video> getVideos() {
        return mVideos;
    }
}
