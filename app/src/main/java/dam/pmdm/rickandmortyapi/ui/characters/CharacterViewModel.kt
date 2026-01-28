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
 * ViewModel encargado de gestionar la carga de personajes
 * asociados a un episodio concreto.
 */
class CharacterViewModel : ViewModel() {

    private val _characters = MutableLiveData<List<CharacterModel>>()
    val characters: LiveData<List<CharacterModel>> = _characters

    /**
     * Carga los personajes a partir de las URLs recibidas desde un episodio.
     */
    fun loadCharacters(urls: List<String>) {

        viewModelScope.launch {

            try {
                // Si no hay personajes asociados, se devuelve una lista vacía
                if (urls.isEmpty()) {
                    _characters.value = emptyList()
                    return@launch
                }

                // Extracción de los IDs de personaje desde las URLs
                val characterIds = urls.map { characterUrl ->
                    characterUrl.substringAfterLast("/").toInt()
                }

                val idsParam = characterIds.joinToString(",")

                // Petición a la API para obtener los personajes por ID
                val characterList =
                    RetrofitInstance.api.getCharactersByIds(idsParam)

                _characters.value = characterList

            } catch (httpException: HttpException) {

                // Control de límite de peticiones
                if (httpException.code() == 429) {
                    loadCharacters(urls)
                } else {
                    _characters.value = emptyList()
                }

            } catch (exception: Exception) {
                _characters.value = emptyList()
            }
        }
    }
}
