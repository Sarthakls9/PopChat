package com.example.popchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.popchat.R.layout
import com.example.popchat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: messageAdapter
    private lateinit var messageList: ArrayList<message>
    private lateinit var mDbRef: DatabaseReference
    private lateinit var backbtn: ImageView


    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_chat)

        backbtn = findViewById(R.id.back)
        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.send_btn)
        messageList = ArrayList()
        messageAdapter = messageAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter
        val name = intent.getStringExtra("name")
        val RecieverUid = intent.getStringExtra("uid")
        val profileurl = intent.getStringExtra("profile")

        Picasso.get().load(profileurl).into(findViewById<CircleImageView>(R.id.profile_image))
        findViewById<TextView>(R.id.chatName).text = name
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()
        supportActionBar?.title = name

        senderRoom = RecieverUid + senderUid
        recieverRoom = senderUid + RecieverUid

        mDbRef.child("chats").child(senderRoom!!).child("messages").addValueEventListener(
            object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()
                    for(postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                    chatRecyclerView.scrollToPosition(chatRecyclerView.adapter!!.itemCount - 1)

                }

                override fun onCancelled(error: DatabaseError) {

                }

            }
        )

        sendButton.setOnClickListener{
            val message = messageBox.text.toString()
            //message: String?, senderId: String?,title: String?, coverImg: String?, preview: String?
            val messageObject = message(message, senderUid,null,null,null)

            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject)
                .addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom!!).child("messages").push().setValue(messageObject)
                }
            messageBox.setText("")
        }

        backbtn.setOnClickListener{
            val b = Intent(this@ChatActivity, Chats::class.java)
            startActivity(b)
            overridePendingTransition(0,0)

        }

    }
}