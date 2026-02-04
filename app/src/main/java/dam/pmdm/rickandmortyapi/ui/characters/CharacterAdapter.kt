package dam.pmdm.rickandmortyapi.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dam.pmdm.rickandmortyapi.R
import dam.pmdm.rickandmortyapi.data.model.CharacterModel
import dam.pmdm.rickandmortyapi.databinding.ItemCharacterBinding

/**
 * Adapter que muestra la lista de personajes de un episodio en RecyclerView.
 */
class CharacterAdapter(
    private var characters: List<CharacterModel> // Lista de personajes a mostrar
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    /**
     * ViewHolder que mantiene referencias a las vistas de un personaje
     */
    class CharacterViewHolder(
        val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        // Infla el layout de cada item de personaje
        val itemCharacterBinding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Devuelve el ViewHolder con el binding asociado
        return CharacterViewHolder(itemCharacterBinding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val character = characters[position] // Obtiene el personaje correspondiente a la posición

        // Asigna nombre y estado del personaje
        holder.binding.tvCharacterName.text = character.name
        holder.binding.tvCharacterStatus.text = character.status

        // Carga la imagen del personaje con Coil
        holder.binding.ivCharacterImage.load(character.image) {
            placeholder(R.drawable.ic_launcher_background) // Muestra imagen temporal mientras carga
            error(R.drawable.ic_launcher_foreground)       // Muestra imagen de error si falla
        }
    }

    override fun getItemCount(): Int = characters.size // Devuelve la cantidad de personajes

    /**
     * Actualiza la lista de personajes y refresca el RecyclerView
     */
    fun setCharacters(newCharacters: List<CharacterModel>) {
        characters = newCharacters // Reemplaza la lista actual
        notifyDataSetChanged()     // Notifica al RecyclerView que los datos cambiaron
    }
}
