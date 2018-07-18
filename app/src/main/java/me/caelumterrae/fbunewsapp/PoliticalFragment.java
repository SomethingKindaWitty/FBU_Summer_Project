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

public class PoliticalFragment extends Fragment {

    private SeekBar seekBar;
    private Button seekButton;
    private String affiliationNum;
    public static final String FILE_NAME = "political_affiliation.txt";

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

        readItems(); // loads last saved affiliation number (persistence)

        seekButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                affiliationNum = Integer.toString(seekBar.getProgress());
                writeItems(affiliationNum);
            }
        });

    }

//    // Submit Button Handler - Saves data from button and brings user to activity main
//    public void onSubmit(View v) {
//        affiliationNum = Integer.toString(seekBar.getProgress());
//        writeItems(affiliationNum); // store political affiliation number in political_affiliation.txt
////        Intent intent = new Intent(getActivity(), MainActivity.class);
////        // prepare intent to pass back to MainActivity
////        startActivity(intent);
//    }

    // Save the political affiliation number in text file
    private void writeItems(String text) {
        try {
            FileUtils.writeStringToFile(getDataFile(), text);  // writes line to FILE_NAME
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Return file which the data is stored in
    private File getDataFile() {
        return new File(getContext().getFilesDir(), FILE_NAME);
    }

    // Read last saved (if any) affiliation number from the file system and display it on seekbar
    private void readItems() {
        try {
            affiliationNum = FileUtils.readFileToString(getDataFile());
            seekBar.setProgress(Integer.parseInt(affiliationNum));
        }  catch (IOException e) {
            e.printStackTrace(); // print error to console
        }
    }
}
