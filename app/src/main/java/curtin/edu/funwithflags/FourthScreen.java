package curtin.edu.funwithflags;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * The Fourth Activity displays the question and possible answers to the user
 * In tablet mode this activity is not accessed
 */
public class FourthScreen extends AppCompatActivity {

    Intent returnIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_screen);

        Bundle data = getIntent().getBundleExtra("Bundle");
        int pos = data.getInt("POS");
        int pos1 = data.getInt("POS1");

        Country.CountryList countryList = data.getParcelable("CountryList");


        FragmentManager fm = getSupportFragmentManager();
        FragmentE fragmentE = (FragmentE) fm.findFragmentById(R.id.fragmentTop);
        FragmentC fragmentC = (FragmentC) fm.findFragmentById(R.id.fragmentBottom);

        if (fragmentE == null)
        {
            fragmentE = new FragmentE();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            bundle.putInt("POS", pos);
            bundle.putInt("POS1", pos1);
            fragmentE.setArguments(bundle);

            fm.beginTransaction().add(R.id.fragmentTop, fragmentE).commit();
        }

        if (fragmentC == null)
        {
            fragmentC = new FragmentC();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            fragmentC.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragmentBottom, fragmentC).commit();
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