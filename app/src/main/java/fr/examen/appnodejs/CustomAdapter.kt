package fr.examen.appnodejs

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.ListShop
import fr.examen.appnodejs.api.ListShopRequest
import fr.examen.appnodejs.api.User
import kotlinx.android.synthetic.main.list_share_edit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CustomAdapter(
    val context: Context,
    val apiClient: ApiClient
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


        holder.btShare.setOnClickListener {

            val obj = list[position]

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.list_share_edit)
            /*
            val etDate = dlg.findViewById<EditText>(R.id.editTextDate)
            val etShop = dlg.findViewById<EditText>(R.id.editShop)
            etShop.setText(obj.shop.toString())
            */
            val isState = dlg.findViewById<CheckBox>(R.id.isState)
            val spUser = dlg.findViewById<Spinner>(R.id.spUser)



            // Pass the token as parameter
            apiClient.getApiService(context).fetchUsers()
                    .enqueue(object : Callback<List<User>> {

                        override fun onFailure(call: Call<List<User>>, t: Throwable) {
                            TODO("Not yet implemented")
                        }

                        override fun onResponse(
                            call: Call<List<User>>,
                            response: Response<List<User>>
                        ) {

                            val UsersResponse = response.body()!!

                            if (response.code() == 200) {

                                val adapter: ArrayAdapter<User> = ArrayAdapter(
                                    context,
                                    android.R.layout.simple_spinner_item,
                                    UsersResponse.toMutableList()
                                )
                                spUser.adapter = adapter

                            }
                        }
                    })


            dlg.show()

            dlg.findViewById<Button>(R.id.btCancel).setOnClickListener {
                dlg.dismiss()
            }

            dlg.findViewById<Button>(R.id.btPartage).setOnClickListener {
                //if(!etShop.text.isBlank() && !etDate.text.isBlank()) {

                val user = spUser.selectedItem as User

                var state = 0
                if(isState.toString().toBoolean()){
                     state = 1
                }

                    apiClient.getApiService(context).addListShare(obj.id, user.id, state)
                            .enqueue(object : Callback<ListShop> {
                                override fun onFailure(call: Call<ListShop>, t: Throwable) {
                                }

                                override fun onResponse(
                                    call: Call<ListShop>,
                                    response: Response<ListShop>
                                ) {
                                }

                            })

                    notifyDataSetChanged()
                    dlg.dismiss()
                }
            //}
        }

        holder.btArchive.setOnClickListener {

            val obj = list[position]

            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.list_message_archive_confir)
                .setNegativeButton(R.string.buttoncancel,
                    DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                .setPositiveButton(R.string.buttonarchive,
                    DialogInterface.OnClickListener { dialog, _ ->

                        obj.archived = true

                        apiClient.getApiService(context).updateList(ListShopRequest(list = obj))
                            .enqueue(object : Callback<ListShop> {

                                override fun onFailure(call: Call<ListShop>, t: Throwable) {
                                }

                                override fun onResponse(
                                    call: Call<ListShop>,
                                    response: Response<ListShop>
                                ) {
                                }

                            })
                        list.removeAt(position)
                        notifyDataSetChanged()
                        dialog.dismiss()
                    })
            builder.create().show()
        }

        holder.btModif.setOnClickListener {
            val obj = list[position]

            val dlg = Dialog(context)
            dlg.setContentView(R.layout.list_edit)
            val etShop = dlg.findViewById<EditText>(R.id.editShop)
            etShop.setText(obj.shop.toString())

            val etDate = dlg.findViewById<EditText>(R.id.editTextDate)

            val parser =  SimpleDateFormat("yyyy-MM-dd")
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val formattedDate = formatter.format(parser.parse(obj.date))

            etDate.setOnClickListener(View.OnClickListener {
                val cldr = Calendar.getInstance()
                cldr.setTime(parser.parse(obj.date)) // all done
                val day = cldr[Calendar.DAY_OF_MONTH]
                val month = cldr[Calendar.MONTH]
                val year = cldr[Calendar.YEAR]
                // date picker dialog
                val picker = DatePickerDialog(
                    context,
                    { view, year, monthOfYear, dayOfMonth -> etDate.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year) },
                    year,
                    month,
                    day
                )
                picker.show()
            })

            etDate.setText(formattedDate)

            dlg.show()

            dlg.findViewById<Button>(R.id.btEditCancel).setOnClickListener {
                dlg.dismiss()
            }

            dlg.findViewById<Button>(R.id.btEditAdd).setOnClickListener {
                if(!etShop.text.isBlank() && !etDate.text.isBlank()) {

                    obj.shop= etShop.text.toString()
                    obj.date = etDate.text.toString()

                    apiClient.getApiService(context).updateList(ListShopRequest(list = obj))
                        .enqueue(object : Callback<ListShop> {
                            override fun onFailure(call: Call<ListShop>, t: Throwable) {
                            }

                            override fun onResponse(
                                call: Call<ListShop>,
                                response: Response<ListShop>
                            ) {
                            }

                        })

                    notifyDataSetChanged()
                    etDate.text.clear()
                    etShop.text.clear()
                    dlg.dismiss()
                }
            }
        }

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

                        list.removeAt(position)
                        notifyDataSetChanged()
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
        val btModif = itemView.findViewById(R.id.btModif) as ImageView
        val btArchive = itemView.findViewById(R.id.btArchive) as ImageView
        val btShare = itemView.findViewById(R.id.btShare) as ImageView

        fun bindItems(list: ListShop) {
            tvShop.text = list.shop
            if(list.date.length <= 10){
                tvDate.text = list.date
            } else {
                val parser =  SimpleDateFormat("yyyy-MM-dd")
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                val formattedDate = formatter.format(parser.parse(list.date))
                tvDate.text = formattedDate
            }

        }
    }



}