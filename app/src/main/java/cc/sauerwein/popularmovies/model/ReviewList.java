package cc.sauerwein.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewList {
    @SerializedName("results")
    private List<Review> mReviews;

    public List<Review> getReviews() {
        return mReviews;
    }
}
