package fr.examen.appnodejs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.Notification
import fr.examen.appnodejs.api.NotificationRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

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

            val toast = Toast.makeText(context, "Notification marquée comme lue.", Toast.LENGTH_LONG)
            toast.show()
            notifications.removeAt(position)
            notifyDataSetChanged()
        }

        holder.btSee.setOnClickListener {

            val obj = notifications[position]

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.notifications_show)
            val tvNotifTitle = dlg.findViewById<TextView>(R.id.tvNotifTitle)
            val tvNotifContenu = dlg.findViewById<TextView>(R.id.tvNotifContenu)

            tvNotifTitle.setText(obj.titre.toString())
            tvNotifContenu.setText(obj.text.toString())

            dlg.show()

            dlg.findViewById<Button>(R.id.btFermer).setOnClickListener {
                dlg.dismiss()
            }

        }

        holder.tvNTitle.setOnClickListener {

            val obj = notifications[position]

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.notifications_show)
            val tvNotifTitle = dlg.findViewById<TextView>(R.id.tvNotifTitle)
            val tvNotifContenu = dlg.findViewById<TextView>(R.id.tvNotifContenu)

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
        val btSee = itemView.findViewById(R.id.btSee) as ImageView
        val tvContenu = itemView.findViewById(R.id.textViewContenu) as TextView

        fun bindItems(notification: Notification) {
            tvNTitle.text = notification.titre
            tvContenu.text = notification.text
            val parser =  SimpleDateFormat("yyyy-dd-MM")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = formatter.format(parser.parse(notification.created_at))
            tvDate.text = formattedDate
        }
    }

}