package dam.pmdm.rickandmortyapi.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import dam.pmdm.rickandmortyapi.databinding.ActivityLoginBinding
import dam.pmdm.rickandmortyapi.ui.main.MainActivity

/**
 * Se encarga del inicio de sesión del usuario mediante Firebase Authentication.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inicialización del ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialización de Firebase Authentication
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(binding.loginLayout) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )
            insets
        }

        // Si el usuario ya está autenticado, se accede directamente a la pantalla principal
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {

            // Acción del botón de inicio de sesión
            binding.btnLogin.setOnClickListener {

                val email = binding.etEmail.text.toString().trim()
                val password = binding.etPassword.text.toString().trim()

                // Validación de campos
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(
                        this,
                        "Introduce correo y contraseña para continuar",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    loginFirebase(email, password)
                }
            }

            // Navegación a la pantalla de registro
            binding.tvGoRegister.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    /**
     * Realiza el inicio de sesión del usuario usando Firebase Authentication.
     */
    private fun loginFirebase(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { loginResult ->

                if (loginResult.isSuccessful) {

                    // Acceso correcto a la aplicación
                    Toast.makeText(
                        this,
                        "Sesión iniciada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    // Se cierra la pantalla de login para evitar volver atrás
                    finish()

                } else {

                    // Error durante la autenticación
                    val errorMessage =
                        loginResult.exception?.message ?: "No se pudo iniciar sesión"

                    Toast.makeText(
                        this,
                        errorMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
