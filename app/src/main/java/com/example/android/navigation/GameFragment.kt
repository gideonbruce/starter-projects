package com.example.android_trivia_starter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    data class Question(
        val text: String,
        val answers: List<String>)

    private val questions: MutableList<Question> = mutableListOf(
        Question(text = "What is Android Jetpack?",
            answers = listOf("All of these", "Tools", "Documentation", "Libraries")),
        Question(text = "What is the base class for layouts?",
            answers = listOf("ViewGroup", "ViewSet", "ViewCollection", "ViewRoot"))
    )


    lateinit var currentQuestion: Question
    lateinit var answers: MutableList<String>
    private var questionIndex = 0
    private val numQuestions = Math.min((questions.size + 1) / 2, 3)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false
        )

        //shuffle the question and set index to the first question
        randomizeQuestions()

        //bind this fragment class to the layout
        binding.game = this

        //set the onClickListener for the submit button
        binding.submitButton.setOnClickListener @Suppress("UNUSED_ANONYMOUS_PARAMETER")
        { view: View ->
            val checkedId = binding.questionRadioGroup.checkedRadioButtonId
            // do nothing when nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.secondAnswerRadioButton -> answerIndex = 1
                    R.id.thirdAnswerRadioButton -> answerIndex = 2
                    R.id.fourthAnswerRadioButton -> answerIndex = 3
                }

                if (answers[answerIndex] == currentQuestion.answers[0]) {
                    questionIndex++
                    // advance to next question
                    if (questionIndex < numQuestions) {
                        currentQuestion = questions[questionIndex]
                        setQuestion()
                        binding.invalidateAll()
                    } else {
                        //we've won navigate to the gamewonfragment
                    }
                }
            }
            return binding.root
        }

        private fun randomizeQuestions() {
            questions.shuffle()
            questionIndex = 0
            setQuestion()
        }

        private fun setQuestion() {
            currentQuestion = questions[questionIndex]
            //randomize answers into a copy of the array
            answers = currentQuestion.answers.toMutableList()
            // and shuffle them
            answers.shuffle()
            (activity as AppCompatActivity).supportActionBar?.title =
                getString(R.string.title_android_trivia_question, questionIndex + 1, numQuestions)
        }
    }
}