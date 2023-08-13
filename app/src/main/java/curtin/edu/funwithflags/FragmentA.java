package curtin.edu.funwithflags;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Fragment A allows the user to select the number of rows they want as well as the scroll direction
 */
public class FragmentA extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.activity_fragment_a, ui, false);

        Country.CountryList countryList = getArguments().getParcelable("CountryList");
        int pos = getArguments().getInt("POS");

        ImageButton oneRow, twoRow, threeRow, orientButton;

        oneRow = view.findViewById(R.id.oneColumn);
        twoRow = view.findViewById(R.id.twoColumn);
        threeRow = view.findViewById(R.id.threeColumn);
        orientButton = view.findViewById(R.id.orientation);

        if (countryList.getOrientation() == GridLayoutManager.VERTICAL) //different image depending on what scroll is active
            orientButton.setImageResource(R.drawable.orient_right);
        else
            orientButton.setImageResource(R.drawable.orient_down);

        oneRow.setOnClickListener(new View.OnClickListener() { // A for loop and array of buttons could be used to reduce this section
            @Override
            public void onClick(View view) {
                countryList.setRows(1);
                applyChange(countryList, pos);
            }
        });

        twoRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryList.setRows(2);
                applyChange(countryList, pos);
            }
        });

        threeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countryList.setRows(3);
                applyChange(countryList, pos);
            }
        });

        orientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countryList.getOrientation() == GridLayoutManager.VERTICAL) //replaces vertical scroll with horizontal and vise versa
                    countryList.setOrientDown(GridLayoutManager.HORIZONTAL);
                else
                    countryList.setOrientDown(GridLayoutManager.VERTICAL);

                applyChange(countryList, pos);
            }
        });
        return view;
    }

    /**
     * Restarts the current activity with the updated user setting
     */
    private void applyChange(Country.CountryList countryList, int pos)
    {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getActivity(), getActivity().getClass());
        bundle.putParcelable("CountryList", countryList);
        bundle.putInt("POS", pos);
        intent.putExtra("Bundle", bundle);

        startActivity(intent);
    }
}