package curtin.edu.funwithflags;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Third Screen activity that displays the questions of each flag selected
 * On the special condition of tablet mode, it can display the questions and possible answers
 */
public class ThirdScreen extends AppCompatActivity {

    Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_screen);
        View view = getLayoutInflater().inflate(R.layout.activity_third_screen, null, false);


        Bundle data = getIntent().getBundleExtra("Bundle");

        Country.CountryList countryList = data.getParcelable("CountryList");
        int pos = data.getInt("POS"); //stores the index of the country that called the activity

        FragmentManager fm = getSupportFragmentManager();
        FragmentA fragmentA =  (FragmentA) fm.findFragmentById(R.id.fragmentA);
        FragmentD fragmentD = (FragmentD) fm.findFragmentById(R.id.fragmentD);
        FragmentC fragmentC = (FragmentC) fm.findFragmentById(R.id.fragmentC);

        if (fragmentA == null)
        {
            fragmentA = new FragmentA();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            bundle.putInt("POS", pos);
            fragmentA.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragmentA, fragmentA).commit();
        }
        if (fragmentD == null)
        {
            fragmentD = new FragmentD();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            bundle.putInt("POS", pos);
            fragmentD.setArguments(bundle);

            if(view.getTag().toString().equals("tablet"))
                fm.beginTransaction().add(R.id.fragmentD, fragmentD, "tablet").commit(); // sets a tag for the special condition for tablets
            else
                fm.beginTransaction().add(R.id.fragmentD, fragmentD).commit();
        }
        if (fragmentC == null)
        {
            fragmentC = new FragmentC();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            fragmentC.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragmentC, fragmentC).commit();
        }
    }

    public void goBack(Intent intent)
    {
        returnIntent = intent;
    } //gets the intent from fragment D that will be stated on a back press

    @Override
    public void onBackPressed() {
        startActivity(returnIntent);
    } // starts the activity specified by fragment D on the users back press
}