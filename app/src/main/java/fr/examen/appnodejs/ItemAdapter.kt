package fr.examen.appnodejs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.examen.appnodejs.api.Item
import fr.examen.appnodejs.api.ItemRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemAdapter(
    val context: Context,
    val apiClient: ApiClient,
    val isArchive: Boolean
): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    var item = mutableListOf<Item>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        var v = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        if(isArchive){
            v = LayoutInflater.from(parent.context).inflate(R.layout.item_archive_layout, parent, false)
        }
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        holder.bindItems(item[position])

        if(isArchive === false) {

            holder.btModif.setOnClickListener {

                val obj = item[position]

                val dlg = Dialog(context)
                dlg.setContentView(R.layout.item_edit)
                val qte = dlg.findViewById<EditText>(R.id.editQte)
                val label = dlg.findViewById<EditText>(R.id.editLabel)

                qte.setText(obj.quantity.toString())
                label.setText(obj.label)

                dlg.show()

                dlg.findViewById<Button>(R.id.btEditCancel).setOnClickListener {
                    dlg.dismiss()
                }

                dlg.findViewById<Button>(R.id.btEditAdd).setOnClickListener {
                    if(!label.text.isBlank() && !qte.text.isBlank()) {

                        obj.label = label.text.toString()
                        obj.quantity = qte.text.toString().toInt()

                        apiClient.getApiService(context).updateItem(ItemRequest(item = obj))
                            .enqueue(object : Callback<Item> {

                                override fun onFailure(call: Call<Item>, t: Throwable) {
                                }

                                override fun onResponse(call: Call<Item>, response: Response<Item>) {
                                }

                            })

                        notifyDataSetChanged()
                        qte.text.clear()
                        label.text.clear()
                        dlg.dismiss()
                    }
                }
            }


            holder.btDelete.setOnClickListener {
                val obj = item[position]

                val builder = AlertDialog.Builder(context)
                builder.setMessage(R.string.art_message_delete_confir)
                    .setNegativeButton(R.string.buttoncancel,
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                    .setPositiveButton(R.string.buttondelete,
                        DialogInterface.OnClickListener { dialog, _ ->

                            apiClient.getApiService(context).deleteItem(id = obj.id)
                                .enqueue(object : Callback<Item> {

                                    override fun onFailure(call: Call<Item>, t: Throwable) {
                                    }

                                    override fun onResponse(call: Call<Item>, response: Response<Item>) {
                                    }

                                })
                            item.removeAt(position)
                            notifyDataSetChanged()
                            dialog.dismiss()
                        })
                builder.create().show()
            }

            holder.check.setOnClickListener {

                val obj = item[position]

                // if ternaire
                if(holder.check.isChecked){ obj.checked = true } else {  obj.checked = false }

                apiClient.getApiService(context).updateItem(ItemRequest(item = obj))
                    .enqueue(object : Callback<Item> {

                        override fun onFailure(call: Call<Item>, t: Throwable) {
                        }

                        override fun onResponse(call: Call<Item>, response: Response<Item>) {
                        }

                    })
            }
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
        val btDelete = itemView.findViewById(R.id.btDeleteItem) as ImageView
        val btModif = itemView.findViewById(R.id.bteditItem) as ImageView

        fun bindItems(item: Item) {
            tvLabel.text = item.label
            tvQte.text = item.quantity.toString()
            check.isChecked = item.checked
        }
    }

}