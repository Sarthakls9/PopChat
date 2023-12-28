package com.example.popchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtpasswd: EditText
    private lateinit var btnlogin: Button
    private lateinit var showHidepswd: ImageView
    private lateinit var btnsignup: Button
    private lateinit var mAuth: FirebaseAuth
    var isPswdshown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.EmailET)
        edtpasswd = findViewById(R.id.PasswdET)
        btnlogin = findViewById(R.id.loginBtn)
        btnsignup = findViewById(R.id.signupBtn)
        showHidepswd = findViewById(R.id.show_pass_img)

        showHidepswd.setOnClickListener{
            if(isPswdshown)
            {
                edtpasswd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                showHidepswd.setImageResource(R.drawable.eye)
                isPswdshown = false
            }else{
                edtpasswd.inputType = InputType.TYPE_CLASS_TEXT
                showHidepswd.setImageResource(R.drawable.close_eye)
                isPswdshown = true
            }
            edtpasswd.setSelection(edtpasswd.text.length)
        }

        btnsignup.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        btnlogin.setOnClickListener {
            val email = edtEmail.text.toString()
            val password = edtpasswd.text.toString()

            login(email, password)
        }

    }

    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this@Login, Home::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
}