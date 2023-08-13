package curtin.edu.funwithflags;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.recyclerview.widget.GridLayoutManager;

import java.util.ArrayList;

/**
 * This file holds all the data, implementations and methods for the quiz game
 *
 * Author: Emmanuel Iloh
 * Last edited: 26/09/2020
 */


/**
 * The Country class holds all the information about each country in the quiz
 */
public class Country implements Parcelable {
    private String name;
    private int flag;
    private ArrayList<Question> questions;
    private boolean complete;

    public Country(String name, int flag, ArrayList<Question> questions)
    {
        this.name = name;
        this.flag = flag;
        this.questions = questions;
        complete = false;  // the questions in the quiz cannot be answered already
    }

    /**
     * The next few methods are required to allow the class object to be parcelable
     */
    protected Country(Parcel in) {
        name = in.readString();
        flag = in.readInt();
        questions = in.createTypedArrayList(Question.CREATOR);
        complete = in.readByte() != 0;
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(flag);
        parcel.writeTypedList(questions);
        parcel.writeByte((byte) (complete ? 1 : 0));
    }

    // end parcelable methods

    public boolean isComplete() {
        int count = 0;

        for (Question question: questions) { // this checks and confirms if all the questions are answered
            if (question.answered)
                count++;
        }

        if (count == questions.size())
            complete = true;

        return complete;
    }

    /**
     * This method implements the special condition when a special question is answered
     */
    public void addSpecial() {
        for (Question question: questions)
        {
            question.setPoints(question.getPoints()+10);
        }
    }

    public int getFlag() {
        return flag;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }


    /**
     * This class holds all the countries in the quiz and handles high level methods
     */
    public static class CountryList implements Parcelable {

        private ArrayList<Country> countries;
        private int pointsGoal;
        private int currentPoints;
        private int seedPoints;
        private int rows; // this is the number of rows of countries and questions
        private int orientation; // this is the scroll direction of the countries and questions
        private boolean special; // this holds if the special condition is active or not

        CountryList(ArrayList<Country> countries, int pointsGoal, int seedPoints) {
            this.countries = countries;
            this.pointsGoal = pointsGoal;
            this.seedPoints = seedPoints;
            currentPoints = 0;
            rows = 2; // the default number of rows is 2
            orientation = GridLayoutManager.VERTICAL; // default is vertical scroll
            special = false;
        }

        /**
         * The next few methods are required to allow the class object to be parcelable
         */
        protected CountryList(Parcel in) {
            countries = in.createTypedArrayList(Country.CREATOR);
            pointsGoal = in.readInt();
            currentPoints = in.readInt();
            seedPoints = in.readInt();
            rows = in.readInt();
            orientation = in.readInt();
            special = in.readByte() != 0;
        }

        public static final Creator<CountryList> CREATOR = new Creator<CountryList>() {
            @Override
            public CountryList createFromParcel(Parcel in) {
                return new CountryList(in);
            }

            @Override
            public CountryList[] newArray(int size) {
                return new CountryList[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeTypedList(countries);
            parcel.writeInt(pointsGoal);
            parcel.writeInt(currentPoints);
            parcel.writeInt(seedPoints);
            parcel.writeInt(rows);
            parcel.writeInt(orientation);
            parcel.writeByte((byte) (special ? 1 : 0));
        }

        // end parcelable methods

        public int getRows() {
            return rows;
        }

        public boolean isSpecial() {
            return special;
        }

        public int getCurrentPoints() {  // gets the total current points
            currentPoints = 0;
            for (Country country : countries) {
                for (Question question : country.getQuestions()) {
                    if (question.isAnswered()) {
                        if (question.isCorrect())
                            currentPoints += question.getPoints();
                        else
                            currentPoints -= question.getPenalty();
                    }
                }
            }
            return (currentPoints + seedPoints); //combines the random seed and the users total points
        }

        public int getPointsGoal() {
            return pointsGoal;
        }

        public int getOrientation() {
            return orientation;
        }

        public ArrayList<Country> getCountries() {
            return countries;
        }

        public void setOrientDown(int orientation) {
            this.orientation = orientation;
        }

        public void setSpecial(boolean special) {
            this.special = special;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }
    }

    /**
     * Question inner class of Country that holds a question and it's answers
     */
    protected static class Question implements Parcelable{
        private String question;
        private ArrayList<Answer> answers;
        private int points;
        private int penalty;
        private boolean correct;
        private boolean answered;
        private boolean special;

        public Question(String question, ArrayList<Answer> answers, int points, int penalty, boolean special)
        {
            this.question = question;
            this.answers = answers;
            this.points = points;
            this.penalty = penalty;
            correct = false;
            answered = false;
            this.special = special;
        }

        protected Question(Parcel in) {
            question = in.readString();
            answers = in.createTypedArrayList(Answer.CREATOR);
            points = in.readInt();
            penalty = in.readInt();
            correct = in.readByte() != 0;
            answered = in.readByte() != 0;
            special = in.readByte() != 0;
        }

        public static final Creator<Question> CREATOR = new Creator<Question>() {
            @Override
            public Question createFromParcel(Parcel in) {
                return new Question(in);
            }

            @Override
            public Question[] newArray(int size) {
                return new Question[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(question);
            parcel.writeTypedList(answers);
            parcel.writeInt(points);
            parcel.writeInt(penalty);
            parcel.writeByte((byte) (correct ? 1 : 0));
            parcel.writeByte((byte) (answered ? 1 : 0));
            parcel.writeByte((byte) (special ? 1 : 0));
        }

        public ArrayList<Answer> getAnswers() {
            return answers;
        }

        public String getQuestion() {
            return question;
        }

        public int getPenalty() {
            return penalty;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public void setSpecial(boolean special) {
            this.special = special;
        }

        public void setCorrect(boolean correct) {
            this.correct = correct;
            answered = true;
        }

        public boolean isAnswered() {
            return answered;
        }

        public boolean isCorrect() {
            return correct;
        }

        public boolean isSpecial() {
            return special;
        }
    }

    /**
     * Inner class of Question that holds a answer and handles it's implementation
     */
    protected static class Answer implements Parcelable{
        private String answer;
        private boolean correct;

        public Answer(String answer, boolean correct)
        {
            this.answer = answer;
            this.correct = correct;
        }

        public boolean isCorrect() {
            return correct;
        }

        public String getAnswer() {
            return answer;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(answer);
            parcel.writeByte((byte) (correct ? 1 : 0));
        }

        protected Answer(Parcel in) {
            answer = in.readString();
            correct = in.readByte() != 0;
        }

        public static final Creator<Answer> CREATOR = new Creator<Answer>() {
            @Override
            public Answer createFromParcel(Parcel in) {
                return new Answer(in);
            }

            @Override
            public Answer[] newArray(int size) {
                return new Answer[size];
            }
        };

    }


    /**
     * This is where all the countries and questions were hard coded
     *
     * I ran out of time, so from Czech Republic the questions are simply copy and pasted :(
     */
    public static ArrayList<Country> generateCountries()
    {
        ArrayList<Country> countries = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        ArrayList<Answer> answers = new ArrayList<>();

        answers.add(new Answer("Canberra", true));
        answers.add(new Answer("Perth", false));
        answers.add(new Answer("Sydney", false));
        answers.add(new Answer("Melbourne", false));

        questions.add(new Question("What is the capital city of Australia?", answers, 20, 5, false));

        answers = new ArrayList<>();

        answers.add(new Answer("20,000,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("2,000,000", true));
        answers.add(new Answer("750,000", false));

        questions.add(new Question("What is the population of Perth in 2020?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", true));
        answers.add(new Answer("False", false));

        questions.add(new Question("The  Australian Alps get more snow than the Swiss Alps", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("50%", false));
        answers.add(new Answer("90%", true));
        answers.add(new Answer("10%", false));
        answers.add(new Answer("75%", false));

        questions.add(new Question("What percent of Australians live close to the coast?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Western Australia", false));
        answers.add(new Answer("Victoria", false));
        answers.add(new Answer("Tasmania", true));
        answers.add(new Answer("South Australia", false));

        questions.add(new Question("Which state has the cleanest air in the world", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("80%", true));
        answers.add(new Answer("20%", false));
        answers.add(new Answer("65%", false));
        answers.add(new Answer("100%", false));

        questions.add(new Question("What percent of the animals are unique to Australia?", answers, 20, 15, false));

        countries.add(new Country("Australia", R.drawable.flag_au, questions));

        // Start UK

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("Liverpool", false));
        answers.add(new Answer("London", true));
        answers.add(new Answer("Dublin", false));
        answers.add(new Answer("Glasgow", false));

        questions.add(new Question("What is the Capital of UK?", answers, 20, 8, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000,000", false));
        answers.add(new Answer("40,000,000", false));
        answers.add(new Answer("100,000,000", false));
        answers.add(new Answer("67,000,000", true));

        questions.add(new Question("What's the population of the UK?", answers, 20, 7, false));

        answers = new ArrayList<>();

        answers.add(new Answer("We are the champions", false));
        answers.add(new Answer("We are one", false));
        answers.add(new Answer("God save the Queen", true));
        answers.add(new Answer("God protect our nation", false));

        questions.add(new Question("WHat is the national anthem of the UK?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Lion", true));
        answers.add(new Answer("Hawk", false));
        answers.add(new Answer("Tiger", false));
        answers.add(new Answer("Unicorn", false));

        questions.add(new Question("What is the national animal of the UK?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The Pyramids are older than Stonehenge", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Bacon", false));
        answers.add(new Answer("Sausages", false));
        answers.add(new Answer("Pancakes", true));
        answers.add(new Answer("Tomatoes", false));

        questions.add(new Question("Which one of these is not part of the classic English Breakfast?", answers, 20, 15, false));

        countries.add(new Country("United Kingdom", R.drawable.flag_uk, questions));

        // Start Canada
        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("Ottawa", true));
        answers.add(new Answer("Montreal", false));
        answers.add(new Answer("Toronto", false));
        answers.add(new Answer("Quebec", false));

        questions.add(new Question("What is the Capital of Canada?", answers, 20, 6, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Baffin Islands", false));
        answers.add(new Answer("Cape Breton Island", true));
        answers.add(new Answer("Victoria Island", false));
        answers.add(new Answer(" Ellesmere Island", false));

        questions.add(new Question("Which of these islands is not among the top 10 biggest in the world", answers, 20, 7, false));

        answers = new ArrayList<>();

        answers.add(new Answer("All the lakes", false));
        answers.add(new Answer("32,000", false));
        answers.add(new Answer("3,000,000", true));
        answers.add(new Answer("None, they're all frozen", false));

        questions.add(new Question("How many lakes are there in Canada?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("6,200,000", true));
        answers.add(new Answer("4,200,000", false));
        answers.add(new Answer("11,000,000", false));
        answers.add(new Answer("2,500,000", false));

        questions.add(new Question("What is the population of Toronto?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", true));
        answers.add(new Answer("False", false));

        questions.add(new Question("Canada is the world leader in uranium mining", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("It's all frozen over", false));
        answers.add(new Answer("50%", true));
        answers.add(new Answer("90%", false));
        answers.add(new Answer("20%", false));

        questions.add(new Question("What percent of Canada is land mass", answers, 20, 10, false));

        countries.add(new Country("Canada", R.drawable.flag_ca, questions));

        // Start France

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("73,200,00", false));
        answers.add(new Answer("89,300,000", true));
        answers.add(new Answer("3,400,00", false));
        answers.add(new Answer("124,500,000", false));

        questions.add(new Question("How many people visited france in 2018?", answers, 20, 15, false));

        answers= new ArrayList<>();

        answers.add(new Answer("1960", false));
        answers.add(new Answer("1926", false));
        answers.add(new Answer("1895", false));
        answers.add(new Answer("1915", true));

        questions.add(new Question("The French army were the first to use camouflage, what year was this?", answers, 20, 10, false));


        answers = new ArrayList<>();

        answers.add(new Answer("Cans", false));
        answers.add(new Answer("Hairdryers", false));
        answers.add(new Answer("Air bags", true));
        answers.add(new Answer("Hot air balloons", false));

        questions.add(new Question("Which one of these is not a French invention?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1895", true));
        answers.add(new Answer("1907", false));
        answers.add(new Answer("1876", false));
        answers.add(new Answer("1924", false));

        questions.add(new Question("The first public screening of a movie was in France, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The croissant was invented in France", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("2015", false));
        answers.add(new Answer("2013", true));
        answers.add(new Answer("2009", false));
        answers.add(new Answer("2018", false));

        questions.add(new Question("What year did France legalise same-sex marriage?", answers, 20, 10, false));

        countries.add(new Country("Australia", R.drawable.flag_fr, questions));

        // Start Austria

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("9,000,000", true));
        answers.add(new Answer("1,900,000", false));
        answers.add(new Answer("15,000,000", false));
        answers.add(new Answer("2,500,000", false));

        questions.add(new Question("What is the population of Austria", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("English", false));
        answers.add(new Answer("Austrian", false));
        answers.add(new Answer("French", false));
        answers.add(new Answer("German", true));

        questions.add(new Question("What language do Austrians Speak?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Schilling and Groschen", true));
        answers.add(new Answer("Euros and Cents", false));
        answers.add(new Answer("Pounds and Pence", false));
        answers.add(new Answer("Gold and Silver", false));

        questions.add(new Question("What currency did Austria use before 2002?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Innsbruck", false));
        answers.add(new Answer("Salzburg", false));
        answers.add(new Answer("Vienna", true));
        answers.add(new Answer("Graz", false));

        questions.add(new Question("What is the capital of Austria?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The croissant was invented in France", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Raphael", false));
        answers.add(new Answer("Wolfgang", true));
        answers.add(new Answer("Stefan", false));
        answers.add(new Answer("Fabio", false));

        questions.add(new Question("What is Mozart's first name?", answers, 20, 10, false));

        countries.add(new Country("Australia", R.drawable.flag_at, questions));

        // Stop UK

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("Nessebar", false));
        answers.add(new Answer("Sofia", true));
        answers.add(new Answer("Varna", false));
        answers.add(new Answer("Plovdiv", false));

        questions.add(new Question("What is the Capital of Bulgaria", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("No", false));
        answers.add(new Answer("Please", false));
        answers.add(new Answer("Thank You", false));
        answers.add(new Answer("Yes", true));

        questions.add(new Question("What does shaking your head mean in Bulgaria?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("The Wonderful Bridges", false));
        answers.add(new Answer("The Belogradchik Rocks", false));
        answers.add(new Answer("The Nevski Steps", true));
        answers.add(new Answer("The Tyulenovo Arch", true));

        questions.add(new Question("Which one of these is not a Bulgarian rock formation?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Gaida", true));
        answers.add(new Answer("Sekunda", false));
        answers.add(new Answer("Otivam", false));
        answers.add(new Answer("Malak", false));

        questions.add(new Question("What are the bagpipes known as in Bulgaria?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Crawl Day", false));
        answers.add(new Answer("Walk Day", false));
        answers.add(new Answer("Name Day", true));
        answers.add(new Answer("Talk Day", false));

        questions.add(new Question("What day is celebrated in addition to birthday's in Bulgaria", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", true));
        answers.add(new Answer("False", false));

        questions.add(new Question("World’s first digital wristwatch was developed in Bulgaria", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("8th", false));
        answers.add(new Answer("4th", true));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("16th", false));

        questions.add(new Question("What place was the Bulgarian national football team in the 1994 World Cup?", answers, 20, 10, false));

        countries.add(new Country("Bulgaria", R.drawable.flag_bg, questions));

        // Start Czech Republic

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("They drink A LOT of beer", false));
        answers.add(new Answer("They do a big dance in the city centre", false));
        answers.add(new Answer("They go mushroom picking", true));
        answers.add(new Answer("They exchange gifts", false));

        questions.add(new Question("What happens on St. Wenceslas Day in th Czech Republic?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", true));
        answers.add(new Answer("False", false));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        countries.add(new Country("Czech Republic", R.drawable.flag_cz, questions));

        // Start South Korea

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("Busan", false));
        answers.add(new Answer("Seoul", true));
        answers.add(new Answer("Incheon", false));
        answers.add(new Answer("Daegu", false));

        questions.add(new Question("What is the Capital of South Korea", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000,000", false));
        answers.add(new Answer("15,500,000", false));
        answers.add(new Answer("70,000,000", false));
        answers.add(new Answer("51,000,000", true));

        questions.add(new Question("What's the population of the South Korea as of 2018", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the South Korea was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("South Korea has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the South Korea", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the South Korea\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in South Korea?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a South Korea chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from South Korea", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the South Korea Republic company Škoda Auto founded?", answers, 20, 10, false));

        countries.add(new Country("South Korea", R.drawable.flag_kr, questions));
        // Start Russia

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("Canbora", true));
        answers.add(new Answer("Perth", false));
        answers.add(new Answer("Sydney", false));
        answers.add(new Answer("Melbourne", false));

        questions.add(new Question("What is the Capital of Australia ", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", true));
        answers.add(new Answer("69,696", false));

        questions.add(new Question("What is the size of Perth", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        countries.add(new Country("Russia", R.drawable.flag_ru, questions));

        // Stop Italy

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("New Hamburg", false));
        answers.add(new Answer("London", true));
        answers.add(new Answer("Dumb Land", false));
        answers.add(new Answer("Jazzed", false));

        questions.add(new Question("What is the Capital of UK", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", false));
        answers.add(new Answer("69,696", true));

        questions.add(new Question("What's the population of the UK", answers, 20, 10, false));

        countries.add(new Country("Mexico", R.drawable.flag_mx, questions));

        // Start Malaysia

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("Canbora", true));
        answers.add(new Answer("Perth", false));
        answers.add(new Answer("Sydney", false));
        answers.add(new Answer("Melbourne", false));

        questions.add(new Question("What is the Capital of Australia ", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", true));
        answers.add(new Answer("69,696", false));

        questions.add(new Question("What is the size of Perth", answers, 20, 10, false));

        countries.add(new Country("Malaysia", R.drawable.flag_my, questions));

        // Stop France

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("New Hamburg", false));
        answers.add(new Answer("London", true));
        answers.add(new Answer("Dumb Land", false));
        answers.add(new Answer("Jazzed", false));

        questions.add(new Question("What is the Capital of France", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", false));
        answers.add(new Answer("69,696", true));

        questions.add(new Question("What's the population of the UK", answers, 20, 10, false));

        countries.add(new Country("Lithuania", R.drawable.flag_lt, questions));
        // Start Australia

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("Canbora", true));
        answers.add(new Answer("Perth", false));
        answers.add(new Answer("Sydney", false));
        answers.add(new Answer("Melbourne", false));

        questions.add(new Question("What is the Capital of Australia ", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", true));
        answers.add(new Answer("69,696", false));

        questions.add(new Question("What is the size of Perth", answers, 20, 10, false));

        countries.add(new Country("Netherlands", R.drawable.flag_nl, questions));

        // Start Japan

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("New Hamburg", false));
        answers.add(new Answer("London", true));
        answers.add(new Answer("Dumb Land", false));
        answers.add(new Answer("Jazzed", false));

        questions.add(new Question("What is the Capital of UK", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", false));
        answers.add(new Answer("69,696", true));

        questions.add(new Question("What's the population of the UK", answers, 20, 10, false));

        countries.add(new Country("Japan", R.drawable.flag_jp, questions));

        // Start Denmark

        answers = new ArrayList<>();
        questions = new ArrayList<>();

        answers.add(new Answer("Canbora", true));
        answers.add(new Answer("Perth", false));
        answers.add(new Answer("Sydney", false));
        answers.add(new Answer("Melbourne", false));

        questions.add(new Question("What is the Capital of Australia ", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", true));
        answers.add(new Answer("69,696", false));

        questions.add(new Question("What is the size of Perth", answers, 20, 10, false));

        countries.add(new Country("Denmark", R.drawable.flag_dk, questions));

        // Stop France

        answers= new ArrayList<>();
        questions= new ArrayList<>();

        answers.add(new Answer("New Hamburg", false));
        answers.add(new Answer("London", true));
        answers.add(new Answer("Dumb Land", false));
        answers.add(new Answer("Jazzed", false));

        questions.add(new Question("What is the Capital of France", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("4th", false));
        answers.add(new Answer("1st", false));
        answers.add(new Answer("7th", true));
        answers.add(new Answer("10th", false));

        questions.add(new Question("As of 2019 the Czech Republic was among the 10 safest countries in the world, what place was it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("10,000", false));
        answers.add(new Answer("2,000", true));
        answers.add(new Answer("500", false));
        answers.add(new Answer("4,500", false));

        questions.add(new Question("Czech Republic has the most castles in Europe, how much is it?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Hluboká Castle", false));
        answers.add(new Answer("Orlík Castle", false));
        answers.add(new Answer("Lednice Castle", false));
        answers.add(new Answer("Ozalj Castle", true));

        questions.add(new Question("Which one of these is not a castle in the Czech Republic", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("Brno", false));
        answers.add(new Answer("Český Krumlov", false));
        answers.add(new Answer("Prague", true));
        answers.add(new Answer("Olomouc", true));

        questions.add(new Question("What is the capital of the Czech Republic\n?", answers, 20, 10, false));

        answers = new ArrayList<>();

        answers.add(new Answer("Ice Hockey", true));
        answers.add(new Answer("Football", false));
        answers.add(new Answer("Tennis", false));
        answers.add(new Answer("Water Polo", false));

        questions.add(new Question("What is the most popular sport in Czech?", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1968", false));
        answers.add(new Answer("1893", false));
        answers.add(new Answer("1959", true));
        answers.add(new Answer("1970", false));

        questions.add(new Question("The soft contact lenses were invented by a Czech chemist Otto Wichterle, what year was it?", answers, 20, 15, true));

        answers = new ArrayList<>();

        answers.add(new Answer("True", false));
        answers.add(new Answer("False", true));

        questions.add(new Question("The word 'robot' came from Czech", answers, 20, 15, false));

        answers = new ArrayList<>();

        answers.add(new Answer("1950", false));
        answers.add(new Answer("1895", true));
        answers.add(new Answer("1900", false));
        answers.add(new Answer("1890", false));

        questions.add(new Question("When was the Czech Republic company Škoda Auto founded?", answers, 20, 10, false));

        answers= new ArrayList<>();

        answers.add(new Answer("80,000", false));
        answers.add(new Answer("500,000", false));
        answers.add(new Answer("7,000", false));
        answers.add(new Answer("69,696", true));

        questions.add(new Question("What's the population of the UK", answers, 20, 10, false));

        countries.add(new Country("Barbados", R.drawable.flag_bb, questions));
        return countries;
    }
}
