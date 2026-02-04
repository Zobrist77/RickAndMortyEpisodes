package dam.pmdm.rickandmortyapi.ui.episodes

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dam.pmdm.rickandmortyapi.data.api.RetrofitInstance
import dam.pmdm.rickandmortyapi.data.model.EpisodeModel
import kotlinx.coroutines.launch

/**
 * ViewModel que gestiona la lista de episodios,
 * sincroniza los episodios vistos con Firestore
 * y proporciona funciones para actualizar el estado de cada episodio.
 */
class EpisodeViewModel : ViewModel() {

    // LiveData privado para almacenar la lista de episodios
    private val _episodes = MutableLiveData<List<EpisodeModel>>()
    // LiveData público para mostrar los episodios
    val episodes: LiveData<List<EpisodeModel>> = _episodes

    // Instancia de Firebase Firestore para guardar y leer datos del usuario
    private val firestore = FirebaseFirestore.getInstance()
    // Instancia de FirebaseAuth para obtener el usuario actual
    private val auth = FirebaseAuth.getInstance()

    /**
     * Carga la lista de episodios desde la API
     * y sincroniza los vistos desde Firestore
     */
    fun loadEpisodes() {
        viewModelScope.launch {
            try {
                // Llama a la API para obtener todos los episodios
                val response = RetrofitInstance.api.getEpisodes()
                _episodes.value = response.results // Actualiza LiveData con la respuesta

                // Sincroniza la información de episodios vistos desde Firestore
                syncViewedFromFirestore()
            } catch (e: Exception) {
                // En caso de error al llamar la API, pone la lista vacía
                _episodes.value = emptyList()
            }
        }
    }

    /**
     * Obtiene los episodios que el usuario ha marcado como vistos en Firestore
     * y actualiza el LiveData correspondiente
     */
    private fun syncViewedFromFirestore() {
        val userId = auth.currentUser?.uid ?: return // Sale si no hay usuario
        val currentEpisodes = _episodes.value ?: return // Sale si no hay episodios cargados

        firestore.collection("users")
            .document(userId)
            .collection("episodes")
            .get() // Obtiene los episodios guardados en Firestore
            .addOnSuccessListener { querySnapshot ->
                val firestoreEpisodes = querySnapshot.documents // Lista de documentos de Firestore

                // Recorre cada documento de Firestore
                firestoreEpisodes.forEach { doc ->
                    val episodeId = doc.id.toIntOrNull() ?: return@forEach // Convierte ID a Int, salta si falla
                    val viewedStatus = doc.getBoolean("viewed") ?: false // Obtiene si está marcado como visto

                    // Busca el episodio correspondiente y actualiza su estado
                    currentEpisodes.find { episodioActual ->
                        episodioActual.id == episodeId
                    }?.viewed = viewedStatus
                }

                // Actualiza LiveData para notificar a la UI de los cambios
                _episodes.value = currentEpisodes
            }
            .addOnFailureListener {
                // Informa al usuario de que hubo un error al actualizar los datos
                Toast.makeText(null, "Error al actualizar los episodios vistos", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Cambia el estado "visto" de un episodio
     * Este es el único punto de actualización del estado desde la UI
     */
    fun setEpisodeViewed(episodeId: Int, viewed: Boolean) {
        val episodesList = _episodes.value?.toMutableList() ?: return // Sale si no hay episodios
        val index = episodesList.indexOfFirst { episode -> episode.id == episodeId } // Busca el episodio
        if (index == -1) return // Sale si no encuentra el episodio

        val episodeToUpdate = episodesList[index] // Obtiene el episodio a actualizar
        episodeToUpdate.viewed = viewed // Cambia el estado "visto"

        // Actualiza LiveData para notificar a la UI
        _episodes.value = episodesList

        // Guarda el cambio en Firestore
        saveEpisodeToFirestore(episodeToUpdate)
    }

    /**
     * Guarda la información del episodio en Firestore
     */
    private fun saveEpisodeToFirestore(episode: EpisodeModel) {
        val userId = auth.currentUser?.uid ?: return // Sale si no hay usuario

        firestore.collection("users")
            .document(userId)
            .collection("episodes")
            .document(episode.id.toString())
            .set(
                mapOf(
                    "name" to episode.name,           // Nombre del episodio
                    "episode" to episode.episode,     // Código del episodio (S01E01)
                    "air_date" to episode.air_date,   // Fecha de emisión
                    "characters" to episode.characters, // Lista de personajes
                    "viewed" to episode.viewed        // Estado visto/no visto
                )
            )
    }
}
