package center.tilda.tildaapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_novi.*
import kotlinx.android.synthetic.main.content_novi.*

class NoviActivity : AppCompatActivity() {

    var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novi)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        textView.text = this.intent.getStringExtra("kljuc")
    }

    override fun onResume() {
        super.onResume()
        this.counter++
        counterTextView.text = this.counter.toString()
    }
}
