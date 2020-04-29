package center.tilda.tildaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.activity_novi.fab
import kotlinx.android.synthetic.main.activity_novi.toolbar
import kotlinx.android.synthetic.main.content_novi.*

class NoviActivity : AppCompatActivity() {

    private lateinit var viewAdapter: ObjavaAdapter
    private lateinit var viewManager: LinearLayoutManager

    private val listaObjava = ArrayList<Objava>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novi)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { _ ->
            //listaObjava.add(Objava("Nova Objava", "Tekst objave"))
            //listaObjava[3].naslov = "Novi naslov"
            listaObjava.removeAt(3)
            viewAdapter.notifyDataSetChanged()
        }

        listaObjava.add(Objava("Naslov 1", "Tekst objave"))
        listaObjava.add(Objava("Naslov 2", "Tekst objave"))
        listaObjava.add(Objava("Naslov 3", "Tekst objave"))
        listaObjava.add(Objava("Naslov 4", "Tekst objave"))
        listaObjava.add(Objava("Naslov 5", "Tekst objave"))
        listaObjava.add(Objava("Naslov 6", "Tekst objave"))
        listaObjava.add(Objava("Naslov 7", "Tekst objave"))
        listaObjava.add(Objava("Naslov 8", "Tekst objave"))
        listaObjava.add(Objava("Naslov 9", "Tekst objave"))
        listaObjava.add(Objava("Naslov 10", "Tekst objave"))
        listaObjava.add(Objava("Naslov 11", "Tekst objave"))
        listaObjava.add(Objava("Naslov 12", "Tekst objave"))
        listaObjava.add(Objava("Naslov 13", "Tekst objave"))
        listaObjava.add(Objava("Naslov 14", "Tekst objave"))

        viewAdapter = ObjavaAdapter(listaObjava)

        viewManager = LinearLayoutManager(this)

        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager

            adapter = viewAdapter
        }

    }
}
