package fr.examen.appnodejs

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.Item

class ItemAdapter(): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var item = mutableListOf<Item>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(item[position])

        println("item[position]")
        println(item[position])

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return item.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLabel = itemView.findViewById(R.id.textViewLabel) as TextView
        val tvQte = itemView.findViewById(R.id.textViewQte) as TextView

        fun bindItems(item: Item) {
            tvLabel.text = item.label
            tvQte.text = item.quantity.toString()
        }
    }

}