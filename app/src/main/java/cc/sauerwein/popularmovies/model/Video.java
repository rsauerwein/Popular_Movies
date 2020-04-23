package cc.sauerwein.popularmovies.model;

/**
 * Stores data from /movie/{movie_id}/videos
 * https://developers.themoviedb.org/3/movies/get-movie-videos
 */
public class Video {
    private String name;
    private String key;
    private String site;

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }
}


