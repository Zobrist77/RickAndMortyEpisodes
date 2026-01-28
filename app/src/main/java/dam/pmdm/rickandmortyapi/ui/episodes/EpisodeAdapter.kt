package dam.pmdm.rickandmortyapi.ui.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.rickandmortyapi.R
import dam.pmdm.rickandmortyapi.data.model.EpisodeModel
import dam.pmdm.rickandmortyapi.databinding.ItemEpisodeBinding

/**
 * Adapter que muestra la lista de episodios en el RecyclerView.
 */
class EpisodeAdapter(
    private val episodes: MutableList<EpisodeModel>
) : RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder>() {

    /**
     * ViewHolder que mantiene las referencias a las vistas de cada episodio.
     */
    class EpisodeViewHolder(
        val binding: ItemEpisodeBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: EpisodeViewHolder,
        position: Int
    ) {
        val episode = episodes[position]

        // Asignación de datos del episodio
        holder.binding.tvEpisodeName.text = episode.name
        holder.binding.tvEpisodeCode.text = episode.episode
        holder.binding.tvAirDate.text = episode.air_date

        // Indicador visual si el episodio ha sido marcado como visto
        holder.binding.episodeWatchedIndicator.visibility =
            if (episode.viewed) View.VISIBLE else View.GONE

        // Navegamos al detalle del episodio pasando un bundle con los datos
        holder.itemView.setOnClickListener { itemView ->

            val bundle = Bundle().apply {
                putInt("episodeId", episode.id)
                putString("episodeName", episode.name)
                putString("episodeCode", episode.episode)
                putString("episodeAirDate", episode.air_date)
                putStringArrayList("characters", ArrayList(episode.characters))
            }

            // Navegación usando el NavController del itemView
            itemView.findNavController().navigate(
                R.id.action_episodeFragment_to_episodeDetailFragment,
                bundle
            )
        }
    }

    override fun getItemCount(): Int = episodes.size

    /**
     * Actualiza la lista de episodios mostrada en el RecyclerView.
     */
    fun setEpisodes(newEpisodes: List<EpisodeModel>) {
        episodes.clear()
        episodes.addAll(newEpisodes)
        notifyDataSetChanged()
    }
}
