package dam.pmdm.rickandmortyapi.ui.episodes

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
 * la sincronización de los vistos con Firestore
 * y proporciona funciones para actualizar el estado de cada episodio.
 */
class EpisodeViewModel : ViewModel() {

    private val _episodes = MutableLiveData<List<EpisodeModel>>()
    val episodes: LiveData<List<EpisodeModel>> = _episodes

    // Instancias de Firebase necesarias
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    /**
     * Carga la lista de episodios desde la API y sincroniza los vistos desde Firestore
     */
    fun loadEpisodes() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getEpisodes()
                _episodes.value = response.results

                // Sincroniza la información de episodios vistos desde Firestore
                syncViewedFromFirestore()
            } catch (e: Exception) {
                _episodes.value = emptyList() // fallback en caso de error
            }
        }
    }

    /**
     * Obtiene del Firestore los episodios que el usuario ha marcado como vistos
     * y actualiza el LiveData correspondiente
     */
    private fun syncViewedFromFirestore() {
        val userId = auth.currentUser?.uid ?: return
        val currentEpisodes = _episodes.value ?: return

        firestore.collection("users")
            .document(userId)
            .collection("episodes")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val firestoreEpisodes = querySnapshot.documents

                firestoreEpisodes.forEach { doc ->
                    val episodeId = doc.id.toIntOrNull() ?: return@forEach
                    val viewedStatus = doc.getBoolean("viewed") ?: false

                    // Buscamos el episodio correspondiente y actualizamos su estado
                    currentEpisodes.find { episodioActual ->
                        episodioActual.id == episodeId
                    }?.viewed = viewedStatus
                }

                // Actualizamos LiveData para notificar al UI
                _episodes.value = currentEpisodes
            }
            .addOnFailureListener {
                // En caso de error, simplemente no actualizamos vistos
            }
    }

    /**
     * Cambia el estado "visto" de un episodio
     * Este es el único punto de actualización del estado
     */
    fun setEpisodeViewed(episodeId: Int, viewed: Boolean) {
        val episodesList = _episodes.value?.toMutableList() ?: return
        val index = episodesList.indexOfFirst { it.id == episodeId }
        if (index == -1) return

        val episodeToUpdate = episodesList[index]
        episodeToUpdate.viewed = viewed

        // Actualizamos LiveData
        _episodes.value = episodesList

        // Guardamos el cambio en Firestore
        saveEpisodeToFirestore(episodeToUpdate)
    }

    /**
     * Guarda la información del episodio en Firestore
     */
    private fun saveEpisodeToFirestore(episode: EpisodeModel) {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .collection("episodes")
            .document(episode.id.toString())
            .set(
                mapOf(
                    "name" to episode.name,
                    "episode" to episode.episode,
                    "air_date" to episode.air_date,
                    "characters" to episode.characters,
                    "viewed" to episode.viewed
                )
            )
    }
}
