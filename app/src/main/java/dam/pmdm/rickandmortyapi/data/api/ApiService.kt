package dam.pmdm.rickandmortyapi.data.api

import dam.pmdm.rickandmortyapi.data.model.CharacterModel
import dam.pmdm.rickandmortyapi.data.model.EpisodesApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz que define los endpoints de la API de Rick and Morty.
 * Se utiliza con Retrofit para realizar las peticiones HTTP.
 */
interface ApiService {

    /**
     * Obtiene la lista completa de episodios desde la API.
     */
    @GET("episode")
    suspend fun getEpisodes(): EpisodesApiResponse

    /**
     * Obtiene un listado de personajes a partir de sus IDs.
     * Se permite pasar múltiples IDs separados por comas.
     */
    @GET("character/{ids}")
    suspend fun getCharactersByIds(
        // IDs de los personajes a consultar
        @Path("ids") ids: String
    ): List<CharacterModel>
}
