package dam.pmdm.rickandmortyapi.data.model

/**
 * Modelo que representa un episodio de la serie Rick and Morty.
 * Se utiliza tanto en el listado principal como en la vista de detalle.
 */
data class EpisodeModel(

    // Identificador único del episodio
    val id: Int,

    // Nombre del episodio
    val name: String,

    // Fecha de emisión del episodio
    val air_date: String,

    // Código del episodio
    val episode: String,

    // Lista de URLs de los personajes que aparecen en el episodio
    val characters: List<String>,

    // Indica si el usuario ha marcado el episodio como visto
    var viewed: Boolean = false
)

///**
// * Información de paginación devuelta por la API.
// * Se usa para controlar el total de resultados y páginas.
// */
//data class Info(
//
//    // Número total de elementos disponibles
//    val count: Int,
//
//    // Número total de páginas
//    val pages: Int,
//
//    // URL de la siguiente página (si existe)
//    val next: String?,
//
//    // URL de la página anterior (si existe)
//    val prev: String?
//)

/**
 * Respuesta completa del endpoint de episodios.
 * Contiene la información de paginación y la lista de episodios.
 */
data class EpisodesApiResponse(

    // Datos de paginación de la respuesta
    //val info: Info,

    // Lista de episodios obtenidos desde la API
    val results: List<EpisodeModel>
)
