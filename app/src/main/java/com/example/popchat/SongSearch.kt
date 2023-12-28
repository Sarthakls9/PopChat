package com.example.popchat

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.database.ValueEventListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SongSearch : AppCompatActivity() {
    private lateinit var bottomNavigationView: BottomNavigationView
    private val mediaPlayer = MediaPlayer()
    lateinit var myRecyclerView: RecyclerView
    lateinit var myAdapter: MyAdapter
    private lateinit var dataList2 :ArrayList<Data>
    lateinit var searchView: SearchView
    private lateinit var userList: ArrayList<User>
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_search)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
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

        myRecyclerView = findViewById(R.id.recycleView)
        searchView = findViewById<SearchView>(R.id.searchview)
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)

        dataList2 = ArrayList()
        var retrofitData = retrofitBuilder.getData("eminem")

        retrofitData.enqueue(object : Callback<Mydata?> {
            override fun onResponse(call: Call<Mydata?>, response: Response<Mydata?>) {
                //success
                val dataList = response.body()?.data!!
                dataList2 = dataList as ArrayList<Data>

            }
            override fun onFailure(call: Call<Mydata?>, t: Throwable) {
                //failure
                Log.d("TAG:onFailure", "onFailure: " + t.message)
            }
        })
        retrofitData = retrofitBuilder.getData("joeyy")

        retrofitData.enqueue(object : Callback<Mydata?> {
            override fun onResponse(call: Call<Mydata?>, response: Response<Mydata?>) {
                //success
                val dataList = response.body()?.data!!
                dataList2 += dataList as ArrayList<Data>
                myAdapter=MyAdapter(this@SongSearch,dataList,mediaPlayer,userList)
                myRecyclerView.adapter=myAdapter
                myRecyclerView.layoutManager= LinearLayoutManager(this@SongSearch)
                Log.d("TAG:onResponse","onResponse:" + response.body())

            }
            override fun onFailure(call: Call<Mydata?>, t: Throwable) {
                //failure
                Log.d("TAG:onFailure", "onFailure: " + t.message)
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

        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setSelectedItemId(R.id.idSearch)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idFind -> {
                    val a = Intent(this@SongSearch, FindFriend::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idSong -> {
                    val a = Intent(this@SongSearch, Home::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idChat -> {
                    val b = Intent(this@SongSearch, Chats::class.java)
                    startActivity(b)
                    overridePendingTransition(0,0)
                }
            }
            false
        }

    }
    private fun filterList(query: String?){
        if(query != null){
            var filteredList = ArrayList<Data>()
            for(i in dataList2 ){
                if(i.title!!.lowercase().contains(query)){
                    filteredList.add(i)
                }
            }
            if(filteredList.isEmpty()){
                Toast.makeText(this,"No song Found", Toast.LENGTH_SHORT).show()
            }else{
                myAdapter.setFilteredList(filteredList)
            }
        }
    }
}