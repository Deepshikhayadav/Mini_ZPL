package com.deepshikhayadav.zomatopremierleague

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AdminAdapter (private val res: List<MyModel>,private val myClickListener: MyClickListener)
: RecyclerView.Adapter<AdminAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var team1=itemView.findViewById<TextView>(R.id.team1)
        var team2= itemView.findViewById<TextView>(R.id.team2)
        fun bind(myModel: MyModel,myClickListener: MyClickListener){
            team1.text=myModel.team1
            team2.text=myModel.team2
            val id=myModel.id
            val time=myModel.time
            val game=myModel.game
            itemView.setOnClickListener{
                myClickListener.onClick(myModel.team1.toString(),myModel.team2.toString(),id,time,game)
            }
        }

    }

    override fun getItemCount(): Int = res.size
    override fun onBindViewHolder(holder: AdminAdapter.MyViewHolder, position: Int) =holder.bind(res[position],myClickListener)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.admin_item, parent, false)
        return AdminAdapter.MyViewHolder(view)
    }
    interface MyClickListener{
        fun onClick(team1:String,team2:String,id:String?,time:String?,game:String?)
    }

}