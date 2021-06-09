package fr.examen.appnodejs

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import fr.examen.appnodejs.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubscribActivity : AppCompatActivity() {

    private var TAG = "SubsrcibActivity"
    private var isAbonne:Boolean = false
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscrib)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        val btSubscrib = findViewById<Button>(R.id.btSubscrib)
        val etNom = findViewById<EditText>(R.id.etFirstName)
        val etPrenom = findViewById<EditText>(R.id.etLastName)

        apiClient.getApiService(this).fetchCurrentUser()
                .enqueue(object : Callback<User> {
                    override fun onFailure(call: Call<User>, t: Throwable) {
                    }

                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val UsersResponse = response.body()!!
                        apiClient.getApiService(this@SubscribActivity).fetchRolesUser(userId = UsersResponse.id)
                                .enqueue(object : Callback<List<Role>> {

                                    override fun onFailure(call: Call<List<Role>>, t: Throwable) {
                                    }

                                    override fun onResponse(call: Call<List<Role>>, response: Response<List<Role>>) {

                                        val roles = response.body()
                                        if (roles != null) {
                                            for (role in roles.toMutableList()) {
                                                println(role)

                                                if (role.level == 20) {
                                                    println("isAbonne a true")

                                                    isAbonne = true
                                                }
                                            }
                                        }

                                    }

                                })
                    }
                })

        btSubscrib.setOnClickListener {
            if(isAbonne == true){
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.subscrib_message_erreur)
                        .setPositiveButton(R.string.buttonclose,
                                DialogInterface.OnClickListener { dialog, _ ->
                                    dialog.dismiss()
                                })
                builder.create().show()
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.subscrib_message_confir)
                        .setNegativeButton(R.string.buttoncancel,
                                DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() })
                        .setPositiveButton(R.string.buttonconfirm,
                                DialogInterface.OnClickListener { dialog, _ ->

                                    apiClient.getApiService(this).fetchCurrentUser()
                                            .enqueue(object : Callback<User> {

                                                override fun onFailure(call: Call<User>, t: Throwable) {
                                                }

                                                override fun onResponse(call: Call<User>, response: Response<User>) {
                                                    val UsersResponse = response.body()!!
                                                    val userId =  UsersResponse.id
                                                    val obj = Payment(0,  etNom.text.toString(), etPrenom.text.toString(),userId, "02/02/2020")

                                                    apiClient.getApiService(this@SubscribActivity).insertPayment(PaymentRequest(obj))
                                                            .enqueue(object : Callback<Payment> { override fun onFailure(call: Call<Payment>,t: Throwable) {
                                                            }

                                                                override fun onResponse( call: Call<Payment>, response: Response<Payment>) {
                                                                }
                                                            })

                                                    apiClient.getApiService(this@SubscribActivity).addRoleUser(userId= userId, roleId = 3)
                                                            .enqueue(object : Callback<User> {
                                                                override fun onFailure(    call: Call<User>,   t: Throwable ) {
                                                                }

                                                                override fun onResponse(  call: Call<User>,  response: Response<User>) {
                                                                }

                                                            })

                                                }
                                            })

                                    dialog.dismiss()
                                })
                builder.create().show()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.menu_deco){
            sessionManager = SessionManager(this)
            sessionManager.saveAuthToken("")
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_notif){
            var intent = Intent(this, NotificationsActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_abonnement){
            var intent = Intent(this, SubscribActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_historique){
            var intent = Intent(this, ListArchiveActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_share){
            var intent = Intent(this, ListShareActivity::class.java)
            startActivity(intent)
        }

        if(item.itemId == R.id.menu_accueil){
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listofcourse_menu, menu)
        return true
    }

}