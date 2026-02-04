package dam.pmdm.rickandmortyapi.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import dam.pmdm.rickandmortyapi.databinding.FragmentSettingsBinding
import dam.pmdm.rickandmortyapi.ui.auth.LoginActivity
import androidx.core.content.edit

/**
 * Fragment de Ajustes de la aplicación.
 * Permite:
 * - Cambiar idioma de la app
 * - Cambiar tema (claro/oscuro)
 * - Cerrar sesión del usuario
 */
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    // Nombre del archivo SharedPreferences donde guardaremos tema e idioma
    private val PREFS_NAME = "app_settings"

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {

        // Infla el layout con ViewBinding
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Carga las preferencias guardadas
        val sharedPrefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Recuperamos valores guardados de idioma y tema
        val isSpanishLanguageSaved = sharedPrefs.getBoolean("language_spanish", true)
        val isDarkThemeSaved = sharedPrefs.getBoolean("theme_dark", false)

        // Aplicamos los ajustes guardados al abrir el fragment
        applyTheme(isDarkThemeSaved)
        applyLanguage(isSpanishLanguageSaved)

        // Configura los switches con los valores actuales
        binding.switchLanguage.isChecked = isSpanishLanguageSaved
        binding.switchTheme.isChecked = isDarkThemeSaved

        // Listener switch de idioma
        binding.switchLanguage.setOnCheckedChangeListener { switchLanguage: CompoundButton, isSpanishSelected: Boolean ->

            // Guarda elección en SharedPreferences
            sharedPrefs.edit().putBoolean("language_spanish", isSpanishSelected).apply()

            // Aplica idioma seleccionado
            applyLanguage(isSpanishSelected)

            // Muestra mensaje de confirmación
            Toast.makeText(
                requireContext(),
                "Idioma cambiado a: ${if (isSpanishSelected) "Español" else "Inglés"}",
                Toast.LENGTH_SHORT
            ).show()

            // Reinicia Activity para aplicar el idioma
            requireActivity().recreate()
        }


        // Listener switch de tema
        binding.switchTheme.setOnCheckedChangeListener { switchTheme: CompoundButton, isDarkThemeSelected: Boolean ->

            // Guarda elección en SharedPreferences
            sharedPrefs.edit { putBoolean("theme_dark", isDarkThemeSelected) }

            // Aplica tema seleccionado
            applyTheme(isDarkThemeSelected)

            // Muestra la selección al usuario
            Toast.makeText(
                requireContext(),
                "Tema cambiado a: ${if (isDarkThemeSelected) "Oscuro" else "Claro"}",
                Toast.LENGTH_SHORT
            ).show()

            // Reinicia la Activity para aplicar cambios visuales
            requireActivity().recreate()
        }


        // Listener botón de logout
        binding.btnLogout.setOnClickListener { logoutButton ->

            // Cierra la sesión con FirebaseAuth
            FirebaseAuth.getInstance().signOut()

            // Toast para informar al usuario del cierre de la sesión
            Toast.makeText(
                requireContext(),
                "Sesión cerrada",
                Toast.LENGTH_SHORT
            ).show()

            // Redirige a LoginActivity y limpiar el historial
            val intentToLogin = Intent(requireContext(), LoginActivity::class.java)
            intentToLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentToLogin)
        }

        return binding.root
    }

    /**
     * Aplica tema claro u oscuro
     * @param isDark true si se quiere modo oscuro, false para claro
     */
    private fun applyTheme(isDark: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    /**
     * Aplica idioma de la app (Español/Inglés)
     * @param isSpanish true si es Español, false si es Inglés
     */
    private fun applyLanguage(isSpanish: Boolean) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(if (isSpanish) "es" else "en")
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}
