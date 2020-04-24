package cc.sauerwein.popularmovies.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.databinding.library.baseAdapters.BR;
import androidx.recyclerview.widget.RecyclerView;

import cc.sauerwein.popularmovies.R;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {
    private final MovieDetailViewModel mViewModel;

    public ReviewAdapter(MovieDetailViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @NonNull
    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.review, parent, false);

        return new ReviewAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapterViewHolder holder, int position) {
        holder.bind(mViewModel, position);
    }

    @Override
    public int getItemCount() {
        if (mViewModel.getMovie().getReviews() == null) return 0;
        return mViewModel.getMovie().getReviews().size();
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        public ReviewAdapterViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MovieDetailViewModel viewModel, Integer position) {
            binding.setVariable(BR.view_model, viewModel);
            binding.setVariable(BR.position, position);
        }
    }
}
