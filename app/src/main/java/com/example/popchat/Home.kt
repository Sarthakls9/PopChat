package com.example.popchat

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
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
import java.io.BufferedReader
import java.io.InputStreamReader

class Home : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private val mediaPlayer = MediaPlayer()
    lateinit var myRecyclerView: RecyclerView
    lateinit var myRecyclerView2: RecyclerView
    lateinit var myRecyclerView3: RecyclerView
    lateinit var myAdapter: Horizontal_songs1
    private lateinit var userList: ArrayList<User>
    var curName: String = "Greetings .."
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    lateinit var myAdapter2: csvAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()

        if (! Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }



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

        findViewById<TextView>(R.id.titleName).text = "$curName"

        myRecyclerView= findViewById(R.id.recycleView1)
        myRecyclerView3 = findViewById(R.id.rv3)
        val songList = readCSVFile()
        myAdapter2 = csvAdapter(this@Home, songList, mediaPlayer)
        myRecyclerView3.adapter=myAdapter2
        myRecyclerView3.layoutManager = LinearLayoutManager(this@Home, LinearLayoutManager.HORIZONTAL, false)

        myRecyclerView2= findViewById(R.id.rv2)

        var retrofitBuilder = Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)



        var retrofitData= retrofitBuilder.getData("eminem")

        retrofitData.enqueue(object : Callback<Mydata?> {
            override fun onResponse(call: Call<Mydata?>, response: Response<Mydata?>) {
                //success
                val dataList = response.body()?.data!!

                myAdapter= Horizontal_songs1(this@Home,dataList,mediaPlayer,userList)
                myRecyclerView.adapter=myAdapter
                myRecyclerView.layoutManager=LinearLayoutManager(this@Home,LinearLayoutManager.HORIZONTAL,false)
                Log.d("TAG:onResponse","onResponse:" + response.body())

            }
            override fun onFailure(call: Call<Mydata?>, t: Throwable) {
                //failure
                Log.d("TAG:onFailure", "onFailure: " + t.message)
            }
        })


        var retrofitData2 = retrofitBuilder.getData("joeyy")

        retrofitData2.enqueue(object : Callback<Mydata?> {
            override fun onResponse(call: Call<Mydata?>, response: Response<Mydata?>) {
                //success
                val dataList2 = response.body()?.data!!

                myAdapter= Horizontal_songs1(this@Home,dataList2,mediaPlayer,userList)
                myRecyclerView2.adapter=myAdapter
                myRecyclerView2.layoutManager=LinearLayoutManager(this@Home,LinearLayoutManager.HORIZONTAL,false)
                Log.d("TAG:onResponse","onResponse:" + response.body())

            }
            override fun onFailure(call: Call<Mydata?>, t: Throwable) {
                //failure
                Log.d("TAG:onFailure", "onFailure: " + t.message)
            }
        })


        
        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setSelectedItemId(R.id.idSong)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.idFind -> {
                    val a = Intent(this@Home, FindFriend::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idSearch -> {
                    val a = Intent(this@Home, SongSearch::class.java)
                    startActivity(a)
                    overridePendingTransition(0,0)
                }
                R.id.idChat -> {
                    val b = Intent(this@Home, Chats::class.java)
                    startActivity(b)
                    overridePendingTransition(0,0)
                }
            }
            false
        }



    }

    private fun readCSVFile(limit:  Int = 7): List<csvData> {
        val csvDataList = mutableListOf<csvData>()
        val inputStream = assets.open("deezera.csv")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line: String? = ""
        var count = 0
        reader.readLine()
        while (count < limit && reader.readLine().also { line = it } != null){
            Log.d("CSVData", line ?: "Empty line")
            val parts = line?.split(",") // adjust separator if needed
            if (parts?.size == 36){
                val song = csvData(
                    parts[0].toBigInteger(),
                    parts[1].toBoolean(),
                    parts[2],
                    parts[3],
                    parts[4],
                    parts[5],
                    parts[6].toInt(),
                    parts[7].toInt(),
                    parts[8].toBoolean(),
                    parts[9].toInt(),
                    parts[10].toInt(),
                    parts[11],
                    parts[12],
                    parts[13].toInt(),
                    parts[14],
                    parts[15],
                    parts[16],
                    parts[17],
                    parts[18],
                    parts[19],
                    parts[20],
                    parts[21],
                    parts[22],
                    parts[23].toInt(),
                    parts[24],
                    parts[25],
                    parts[26],
                    parts[27],
                    parts[28],
                    parts[29],
                    parts[30],
                    parts[31],
                    parts[32],
                    parts[33],
                    parts[34],
                    parts[35]
                )
                csvDataList.add(song)
                count++

            }
        }
        reader.close()
        for (song in csvDataList) {
            Log.d("CSVDataList", "Song: ${song.data__title}")
        }
        return csvDataList
    }
}


