package cc.sauerwein.popularmovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import cc.sauerwein.popularmovies.databinding.FragmentOverviewBinding;
import cc.sauerwein.popularmovies.viewmodels.MovieDetailViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    private MovieDetailViewModel mViewModel;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mViewModel = new ViewModelProvider(requireActivity()).get(MovieDetailViewModel.class);
        FragmentOverviewBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_overview, container, false);
        binding.setViewModel(mViewModel);
        binding.setVariable(BR.view_model, mViewModel);
        binding.setLifecycleOwner(requireActivity());
        return binding.getRoot();
    }
}
