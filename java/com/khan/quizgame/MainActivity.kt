package com.khan.quizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.khan.quizgame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    lateinit var MainActivityBinding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityBinding=ActivityMainBinding.inflate(layoutInflater)
        val view = MainActivityBinding.root
        setContentView(view)

        MainActivityBinding.buttonStartQuiz.setOnClickListener {
            val intent =Intent(this@MainActivity,QuizActivity::class.java)
            startActivity(intent)
        }

        MainActivityBinding.buttonSignOut.setOnClickListener {
            //email and password only
            auth.signOut()
            //google sign out
            val gso =GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()
            val googleSignInClient = GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    Toast.makeText(applicationContext,"Sign Out is Successful",Toast.LENGTH_SHORT).show()
                }
            }
            val intent= Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}