package center.tilda.tildaapp

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

object Server {
    var host: String = "http://192.168.113.15:5000"

    private lateinit var context: Context

    private lateinit var requestQueue: RequestQueue

    public fun setContext(context: Context){
        this.context = context
        requestQueue = Volley.newRequestQueue(this.context)
    }

    public fun getRequestQueue(): RequestQueue?{
        return requestQueue
    }

    public fun getBlogs(callback: (Boolean, JSONObject?) -> Unit){

        val url = "$host/api/v0/blogs"

        // Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
                Response.Listener { response ->
                    callback(true, response)
                },
                Response.ErrorListener { error ->
                    // TODO: Handle error
                    callback(false, null)
                }
        )

        // Add the request to the RequestQueue.
        this.requestQueue.add(jsonObjectRequest)
    }


}