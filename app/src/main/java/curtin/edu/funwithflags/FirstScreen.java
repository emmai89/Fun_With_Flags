package curtin.edu.funwithflags;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

/**
 * The First Screen that acts as the title, it's the first thing seen by the user and starts the game
 */
public class FirstScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        Button start = findViewById(R.id.startButton);
        TextView seed_text, goal_text;
        int seed, goal;
        Random rand = new Random();

        goal = rand.nextInt(2000) + 1; //random goal between 1 and 2000 points
        seed = rand.nextInt(goal); // the amount of points the user starts with, will be below the goal by at least 1

        Country.CountryList countryList = new Country.CountryList(Country.generateCountries(), goal, seed); // list of countries and questions initialised

        seed_text = findViewById(R.id.seed);
        goal_text = findViewById(R.id.target);

        seed_text.setText("Seed: " +countryList.getCurrentPoints());
        goal_text.setText("Goal: " +countryList.getPointsGoal());

        start.setOnClickListener(v -> {
            Intent intent = new Intent(FirstScreen.this, SecondScreen.class); // when the start button is clicked the second activity is started with the generated information
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            intent.putExtra("Bundle", bundle);

            startActivity(intent);
        });
    }
}