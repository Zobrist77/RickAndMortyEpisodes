package dam.pmdm.rickandmortyapi.data.model

/**
 * Modelo que representa un personaje de Rick and Morty.
 * Se utiliza tanto en la lista de personajes de un episodio
 * como en la vista de detalle.
 */
data class CharacterModel(

    // Identificador único del personaje
    val id: Int,

    // Nombre del personaje
    val name: String,

    // URL de la imagen del personaje
    val image: String,

    // Estado vital del personaje (Alive, Dead, Unknown)
    val status: String,

    // URL completa del recurso en la API
    val url: String
)