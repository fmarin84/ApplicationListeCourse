package fr.examen.appnodejs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop
import fr.examen.appnodejs.api.ListShopRequest
import fr.examen.appnodejs.api.Notification
import fr.examen.appnodejs.api.NotificationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationAdapter (
    val context: Context,
    val apiClient: ApiClient
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>(){

    var notifications = mutableListOf<Notification>()

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.notification_layout, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: NotificationAdapter.ViewHolder, position: Int) {
        holder.bindItems(notifications[position])

        holder.btLue.setOnClickListener {
            val obj = notifications[position]

            obj.isLue = true

            apiClient.getApiService(context).updateNotification(NotificationRequest(obj))
                .enqueue(object : Callback<Notification> {

                    override fun onFailure(call: Call<Notification>, t: Throwable) {
                    }

                    override fun onResponse(
                        call: Call<Notification>,
                        response: Response<Notification>
                    ) {
                    }

                })

            notifyItemRemoved(position)
            //notifyDataSetChanged()
        }

        holder.tvNTitle.setOnClickListener {

            val obj = notifications[position]

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.notifications_show)
            val tvNotifTitle = dlg.findViewById<TextView>(R.id.tvNotifTitle)
            val tvNotifContenu = dlg.findViewById<TextView>(R.id.tvNotifContenu)

//println("dateTime")
//            etDate.setText(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(obj.date))
            tvNotifTitle.setText(obj.titre.toString())
            tvNotifContenu.setText(obj.text.toString())

            dlg.show()

            dlg.findViewById<Button>(R.id.btFermer).setOnClickListener {
                dlg.dismiss()
            }

        }

    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return notifications.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNTitle = itemView.findViewById(R.id.tvNTitle) as TextView
        val tvDate = itemView.findViewById(R.id.textViewDate) as TextView
        val btLue = itemView.findViewById(R.id.btLue) as ImageView

        fun bindItems(notification: Notification) {
            tvNTitle.text = notification.titre
            tvDate.text = notification.created_at
        }
    }

}