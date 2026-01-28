package dam.pmdm.rickandmortyapi.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Singleton que configura y proporciona la instancia de Retrofit
 * para acceder a la API de Rick and Morty.
 */
object RetrofitInstance {

    // URL base de la API pública de Rick and Morty
    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    // Servicio de la API usando inicialización lazy
    val api: ApiService by lazy {

        Retrofit.Builder()
            // Establece la URL base para todas las peticiones
            .baseUrl(BASE_URL)
            // Añade el conversor para transformar JSON en objetos Kotlin
            .addConverterFactory(GsonConverterFactory.create())
            // Construye la instancia final de Retrofit
            .build()
            .create(ApiService::class.java)
    }
}
