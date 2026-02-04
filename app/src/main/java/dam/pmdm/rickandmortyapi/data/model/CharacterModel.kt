package dam.pmdm.rickandmortyapi.data.model

/**
 * Modelo que representa un personaje de la serie Rick and Morty.
 * Se utiliza en la lista de personajes de un episodio y en la vista de detalle.
 */
data class CharacterModel(

    // Identificador único del personaje
    val id: Int,

    // Nombre del personaje
    val name: String,

    // URL de la imagen del personaje
    val image: String,

    // Estado vital del personaje: "Alive", "Dead" o "unknown"
    val status: String,

    // URL completa del recurso del personaje en la API
    val url: String
)
