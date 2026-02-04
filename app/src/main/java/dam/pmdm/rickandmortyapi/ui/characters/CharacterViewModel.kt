package dam.pmdm.rickandmortyapi.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dam.pmdm.rickandmortyapi.data.api.RetrofitInstance
import dam.pmdm.rickandmortyapi.data.model.CharacterModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

/**
 * ViewModel que gestiona la carga de personajes
 * asociados a un episodio concreto.
 */
class CharacterViewModel : ViewModel() {

    // LiveData mutable privado que almacena la lista de personajes
    private val _characters = MutableLiveData<List<CharacterModel>>()
    // LiveData público para mostrar los personajes
    val characters: LiveData<List<CharacterModel>> = _characters

    /**
     * Carga los personajes a partir de las URLs recibidas desde un episodio
     */
    fun loadCharacters(urls: List<String>) {

        viewModelScope.launch {

            try {
                // Devuelve lista vacía si no hay URLs
                if (urls.isEmpty()) {
                    _characters.value = emptyList()
                    return@launch
                }

                // Extrae los IDs de los personajes desde las URLs
                val characterIds = urls.map { characterUrl ->
                    characterUrl.substringAfterLast("/").toInt() // Convierte ID a Int
                }

                // Une los IDs en una cadena separada por comas para la API
                val idsParam = characterIds.joinToString(",")

                // Llama a la API para obtener los personajes por IDs
                val characterList = RetrofitInstance.api.getCharactersByIds(idsParam)

                // Actualiza LiveData con la lista de personajes
                _characters.value = characterList

            } catch (httpException: HttpException) {

                // Si la API responde 429 (too many requests), reintenta
                if (httpException.code() == 429) {
                    loadCharacters(urls)
                } else {
                    // En otros errores HTTP, devuelve lista vacía
                    _characters.value = emptyList()
                }

            } catch (exception: Exception) {
                // Captura cualquier otro error y devuelve lista vacía
                _characters.value = emptyList()
            }
        }
    }
}
