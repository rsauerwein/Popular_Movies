package cc.sauerwein.popularmovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cc.sauerwein.popularmovies.databinding.FragmentVideoBinding;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoFragment extends Fragment {
    private MovieDetailViewModel mViewModel;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewModel = new ViewModelProvider(requireActivity()).get(MovieDetailViewModel.class);
        FragmentVideoBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video, container, false);
        mViewModel.setupVideoRecyclerView(binding.rvVideos);
        binding.setVariable(BR.view_model, mViewModel);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }
}
