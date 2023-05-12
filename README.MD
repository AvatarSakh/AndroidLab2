<h2 align="center">  МИНИСТЕРСТВО НАУКИ И ВЫСШЕГО ОБРАЗОВАНИЯ РОССИЙСКОЙ ФЕДЕРАЦИИ ФЕДЕРАЛЬНОЕ ГОСУДАРСТВЕННОЕ БЮДЖЕТНОЕ ОБРАЗОВАТЕЛЬНОЕ УЧРЕЖДЕНИЕ ВЫСШЕГО ОБРАЗОВАНИЯ «САХАЛИНСКИЙ ГОСУДАРСТВЕННЫЙ УНИВЕРСИТЕТ» </h2>
<div align="center">
<h3>Институт естественных наук и техносферной безопасности
<br>
Кафедра информатики
<br>
Половников Владислав Олегович</h3>

<br>
<h3>Лабораторная работа №2
<br>
“Android и модель MVC”
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

1. Добавление слушателя для TextView 
Кнопка NEXT удобна, но было бы неплохо, если бы пользователь мог переходить к следующему вопросу простым нажатием на виджет TextView. 
Подсказка. Для TextView можно использовать слушателя View.OnClickListener, который использовался с Button, потому что класс TextView также является производным от View.

2. Добавление кнопки возврата
Добавьте кнопку для возвращения к предыдущему вопросу. Пользовательский интерфейс должен выглядеть примерно так, как показано на рис. 2.13. 

3. От Button к ImageButton 
Возможно, пользовательский интерфейс будет смотреться еще лучше, если на кнопках будут отображаться только значки, как на рис. 2.14.




<h1 align="center">Решение</h2>
<h2 align="center">MainActivity</h2>

```kotlin
package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var prevButton: ImageButton

    private val questionBank = listOf(
        Question(R.string.question_australia,true),
        Question(R.string.question_oceans,true),
        Question(R.string.question_mideast,false),
        Question(R.string.question_africa,false),
        Question(R.string.question_americas,true),
        Question(R.string.question_asia,true))

    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            currentIndex = (currentIndex+1) % questionBank.size
            questionUpdate()
        }

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            questionUpdate()
        }

        prevButton.setOnClickListener{
            if (currentIndex == 0)
                currentIndex = questionBank.size-1
            else
                currentIndex--
            questionUpdate()
        }
        questionUpdate()
    }

    private fun questionUpdate(){
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else{
            R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

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
            android:contentDescription="Prev"
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
</resources>

```
<h2 align="center">Вывод</h2>
Я научился создавать динамический опросник и кнопки перехода между вопросами