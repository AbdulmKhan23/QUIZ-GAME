package com.khan.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.khan.quizgame.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var ForgotPasswordActivityBinding:ActivityForgotPasswordBinding
    val auth =FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ForgotPasswordActivityBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = ForgotPasswordActivityBinding.root
        setContentView(view)

        ForgotPasswordActivityBinding.buttonForgot.setOnClickListener {
            val userEmail = ForgotPasswordActivityBinding.editTextForgotEmail.text.toString()
            forgotPassword(userEmail)

        }
    }
    fun forgotPassword(userEmail:String){
        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                Toast.makeText(applicationContext,"Password Reset Email Sent",Toast.LENGTH_SHORT).show()
                finish()

            }
            else
            {
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        }

    }
}