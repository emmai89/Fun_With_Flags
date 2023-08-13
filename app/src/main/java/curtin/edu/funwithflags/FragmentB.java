package curtin.edu.funwithflags;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Fragment B is the flags recycle view where a flag can be selected by the user to answer questions
 */
public class FragmentB extends Fragment {

    private Country.CountryList countryList; // easiest implementation for passing this data to other activities

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.activity_fragment_b_flag_selector, ui, false);

        RecyclerView recyclerView = view.findViewById(R.id.flagRecyclerView);

        countryList = getArguments().getParcelable("CountryList");

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), countryList.getRows(), countryList.getOrientation(), false)); //orientation and rows of the flag grid are implemented here

        CountryAdaptor adaptor = new CountryAdaptor(countryList.getCountries());

        recyclerView.setAdapter(adaptor);

        return view;
    }

    private class CountryVHolder extends RecyclerView.ViewHolder
    {
        private ImageView flag;

        public CountryVHolder(LayoutInflater li, final ViewGroup parent) {
            super(li.inflate(R.layout.flag_grid, parent, false));

            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            flag = itemView.findViewById(R.id.flag_logo);


            if (countryList.getOrientation() == GridLayoutManager.VERTICAL) { // keeps the flags centred with different layouts from the user
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
                flag.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            }
            else {
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
                flag.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            }
        }

        public void bind(Country data)
        {
            this.flag.setImageResource(data.getFlag());
        }
    }

    private class CountryAdaptor extends RecyclerView.Adapter<CountryVHolder> {

        private ArrayList<Country> data;

        public CountryAdaptor(ArrayList<Country> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public CountryVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new CountryVHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(CountryVHolder holder, int position) {
            holder.bind(data.get(position));

            if (countryList.isSpecial()) // When a special question is answered and the user returns to this fragment this is implemented
            {
                holder.flag.setBackgroundColor(Color.LTGRAY); //they background is highlighted to inform the user special mode is active
                holder.flag.setOnClickListener(view -> {
                    data.get(position).addSpecial(); // the special condition is added to the selected flag
                    countryList.setSpecial(false); // the special condition is removed

                    Bundle bundle = new Bundle(); // the activity is restarted to allow the game to continue
                    Intent intent = new Intent(getActivity(), SecondScreen.class);

                    bundle.putParcelable("CountryList", countryList);
                    intent.putExtra("Bundle", bundle);

                    startActivity(intent);
                });
            }
            else { // when the special condition is not active
                if (data.get(position).isComplete()) // if the question of a flag are complete, it's can't be clicked
                    holder.flag.setAlpha(100);       // and it's transparent to let the user know
                else {
                    holder.flag.setAlpha(255); // this fixes the problem of having unfinished flags been transparent
                    holder.flag.setOnClickListener(view -> {

                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(getActivity(), ThirdScreen.class); // starts 3rd activity with flag questions

                        bundle.putParcelable("CountryList", countryList);
                        bundle.putInt("POS", position);
                        intent.putExtra("Bundle", bundle);

                        startActivity(intent);
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}