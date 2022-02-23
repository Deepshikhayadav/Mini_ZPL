package com.deepshikhayadav.zomatopremierleague

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.app.DatePickerDialog
import android.app.Dialog
import java.util.*
import kotlin.collections.HashMap
import android.widget.TimePicker

import android.app.TimePickerDialog
import android.content.Intent
import android.text.format.DateFormat.is24HourFormat
import android.view.ViewGroup
import android.view.Window

import android.widget.DatePicker
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialDialogs
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import android.widget.Toast

import androidx.annotation.NonNull
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.firestore.FirestoreRecyclerOptions

import com.google.android.gms.tasks.OnFailureListener

import com.google.firebase.firestore.DocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.FirebaseFirestoreException

import android.view.LayoutInflater

import com.google.android.material.snackbar.Snackbar

import com.firebase.ui.firestore.FirestoreRecyclerAdapter


class AdminLogin : AppCompatActivity(),DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,AdminAdapter.MyClickListener {
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    private val TAG: String = MainActivity::class.java.simpleName
    var hashMap= HashMap<String,String>()

    var day = 0
    var month:Int = 0
    var year:Int = 0
    var hour:Int = 0
    var minute:Int = 0
    var myday = 0
    var myMonth:Int = 0
    var myYear:Int = 0
    var myHour:Int = 0
    var myMinute:Int = 0
    var myTime:String?=null
    lateinit var tv_time:TextView
    var list= ArrayList<MyModel>()
    var mList= ArrayList<MyModel>()
    var c=MyModel()
    var myAdapter=AdminAdapter(list,this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)
       // val user = findViewById<EditText>(R.id.user)
        val team1 = findViewById<Spinner>(R.id.team1)
        val team2 = findViewById<Spinner>(R.id.team2)
        val spin = findViewById<Spinner>(R.id.spin)
        val button = findViewById<Button>(R.id.button)
        val picker=findViewById<ImageView>(R.id.picker)
        tv_time=findViewById<TextView>(R.id.time)
        val winner_tv=findViewById<TextView>(R.id.winner)
        val coin_tv=findViewById<TextView>(R.id.coin)
        var recyclerView=findViewById<RecyclerView>(R.id.adminRecycler)
        var recyclerView2=findViewById<RecyclerView>(R.id.recycleCoin)
        val logout=findViewById<ImageView>(R.id.logout)

        logout.setOnClickListener {
            // auth.signOut()
            AuthUI.getInstance().signOut(this).addOnSuccessListener {
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,MainActivity::class.java))
            }
        }
         auth = Firebase.auth



        db.collection("winner team")
            .get()
            .addOnSuccessListener {
                 queryDocumentSnapshots ->
                        if (!queryDocumentSnapshots.isEmpty) {
                            val list2 = queryDocumentSnapshots.documents
                            for (d in list2) {
                                c= d.toObject(MyModel::class.java)!!
                                c.id=d.id
                                list.add(c)
                            }

                            myAdapter= AdminAdapter(list,this)
                            recyclerView.adapter =myAdapter

                        }
                    }.addOnFailureListener {
                        Toast.makeText(this, "Failed to get the data.", Toast.LENGTH_SHORT)
                            .show()
                    }

        db.collection("Coin Toss")
            .get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (!queryDocumentSnapshots.isEmpty) {
                    val list2 = queryDocumentSnapshots.documents
                    for (d in list2) {
                        c = d.toObject(MyModel::class.java)!!
                        c.id = d.id
                        list.add(c)
                    }
                    recyclerView.adapter = AdminAdapter(list, this)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to get the data.", Toast.LENGTH_SHORT)
                    .show()
            }



        picker.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH)
            day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                DatePickerDialog(this, this, year, month, day)
            datePickerDialog.show()

        }

        auth = Firebase.auth

        val a_team1 = arrayOf(
            "Mumbai Indians",
            "chennai super kings",
            "delhi capitals",
            "knight riders",
            "rajsthan royals",
            "royal challenger banglore",
            "sunriser"
        )
        val a_team2 = arrayOf(
            "Mumbai Indians",
            "chennai super kings",
            "delhi capitals",
            "knight riders",
            "rajsthan royals",
            "royal challenger banglore",
            "sunriser"
        )

        val game = arrayOf("Coin Toss", "Man of the match", "winner team")
        setAdapter(spin, game,"game")
        setAdapter(team1, a_team1,"team1")
        setAdapter(team2, a_team2,"team2")

    button.setOnClickListener {
            list.clear()
            mList.clear()
            hashMap["time"] = myTime.toString()
            hashMap["winner"]=""

        if(hashMap["game"]=="Coin Toss"){
            db.collection("Coin Toss")
                .add(hashMap)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this@AdminLogin,"Data submitted", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            db.collection("Coin Toss")
                .get()
                .addOnSuccessListener {
                        queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        val list2 = queryDocumentSnapshots.documents
                        for (d in list2) {
                            c = d.toObject(MyModel::class.java)!!
                            c.id = d.id
                            mList.add(c)
                        }

                        myAdapter = AdminAdapter(mList, this)
                        recyclerView2.adapter = myAdapter
                        if (myAdapter.itemCount>0){
                            coin_tv.visibility=View.VISIBLE
                        }
                    }

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }

       else if(hashMap["game"]=="winner team"){
            db.collection("winner team")
                .add(hashMap)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this@AdminLogin,"Data submitted", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            db.collection("winner team")
                .get()
                .addOnSuccessListener {
                        queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        val list2 = queryDocumentSnapshots.documents
                        for (d in list2) {
                            c = d.toObject(MyModel::class.java)!!
                            c.id = d.id
                            mList.add(c)
                        }

                        myAdapter = AdminAdapter(mList, this)
                        recyclerView.adapter = myAdapter
                        if (myAdapter.itemCount>0){
                            winner_tv.visibility=View.VISIBLE
                        }
                    }

                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

        }

                   }
        mList.clear()
    }


    private fun setAdapter(spin: Spinner, game: Array<String>,s:String) {
        if (spin != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, game)
            spin.adapter=adapter

            spin.onItemSelectedListener= object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    hashMap[s] = game[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {

        myYear = p1
        myday = p3
        myMonth = p2
        val c = Calendar.getInstance()
        hour = c[Calendar.HOUR]
        minute = c[Calendar.MINUTE]

        val timePickerDialog = TimePickerDialog(
            this,
            this,
            hour,
            minute,
            is24HourFormat(this)
        )
        timePickerDialog.show()


    }


    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        myHour = p1
        myMinute = p2

        if(myMinute/10==0){
            if(myHour/10==0){
                if(myday/10==0){
                    if(myMonth/10==0){
                        myTime="0${myHour}:0${myMinute}:00 0${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="0${myHour}:0${myMinute}:00 0${myday}/${myMonth}/${myYear}"
                    }
                }
                else{
                    if(myMonth/10==0){
                        myTime="0${myHour}:0${myMinute}:00 ${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="0${myHour}:0${myMinute}:00 ${myday}/${myMonth}/${myYear}"
                    }

                }

            }
            else{
                if(myday/10==0){
                    if(myMonth/10==0){
                        myTime="${myHour}:0${myMinute}:00 0${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="${myHour}:0${myMinute}:00 0${myday}/${myMonth}/${myYear}"
                    }
                }
                else{
                    if(myMonth/10==0){
                        myTime="${myHour}:0${myMinute}:00 ${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="${myHour}:0${myMinute}:00 ${myday}/${myMonth}/${myYear}"
                    }

                }
            }
        }
        else{

            if(myHour/10==0){
                if(myday/10==0){
                    if(myMonth/10==0){
                        myTime="0${myHour}:${myMinute}:00 0${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="0${myHour}:${myMinute}:00 0${myday}/${myMonth}/${myYear}"
                    }
                }
                else{
                    if(myMonth/10==0){
                        myTime="0${myHour}:${myMinute}:00 ${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="0${myHour}:${myMinute}:00 ${myday}/${myMonth}/${myYear}"
                    }

                }

            }
            else{
                if(myday/10==0){
                    if(myMonth/10==0){
                        myTime="${myHour}:${myMinute}:00 0${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="${myHour}:${myMinute}:00 0${myday}/${myMonth}/${myYear}"
                    }
                }
                else{
                    if(myMonth/10==0){
                        myTime="${myHour}:${myMinute}:00 ${myday}/0${myMonth}/${myYear}"
                    }
                    else{
                        myTime="${myHour}:${myMinute}:00 ${myday}/${myMonth}/${myYear}"
                    }

                }
            }
        }
        tv_time.text=myTime.toString()
          }

    override fun onClick(team1: String, team2: String, myId:String?,time:String?,game:String?) {
        val a_team = arrayOf(
            team1,
            team2
        )
        var hashMap2=HashMap<String,String>()

        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_layout)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        val spin = dialog.findViewById(R.id.editText) as Spinner
        val yesBtn = dialog.findViewById(R.id.button) as Button
        val noBtn=dialog.findViewById(R.id.cancel) as Button

        if (spin != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, a_team)
            spin.adapter=adapter

            spin.onItemSelectedListener= object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                    hashMap2["winner"] = a_team[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
       noBtn.setOnClickListener {
           dialog.dismiss()
       }
        yesBtn.setOnClickListener {
            val myModel=MyModel("",team1,team2,time,game,hashMap2["winner"])
           db.collection("winner team")
                .document(myId!!).get().addOnCompleteListener {task->
                    if(task.isSuccessful){
                        val document=task.result
                        if(document!=null){
                            if(document.exists()){
                                if (myId != null) {
                                    db.collection("winner team")
                                        .document(myId)
                                        .set(myModel)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Data has been updated..", Toast.LENGTH_SHORT)
                                                .show()

                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                this,
                                                "Fail to update the data..",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }

                            }
                        }
                    }
                }
            db.collection("Coin Toss")
                .document(myId!!).get().addOnCompleteListener {task->
                    if(task.isSuccessful){
                        val document=task.result
                        if(document!=null){
                            if(document.exists()){
                                if (myId != null) {
                                    db.collection("Coin Toss")
                                        .document(myId)
                                        .set(myModel)
                                        .addOnSuccessListener {
                                            Toast.makeText(this, "Data has been updated..", Toast.LENGTH_SHORT)
                                                .show()

                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                this,
                                                "Fail to update the data..",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }

                            }
                        }
                    }
                }


            dialog.dismiss()
        }

        dialog.show()
    }


}
