package center.tilda.tildaapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar

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

        Database.add(Objava("Naslov 1", "Tekst objave"))
        Database.add(Objava("Naslov 2", "Tekst objave"))
        Database.add(Objava("Naslov 3", "Tekst objave"))
        Database.add(Objava("Naslov 4", "Tekst objave"))
        Database.add(Objava("Naslov 5", "Tekst objave"))
        Database.add(Objava("Naslov 6", "Tekst objave"))
        Database.add(Objava("Naslov 7", "Tekst objave"))
        Database.add(Objava("Naslov 8", "Tekst objave"))
        Database.add(Objava("Naslov 9", "Tekst objave"))
        Database.add(Objava("Naslov 10", "Tekst objave"))
        Database.add(Objava("Naslov 11", "Tekst objave"))
        Database.add(Objava("Naslov 12", "Tekst objave"))
        Database.add(Objava("Naslov 13", "Tekst objave"))
        Database.add(Objava("Naslov 14", "Tekst objave"))

        viewAdapter = ObjavaAdapter(Database.listaObjava, this)

        viewManager = LinearLayoutManager(this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager

            adapter = viewAdapter
        }

    }

    override fun onResume(){
        super.onResume()
        viewAdapter.notifyDataSetChanged()
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
