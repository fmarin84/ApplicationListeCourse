package fr.examen.appnodejs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop

class CustomAdapter() : RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    var list = mutableListOf<ListShop>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: CustomAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return list.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName = itemView.findViewById(R.id.textViewShop) as TextView
        val textViewAddress  = itemView.findViewById(R.id.textViewDate) as TextView

        fun bindItems(list: ListShop) {
            textViewName.text = list.shop
            textViewAddress.text = list.date
        }
    }


}