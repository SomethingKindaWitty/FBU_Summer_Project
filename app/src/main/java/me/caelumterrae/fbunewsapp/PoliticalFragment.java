package me.caelumterrae.fbunewsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.file.PoliticalAffData;

public class PoliticalFragment extends Fragment {

    private SeekBar seekBar;
    private Button seekButton;
    private String affiliationNum;
    PoliticalAffData data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_political, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        seekBar = view.findViewById(R.id.sbSeekBar); // ranges from 0 (liberal) to 100 (conservative)
        seekButton = view.findViewById(R.id.btnSubmit); //submit button

        // loads last saved affiliation number (persistence)
        data = new PoliticalAffData(getContext());
        seekBar.setProgress((int)data.getAffiliationNum());

        seekButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // Save the political affiliation number in text file, display it on seekbar
                affiliationNum = Integer.toString(seekBar.getProgress());
                data.resetAffiliationNum(affiliationNum);
                seekBar.setProgress(Integer.parseInt(affiliationNum));

            }
        });

    }

}
