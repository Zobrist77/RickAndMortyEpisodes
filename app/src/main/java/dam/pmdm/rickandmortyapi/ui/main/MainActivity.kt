package dam.pmdm.rickandmortyapi.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import dam.pmdm.rickandmortyapi.R
import dam.pmdm.rickandmortyapi.databinding.ActivityMainBinding

/**
 * MainActivity que contiene:
 * - Toolbar
 * - Drawer (menu lateral)
 * - NavHostFragment para la navegación entre fragments
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Infla el layout con ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentLayout) { contentView, windowInsets ->
            val systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            contentView.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom)
            windowInsets
        }

        // Configura la Toolbar como ActionBar
        setSupportActionBar(binding.toolbar)

        // Obtiene el NavController desde el NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController

        // Configura el toggle del drawer (icono hamburguesa)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        // Escucha cambios en el drawer
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        // Controla la selección de elementos en el Navigation Drawer
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->

            // Configura NavOptions para controlar el back stack y evitar duplicados
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.episodeFragment, false) // limpia stack hasta episodio sin eliminarlo
                .setLaunchSingleTop(true)                // evita crear fragmentos duplicados
                .build()

            when (menuItem.itemId) {

                R.id.episodesFragment -> {
                    // Navega a la pantalla de Episodios
                    navController.navigate(R.id.episodeFragment, null, navOptions)
                }

                R.id.statsFragment -> {
                    // Navega a la pantalla de Estadísticas
                    navController.navigate(R.id.statsFragment, null, navOptions)
                }

                R.id.settingsFragment -> {
                    // Navega a la pantalla de Ajustes
                    navController.navigate(R.id.settingsFragment, null, navOptions)
                }

                R.id.aboutFragment -> {
                    // Muestra un diálogo con información de la app
                    showAboutDialog()
                }
            }

            // Cierra el drawer después de seleccionar una opción
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    /**
     * Muestra un diálogo "Acerca de" con información del desarrollador y versión
     */
    private fun showAboutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Acerca de")
            .setMessage(
                "Desarrollador: Jesús Díaz Romero\n\n" +
                        "Versión: 1.0.0"
            )
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
