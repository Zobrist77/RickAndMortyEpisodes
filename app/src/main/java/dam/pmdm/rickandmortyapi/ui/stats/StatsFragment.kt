package dam.pmdm.rickandmortyapi.ui.stats

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dam.pmdm.rickandmortyapi.databinding.FragmentStatsBinding
import dam.pmdm.rickandmortyapi.ui.episodes.EpisodeViewModel

/**
 * Fragment que muestra estadísticas de los episodios:
 * - Cantidad total de episodios
 * - Episodios vistos
 * - Porcentaje completado
 * - PieChart visual
 */
class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var pieChart: PieChart

    // Compartimos el mismo ViewModel que EpisodesFragment
    private val episodeViewModel: EpisodeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflamos layout con ViewBinding
        binding = FragmentStatsBinding.inflate(inflater, container, false)

        // Referencia al PieChart
        pieChart = binding.pieChart

        // Configuración inicial del PieChart
        setupPieChart()

        // Observamos los episodios y actualizamos estadísticas
        episodeViewModel.episodes.observe(viewLifecycleOwner) { episodesList ->

            val totalEpisodes = episodesList.size
            val watchedEpisodes = episodesList.count { episode -> episode.viewed }

            // Actualizamos UI y gráfico
            updateStatsUI(watchedEpisodes, totalEpisodes)
        }

        return binding.root
    }

    /**
     * Configuración inicial del PieChart
     */
    private fun setupPieChart() {
        pieChart.apply {
            description.isEnabled = false       // Oculta descripción por defecto
            isRotationEnabled = true            // Permite girar el gráfico
            setUsePercentValues(true)           // Muestra valores en porcentaje
            centerText = "Progreso"             // Texto central
            setCenterTextSize(18f)              // Tamaño del texto central
            setEntryLabelColor(Color.BLACK)     // Color de etiquetas de cada slice
            animateY(1000)                      // Animación al cargar
        }
    }

    /**
     * Actualiza la UI y el PieChart con los datos de episodios vistos y pendientes
     * @param watched Cantidad de episodios vistos
     * @param total Total de episodios
     */
    private fun updateStatsUI(watched: Int, total: Int) {

        // Actualizamos textos
        binding.tvEpisodesWatched.text = "Has visto $watched de $total episodios"
        val percentage = if (total == 0) 0 else (watched * 100 / total)
        binding.tvPercentageWatched.text = "$percentage % completado"

        // Creamos las entradas para el gráfico
        val pieEntries = ArrayList<PieEntry>().apply {
            add(PieEntry(watched.toFloat(), "Vistos"))
            add(PieEntry((total - watched).toFloat(), "Pendientes"))
        }

        // Configuramos DataSet del PieChart
        val pieDataSet = PieDataSet(pieEntries, "").apply {
            colors = listOf(Color.GREEN, Color.RED) // Verde = vistos, Rojo = pendientes
            valueTextSize = 16f
            sliceSpace = 2f
        }

        // Asignamos datos al PieChart y refrescamos
        pieChart.data = PieData(pieDataSet)
        pieChart.invalidate()
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatsFragment()
    }
}
