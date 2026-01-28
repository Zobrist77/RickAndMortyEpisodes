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

        // Inflamos layout con ViewBinding
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Inicializamos SharedPreferences
        val sharedPrefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Recuperamos valores guardados de idioma y tema
        val isSpanishLanguageSaved = sharedPrefs.getBoolean("language_spanish", true)
        val isDarkThemeSaved = sharedPrefs.getBoolean("theme_dark", false)

        // Aplicamos los ajustes guardados al abrir el fragment
        applyTheme(isDarkThemeSaved)
        applyLanguage(isSpanishLanguageSaved)

        // Configuramos los switches con los valores actuales
        binding.switchLanguage.isChecked = isSpanishLanguageSaved
        binding.switchTheme.isChecked = isDarkThemeSaved

        // Listener switch de idioma
        binding.switchLanguage.setOnCheckedChangeListener { _: CompoundButton, isSpanishSelected: Boolean ->

            // Guardamos elección en SharedPreferences
            sharedPrefs.edit().putBoolean("language_spanish", isSpanishSelected).apply()

            // Aplicamos idioma seleccionado
            applyLanguage(isSpanishSelected)

            // Mostramos feedback
            Toast.makeText(
                requireContext(),
                "Idioma cambiado a: ${if (isSpanishSelected) "Español" else "Inglés"}",
                Toast.LENGTH_SHORT
            ).show()

            // Reiniciamos Activity para aplicar nuevo idioma
            requireActivity().recreate()
        }


        // Listener switch de tema
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton, isDarkThemeSelected: Boolean ->

            // Guardamos elección en SharedPreferences
            sharedPrefs.edit { putBoolean("theme_dark", isDarkThemeSelected) }

            // Aplicamos tema seleccionado
            applyTheme(isDarkThemeSelected)

            // Mostramos feedback
            Toast.makeText(
                requireContext(),
                "Tema cambiado a: ${if (isDarkThemeSelected) "Oscuro" else "Claro"}",
                Toast.LENGTH_SHORT
            ).show()

            // Reiniciamos Activity para aplicar nuevo tema
            requireActivity().recreate()
        }


        // Listener botón de logout
        binding.btnLogout.setOnClickListener { logoutButton ->

            // Cerrar sesión con FirebaseAuth
            FirebaseAuth.getInstance().signOut()

            // Feedback al usuario
            Toast.makeText(requireContext(), "Sesión cerrada", Toast.LENGTH_SHORT).show()

            // Redirigir a LoginActivity y limpiar back stack
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
     * @param isSpanish true si Español, false si Inglés
     */
    private fun applyLanguage(isSpanish: Boolean) {
        AppCompatDelegate.setApplicationLocales(
            LocaleListCompat.forLanguageTags(if (isSpanish) "es" else "en")
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}
