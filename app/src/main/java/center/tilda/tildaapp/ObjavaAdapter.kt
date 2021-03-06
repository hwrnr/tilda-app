package center.tilda.tildaapp
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.objava_content.view.*

//context predstavlja activity unutar kog se nalazi ovaj adapter
class ObjavaAdapter (private val myDataset: ArrayList<Objava>, private val context: Context) : RecyclerView.Adapter<ObjavaAdapter.MyViewHolder>() {

    class MyViewHolder(val root: ConstraintLayout) : RecyclerView.ViewHolder(root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.objava_content,
            parent,
            false
        ) as ConstraintLayout //Koristimo ConstraintLayout umesto LinearLayout, zbog veće fleksibilnosti

        return MyViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.root.naslovTextView.text = myDataset[position].naslov

        var sadrzaj = myDataset[position].sadrzaj

        //Ne popunjavamo ceo sadržaj pošto je prikazan samo deo na početnom ekranu
        if (sadrzaj.length > 500){
            sadrzaj = sadrzaj.dropLast(sadrzaj.length - 500)
        }
        holder.root.sadrzajTextView.text = sadrzaj

        Picasso.get()
            .load(myDataset[position].imageLink)
            .placeholder(R.drawable.ic_image_loading)
            .error(R.drawable.ic_image_loading_error)
            .centerInside()
            .into(holder.root.slikaObjaveImageview)

        //Postavljamo funkciju koja će se izvršiti prilikom klika na dugme Obriši
        holder.root.obrisiButton.setOnClickListener {
            val objava = myDataset[position]
            Server.deleteBlog(objava.slug){isOk ->
                if (isOk){
                    myDataset.removeAt(position)
                    this.notifyDataSetChanged()
                }
            }
        }

        //Postavljamo funkciju koja će se izvršiti kada kliknemo na activity
        holder.root.izmeniButton.setOnClickListener {
            //Prosleđujemo context, a ne this, da bi ,,prevarili'' sistem o tome ko je pozvao DodajObjavu activity
            val intent = Intent(context, DodajObjavu::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }

        holder.root.setOnClickListener {
            //Prosleđujemo context, a ne this, da bi ,,prevarili'' sistem o tome ko je pozvao DodajObjavu activity
            val intent = Intent(context, PregledajObjavu::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

}
