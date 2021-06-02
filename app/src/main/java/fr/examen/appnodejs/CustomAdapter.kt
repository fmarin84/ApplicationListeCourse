package fr.examen.appnodejs

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop


class CustomAdapter(
    val context: Context
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    var list = mutableListOf<ListShop>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position])

//        println("list[position]")
//        println(list[position])

        holder.tvShop.setOnClickListener {
            val intent = Intent(context, ItemActivity::class.java)
            intent.putExtra("list", list[position])
            context.startActivity(intent)
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return list.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvShop = itemView.findViewById(R.id.textViewShop) as TextView
        val tvDate  = itemView.findViewById(R.id.textViewDate) as TextView

        fun bindItems(list: ListShop) {
            tvShop.text = list.shop
            tvDate.text = list.date
        }
    }


}