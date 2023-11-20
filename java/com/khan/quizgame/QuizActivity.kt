package com.khan.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.khan.quizgame.databinding.ActivityQuizBinding
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {

    lateinit var QuizActivityBinding : ActivityQuizBinding
    val database = FirebaseDatabase.getInstance()
    val  databaseReference = database.reference.child("questions")
    val scoreReference = database.reference

    var question = ""
    var answerA =""
    var answerB =""
    var answerC =""
    var answerD =""
    var correctAnswer = ""
   // var questionCount = 0
    var questionNumber= 0
    var userAnswer = ""
    var userCorrect = 0
    var userWrong = 0

    lateinit var timer: CountDownTimer
    private  val totalTime = 60000L
    var timerContinue = false
    var leftTime = totalTime

    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    val questions = HashSet<Int>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuizActivityBinding=ActivityQuizBinding.inflate(layoutInflater)
        val view = QuizActivityBinding.root
        setContentView(view)

         do {
             val number = Random.nextInt(1,11)
             questions.add(number)
         }while(questions.size < 5)
        gamelogic()

        QuizActivityBinding.buttonNext.setOnClickListener {
            resetTimer()
            gamelogic()

        }
        QuizActivityBinding.buttonFinish.setOnClickListener {
            sendScore()
        }
        QuizActivityBinding.textViewA.setOnClickListener {
            pauseTimer()
            userAnswer= "a"
            if(correctAnswer == userAnswer)
            {
                QuizActivityBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++
                QuizActivityBinding.textViewCorrect.text =userCorrect.toString()
            }
            else
            {
                QuizActivityBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                QuizActivityBinding.textViewWrong.text =userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
       QuizActivityBinding.textViewB.setOnClickListener {
           pauseTimer()
            userAnswer="b"
           if(correctAnswer == userAnswer)
           {
               QuizActivityBinding.textViewB.setBackgroundColor(Color.GREEN)
               userCorrect++
               QuizActivityBinding.textViewCorrect.text =userCorrect.toString()
           }
           else
           {
               QuizActivityBinding.textViewB.setBackgroundColor(Color.RED)
               userWrong++
               QuizActivityBinding.textViewWrong.text =userWrong.toString()
               findAnswer()
           }
           disableClickable()
        }
        QuizActivityBinding.textViewC.setOnClickListener {
            pauseTimer()
            userAnswer="c"
            if(correctAnswer == userAnswer)
            {
                QuizActivityBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++
                QuizActivityBinding.textViewCorrect.text =userCorrect.toString()
            }
            else
            {
                QuizActivityBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                QuizActivityBinding.textViewWrong.text =userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
        QuizActivityBinding.textViewD.setOnClickListener {
            pauseTimer()
            userAnswer="d"
            if(correctAnswer == userAnswer)
            {
                QuizActivityBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++
                QuizActivityBinding.textViewCorrect.text =userCorrect.toString()
            }
            else
            {
                QuizActivityBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                QuizActivityBinding.textViewWrong.text =userWrong.toString()
                findAnswer()
            }
            disableClickable()
        }
    }

    private fun gamelogic(){

        restoreOptions()

        databaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

               // questionCount = snapshot.childrenCount.toInt()

                if (questionNumber < questions.size)
                {
                    question = snapshot.child(questions.elementAt(questionNumber).toString()).child("q").value.toString()
                    answerA = snapshot.child(questions.elementAt(questionNumber).toString()).child("a").value.toString()
                    answerB = snapshot.child(questions.elementAt(questionNumber).toString()).child("b").value.toString()
                    answerC = snapshot.child(questions.elementAt(questionNumber).toString()).child("c").value.toString()
                    answerD = snapshot.child(questions.elementAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer = snapshot.child(questions.elementAt(questionNumber).toString()).child("answer").value.toString()

                    QuizActivityBinding.textViewQuestion.text= question
                    QuizActivityBinding.textViewA.text= answerA
                    QuizActivityBinding.textViewB.text= answerB
                    QuizActivityBinding.textViewC.text= answerC
                    QuizActivityBinding.textViewD.text= answerD

                    QuizActivityBinding.progressBarQuiz.visibility= View.INVISIBLE
                    QuizActivityBinding.linearLayoutInfo.visibility = View.VISIBLE
                    QuizActivityBinding.linearLayoutQuestion.visibility = View.VISIBLE
                    QuizActivityBinding.linearLayoutButons.visibility = View.VISIBLE

                    startTimer()
                }
                else{
                    val dialogMessage = AlertDialog.Builder(this@QuizActivity)
                    dialogMessage.setTitle("Quiz Game")
                    dialogMessage.setMessage("Congratulations!!! \nYou have answered all questions. Do you want to see the result? ")
                    dialogMessage.setCancelable(false)
                    dialogMessage.setPositiveButton("See Result"){dialogWindow, position ->
                        sendScore()
                    }
                    dialogMessage.setNegativeButton("Play Again"){dialogWindow,position->
                        val intent=Intent(this@QuizActivity,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    dialogMessage.create().show()

                }
                questionNumber++


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }
    fun findAnswer(){
        when(correctAnswer)
        {
            "a"->QuizActivityBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b"->QuizActivityBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c"->QuizActivityBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d"->QuizActivityBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }
    fun disableClickable()
    {
        QuizActivityBinding.textViewA.isClickable=false
        QuizActivityBinding.textViewB.isClickable=false
        QuizActivityBinding.textViewC.isClickable=false
        QuizActivityBinding.textViewD.isClickable=false
    }
    fun restoreOptions()
    {
        QuizActivityBinding.textViewA.setBackgroundColor(Color.WHITE)
        QuizActivityBinding.textViewB.setBackgroundColor(Color.WHITE)
        QuizActivityBinding.textViewC.setBackgroundColor(Color.WHITE)
        QuizActivityBinding.textViewD.setBackgroundColor(Color.WHITE)

        QuizActivityBinding.textViewA.isClickable=true
        QuizActivityBinding.textViewB.isClickable=true
        QuizActivityBinding.textViewC.isClickable=true
        QuizActivityBinding.textViewD.isClickable=true
    }

    private fun startTimer()
    {
            timer = object : CountDownTimer(leftTime,1000L){
                override fun onTick(millisUntilFinish: Long) {
                    leftTime=millisUntilFinish
                    updateCountDownText()
                }

                override fun onFinish() {
                    disableClickable()
                    resetTimer()
                    updateCountDownText()
                    QuizActivityBinding.textViewQuestion.text="Sorry, Time is Up ! Continue with next question. "
                    timerContinue=false
                }

            }.start()
        timerContinue=true
    }
    fun updateCountDownText()
    {
        val remainingTime :Int =(leftTime/1000).toInt()
        QuizActivityBinding.textViewTime.text=remainingTime.toString()
    }
    fun pauseTimer()
    {
        timer.cancel()
        timerContinue=false
    }
    fun resetTimer()
    {
        pauseTimer()
        leftTime=totalTime
        updateCountDownText()
    }
    fun sendScore(){
        user?.let{
            val userUID = it.uid
            scoreReference.child("scores").child(userUID).child("correct").setValue(userCorrect)
            scoreReference.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {
                Toast.makeText(applicationContext,"Score sent to database successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}