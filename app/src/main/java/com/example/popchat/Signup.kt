package com.example.popchat

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.FirebaseStorage.*
import de.hdodenhof.circleimageview.CircleImageView


class Signup : AppCompatActivity() {

    private lateinit var circleImageView: CircleImageView
    private lateinit var SelectImage: CircleImageView

    private var imageUri: Uri? = null

    private lateinit var edtEmail: EditText
    private lateinit var edtpasswd: EditText
    private lateinit var edtName: EditText

    private lateinit var btnsignup: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()
        edtEmail = findViewById(R.id.EmailET)
        edtpasswd = findViewById(R.id.PasswdET)
        edtName = findViewById(R.id.nameET)
        btnsignup = findViewById(R.id.signupBtn)

        btnsignup.setOnClickListener {
            val name = edtName.text.toString()
            val email = edtEmail.text.toString()
            val password = edtpasswd.text.toString()

            signUp(name, email, password)

        }

        circleImageView = findViewById(R.id.profile_image)
        SelectImage = findViewById(R.id.profile_image)

        SelectImage.setOnClickListener {
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@Signup, Home::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Signup, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {

        mDbRef = FirebaseDatabase.getInstance().getReference()
        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(uid)
            .child("profie.jpg")

        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    mDbRef.child("user").child(uid).setValue(User(name, email, uid, it.toString()))

                }.addOnFailureListener {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }



    }

    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
                this.imageUri = imgUri
                circleImageView.setImageURI(imgUri)
            }
        }


}