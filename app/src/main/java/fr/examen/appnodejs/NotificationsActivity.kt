package fr.examen.appnodejs

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.examen.appnodejs.api.Notification
import fr.examen.appnodejs.api.User
import kotlinx.android.synthetic.main.activity_notifications.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsActivity : AppCompatActivity() {

    private var TAG = "NotificationActivity"
    lateinit var notificationAdapter: NotificationAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var apiClient: ApiClient

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        var context = this
        apiClient = ApiClient()
        sessionManager = SessionManager(this)

        initRecyclerView()

        //getting recyclerview from xml
        val recyclerView = findViewById<RecyclerView>(R.id.rcNotifications)

        //adding a layoutmanager
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        fetchAllNotifications()
    }

    private fun initRecyclerView(){
        rcNotifications.apply {
            notificationAdapter = NotificationAdapter(context, apiClient)
            adapter = notificationAdapter
        }
    }


    /**
     * Function to fetch Notifications
     */
    private fun fetchAllNotifications() {

        // Pass the token as parameter

        apiClient.getApiService(this).fetchCurrentUser()
            .enqueue(object : Callback<User> {

                override fun onFailure(call: Call<User>, t: Throwable) {
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val UsersResponse = response.body()!!

                    apiClient.getApiService(this@NotificationsActivity)
                        .fetchAllNotifications(UsersResponse.id)
                        .enqueue(object : Callback<List<Notification>> {

                            override fun onResponse(
                                call: Call<List<Notification>>,
                                response: Response<List<Notification>>
                            ) {
                                val NotificationResponse = response.body()!!

                                if (response.code() == 200) {
                                    notificationAdapter.notifications =
                                        NotificationResponse.toMutableList()
                                    notificationAdapter.notifyDataSetChanged()
                                }
                            }

                            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                            }
                        })
                }
            })
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