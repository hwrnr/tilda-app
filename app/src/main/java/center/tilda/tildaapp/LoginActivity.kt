package center.tilda.tildaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_novi.*
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun loginToServer(view: View) {
        val username = usernameEditText.text.toString()
        val password = passwordEditText.text.toString()

        Server.setContext(this.applicationContext)

        try {
            Server.login(username, password) { isOk: Boolean, response: JSONObject? ->
                if (!isOk) {
                    Snackbar.make(loginRoot, "Error authenticating to server", Snackbar.LENGTH_LONG)
                        .show()
                    return@login
                }
                val intent = Intent(this, NoviActivity::class.java)
                startActivity(intent)
                this.finish()
            }
        }catch (exception: ServerException){
            Snackbar.make(loginRoot, exception.message.toString(), Snackbar.LENGTH_LONG).show()
        }
    }
}
