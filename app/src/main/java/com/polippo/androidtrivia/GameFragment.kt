package com.polippo.androidtrivia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.polippo.androidtrivia.databinding.FragmentGameBinding

class GameFragment: Fragment() {

    data class Question(
        val text: String,
        val answers: List<String>
    )

    // The first answer is the correct one.  We randomize the answers before showing the text.
    // All questions must have four answers.  We'd want these to contain references to string
    // resources so we could internationalize. (or better yet, not define the questions in code...)

    private val questions: MutableList<Question> = mutableListOf(
        Question("What is Android Jetpack?",
            answers = listOf("all of these", "tools", "documentation", "libraries")),
        Question("Base class for layout?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot")),
        Question("Layout for complex Screens?",
            answers = listOf("ConstraintLayout", "GridLayout", "LinearLayout", "FrameLayout")),
        Question("Pushing structured data into a Layout",
            answers = listOf("Data Binding", "Data Pushing", "Set Text", "OnClick")),
        Question("Inflate layout in fragments?",
            answers = listOf("onCreateView", "onViewCreate", "onCreateLayout", "onInflateLayout")),
        Question("Build system for Android?",
            answers = listOf("Gradle", "Graddle", "Grodle", "Groyle")),
        Question("Android vector format?",
            answers = listOf("VectorDrawable", "AndroidVectorDrawable", "DrawableVector", "AndroidVector")),
        Question("Android navigation component?",
            answers = listOf("NavController", "NavCentral", "NavMaster", "NavSwitcher")),
        Question("Registers app with Launcher?",
            answers = listOf("intent-filter", "app-registry", "launcher-registry", "app-launcher")),
        Question("Mark a layout for Data Binding?",
            answers = listOf("<layout>", "<binding>", "<data-binding>", "<dbinding>"))
    )

    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1)/ 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //Inflate th layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(inflater, R.layout.fragment_game, container, false)

        //Shuffle the questions and sets the question index to the first question
        randomizeQuestion()

        //Bind this fragment class to the layout
        binding.game = this

        //setOnClickListener for submit Button
        binding.submitButton.setOnClickListener{ view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            //do nothing if nothing is checked (id == -1)
            if(-1 != checkedId){
                var answerIndex = 0
                when(checkedId){
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }

                //The first answer in the original question is always th correct one, so if our answer matches, we have the correct answer
                if(answers[answerIndex] == currentQuestion.answers[0]){
                    questionIndex++
                    //Advanced to the next question
                    if(questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        view.findNavController().navigate(R.id.action_gameFragment_to_gameWonFragment)
                    }
                } else {
                    view.findNavController().navigate(R.id.action_gameFragment_to_gameOverFragment2)
                }
            }
        }

        return binding.root

    }

    //Randomize the questions  and set the first question
    private fun randomizeQuestion() {
        questions.shuffle()
        questionIndex = 0
        setQuestion()
    }

    //Sets the questions and randomize the answers. This only changes the data, not the UI
    //Calling invalidateAll() on the FragmentGameBinding updates the data
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        //randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        //and shuffle them
        answers.shuffle()
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
    }

}