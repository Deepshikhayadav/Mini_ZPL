package com.deepshikhayadav.zomatopremierleague

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HomeAdapter (private val res: List<ChoiceModel>) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var team=itemView.findViewById<TextView>(R.id.teamName)
       var win= itemView.findViewById<TextView>(R.id.win)
        var category= itemView.findViewById<TextView>(R.id.category)
        fun bind(myModel: ChoiceModel){
           team.text=myModel.Choice
            win.text=myModel.Win
           category.text=myModel.category
        }

    }

    override fun getItemCount(): Int = res.size
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =holder.bind(res[position])
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_recycler, parent, false)
        return MyViewHolder(view)
    }
}
