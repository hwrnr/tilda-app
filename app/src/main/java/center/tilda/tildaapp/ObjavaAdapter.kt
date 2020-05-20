package center.tilda.tildaapp
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.objava_content.view.*

//context predstavlja activity unutar kog se nalazi ovaj adapter
class ObjavaAdapter (private val myDataset: ArrayList<Objava>, private val context: Context) : RecyclerView.Adapter<ObjavaAdapter.MyViewHolder>() {

    class MyViewHolder(val root: LinearLayout) : RecyclerView.ViewHolder(root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val linearLayout = LayoutInflater.from(parent.context).inflate(
            R.layout.objava_content,
            parent,
            false
        ) as LinearLayout

        return MyViewHolder(linearLayout)
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.root.naslovTextView.text = myDataset[position].naslov
        holder.root.sadrzajTextView.text = myDataset[position].sadrzaj

        //Postavljamo funkciju koja će se izvršiti prilikom klika na dugme Obriši
        holder.root.obrisiButton.setOnClickListener {
            myDataset.removeAt(position)
            this.notifyDataSetChanged()
        }

        //Postavljamo funkciju koja će se izvršiti kada kliknemo na activity
        holder.root.setOnClickListener {
            //Prosleđujemo context, a ne this, da bi ,,prevarili'' sistem o tome ko je pozvao DodajObjavu activity
            val intent = Intent(context, DodajObjavu::class.java)
            intent.putExtra("position", position)
            context.startActivity(intent)
        }
    }

}
