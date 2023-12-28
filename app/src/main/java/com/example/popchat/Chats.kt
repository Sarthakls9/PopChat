package com.example.popchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Chats : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chats)

        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setSelectedItemId(R.id.idChat)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idFind -> {
                    val a = Intent(this@Chats, FindFriend::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idSearch -> {
                    val a = Intent(this@Chats, SongSearch::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idSong -> {
                    val b = Intent(this@Chats, Home::class.java)
                    startActivity(b)
                    overridePendingTransition(0,0)
                }
            }
            false
        }

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }

                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.log_out_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout){
            mAuth.signOut()
            val intent = Intent(this@Chats, Login::class.java)
            finish()
            startActivity(intent)
            return true
        }
        return true
    }


}