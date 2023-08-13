package curtin.edu.funwithflags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * Fragment C displays the players score and their goal, it also informs them if the win or lose
 */
public class FragmentC extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.activity_fragment_c, ui, false);
        Country.CountryList countryList = getArguments().getParcelable("CountryList");
        TextView points, status;

        points = view.findViewById(R.id.points);
        status = view.findViewById(R.id.status);

        points.setText("Points: " +countryList.getCurrentPoints());

        if (countryList.getCurrentPoints() >= countryList.getPointsGoal())
            status.setText("Game Complete! You Win!!");
        else if (countryList.getCurrentPoints() <= 0)
            status.setText("Game Over! You Lost!!");
        else
            status.setText("Goal: " + countryList.getPointsGoal());

        return view;
    }
}