package com.khan.quizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.khan.quizgame.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var loginActivityBinding:ActivityLoginBinding
    val auth = FirebaseAuth.getInstance()
    lateinit var googleSignInClient:GoogleSignInClient
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding=ActivityLoginBinding.inflate(layoutInflater)
        val view = loginActivityBinding.root
        setContentView(view)

        val textOfGoogleButon = loginActivityBinding.buttonGoogleSignIn.getChildAt(0) as TextView
        textOfGoogleButon.text="Continue With Google"
        textOfGoogleButon.setTextColor(Color.BLACK)
        textOfGoogleButon.textSize= 18F

        registerActivityForGoogleSignIn()

        loginActivityBinding.buttonSignIn.setOnClickListener {
            val email= loginActivityBinding.editTextEmail.text.toString()
            val password= loginActivityBinding.editTextPassword.text.toString()

            signInWithFirebase(email,password)
        }
        loginActivityBinding.buttonGoogleSignIn.setOnClickListener {
            signInGoogle()

        }
        loginActivityBinding.textViewSignUp.setOnClickListener {

            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)

        }
        loginActivityBinding.textViewForgotPassword.setOnClickListener {
            val intent =Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)

        }
    }

    fun signInWithFirebase(email:  String,password: String)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                Toast.makeText(applicationContext,"Welcome to Quiz Game",Toast.LENGTH_SHORT).show()
                val intent=Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
            {
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()

            }
        }

    }

    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if(user  != null)
        {
            Toast.makeText(applicationContext,"Welcome to Quiz Game",Toast.LENGTH_SHORT).show()
            val intent=Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

   private fun signInGoogle()
    {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("127652346339-khe39mvgg434u6hde0dv30cocgs1p7hc.apps.googleusercontent.com").requestEmail().build()

        googleSignInClient=GoogleSignIn.getClient(this,gso)
        signIn()
    }
    private fun signIn()
    {
        val signInIntent :Intent=googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }
    private fun registerActivityForGoogleSignIn(){
        activityResultLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result ->
                val resultCode=result.resultCode
                 val data = result.data

                if(resultCode == RESULT_OK && data!=null){
                    val task:Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
                    firebaseSignInWithGoogle(task)
                }
            })
    }
    private fun firebaseSignInWithGoogle(task:Task<GoogleSignInAccount>){

        try{
            val account : GoogleSignInAccount = task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext,"Welcome To Quiz Game",Toast.LENGTH_SHORT).show()
            val intent=Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }
        catch (e:ApiException){
            Toast.makeText(applicationContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
        }

    }

    private fun firebaseGoogleAccount(account:GoogleSignInAccount){
        val authCredential = GoogleAuthProvider.getCredential(account.idToken,null)
        auth.signInWithCredential(authCredential).addOnCompleteListener { task->
            if(task.isSuccessful)
            {
                val auth = auth.currentUser

            }
        }
    }
}