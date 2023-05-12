package com.example.lab2

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

private const val TAG = "MainActivity"
private const val KEY_CURRENTINDEX = "current_index"
private const val KEY_COUNTANSWER = "count_answer"
private const val KEY_CORRECTANSWER = "correct_answer"
private const val KEY_GETANSWER = "get_answer"


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: ImageButton

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProviders.of(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"OnCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_CURRENTINDEX,0)
            quizViewModel.countAnswers = savedInstanceState.getInt(KEY_COUNTANSWER,0)
            quizViewModel.countCorrectAnswers = savedInstanceState.getDouble(KEY_CORRECTANSWER,0.0)
            quizViewModel.getAnswer = savedInstanceState?.getBooleanArray(KEY_GETANSWER) ?: BooleanArray(5)
        }


        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)
        prevButton = findViewById(R.id.prev_button)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
        }
        falseButton.setOnClickListener { view: View ->
            checkAnswer(false)
        }
        nextButton.setOnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            questionUpdate()
        }

        questionTextView.setOnClickListener {
            quizViewModel.currentIndex = (quizViewModel.currentIndex + 1) % quizViewModel.questionBank.size
            questionUpdate()
        }

        prevButton.setOnClickListener{
            if (quizViewModel.currentIndex == 0) {
                quizViewModel.currentIndex = quizViewModel.questionBank.size - 1
            }
            else{
                quizViewModel.currentIndex--
            }
            questionUpdate()
        }
        questionUpdate()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "OnStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "OnResume() called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "OnPause called")
    }
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_CURRENTINDEX, quizViewModel.currentIndex)
        savedInstanceState.putDouble(KEY_CORRECTANSWER, quizViewModel.countCorrectAnswers)
        savedInstanceState.putInt(KEY_COUNTANSWER, quizViewModel.countAnswers)
        savedInstanceState.putBooleanArray(KEY_GETANSWER,quizViewModel.getAnswer)
    }
    override fun onStop(){
        super.onStop()
        Log.d(TAG, "OnStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnDestroy() called")
    }

    private fun questionUpdate(){
        val questionTextResId = quizViewModel.questionBank[quizViewModel.currentIndex].textResId
        questionTextView.setText(questionTextResId)

        if(quizViewModel.getAnswer[quizViewModel.currentIndex]){
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }
        else{
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.questionBank[quizViewModel.currentIndex].answer
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        quizViewModel.getAnswer[quizViewModel.currentIndex] = true
        quizViewModel.countAnswers++

        if (userAnswer == correctAnswer) {
            quizViewModel.countCorrectAnswers++
        }

        if (quizViewModel.countAnswers == quizViewModel.questionBank.size){

            val value = (quizViewModel.countCorrectAnswers/(quizViewModel.questionBank.size))*100

            var result = "Correct answers is: " + Math.round(value) + "%"
            Toast.makeText(this,result,Toast.LENGTH_SHORT).show()
        }

    }

}