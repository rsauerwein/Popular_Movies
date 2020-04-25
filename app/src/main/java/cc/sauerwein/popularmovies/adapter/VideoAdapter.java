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

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoAdapterViewHolder> {
    private final MovieDetailViewModel mViewModel;

    public VideoAdapter(MovieDetailViewModel viewModel) {
        this.mViewModel = viewModel;
    }

    @NonNull
    @Override
    public VideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.video, parent, false);

        return new VideoAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoAdapterViewHolder holder, int position) {
        holder.bind(mViewModel, position);
    }

    @Override
    public int getItemCount() {
        if (mViewModel.getMovie().getVideos() == null) return 0;
        return mViewModel.getMovie().getVideos().size();
    }

    class VideoAdapterViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        VideoAdapterViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(MovieDetailViewModel viewModel, Integer position) {
            binding.setVariable(BR.view_model, viewModel);
            binding.setVariable(BR.position, position);
        }
    }
}
