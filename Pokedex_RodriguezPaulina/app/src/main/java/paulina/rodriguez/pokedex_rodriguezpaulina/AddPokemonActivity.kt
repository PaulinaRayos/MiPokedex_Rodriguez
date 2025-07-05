package paulina.rodriguez.pokedex_rodriguezpaulina

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback

class AddPokemonActivity : AppCompatActivity() {


    companion object {
        const val REQUEST_IMAGE_GET = 1
        const val CLOUD_NAME = "dkrhc3kiy"
        const val UPLOAD_PRESET = "pokemon-upload"
    }

    private var selectedImageUri: Uri? = null
    private lateinit var ivPokemon: ImageView
    private lateinit var etNombrePokemon: EditText
    private lateinit var etNumeroPokemon: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_pokemon)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializa Cloudinary
        initCloudinary()

        ivPokemon = findViewById(R.id.ivPokemon)
        etNombrePokemon = findViewById(R.id.etNombrePokemon)
        etNumeroPokemon = findViewById(R.id.etNumeroPokemon)
        val btnSubirImagen: Button = findViewById(R.id.btnSubirImagen)
        val btnGuardarPokemon: Button = findViewById(R.id.btnGuardarPokemon)

        btnSubirImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }

        btnGuardarPokemon.setOnClickListener {
            val nombre = etNombrePokemon.text.toString().trim()
            val numero = etNumeroPokemon.text.toString().trim()

            if (nombre.isEmpty() || numero.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImageUri == null) {
                Toast.makeText(this, "Selecciona una imagen", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            uploadPokemon(selectedImageUri!!) { imageUrl ->

                // GUARDAR EN FIREBASE AQUI
                val database = com.google.firebase.database.FirebaseDatabase.getInstance()
                val ref = database.getReference("pokemones")
                val id = ref.push().key ?: return@uploadPokemon

                val nuevoPokemon = Pokemon(nombre, numero, imageUrl)

                ref.child(id).setValue(nuevoPokemon)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Pokémon guardado en Firebase", Toast.LENGTH_SHORT).show()

                        val resultIntent = Intent()
                        resultIntent.putExtra("nombre", nombre)
                        resultIntent.putExtra("numero", numero)
                        resultIntent.putExtra("imagenUrl", imageUrl)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar en Firebase", Toast.LENGTH_SHORT).show()
                    }
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                selectedImageUri = uri
                changeImage(uri)
            }
        }
    }

    private fun changeImage(uri: Uri) {
        ivPokemon.setImageURI(uri)
    }

    private var cloudinaryInitialized = false

    private fun initCloudinary() {
        if (!cloudinaryInitialized) {
            val config: MutableMap<String, String> = HashMap()
            config["cloud_name"] = CLOUD_NAME
            MediaManager.init(this, config)
            cloudinaryInitialized = true
        }
    }

    private fun uploadPokemon(uri: Uri, onComplete: (String) -> Unit) {
        MediaManager.get().upload(uri)
            .unsigned(UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    Toast.makeText(this@AddPokemonActivity, "Subiendo imagen...", Toast.LENGTH_SHORT).show()
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {}

                override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                    val imageUrl = resultData?.get("url") as? String ?: ""
                    onComplete(imageUrl)
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    Toast.makeText(this@AddPokemonActivity, "Error al subir imagen", Toast.LENGTH_SHORT).show()
                    Log.e("Cloudinary", error?.description ?: "Error desconocido")
                }

                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            }).dispatch()
    }
}