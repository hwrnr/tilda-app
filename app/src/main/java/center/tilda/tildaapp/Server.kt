package center.tilda.tildaapp

import android.content.Context
import android.util.Log
import com.android.volley.NetworkResponse
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

object Server {
    const val host: String = "http://192.168.113.15:5000"

    private lateinit var context: Context

    private lateinit var requestQueue: RequestQueue

    private var CSRF_ACCESS_TOKEN: String? = null
    private var ACCESS_TOKEN_COOKIE: String? = null
    private var CSRF_REFRESH_TOKEN: String? = null
    private var REFRESH_TOKEN_COOKIE: String? = null

    private var validUntil: Date = Date()

    private var brojNeuspelihPrijava = 0

    public fun setContext(context: Context){
        this.context = context
        requestQueue = Volley.newRequestQueue(this.context)
    }

    public fun login(username: String, password: String, callback: (Boolean, JSONObject?) -> Unit){

        if ( brojNeuspelihPrijava > 5){
            throw ServerException("Prekoračen broj pokušaja prijava")
        }

        val url = "$host/api/v0/auth/login"

        val params = HashMap<String, String>()
        params["email"] = username
        params["password"] = password

        val loginData = JSONObject(params as Map<*, *>)

        Log.i("JSONObject", loginData.getString("email"))
        // Request a string response from the provided URL.
        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, loginData,
                Response.Listener { response ->

                    this@Server.validUntil = Date()
                    this@Server.validUntil.time += Integer.parseInt(response["accessExpire"].toString()) * 1000 * 9/10 //Time in miliseconds

                    callback(true, response)

                },
                Response.ErrorListener { error ->

                    brojNeuspelihPrijava++
                    callback(false, null)


                }
        ){
            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {

                val responseHeaders = response!!.allHeaders

                for (i in responseHeaders) {
                    Log.i("Headers", i.name + ": " + i.value)
                    if (i.value.startsWith("csrf_access_token=")){
                        this@Server.CSRF_ACCESS_TOKEN = i.value.substring(18, 54) //TODO: Don't hardcode csrf cookie
                    }
                    else if (i.value.startsWith("access_token_cookie=")){
                        this@Server.ACCESS_TOKEN_COOKIE = i.value.substring(20, i.value.indexOf(';'))
                    }
                    else if (i.value.startsWith("csrf_refresh_token=")){
                        this@Server.CSRF_REFRESH_TOKEN = i.value.substring(19, i.value.indexOf(";"))
                    }
                    else if (i.value.startsWith("refresh_token_cookie=")){
                        this@Server.REFRESH_TOKEN_COOKIE = i.value.substring("refresh_token_cookie=".length, i.value.indexOf(';'))
                    }
                }
                Log.i("csrf_access_token=", this@Server.CSRF_ACCESS_TOKEN!!)
                Log.i("access_token_cookie=", this@Server.ACCESS_TOKEN_COOKIE!!)
                Log.i("csrf_refresh_token=", this@Server.CSRF_REFRESH_TOKEN!!)
                Log.i("refresh_token_cookie=", this@Server.REFRESH_TOKEN_COOKIE!!)
                return super.parseNetworkResponse(response)
            }
        }

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest)

    }

    private fun refreshIfNeeded() {
        if (Date().after(validUntil)){
            this.refresh();
        }
    }

    private fun refresh(){
        val url = "$host/api/v0/auth/refresh"

        val jsonObjectRequest = object: JsonObjectRequest(Request.Method.POST, url, null,
                Response.Listener { response ->

                    Log.i("Body", response.toString())
                    this@Server.ACCESS_TOKEN_COOKIE = response["access"].toString()
                    this@Server.validUntil = Date()
                    this@Server.validUntil.time += Integer.parseInt(response["accessExpire"].toString()) * 1000 * 9/10//Time in miliseconds
                },
                Response.ErrorListener { error ->

                    var temp : String = "";
                    for (i in error.networkResponse.data){
                        temp += i.toChar()
                    }
                    Log.i("Body", temp)
                    // TODO: Handle error
                }
        ){
            override fun getHeaders(): MutableMap<String, String> {
                //val params = super.getHeaders() as MutableMap<String, String>
                val params = HashMap<String, String>()
                try {
                    params["Cookie"] = "refresh_expire=\"2021-02-26 21:00:47.044989\"" +
                            "; refresh_token_cookie=" + this@Server.REFRESH_TOKEN_COOKIE +
                            "; access_token_cookie=" + this@Server.ACCESS_TOKEN_COOKIE
                            //"; csrf_access_token=" + this@Server.CSRF_ACCESS_TOKEN +
                            //"; csrf_refresh_token=" + this@Server.CSRF_REFRESH_TOKEN

                    params["X-CSRF-TOKEN"] = this@Server.CSRF_REFRESH_TOKEN!!

                    Log.i("Cookie", params["Cookie"]!!)
                    Log.i("X-CSRF-TOKEN", params["X-CSRF-TOKEN"]!!)
                }catch (ignored: Exception){
                    Log.e("Cookie", "Ovde je greska " + ignored.javaClass)
                    Log.e("Cookie", this@Server.ACCESS_TOKEN_COOKIE!!)
                    Log.e("Cookie", this@Server.CSRF_ACCESS_TOKEN!!)
                    Log.e("Cookie", this@Server.CSRF_REFRESH_TOKEN!!)
                }
                return params
            }
            override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject> {

                val responseHeaders = response!!.allHeaders

                for (i in responseHeaders) {
                    Log.i("Headers", i.name + ": " + i.value)
                    if (i.value.startsWith("csrf_access_token=")){
                        this@Server.CSRF_ACCESS_TOKEN = i.value.substring(18, 54) //TODO: Don't hardcode csrf cookie
                    }
                    else if (i.value.startsWith("access_token_cookie=")){
                        this@Server.ACCESS_TOKEN_COOKIE = i.value.substring(20, i.value.length)
                    }
                    else if (i.value.startsWith("csrf_refresh_token=")){
                        this@Server.CSRF_REFRESH_TOKEN = i.value.substring(19, 54)
                    }
                }
                Log.i("csrf_access_token=", this@Server.CSRF_ACCESS_TOKEN!!)
                Log.i("access_token_cookie=", this@Server.ACCESS_TOKEN_COOKIE!!)
                Log.i("csrf_refresh_token=", this@Server.CSRF_REFRESH_TOKEN!!)
                return super.parseNetworkResponse(response)

            }
        }

        // Add the request to the RequestQueue.
        requestQueue.add(jsonObjectRequest)

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

class ServerException(message: String?) : Exception(message) {

}