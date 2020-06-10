package center.tilda.tildaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_dodaj_objavu.*

class DodajObjavu : AppCompatActivity() {

    private var kreiramoNovuObjavu = true
    private var indexObjaveKojuMenjamo : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dodaj_objavu)

        this.indexObjaveKojuMenjamo = this.intent.getIntExtra("position", -1)

        if (this.indexObjaveKojuMenjamo > -1){ //menjamo postojeću objavu
            this.kreiramoNovuObjavu = false
            dodajObjavuButton.text = "Promeni objavu"
            naslovEditText.setText(Database.listaObjava[this.indexObjaveKojuMenjamo].naslov) //Uzimamo naslov objave koju menjamo i postavljamo je kao početnu vrednost
            sadrzajEditText.setText(Database.listaObjava[this.indexObjaveKojuMenjamo].sadrzaj) //Uzimamo sadržaj objave koju menjamo i postavljamo je kao početnu vrednost
        }
        else{ //kreiramo novu objavu
        }
    }

    fun dodajObjavu(view: View){
        val naslov = naslovEditText.text.toString()
        val sadrzaj = sadrzajEditText.text.toString()

        if (naslov == "" || sadrzaj == ""){
            Snackbar.make(dodajObjavuRoot, "Niste uneli sva potrebna polja", Snackbar.LENGTH_LONG).show()
            return
        }

        // Ukoliko kreiramo novu objavu, dodajemo je na kraj postojeće liste
        // U protivnom, menjamo naslov i sadržaj postojeće objave
        if (this.kreiramoNovuObjavu) {

            Server.createBlog(naslov, sadrzaj){ isOk, response ->
                if (isOk && response != null){
                    val novaObjava = Objava(response.getString("title"), response.getString("content"), response.getString("slug"))
                    Database.add(novaObjava)
                    this@DodajObjavu.setResult(0)
                    this@DodajObjavu.finish() // Sklanjamo activity sa ekrana
                }
                else {
                    Snackbar.make(dodajObjavuRoot, "Došlo je do greške prilikom kreiranja objave", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        else {
            val objava = Database.listaObjava[indexObjaveKojuMenjamo]
            Server.editBlog(naslov, sadrzaj, objava.slug) { isOk, response ->
                if (isOk){
                    objava.naslov = response!!.getString("title")
                    //objava.naslov = naslov
                    objava.naslov = response!!.getString("content")
                    //objava.sadrzaj = sadrzaj
                    objava.slug = response!!.getString("slug")
                    this.setResult(0)
                    this.finish() // Sklanjamo activity sa ekrana
                }
                else {
                    Snackbar.make(dodajObjavuRoot, "Došlo je do greške prilikom menjanja objave", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        //this.setResult(0)
        //this.finish() // Sklanjamo activity sa ekrana
    }

    override fun onBackPressed() {
        this.setResult(1)
        this.finish()
        //super.onBackPressed()
    }
}
