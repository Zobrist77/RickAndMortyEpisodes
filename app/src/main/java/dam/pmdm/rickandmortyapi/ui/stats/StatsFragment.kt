package dam.pmdm.rickandmortyapi.ui.stats

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import dam.pmdm.rickandmortyapi.R
import dam.pmdm.rickandmortyapi.databinding.FragmentStatsBinding
import dam.pmdm.rickandmortyapi.ui.episodes.EpisodeViewModel

/**
 * Fragment que muestra estadísticas de los episodios:
 * - Cantidad total de episodios
 * - Episodios vistos
 * - Porcentaje completado
 * - Gráfico circular de progreso
 */
class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding
    private lateinit var pieChart: PieChart

    // Comparte el mismo ViewModel que EpisodesFragment
    private val episodeViewModel: EpisodeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Infla el layout con ViewBinding
        binding = FragmentStatsBinding.inflate(inflater, container, false)

        // Obtiene la referencia al gráfico circular
        pieChart = binding.pieChart

        // Configuración inicial del PieChart
        setupPieChart()

        // Observa los episodios y actualiza las estadísticas
        episodeViewModel.episodes.observe(viewLifecycleOwner) { episodesList ->

            val totalEpisodes = episodesList.size
            val watchedEpisodes = episodesList.count { episode -> episode.viewed }

            // Actualiza los textos informativos y el gráfico de progreso
            updateStats(watchedEpisodes, totalEpisodes)
        }

        return binding.root
    }

    /**
     * Configuración inicial del gráfico de progreso
     */
    private fun setupPieChart() {
        // Obtiene si la configuración está en modo noche o modo día, true si es modo noche, false modo día
        val esModoNoche =     (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) ==
            Configuration.UI_MODE_NIGHT_YES

        pieChart.apply {
            description.isEnabled = false        // Oculta descripción por defecto
            isRotationEnabled = true             // Permite girar el gráfico
            setUsePercentValues(true)            // Muestra valores en porcentaje
            centerText = getString(R.string.text_centro_grafico)              // Texto central del gráfico
            setCenterTextSize(18f)               // Tamaño del texto central

            // Color del texto central según modo
            val colorCenterText = ContextCompat.getColor(
                binding.root.context,
                if (esModoNoche) R.color.rick_blue_light else R.color.rick_blue_dark
            )
            setCenterTextColor(colorCenterText)    // Color del texto central

            // Color del fondo del centro (hole) según modo
            val colorHole = ContextCompat.getColor(
                binding.root.context,
                if (esModoNoche) R.color.rick_blue_dark else R.color.rick_blue_light
            )
            setHoleColor(colorHole)           // Color del fondo del centro con el color elegido
            setEntryLabelColor(Color.BLACK)   // Color de las etiquetas de cada sección

            // Color de la leyenda según modo
            val colorLeyenda = ContextCompat.getColor(
                binding.root.context,
                if (esModoNoche) R.color.rick_blue_light else R.color.rick_blue_dark
            )
            legend.textColor = colorLeyenda // Tamaño del texto de la leyenda
            legend.textSize = 14f           // Tamaño del texto de la leyenda

            animateY(1000)    // Animación al cargar
        }

    }

    /**
     * Actualiza los textos de estadísticas y el gráfico de progreso
     * con los datos de episodios vistos y pendientes
     *
     * @param watched Cantidad de episodios vistos
     * @param total Total de episodios
     */
    private fun updateStats(watched: Int, total: Int) {

        // Actualiza textos
        binding.tvEpisodesWatched.text = getString(R.string.episodios_vistos, watched, total)
        val percentage = if (total == 0) 0 else (watched * 100 / total)
        binding.tvPercentageWatched.text = getString(R.string.porcentage_vistos, percentage)

        // Crea las entradas para el gráfico
        val pieEntries = ArrayList<PieEntry>().apply {
            add(PieEntry(watched.toFloat(), getString(R.string.text_vistos)))
            add(PieEntry((total - watched).toFloat(), getString(R.string.text_pendientes)))
        }

        // Configura como se verá el gráfico
        val pieDataSet = PieDataSet(pieEntries, "").apply {
            colors = listOf(Color.GREEN, Color.RED) // Verde = vistos, Rojo = pendientes
            valueTextSize = 16f
            sliceSpace = 2f
        }

        // Asigna datos al gráfico y refresca
        pieChart.data = PieData(pieDataSet)
        pieChart.invalidate()
    }

    companion object {
        @JvmStatic
        fun newInstance() = StatsFragment()
    }
}
