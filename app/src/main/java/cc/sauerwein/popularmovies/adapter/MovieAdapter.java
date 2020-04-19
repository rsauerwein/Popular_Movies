package cc.sauerwein.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.viewmodels.MainActivityViewModel;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {
    private final MainActivityViewModel mViewModel;

    public MovieAdapter(MainActivityViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.movie_poster, parent, false);

        return new MovieAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        holder.bind(mViewModel, position);
    }

    @Override
    public int getItemCount() {
        if (mViewModel.getMovieList() == null) return 0;
        return mViewModel.getMovieList().size();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mMoviePosterImageView;
        private final ViewDataBinding binding;

        private MovieAdapterViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            mMoviePosterImageView = itemView.findViewById(R.id.iv_movie_poster);
        }

        private void bind(MainActivityViewModel viewModel, Integer position) {
            binding.setVariable(BR.view_model, viewModel);
            binding.setVariable(BR.position, position);
            Picasso.get().load(viewModel.getMovieList().get(position).getMoviePosterUrl()).into(mMoviePosterImageView);
        }
    }
}
