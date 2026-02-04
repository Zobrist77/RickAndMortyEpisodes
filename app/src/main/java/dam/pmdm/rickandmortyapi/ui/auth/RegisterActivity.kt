package dam.pmdm.rickandmortyapi.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import dam.pmdm.rickandmortyapi.databinding.ActivityRegisterBinding

/**
 * Se encarga del registro de nuevos usuarios mediante Firebase Authentication.
 */
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth                // Instancia de FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicializa ViewBinding
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.registerLayout) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )
            insets
        }

        // Inicializa Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Configura el botón de registro
        binding.btnRegister.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()       // Obtiene email
            val password = binding.etPassword.text.toString().trim() // Obtiene contraseña

            // Valida que ambos campos estén completos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Completa todos los campos para registrarte",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerFirebase(email, password) // Registra el usuario en Firebase
            }
        }

        // Configura botón para volver al login
        binding.btnBackToLogin.setOnClickListener {
            finish() // Cierra la actividad actual
        }
    }

    /**
     * Registra un nuevo usuario en Firebase Authentication.
     */
    private fun registerFirebase(email: String, password: String) {

        // Desactiva el botón para evitar múltiples registros simultáneos
        binding.btnRegister.isEnabled = false

        // Llama a Firebase para crear el usuario
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { registerResult ->

                if (registerResult.isSuccessful) {

                    // Muestra mensaje de éxito al usuario
                    Toast.makeText(
                        this,
                        "Registro completado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    finish() // Vuelve a la pantalla de login

                } else {

                    // Reactiva el botón si hay error
                    binding.btnRegister.isEnabled = true

                    // Obtiene mensaje de error
                    val errorMessage =
                        registerResult.exception?.message ?: "No se pudo completar el registro"

                    // Muestra mensaje de error al usuario
                    Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
