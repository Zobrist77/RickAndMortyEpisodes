package dam.pmdm.rickandmortyapi.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dam.pmdm.rickandmortyapi.R
import dam.pmdm.rickandmortyapi.data.model.CharacterModel
import dam.pmdm.rickandmortyapi.databinding.ItemCharacterBinding

/**
 * Adapter encargado de mostrar la lista de personajes de un episodio.
 */
class CharacterAdapter(
    private var characters: List<CharacterModel>
) : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    /**
     * ViewHolder que mantiene las referencias a las vistas del personaje.
     */
    class CharacterViewHolder(
        val binding: ItemCharacterBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {

        val itemCharacterBinding = ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return CharacterViewHolder(itemCharacterBinding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {

        val character = characters[position]

        // Asignación de datos del personaje
        holder.binding.tvCharacterName.text = character.name
        holder.binding.tvCharacterStatus.text = character.status

        // Carga de la imagen del personaje usando Coil
        holder.binding.ivCharacterImage.load(character.image) {
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_foreground)
        }
    }

    override fun getItemCount(): Int = characters.size

    /**
     * Actualiza la lista de personajes mostrada en el RecyclerView.
     */
    fun setCharacters(newCharacters: List<CharacterModel>) {
        characters = newCharacters
        notifyDataSetChanged()
    }
}
