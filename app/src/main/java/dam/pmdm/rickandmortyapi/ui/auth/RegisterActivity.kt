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
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización del ViewBinding
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

        // Inicialización de Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Acción del botón de registro
        binding.btnRegister.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Validación de campos
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this,
                    "Completa todos los campos para registrarte",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                registerFirebase(email, password)
            }
        }

        // Volver a la pantalla de login
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
    }

    /**
     * Registra un nuevo usuario en Firebase Authentication.
     */
    private fun registerFirebase(email: String, password: String) {

        // Se desactiva el botón para evitar registros múltiples
        binding.btnRegister.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { registerResult ->

                if (registerResult.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Registro completado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    // Se vuelve al login tras un registro exitoso
                    finish()

                } else {

                    // Se reactiva el botón en caso de error
                    binding.btnRegister.isEnabled = true

                    val errorMessage =
                        registerResult.exception?.message ?: "No se pudo completar el registro"

                    Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
