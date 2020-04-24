package cc.sauerwein.popularmovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cc.sauerwein.popularmovies.databinding.FragmentReviewBinding;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {
    private MovieDetailViewModel mViewModel;

    public ReviewFragment() {
        // Required empty public constructor
    }

    public static ReviewFragment newInstance() {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewModel = new ViewModelProvider(requireActivity()).get(MovieDetailViewModel.class);
        FragmentReviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_review, container, false);
        mViewModel.setupReviewRecyclerView(binding.rvReviews);
        return binding.getRoot();
    }
}
