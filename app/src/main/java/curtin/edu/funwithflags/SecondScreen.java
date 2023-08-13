package curtin.edu.funwithflags;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * The Second Screen activity that holds fragment A,B and C
 */
public class SecondScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secound_screen);

        Country.CountryList countryList;

        Bundle data = getIntent().getBundleExtra("Bundle");

        countryList = data.getParcelable("CountryList");


        FragmentManager fm = getSupportFragmentManager();
        FragmentA fragmentA =  (FragmentA) fm.findFragmentById(R.id.fragmentA);
        FragmentB fragmentB = (FragmentB) fm.findFragmentById(R.id.fragmentB);
        FragmentC fragmentC = (FragmentC) fm.findFragmentById(R.id.fragmentC);

        if (fragmentA == null)
        {
            fragmentA = new FragmentA();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            fragmentA.setArguments(bundle);
            fm.beginTransaction().add(R.id.fragmentA, fragmentA).commit();
        }
        if (fragmentB == null)
        {
            fragmentB = new FragmentB();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            fragmentB.setArguments(bundle);

            fm.beginTransaction().add(R.id.fragmentB, fragmentB).commit();
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
    public void onBackPressed() { //stops the user from accidentally breaking or restarting the game by pressing back
    }
}