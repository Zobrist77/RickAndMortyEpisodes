package dam.pmdm.rickandmortyapi.ui.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dam.pmdm.rickandmortyapi.databinding.FragmentEpisodeDetailBinding
import dam.pmdm.rickandmortyapi.ui.characters.CharacterAdapter
import dam.pmdm.rickandmortyapi.ui.characters.CharacterViewModel

/**
 * Fragment que muestra los detalles de un episodio seleccionado.
 * Permite:
 * - Mostrar información del episodio
 * - Mostrar personajes que aparecen en el episodio
 * - Controlar el estado visto/no visto del episodio
 */
class EpisodeDetailFragment : Fragment() {

    private lateinit var binding: FragmentEpisodeDetailBinding

    // ViewModel de personajes
    private val characterViewModel: CharacterViewModel by viewModels()
    // ViewModel de episodios compartido con la Activity
    private val episodeViewModel: EpisodeViewModel by activityViewModels()

    // Adapter para RecyclerView de personajes
    private lateinit var characterAdapter: CharacterAdapter

    // Controla que el switch no dispare eventos al inicializar
    private var isSwitchInicializado = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Infla el layout con ViewBinding
        binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)

        // Recupera los datos del bundle enviados desde EpisodeAdapter
        val episodeId = arguments?.getInt("episodeId") ?: return binding.root
        val episodeName = arguments?.getString("episodeName") ?: ""
        val episodeCode = arguments?.getString("episodeCode") ?: ""
        val episodeAirDate = arguments?.getString("episodeAirDate") ?: ""
        val characterUrls = arguments?.getStringArrayList("characters") ?: arrayListOf<String>()

        // Muestra los datos del episodio en la UI
        binding.tvEpisodeName.text = episodeName
        binding.tvEpisodeCode.text = "$episodeCode - $episodeAirDate"

        // Observa la lista de episodios para actualizar el estado del switch
        episodeViewModel.episodes.observe(viewLifecycleOwner) { episodesList ->
            val episodeItem = episodesList.find { it.id == episodeId } ?: return@observe

            // Evita disparar el listener mientras inicializa el switch
            isSwitchInicializado = true
            binding.switchViewed.isChecked = episodeItem.viewed // Actualiza el switch según estado
            isSwitchInicializado = false
        }

        // Actualiza el ViewModel si el switch cambia y no está inicializando
        binding.switchViewed.setOnCheckedChangeListener { _, isChecked ->
            if (!isSwitchInicializado) {
                episodeViewModel.setEpisodeViewed(episodeId, isChecked)
            }
        }

        // Configura RecyclerView para mostrar personajes en grid de 2 columnas
        characterAdapter = CharacterAdapter(emptyList())
        binding.rvCharacters.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // Grid de 2 columnas
            adapter = characterAdapter // Asigna el adapter
        }

        // Observa la lista de personajes desde el ViewModel
        characterViewModel.characters.observe(viewLifecycleOwner) { charactersList ->
            characterAdapter.setCharacters(charactersList) // Actualiza adapter con personajes
        }

        // Carga los personajes asociados a este episodio desde la API
        characterViewModel.loadCharacters(characterUrls)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = EpisodeDetailFragment()
    }
}
