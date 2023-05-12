<h2 align="center">  МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ РОССИЙСКОЙ ФЕДЕРАЦИИ ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ ВЫСШЕГО ОБРАЗОВАНИЯ «САХАЛИНСКИЙ ГОСУДАРСТВЕННЫЙ УНИВЕРСИТЕТ» </h2>
<div align="center">
<h3>Институт естественных наук и техносферной безопасности
<br>
Кафедра информатики
<br>
Половников Владислав Олегович</h3>

<br>
<h3>Лабораторная работа №3
<br>
“quizViewModel.”
<br>
01.03.02 Прикладная математика и информатика</h3>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<h3 align="right">Научный руководитель
<br>
Соболев Евгений Игоревич
</h3>

<h3 align="center">Южно-Сахалинск
<br>
2023г.
</h3>
<hr>
</div>
<p>
Kotlin - это высокоэффективный современный язык программирования, разработанный компанией JetBrains. Он имеет очень чёткий и лаконичный синтаксис, что делает ваш код лёгким для чтения. Kotlin широко используется во всём мире, и его популярность среди разработчиков постоянно растёт. Многие разработчики, использующие Kotlin, отмечают, что это делает их работу быстрее и увлекательнее.
</p>

<h3 align="center">Задание</h3>

1.	Предотвращение ввода нескольких ответов.
После того как пользователь введет ответ на вопрос, заблокируйте кнопки этого вопроса, чтобы предотвратить возможность ввода нескольких ответов. 
2.	Вывод оценки.
После того как пользователь введет ответ на все вопросы, отобразите уведомление с процентом правильных ответов. 





<h1 align="center">Решение</h2>
<h2 align="center">MainActivity</h2>

```kotlin
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
```

<h2 align="center">QuizViewModel</h2>

```kotlin
package com.example.lab2

import androidx.lifecycle.ViewModel


private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
     val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true))

    var currentIndex = 0
    var countAnswers = 0
    var countCorrectAnswers = 0.0
    var getAnswer = BooleanArray(questionBank.size)

}

```


<h2 align="center">activity_main</h2>

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tools="http://schemas.android.com/tools"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:id="@+id/question_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:padding="24dp"
        tools:text="@string/question_australia" />

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">

        <Button
            android:id="@+id/true_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/true_button" />
        <Button
            android:id="@+id/false_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/false_button" />
    </LinearLayout>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:orientation="horizontal">
        <ImageButton
            android:id="@+id/prev_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/arrow_left"
            android:contentDescription="Previous"
            />
        <ImageButton
            android:id="@+id/next_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/arrow_right"
            android:contentDescription="Next"/>
    </LinearLayout>

</LinearLayout>

```

<h2 align="center">Landscape activity_main</h2>

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tools="http://schemas.android.com/tools"
    android:gravity="center"
    android:orientation="horizontal"
    tools:context=".MainActivity">

    <TextView
        android:id="@+id/question_text_view"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_horizontal"
        android:layout_marginTop="100dp"
        android:padding="24dp"
        tools:text="@string/question_australia"/>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center_vertical|center_horizontal">

        <Button
            android:id="@+id/true_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/true_button" />

        <Button
            android:id="@+id/false_button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@string/false_button" />
    </LinearLayout>

    <ImageButton
        android:id="@+id/prev_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/arrow_left"
        android:layout_gravity="bottom|left"
        android:contentDescription="Previous question"/>

    <ImageButton
        android:id="@+id/next_button"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:src="@drawable/arrow_right"
        android:layout_gravity="bottom|right"
        android:contentDescription="Next question"/>


</FrameLayout>

```

<h2 align="center">strings</h2>

```kotlin
<resources>
    <string name="app_name">lab2</string>
    <string name="action_settings">Settings</string>
    <string name="true_button">True</string>
    <string name="false_button">False</string>
    <string name="correct_toast">Correct!</string>
    <string name="incorrect_toast">Incorrect!</string>
    <string name="question_australia">Canberra is the capital of Australia.</string>
    <string name="question_oceans">The Pacific Ocean is larger than the Atlantic Ocean.</string>
    <string name="question_mideast">The Suez Canal connects the Red Sea and the Indian Ocean.</string>
    <string name="question_africa">The source of the Nile River is in Egypt.</string>
    <string name="question_americas">The Amazon River is the longest river in the Americas.</string>
    <string name="question_asia">Lake Baikal is the world\'s oldest and deepest freshwater lake.</string>
    <string name="next_button">Next</string>
    <string name="prev_button">Prev</string>
</resources>

```
<h2 align="center">Вывод</h2>
Я научился передавать состояние при повороте экрана