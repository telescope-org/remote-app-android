package org.bert.carehelper;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.bert.carehelper.databinding.FragmentFirstBinding;
import org.bert.carehelper.service.CareHelperService;

import java.io.File;

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
            fetchFile();
            binding.textviewFirst.setText(R.string.text_view_tips);
            binding.buttonFirst.setText(R.string.start_success);
            new CareHelperService().run();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void fetchFile() {
        File file = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            file = Environment.getStorageDirectory();
        }
        if (file == null) {
            Log.w(this.getTag(), "folder is null!");
            return;
        }
        if (file.exists()) {
            Log.i(this.getTag(), "file exists!");
        }
        // 文件读取失败
        File[] files = file.listFiles();
        String[] strings = file.list();
        file.setReadable(true);
        System.out.println( file.canRead());
        System.out.println(strings);
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                Log.i(this.getTag(), files[i].getName());
            }
        }
        Log.i(this.getTag(), "file list!");
    }

}