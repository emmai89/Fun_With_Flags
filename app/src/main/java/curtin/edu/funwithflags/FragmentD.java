package curtin.edu.funwithflags;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Fragment D displays all the question within a flag, and when clicked by the user gives the option to answered them
 */
public class FragmentD extends Fragment {

    private Country.CountryList countryList;
    private int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle)
    {
        View view = inflater.inflate(R.layout.activity_fragment_d, ui, false);

        RecyclerView recyclerView = view.findViewById(R.id.questionRecyclerView);
        countryList = getArguments().getParcelable("CountryList");

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), countryList.getRows(), countryList.getOrientation(), false));

        pos = getArguments().getInt("POS");
        Country country = countryList.getCountries().get(pos);
        ArrayList<Country.Question> questions = country.getQuestions();

        QuestionAdaptor adaptor = new QuestionAdaptor(questions);

        recyclerView.setAdapter(adaptor);

        Bundle bundle_r = new Bundle();
        Intent returnIntent = new Intent(getActivity(), SecondScreen.class);

        bundle_r.putParcelable("CountryList", countryList);
        returnIntent.putExtra("Bundle", bundle_r);

        ((ThirdScreen)getActivity()).goBack(returnIntent); //sends data to activity, allow it to be passed to the secound activity when the back button is pressed by

        return view;
    }

    private class QuestionVHolder extends RecyclerView.ViewHolder
    {

        private Button question;

        public QuestionVHolder(LayoutInflater li, ViewGroup parent) {
            super(li.inflate(R.layout.question_grid, parent, false));

            ViewGroup.LayoutParams lp = itemView.getLayoutParams();

            if (countryList.getOrientation() == GridLayoutManager.VERTICAL) //keeps the questions centred on the screen
                lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            else
                lp.height = ViewGroup.LayoutParams.MATCH_PARENT;

            question = itemView.findViewById(R.id.questionButton);

        }
    }

    private class QuestionAdaptor extends RecyclerView.Adapter<QuestionVHolder> {

        private ArrayList<Country.Question> data;

        public QuestionAdaptor(ArrayList<Country.Question> data) {
            this.data = data;
        }


        @NonNull
        @Override
        public QuestionVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            return new QuestionVHolder(li, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionVHolder holder, int position) {

            if (data.get(position).isSpecial()) // displays if the question is special or not
                holder.question.setText("Q" +String.valueOf(position+1) +": (Special)"+"\nPoints: " +String.valueOf(data.get(position).getPoints()) +"\nPenalty: " +String.valueOf(data.get(position).getPenalty()));
            else
                holder.question.setText("Q" +String.valueOf(position+1) +":"+"\nPoints: " +String.valueOf(data.get(position).getPoints()) +"\nPenalty: " +String.valueOf(data.get(position).getPenalty()));

            if (data.get(position).isAnswered()) { //changes the displayed text of the question is answered and makes it unclickable
                if(data.get(position).isCorrect()) {
                    holder.question.setBackgroundColor(Color.GREEN);
                    holder.question.setText("Q" +String.valueOf(position+1) +": Correct");

                    if (data.get(position).isSpecial()) {
                        countryList.setSpecial(true);
                        data.get(position).setSpecial(false);
                    }
                }
                else {
                    holder.question.setBackgroundColor(Color.RED);
                    holder.question.setText("Q" +String.valueOf(position+1) +": Wrong");
                }
            }
            else {
                holder.question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getFragmentManager().findFragmentByTag("tablet") != null) //if it's a tablet it opens the fragment E on the right side of the screen
                        {
                            FragmentManager fm = getFragmentManager();
                            FragmentE  fragmentE = new FragmentE();
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("CountryList", countryList);
                            bundle.putInt("POS", pos);
                            bundle.putInt("POS1", position);
                            fragmentE.setArguments(bundle);

                            fm.beginTransaction().replace(R.id.fragmentE, fragmentE, "tablet").commit(); // lets fragment E know it's tablet mode
                        }
                        else { // on non tablet mode it starts the Fourth activity
                                Bundle bundle = new Bundle();
                                Intent intent = new Intent(getActivity(), FourthScreen.class);

                                bundle.putParcelable("CountryList", countryList);
                                bundle.putInt("POS", pos);
                                bundle.putInt("POS1", position);
                                intent.putExtra("Bundle", bundle);

                                startActivity(intent);
                            }
                        }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}