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

        // Inflamos el layout con ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.contentLayout) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Usamos Toolbar como ActionBar
        setSupportActionBar(binding.toolbar)

        // Obtenemos el NavController desde el NavHostFragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.fragmentContainer.id) as NavHostFragment
        navController = navHostFragment.navController

        // Configuramos el drawer toggle
        val drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            android.R.string.ok,
            android.R.string.cancel
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()


        // Control del Navigation Drawer
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->

            // NavOptions para controlar el back stack y evitar duplicados
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.episodeFragment, false) // limpia stack hasta episodio sin eliminarlo
                .setLaunchSingleTop(true)                // evita crear fragmentos duplicados
                .build()

            when (menuItem.itemId) {

                R.id.episodesFragment -> {
                    // Navegamos a la pantalla de Episodios
                    navController.navigate(R.id.episodeFragment, null, navOptions)
                }

                R.id.statsFragment -> {
                    // Navegamos a la pantalla de Estadísticas
                    navController.navigate(R.id.statsFragment, null, navOptions)
                }

                R.id.settingsFragment -> {
                    // Navegamos a la pantalla de Ajustes
                    navController.navigate(R.id.settingsFragment, null, navOptions)
                }

                R.id.aboutFragment -> {
                    // Mostramos un diálogo con información de la app
                    showAboutDialog()
                }
            }

            // Cerramos el drawer después de seleccionar una opción
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
