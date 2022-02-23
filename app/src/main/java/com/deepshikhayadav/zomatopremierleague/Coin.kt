package com.deepshikhayadav.zomatopremierleague

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.google.firebase.database.annotations.Nullable
import com.google.firebase.firestore.*
import com.google.firebase.firestore.FirebaseFirestore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import java.util.*
import kotlin.collections.ArrayList

class Coin : AppCompatActivity(),MyTimeUpdate {
    private lateinit var auth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()
    private val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        auth = Firebase.auth
        var list = ArrayList<MyModel>()
        db.collection("Coin Toss")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list2 = queryDocumentSnapshots.documents
                    for (d in list2) {
                        val c = d.toObject(MyModel::class.java)!!
                        c.id = d.id
                        list.add(c)

                    }
                }

                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = MyAdapter(list, this)
            }



    }
    override fun dataUpdate(
        time: String,
        team1: String,
        team2: String,
        time_tv: TextView,
        team1_iv: ImageView,
        team2_iv: ImageView,
        btn1: Button,
        btn2: Button,
        position:Int,
        id:String?
    ) {
        val a_team = mapOf<String,Int>(
            "Mumbai Indians" to R.drawable.mumbai_indians,
            "chennai super kings" to R.drawable.chennai_super_kings,
            "delhi capitals" to R.drawable.delhi_capitals,
            "knight riders" to R.drawable.knight_riders,
            "rajsthan royals" to R.drawable.rajasthan_royals,
            "royal challenger banglore" to R.drawable.royal_challengers_banglore,
            "sunriser" to R.drawable.sunrise
        )
        team1_iv.setImageResource(a_team[team1]!!)
        team2_iv.setImageResource(a_team[team2]!!)

        handler.post(object : Runnable {
            override fun run() {
                handler.postDelayed(this, 1000)
                updateMyTime(time,btn1,btn2,time_tv)

            }

            private fun updateMyTime(time: String, btn1: Button, btn2: Button, timeTv: TextView) {
                val currentDate = Calendar.getInstance()
                 val eventDate = Calendar.getInstance()
                eventDate[Calendar.YEAR] = time.subSequence(15,time.length).toString().toInt()
                eventDate[Calendar.MONTH] =time.subSequence(12,14).toString().toInt()
                eventDate[Calendar.DAY_OF_MONTH] = time.subSequence(9,11).toString().toInt()
                eventDate[Calendar.HOUR] = time.subSequence(0,2).toString().toInt()
                eventDate[Calendar.MINUTE] = time.subSequence(3,5).toString().toInt()
                eventDate[Calendar.SECOND] = 0
                eventDate.timeZone = TimeZone.getTimeZone("GMT")

                val diff = eventDate.timeInMillis - currentDate.timeInMillis
                val days = diff / (24 * 60 * 60 * 1000)
                val hours = diff / (1000 * 60 * 60) % 24
                val minutes = diff / (1000 * 60) % 60
                val seconds = (diff / 1000) % 60
                time_tv.post{
                    if(days>=0 && hours>=0 && minutes>=0 && seconds>=0) {
                        time_tv.text= "${days}D ${hours}H ${minutes}M ${seconds}S"
                        val currentUser = auth.currentUser!!.displayName
                      var docRef:String?=null
                        var docRef2:String?=null


                        btn1.setOnClickListener {
                            val user = hashMapOf(
                                "category" to "Coin Toss",
                                "Choice" to team1,
                                "Win" to "Pending",
                                "prize" to "$0",
                                "id" to id
                            )
                           if(docRef!=null){
                               db.collection("WinOrLossCoin$currentUser")
                                   .document(docRef!!)
                                   .set(user)
                                   .addOnSuccessListener { documentRef->
                                       Toast.makeText(this@Coin," Your choice $team1", Toast.LENGTH_SHORT).show() }
                                   .addOnFailureListener { e->Toast.makeText(this@Coin," Error $e", Toast.LENGTH_SHORT).show() }
                               finish()
                           }
                            else{
                               db.collection("WinOrLossCoin$currentUser")
                                   .add(user)
                                   .addOnSuccessListener { documentRef->
                                       docRef=documentRef.id
                                       Toast.makeText(this@Coin," Your choice $team1", Toast.LENGTH_SHORT).show() }
                                   .addOnFailureListener { e->Toast.makeText(this@Coin," Error $e", Toast.LENGTH_SHORT).show() }
                               finish()
                           }




                        }
                        btn2.setOnClickListener {
                            val user = hashMapOf(
                                "category" to "winner team",
                                "Choice" to team2,
                                "Win" to "Pending",
                                "prize" to "$0",
                                "id" to id
                            )
                            if(docRef!=null){
                                db.collection("WinOrLossCoin$currentUser")
                                    .document(docRef!!)
                                    .set(user)
                                    .addOnSuccessListener { documentRef->
                                        Toast.makeText(this@Coin," Your choice $team1", Toast.LENGTH_SHORT).show() }
                                    .addOnFailureListener { e->Toast.makeText(this@Coin," Error $e", Toast.LENGTH_SHORT).show() }
                                finish()
                            }
                            else{
                                db.collection("WinOrLossCoin$currentUser")
                                    .add(user)
                                    .addOnSuccessListener { documentRef->
                                        docRef=documentRef.id
                                        Toast.makeText(this@Coin," Your choice $team1", Toast.LENGTH_SHORT).show() }
                                    .addOnFailureListener { e->Toast.makeText(this@Coin," Error $e", Toast.LENGTH_SHORT).show() }
                                finish()
                            }

                        }
                    }
                    else{
                        time_tv.text= "Your time has been expired"
                        btn1.isEnabled=false
                        btn2.isEnabled=false
                    }
                }
            }
        })


    }



}