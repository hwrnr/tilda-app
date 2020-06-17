package center.tilda.tildaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pregledaj_objavu.*

class PregledajObjavu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pregledaj_objavu)

        val index = this.intent.getIntExtra("position", -1)

        naslovTextView.text = Database.listaObjava[index].naslov //Uzimamo naslov objave koju menjamo i postavljamo je kao početnu vrednost
        sadrzajTextView.text = Database.listaObjava[index].sadrzaj //Uzimamo sadržaj objave koju menjamo i postavljamo je kao početnu vrednost

        Picasso.get()
            .load(Database.listaObjava[index].imageLink)
            .placeholder(R.drawable.ic_image_loading)
            .error(R.drawable.ic_image_loading_error)
            .centerInside()
            .into(slikaObjaveImageView)

    }
}
