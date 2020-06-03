package center.tilda.tildaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import kotlinx.android.synthetic.main.activity_novi.fab
import kotlinx.android.synthetic.main.activity_novi.toolbar
import kotlinx.android.synthetic.main.content_novi.*

class NoviActivity : AppCompatActivity() {

    private lateinit var viewAdapter: ObjavaAdapter
    private lateinit var viewManager: LinearLayoutManager

    val dodajActivityRequestCode = 9832

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novi)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            //Database.add(Objava("Nova Objava", "Tekst objave"))
            //Database[3].naslov = "Novi naslov"
            //Database.removeAt(3)
            //viewAdapter.notifyDataSetChanged()
            val intent = Intent(this, DodajObjavu::class.java)
            startActivityForResult(intent, dodajActivityRequestCode)
        }

        viewAdapter = ObjavaAdapter(Database.listaObjava, this)

        viewManager = LinearLayoutManager(this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager

            adapter = viewAdapter
        }

        Server.setContext(this.applicationContext)

        swipeRefreshLayout.isRefreshing = true
        this.refreshBlogs()

        swipeRefreshLayout.setOnRefreshListener {
            this.refreshBlogs()
        }
    }

    override fun onResume(){
        super.onResume()
        viewAdapter.notifyDataSetChanged()
    }

    fun refreshBlogs(){
        Server.getBlogs { isOk, response ->
            if (!isOk) {
                Snackbar.make(recyclerView, "Error connecting to server", Snackbar.LENGTH_LONG).show()
                swipeRefreshLayout.isRefreshing = false
                return@getBlogs
            }
            Database.listaObjava.clear()
            val data = response!!.getJSONArray("data")
            //for (i = 0; i < data.length(); ++i)
            for (i in 0 until data.length()){
                val jsonObjave = data.getJSONObject(i)
                val objava = Objava(jsonObjave.getString("title"), jsonObjave.getString("content"))
                Database.add(objava)
            }
            viewAdapter.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == dodajActivityRequestCode){
            if (resultCode == 0) {
                viewAdapter.notifyDataSetChanged()
            }
            else {
                Snackbar.make(recyclerView, "Korisnik je prekinuo unos nove objave", Snackbar.LENGTH_LONG).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
