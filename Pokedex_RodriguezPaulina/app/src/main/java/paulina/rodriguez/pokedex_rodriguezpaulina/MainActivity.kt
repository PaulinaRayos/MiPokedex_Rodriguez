package paulina.rodriguez.pokedex_rodriguezpaulina

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {

    private val listaPokemones = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    val REQUEST_CODE_ADD_POKEMON = 100


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listView: ListView = findViewById(R.id.lvPokemon)
        adapter = PokemonAdapter(this, listaPokemones)
        listView.adapter = adapter


        listView.setOnItemClickListener { _, _, position, _ ->
            val pokemonSeleccionado = listaPokemones[position]

            val intent = Intent(this, VisualizacionActivity::class.java).apply {
                putExtra("nombre", pokemonSeleccionado.nombre)
                putExtra("numero", pokemonSeleccionado.numero)
                putExtra("imagenUrl", pokemonSeleccionado.imagenUrl)
            }
            startActivity(intent)
        }



        val database = FirebaseDatabase.getInstance()
        val pokemonesRef = database.getReference("pokemones")

        pokemonesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaPokemones.clear()
                for (child in snapshot.children) {
                    val pokemon = child.getValue(Pokemon::class.java)
                    if (pokemon != null) {
                        listaPokemones.add(pokemon)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al leer datos: ${error.message}")
            }
        })

        val btnAgregarPokemon: Button = findViewById(R.id.btnAgregarPokemon)
        btnAgregarPokemon.setOnClickListener {
            val intent = Intent(this, AddPokemonActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_POKEMON)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADD_POKEMON && resultCode == Activity.RESULT_OK) {
            /*val nombre = data?.getStringExtra("nombre") ?: return
            val numero = data.getStringExtra("numero") ?: return
            val imagenUrl = data.getStringExtra("imagenUrl") ?: return

            val nuevoPokemon = Pokemon(nombre, numero, imagenUrl)
            listaPokemones.add(nuevoPokemon)
            adapter.notifyDataSetChanged()*/
        }
    }


}