package center.tilda.tildaapp
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.objava_content.view.*

class ObjavaAdapter (private val myDataset: List<Objava>) : RecyclerView.Adapter<ObjavaAdapter.MyViewHolder>() {

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
    }

}
