package cc.sauerwein.popularmovies;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cc.sauerwein.popularmovies.data.Movie;
import cc.sauerwein.popularmovies.utilities.NetworkUtils;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> mMovieData;

    private final MovieAdapterOnClickHandler mClickHandler;

    public MovieAdapter(MovieAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie movie);
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.movie_poster, parent, false);

        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        String posterPath = mMovieData.get(position).getPosterPath();
        Uri posterUri = NetworkUtils.createPosterUri(posterPath);
        Picasso.get().load(posterUri).into(holder.mMoviePosterImageView);
        holder.mMoviePosterImageView.setContentDescription(mMovieData.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null) return 0;
        return mMovieData.size();
    }

    public void resetMovieData() {
        mMovieData = null;
    }

    public void setMovieData(List<Movie> movieData) throws IllegalArgumentException {
        if (movieData != null) {
            this.mMovieData = movieData;
            notifyDataSetChanged();
        } else {
            throw new IllegalArgumentException("movieData must contain movies");
        }
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mMoviePosterImageView;

        private MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mMoviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(mMovieData.get(getAdapterPosition()));
        }
    }
}
