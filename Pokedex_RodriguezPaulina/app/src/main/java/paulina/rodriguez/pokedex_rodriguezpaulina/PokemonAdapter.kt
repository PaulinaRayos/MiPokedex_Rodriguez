package paulina.rodriguez.pokedex_rodriguezpaulina

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class PokemonAdapter(private val context: Context, private val pokemones: List<Pokemon>) :
    BaseAdapter() {

    override fun getCount(): Int = pokemones.size
    override fun getItem(position: Int): Any = pokemones[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_pokemon, parent, false)

        val pokemon = pokemones[position]

        val ivImagen: ImageView = view.findViewById(R.id.ivItemImagen)
        val tvNombre: TextView = view.findViewById(R.id.tvItemNombre)
        val tvNumero: TextView = view.findViewById(R.id.tvItemNumero)

        tvNombre.text = pokemon.nombre
        tvNumero.text = "#${pokemon.numero}"

        val secureUrl = pokemon.imagenUrl.replace("http://", "https://")
        Glide.with(context)
            .load(secureUrl)
            .into(ivImagen)

        return view
    }
}