package paulina.rodriguez.pokedex_rodriguezpaulina

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class VisualizacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_visualizacion)

        val nombre = intent.getStringExtra("nombre")
        val numero = intent.getStringExtra("numero")
        val imagenUrl = intent.getStringExtra("imagenUrl")

        val tvNombre = findViewById<TextView>(R.id.tvNombrePokemon)
        val tvNumero = findViewById<TextView>(R.id.tvNumeroPokemon)
        val ivPokemon = findViewById<ImageView>(R.id.ivPokemon)

        tvNombre.text = nombre
        tvNumero.text = numero

        val secureUrl = imagenUrl?.replace("http://", "https://")

        Glide.with(this)
            .load(secureUrl)
            .placeholder(R.drawable.placeholder)
            .into(ivPokemon)

        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
