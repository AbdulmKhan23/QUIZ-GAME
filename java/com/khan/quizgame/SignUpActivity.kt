package com.khan.quizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.khan.quizgame.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var SignUpActivityBinding:ActivitySignUpBinding
    val auth:FirebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SignUpActivityBinding=ActivitySignUpBinding.inflate(layoutInflater)
        val view = SignUpActivityBinding.root
        setContentView(view)

        SignUpActivityBinding.buttonSignUp.setOnClickListener {
            val email= SignUpActivityBinding.editTextSignUpEmail.text.toString()
            val password = SignUpActivityBinding.editTextSignUpPassword.text.toString()
            signUpWithFirebase(email,password)

        }
    }

    fun signUpWithFirebase(email:String, password:String){

        SignUpActivityBinding.progressBarSignUp.visibility=View.INVISIBLE
        SignUpActivityBinding.buttonSignUp.isClickable=false

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                Toast.makeText(applicationContext,"Account Created Successfully",Toast.LENGTH_SHORT).show()
                SignUpActivityBinding.progressBarSignUp.visibility=View.VISIBLE
                SignUpActivityBinding.buttonSignUp.isClickable=true
                finish()
            }
            else
            {
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                finish()
            }

        }

    }

}