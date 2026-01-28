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
        binding = FragmentEpisodeBinding.inflate(inflater, container, false)

        // Configuración del RecyclerView
        episodeAdapter = EpisodeAdapter(mutableListOf())
        binding.episodesRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = episodeAdapter
        }

        // Listener del toggle para filtrar episodios
        binding.toggleFilter.addOnButtonCheckedListener { toggleGroup, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            currentFilter = when (checkedId) {
                binding.btnAll.id -> EpisodeFilter.ALL
                binding.btnWatched.id -> EpisodeFilter.WATCHED
                else -> EpisodeFilter.ALL
            }
            applyFilter()
        }

        // Observamos la lista de episodios del ViewModel
        episodesViewModel.episodes.observe(viewLifecycleOwner) { episodesList ->
            allEpisodes = episodesList.toMutableList() // guardamos la lista completa
            applyFilter()
        }

        // Iniciamos la carga de episodios desde la API
        episodesViewModel.loadEpisodes()

        return binding.root
    }

    /**
     * Aplica el filtro seleccionado sobre la lista completa de episodios
     * y actualiza el adapter.
     */
    private fun applyFilter() {
        val filteredEpisodes = when (currentFilter) {
            EpisodeFilter.ALL -> allEpisodes
            EpisodeFilter.WATCHED -> allEpisodes.filter { episodeItem ->
                episodeItem.viewed
            }
        }
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
    ALL,
    WATCHED
}

