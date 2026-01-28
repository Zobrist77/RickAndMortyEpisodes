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
 * Fragment que muestra los detalles de un episodio seleccionado,
 * incluyendo la lista de personajes y el control de visto/no visto.
 */
class EpisodeDetailFragment : Fragment() {

    private lateinit var binding: FragmentEpisodeDetailBinding

    // ViewModel de personajes
    private val characterViewModel: CharacterViewModel by viewModels()
    private val episodeViewModel: EpisodeViewModel by activityViewModels()

    private lateinit var characterAdapter: CharacterAdapter

    // Controla que el switch no dispare eventos al inicializar
    private var isSwitchInicializado = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)

        // Recuperamos los datos del bundle enviados desde EpisodeAdapter
        val episodeId = arguments?.getInt("episodeId") ?: return binding.root
        val episodeName = arguments?.getString("episodeName") ?: ""
        val episodeCode = arguments?.getString("episodeCode") ?: ""
        val episodeAirDate = arguments?.getString("episodeAirDate") ?: ""
        val characterUrls = arguments?.getStringArrayList("characters") ?: arrayListOf<String>()

        // Mostramos los datos del episodio
        binding.tvEpisodeName.text = episodeName
        binding.tvEpisodeCode.text = "$episodeCode - $episodeAirDate"

        // Observamos la lista de episodios para actualizar el estado del switch
        episodeViewModel.episodes.observe(viewLifecycleOwner) { episodeList ->
            val episode = episodeList.find { isViewed -> isViewed.id == episodeId } ?: return@observe

            // Evitamos disparar el listener mientras inicializamos el switch
            isSwitchInicializado = true
            binding.switchViewed.isChecked = episode.viewed
            isSwitchInicializado = false
        }

        // Actualiza el ViewModel si no está inicializando
        binding.switchViewed.setOnCheckedChangeListener { group, isChecked ->
            if (!isSwitchInicializado) {
                episodeViewModel.setEpisodeViewed(episodeId, isChecked)
            }
        }

        // Configuramos RecyclerView para mostrar personajes en grid de 2 columnas
        characterAdapter = CharacterAdapter(emptyList())
        binding.rvCharacters.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = characterAdapter
        }

        // Observamos la lista de personajes desde el ViewModel
        characterViewModel.characters.observe(viewLifecycleOwner) { charactersList ->
            characterAdapter.setCharacters(charactersList)
        }

        // Cargamos los personajes asociados a este episodio
        characterViewModel.loadCharacters(characterUrls)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = EpisodeDetailFragment()
    }
}
