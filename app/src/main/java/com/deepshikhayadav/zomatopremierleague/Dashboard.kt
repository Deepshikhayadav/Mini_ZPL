package com.deepshikhayadav.zomatopremierleague

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Dashboard : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView:RecyclerView
    var list= ArrayList<ChoiceModel>()
    lateinit var linear:LinearLayout
    lateinit var linear3:LinearLayout
    lateinit var win_tv:TextView
    lateinit var played:TextView
    lateinit var lose_tv:TextView
    lateinit var award_tv:TextView
    lateinit var logout:ImageView
    var c=MyModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

         linear=findViewById<LinearLayout>(R.id.linear)
        linear3=findViewById<LinearLayout>(R.id.linear3)
         played=findViewById<TextView>(R.id.played)
        win_tv=findViewById<TextView>(R.id.win)
         lose_tv=findViewById<TextView>(R.id.lose)
         award_tv=findViewById<TextView>(R.id.award)
         logout=findViewById<ImageView>(R.id.logout)

        logout.setOnClickListener {
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                Toast.makeText(this,"Log Out",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
        linear.setOnClickListener{
            startActivity(Intent(this,Coin::class.java))
        }

        linear3.setOnClickListener{
            startActivity(Intent(this,TeamWinner::class.java))
        }

        recyclerView=findViewById<RecyclerView>(R.id.recycle)
        auth = Firebase.auth

    }

    override fun onStart() {
        super.onStart()
        list.clear()
    }
    override fun onResume() {
        super.onResume()
        list.clear()
        val currentUser = auth.currentUser!!.displayName
        var winCount =0
        var loseCount=0
        db.collection("WinOrLoss$currentUser")
            .get()
            .addOnSuccessListener {
                for(dc: DocumentChange in it?.documentChanges!!){
                    if(dc.type== DocumentChange.Type.ADDED){
                        val e=dc.document.toObject(ChoiceModel::class.java)

                        db.collection("winner team")
                            .get()
                            .addOnSuccessListener {
                                    queryDocumentSnapshots ->
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list2 = queryDocumentSnapshots.documents
                                    for (d in list2) {
                                        c= d.toObject(MyModel::class.java)!!
                                        c.id=d.id
                                        if(e.id==c.id){
                                           if(c.winner!=""){
                                               if(e.Choice==c.winner){
                                                   e.Win="Won"
                                                   winCount++
                                                   win_tv.text=winCount.toString()
                                                   award_tv.text="${20*winCount}"
                                                   db.collection("WinOrLoss$currentUser")
                                                       .document(dc.document.id!!)
                                                       .set(e)
                                                       .addOnSuccessListener {

                                                       }.addOnFailureListener {
                                                           Toast.makeText(
                                                               this,
                                                               "Fail to update the data..",
                                                               Toast.LENGTH_SHORT
                                                           ).show()
                                                       }
                                               }
                                               else{
                                                   e.Win="Loss"
                                                   loseCount++
                                                   lose_tv.text=loseCount.toString()
                                                   db.collection("WinOrLoss$currentUser")
                                                       .document(dc.document.id!!)
                                                       .set(e)
                                                       .addOnSuccessListener {

                                                       }.addOnFailureListener {
                                                           Toast.makeText(
                                                               this,
                                                               "Failed to get data",
                                                               Toast.LENGTH_SHORT
                                                           ).show()
                                                       }
                                               }
                                           }
                                        }

                                    }

                                }
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed to get the data.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        list.add(e)
                    }
                }

                recyclerView.setHasFixedSize(false)
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = HomeAdapter(list)
                played.text=list.size.toString()
            }

        db.collection("WinOrLossCoin$currentUser")
            .get()
            .addOnSuccessListener {
                for(dc: DocumentChange in it?.documentChanges!!){
                    if(dc.type== DocumentChange.Type.ADDED){
                        val e=dc.document.toObject(ChoiceModel::class.java)

                        db.collection("Coin Toss")
                            .get()
                            .addOnSuccessListener {
                                    queryDocumentSnapshots ->
                                if (!queryDocumentSnapshots.isEmpty) {
                                    val list2 = queryDocumentSnapshots.documents
                                    for (d in list2) {
                                        c= d.toObject(MyModel::class.java)!!
                                        c.id=d.id
                                        if(e.id==c.id){
                                           if(c.winner!=""){
                                               if(e.Choice==c.winner){
                                                   e.Win="Won"
                                                   winCount++
                                                   win_tv.text=winCount.toString()
                                                   award_tv.text="${20*winCount}"

                                                   db.collection("WinOrLossCoin$currentUser")
                                                       .document(dc.document.id!!)
                                                       .set(e)
                                                       .addOnSuccessListener {

                                                       }.addOnFailureListener {
                                                           Toast.makeText(
                                                               this,
                                                               "Failed to get data...",
                                                               Toast.LENGTH_SHORT
                                                           ).show()
                                                       }
                                               }
                                               else{
                                                   e.Win="Loss"
                                                   loseCount++
                                                   lose_tv.text=loseCount.toString()
                                                   db.collection("WinOrLossCoin$currentUser")
                                                       .document(dc.document.id!!)
                                                       .set(e)
                                                       .addOnSuccessListener {

                                                       }.addOnFailureListener {
                                                           Toast.makeText(
                                                               this,
                                                               "Failed to get data...",
                                                               Toast.LENGTH_SHORT
                                                           ).show()
                                                       }
                                               }
                                           }
                                        }

                                    }

                                }
                            }.addOnFailureListener {
                                Toast.makeText(this, "Failed to get the data.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        list.add(e)
                    }
                }
                recyclerView.setHasFixedSize(false)
                recyclerView.itemAnimator = DefaultItemAnimator()
                recyclerView.adapter = HomeAdapter(list)
                played.text=list.size.toString()
            }
    }

}

