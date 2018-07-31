package me.caelumterrae.fbunewsapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;

import me.caelumterrae.fbunewsapp.R;
import me.caelumterrae.fbunewsapp.model.User;

public class CreateActivity extends AppCompatActivity {

    public Button btnSubmit;
    public CheckBox business;
    public CheckBox entertainment;
    public CheckBox general;
    public CheckBox health;
    public CheckBox science;
    public CheckBox sports;
    public CheckBox technology;
    public int numberChecked = 0;
    public ArrayList<String> categories;
    public String categoryString = "";
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);


        business = findViewById(R.id.business);
        entertainment = findViewById(R.id.entertainment);
        general = findViewById(R.id.general);
        health = findViewById(R.id.health);
        science = findViewById(R.id.science);
        sports = findViewById(R.id.sports);
        technology = findViewById(R.id.technology);
        btnSubmit = findViewById(R.id.button);
        categories = new ArrayList<String>();

        //if number of categories selected is valid, adds the new user to the database
        //categories is stored as a String in the user, with types separated by spaces
        btnSubmit.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                if (numberChecked == 0) {
                    Toast.makeText(getBaseContext(), "Please select at least one category", Toast.LENGTH_LONG).show();
                } else if (numberChecked > 3) {
                    Toast.makeText(getBaseContext(), "Please select up to three categories", Toast.LENGTH_LONG).show();
                } else {
                    //getting the name of every selected category
                    for (int i =0; i < categories.size(); i++){
                        categoryString += categories.get(i) + " ";
                    }
                    user.setCategories(categoryString);
                }
            }
        });

    }

    //updates categories array and numberChecked value
    public void onCheckBoxClicked(View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()){
            case R.id.business:
                if (checked){
                    categories.add("business");
                    numberChecked ++;
                } else {
                    categories.remove("business");
                    numberChecked --;
                }
                break;
            case R.id.entertainment:
                if (checked){
                    categories.add("entertainment");
                    numberChecked ++;
                } else {
                    categories.remove("entertainment");
                    numberChecked --;
                }
                break;
            case R.id.general:
                if (checked){
                    categories.add("general");
                    numberChecked ++;
                } else {
                    categories.remove("general");
                    numberChecked --;
                }
                break;
            case R.id.health:
                if (checked){
                    categories.add("health");
                    numberChecked ++;
                } else {
                    categories.remove("health");
                    numberChecked --;
                }
                break;
            case R.id.science:
                if (checked){
                    categories.add("science");
                    numberChecked ++;
                } else {
                    categories.remove("science");
                    numberChecked --;
                }
                break;
            case R.id.sports:
                if (checked){
                    categories.add("sports");
                    numberChecked ++;
                } else {
                    categories.remove("sports");
                    numberChecked --;
                }
                break;
            case R.id.technology:
                if (checked) {
                    categories.add("technology");
                    numberChecked ++;
                } else {
                    categories.remove("technology");
                    numberChecked --;
                }
                break;

        }
    }
}
