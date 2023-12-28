package com.example.popchat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

open class ShareSongAdapter(val context: Context, var userList: ArrayList<User>, val sharedSongTitle: String,
                            val sharedSongcover: String, val sharedSongPreview: String):
    RecyclerView.Adapter<ShareSongAdapter.UserViewHolder>() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    var recieverRoom: String? = null
    var senderRoom: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout2, parent, false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val currentUser = userList[position]
        holder.textName.text = currentUser.name
        Picasso.get().load(currentUser.img_url).into(holder.img)

        holder.itemView.setOnClickListener{
            val senderUid = FirebaseAuth.getInstance().currentUser?.uid
            senderRoom = currentUser.uid + senderUid
            recieverRoom = senderUid + currentUser.uid
            //message: String?, senderId: String?,title: String?, coverImg: String?, preview: String?
            val messageObject = message(null,"song",sharedSongTitle,sharedSongcover,sharedSongPreview)
            mDbRef.child("chats").child(senderRoom!!).child("messages").push().setValue(messageObject)
                .addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom!!).child("messages").push().setValue(messageObject)
                }
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.txtName)
        val img = itemView.findViewById<ImageView>(R.id.profile_image)
    }

}