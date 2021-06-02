package fr.examen.appnodejs

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.examen.appnodejs.api.Item
import fr.examen.appnodejs.api.ItemRequestUpdate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemAdapter(
    val context: Context,
    val apiClient: ApiClient
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var item = mutableListOf<Item>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(item[position])

        holder.check.setOnClickListener {

            val obj = item[position]

            // if ternaire
            if(holder.check.isChecked){
                obj.checked = true
            } else {
                obj.checked = false
            }

            apiClient.getApiService(context).updateItem(ItemRequestUpdate(item = obj))
                .enqueue(object : Callback<Item> {

                    override fun onFailure(call: Call<Item>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<Item>, response: Response<Item>) {
                    }

                })
        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return item.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvLabel = itemView.findViewById(R.id.textViewLabel) as TextView
        val tvQte = itemView.findViewById(R.id.textViewQte) as TextView
        val check = itemView.findViewById(R.id.checkBox) as CheckBox

        fun bindItems(item: Item) {
            tvLabel.text = item.label
            tvQte.text = item.quantity.toString()
            check.isChecked = item.checked
        }
    }

}