package fr.examen.appnodejs

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class ListArchiveAdapter (
    val context: Context,
    val apiClient: ApiClient
) : RecyclerView.Adapter<ListArchiveAdapter.ViewHolder>(){

    var list = mutableListOf<ListShop>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListArchiveAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_archive_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ListArchiveAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position])

        holder.btDelete.setOnClickListener {
            val obj = list[position]

            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.list_message_delete_confir)
                .setNegativeButton(R.string.buttoncancel,
                    DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .setPositiveButton(R.string.buttondelete,
                    DialogInterface.OnClickListener { dialog, _ ->

                        apiClient.getApiService(context).deleteList(id = obj.id)
                            .enqueue(object : Callback<ListShop> {

                                override fun onFailure(call: Call<ListShop>, t: Throwable) {
                                }
                                override fun onResponse(
                                    call: Call<ListShop>,
                                    response: Response<ListShop>
                                ) {
                                }

                            })

                        notifyItemRemoved(position)
//                        notifyItemRangeRemoved(position, item.size )
//                        notifyDataSetChanged()

                        dialog.dismiss()
                    })
            builder.create().show()
        }

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
        val tvShop = itemView.findViewById(R.id.textViewNotif) as TextView
        val tvDate  = itemView.findViewById(R.id.textViewDate) as TextView
        val btDelete = itemView.findViewById(R.id.btDelete) as ImageView

        fun bindItems(list: ListShop) {
            tvShop.text = list.shop
            val parser =  SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = formatter.format(parser.parse(list.date))
            tvDate.text = formattedDate
        }
    }

}