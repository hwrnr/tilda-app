package center.tilda.tildaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_dodaj_objavu.*

class DodajObjavu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dodaj_objavu)
    }

    fun dodajObjavu(view: View){
        val naslov = naslovEditText.text.toString()
        val sadrzaj = sadrzajEditText.text.toString()

        if (naslov == "" || sadrzaj == ""){
            Snackbar.make(dodajObjavuRoot, "Niste uneli sva potrebna polja", Snackbar.LENGTH_LONG).show()
            return
        }

        val novaObjava = Objava(naslov, sadrzaj)
        Database.add(novaObjava)
        this.setResult(0)
        this.finish()
    }

    override fun onBackPressed() {
        this.setResult(1)
        this.finish()
        //super.onBackPressed()
    }
}
