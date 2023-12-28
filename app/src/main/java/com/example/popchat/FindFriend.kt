package com.example.popchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class FindFriend : AppCompatActivity() {
    private lateinit var bottomNavigationView : BottomNavigationView
    private lateinit var friendRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var searchView: SearchView
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_friend)
        bottomNavigationView = findViewById(R.id.bottomNav)
        searchView = findViewById(R.id.searchFriend)
        bottomNavigationView.setSelectedItemId(R.id.idFind)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idSong -> {
                    val a = Intent(this@FindFriend, Home::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idSearch -> {
                    val a = Intent(this@FindFriend, SongSearch::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idChat -> {
                    val b = Intent(this@FindFriend, Chats::class.java)
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

        friendRecyclerView = findViewById(R.id.friendRecyclerView)
        friendRecyclerView.layoutManager = LinearLayoutManager(this)
        friendRecyclerView.adapter = adapter

        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if(mAuth.currentUser?.uid != currentUser?.uid){
                        userList.add(currentUser!!)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

    }
    private fun filterList(query: String?){
        if(query != null){
            val filteredList = ArrayList<User>()
            for(i in userList){
                if(i.name!!.lowercase().contains(query)){
                    filteredList.add(i)
                }
            }
            if(filteredList.isEmpty()){

                Toast.makeText(this,"No User Found", Toast.LENGTH_SHORT).show()
            }else{
                adapter.setFilteredList(filteredList)
            }
        }
    }
}