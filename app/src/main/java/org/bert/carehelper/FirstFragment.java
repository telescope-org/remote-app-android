package org.bert.carehelper;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import org.bert.carehelper.common.CareHelperEnvironment;
import org.bert.carehelper.databinding.FragmentFirstBinding;
import org.bert.carehelper.service.CareHelperService;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonFirst.setOnClickListener(view1 -> {
            new CareHelperService(this.getContext(), CareHelperEnvironment.getInstance().getActivity()).run();
            binding.textviewFirst.setText(R.string.text_view_tips);
            binding.buttonFirst.setText(R.string.start_success);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}