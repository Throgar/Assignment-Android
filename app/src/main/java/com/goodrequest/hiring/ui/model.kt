package com.goodrequest.hiring.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.goodrequest.hiring.PokemonApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PokemonViewModel(
    state: SavedStateHandle,
    private val api: PokemonApi) : ViewModel() {

    val pokemons = state.getLiveData<Result<List<Pokemon>>?>("pokemons", null)

    fun load(page: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val result = api.getPokemons(page = page)
            result.map { pokemons ->
                pokemons.map { pokemon ->
                    val detail = api.getPokemonDetail(pokemon).getOrNull()
//                    detail?.let { pokemon.copy(detail = it) }
                    pokemon.detail = detail
                }
            }
            pokemons.postValue(result)
        }
    }
}

data class Pokemon(
    val id     : String,
    val name   : String,
    var detail : PokemonDetail? = null)

data class PokemonDetail(
    val image  : String,
    val move   : String,
    val weight : Int)