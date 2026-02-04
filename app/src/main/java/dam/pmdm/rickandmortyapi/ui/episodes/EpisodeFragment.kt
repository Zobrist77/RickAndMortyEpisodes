package dam.pmdm.rickandmortyapi.ui.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dam.pmdm.rickandmortyapi.data.model.EpisodeModel
import dam.pmdm.rickandmortyapi.databinding.FragmentEpisodeBinding

/**
 * Fragment que muestra la lista de episodios.
 * Permite filtrar entre todos los episodios o solo los vistos.
 */
class EpisodeFragment : Fragment() {

    private lateinit var binding: FragmentEpisodeBinding

    // ViewModel compartido con la Activity para mantener estado
    private val episodesViewModel: EpisodeViewModel by activityViewModels()

    // Adapter para RecyclerView
    private lateinit var episodeAdapter: EpisodeAdapter

    // Filtro actual aplicado
    private var currentFilter: EpisodeFilter = EpisodeFilter.ALL

    // Lista completa de episodios recibida desde la API
    private var allEpisodes: List<EpisodeModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout con ViewBinding
        binding = FragmentEpisodeBinding.inflate(inflater, container, false)

        // Configura el RecyclerView con layout lineal y adapter
        episodeAdapter = EpisodeAdapter(mutableListOf())
        binding.episodesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()) // Establece layout vertical
            adapter = episodeAdapter // Asigna el adapter
        }

        // Listener del toggle para filtrar episodios
        binding.toggleFilter.addOnButtonCheckedListener { toggleGroup, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener // Sale si se desmarca el botón

            // Actualiza el filtro según el botón seleccionado
            currentFilter = when (checkedId) {
                binding.btnAll.id -> EpisodeFilter.ALL
                binding.btnWatched.id -> EpisodeFilter.WATCHED
                else -> EpisodeFilter.ALL
            }

            // Aplica el filtro actualizado a la lista
            applyFilter()
        }

        // Observa la lista de episodios del ViewModel
        episodesViewModel.episodes.observe(viewLifecycleOwner) { episodesList ->
            allEpisodes = episodesList.toMutableList() // Guarda la lista completa
            applyFilter() // Aplica filtro actual para actualizar la UI
        }

        // Inicia la carga de episodios desde la API
        episodesViewModel.loadEpisodes()

        return binding.root
    }

    /**
     * Aplica el filtro seleccionado sobre la lista completa de episodios
     * y actualiza el adapter.
     */
    private fun applyFilter() {
        // Filtra los episodios según el filtro actual
        val filteredEpisodes = when (currentFilter) {
            EpisodeFilter.ALL -> allEpisodes
            EpisodeFilter.WATCHED -> allEpisodes.filter { episodeItem ->
                episodeItem.viewed // Incluye solo los episodios marcados como vistos
            }
        }

        // Actualiza el adapter con la lista filtrada
        episodeAdapter.setEpisodes(filteredEpisodes)
    }

    companion object {
        @JvmStatic
        fun newInstance() = EpisodeFragment()
    }
}

/**
 * Enum que representa los posibles filtros de episodios.
 */
enum class EpisodeFilter {
    ALL,    // Muestra todos los episodios
    WATCHED // Muestra solo los episodios vistos
}
