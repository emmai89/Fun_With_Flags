package curtin.edu.funwithflags;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Fragment E displays the question and answers to the user and allows them to select an answer
 */
public class FragmentE extends Fragment {

    private Country.CountryList countryList;

    public View onCreateView(LayoutInflater inflater, ViewGroup ui, Bundle bundle) {

        View view = inflater.inflate(R.layout.activity_fragment_e, ui, false);

        Button ans1 = view.findViewById(R.id.ans1);
        Button ans2 = view.findViewById(R.id.ans2);
        Button ans3 = view.findViewById(R.id.ans3);
        Button ans4 = view.findViewById(R.id.ans4);
        TextView questionView = view.findViewById(R.id.question);

        int pos = getArguments().getInt("POS");
        int pos1 = getArguments().getInt("POS1");
        countryList = getArguments().getParcelable("CountryList");
        Country.Question question = countryList.getCountries().get(pos).getQuestions().get(pos1);
        int size = question.getAnswers().size();

        questionView.setText(question.getQuestion());
        ans1.setText(question.getAnswers().get(0).getAnswer());
        ans2.setText(question.getAnswers().get(1).getAnswer());

        if (size >= 3) // hides all the extra button elements in the layout
            ans3.setText(question.getAnswers().get(2).getAnswer());
        else
            ans3.setVisibility(View.INVISIBLE);

        if (size == 4)
            ans4.setText(question.getAnswers().get(3).getAnswer());
        else
            ans4.setVisibility(View.INVISIBLE);

        ans1.setOnClickListener(view1 -> {
            checkAnswer(0, question, ans1, pos, pos1); // when clicked checks if this is the correct answer
        });

        ans2.setOnClickListener(view12 -> checkAnswer(1, question, ans2, pos, pos1));

        ans3.setOnClickListener(view13 -> checkAnswer(2, question, ans3, pos, pos1));

        ans4.setOnClickListener(view14 -> checkAnswer(3, question, ans4, pos, pos1));

        Bundle bundle_r = new Bundle();
        Intent returnIntent = new Intent(getActivity(), ThirdScreen.class);

        bundle_r.putParcelable("CountryList", countryList);
        bundle_r.putInt("POS", pos);
        returnIntent.putExtra("Bundle", bundle_r);

        if (getFragmentManager().findFragmentByTag("tablet") == null) //bundles data to be sent to activity 3 or 4 on back press depending on if tablet mode is been used
            ((FourthScreen) getActivity()).goBack(returnIntent);
        else
            ((ThirdScreen) getActivity()).goBack(returnIntent);

        return view;
    }

    /**
     * checkAnswer checks if the answer is correct and updates all the necessary information
     */
    private void checkAnswer(int choice, Country.Question question, Button ans, int pos, int pos1) {
        Country.Answer answer = question.getAnswers().get(choice);
        FragmentManager fm = getFragmentManager();

        if (question.isAnswered())  // does nothing if already answered
            return;

        if (answer.isCorrect()) { // if correct or wrong changes text and color to reflect that
            question.setCorrect(true);
            ans.setText("Correct");
            ans.setBackgroundColor(Color.GREEN);
        } else {
            question.setCorrect(false);
            ans.setText("Wrong");
            ans.setBackgroundColor(Color.RED);
        }


        if (getFragmentManager().findFragmentByTag("tablet") != null)  // for tablet mode fragment D is replaced to reflect the users choice
        {
            FragmentD fragmentD = new FragmentD();
            Bundle bundle = new Bundle();
            bundle.putParcelable("CountryList", countryList);
            bundle.putInt("POS", pos);
            bundle.putInt("POS1", pos1);
            fragmentD.setArguments(bundle);

            fm.beginTransaction().replace(R.id.fragmentD, fragmentD).commit();
        }

        /*
        this section updates fragment C if the answer is correct or not
         */
        FragmentC fragmentC = new FragmentC();
        Bundle bundle = new Bundle();
        bundle.putParcelable("CountryList", countryList);
        bundle.putInt("POS", pos);
        bundle.putInt("POS1", pos1);
        fragmentC.setArguments(bundle);

        if (getFragmentManager().findFragmentByTag("tablet") != null) // fragment C is in a different location depending on tablet or phone
            fm.beginTransaction().replace(R.id.fragmentC, fragmentC).commit();
        else
            fm.beginTransaction().replace(R.id.fragmentBottom, fragmentC).commit();

    }
}