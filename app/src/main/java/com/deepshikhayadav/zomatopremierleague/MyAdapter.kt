package com.deepshikhayadav.zomatopremierleague

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val res: List<MyModel>,private val myTimeUpdate: MyTimeUpdate) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
//private var r=res.values
    companion object {
        var clicked:Boolean=false
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var time2=itemView.findViewById<TextView>(R.id.time2)
        var team1=itemView.findViewById<ImageView>(R.id.imageView2)
        var team2=itemView.findViewById<ImageView>(R.id.imageView)

        var btn1=itemView.findViewById<Button>(R.id.btn3)
        var btn2=itemView.findViewById<Button>(R.id.btn4)


        fun bind(myModel: MyModel, myTimeUpdate: MyTimeUpdate){
            val mTime=myModel.time.toString()
            val mTeam1=myModel.team1.toString()
            val mTeam2=myModel.team2.toString()
           /*btn1.setOnClickListener {
               if(clicked){
                   btn1.isEnabled= true
                   btn2.isEnabled= true
               }
               else{
                   btn1.isEnabled= false
                   btn2.isEnabled= false
               }

           }
            btn2.setOnClickListener {
                if(clicked){
                    btn1.isEnabled= true
                    btn2.isEnabled= true
                }
                else{
                    btn1.isEnabled= false
                    btn2.isEnabled= false
                }
            }*/

            myTimeUpdate.dataUpdate(mTime,mTeam1,mTeam2,time2,team1,team2,btn1,btn2,adapterPosition,myModel.id)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.coin_recycler_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =holder.bind(res[position],myTimeUpdate)

    override fun getItemCount(): Int = res.size /*r.size*/
}

interface MyTimeUpdate{
    fun dataUpdate(time:String,team1:String,team2:String,time_tv:TextView,team1_iv:ImageView,team2_iv:ImageView,btn1:Button,
                   btn2:Button,position: Int,id:String?)
}