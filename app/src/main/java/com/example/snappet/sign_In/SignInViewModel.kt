package com.example.snappet.sign_In

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

//o view model gere e expõe data para "view" (UI)
//serve de intermédio entre a view (UI) e as "data models" ou "business logic" debaixo
//prepara a data para a "view" a poder usar
//neste caso o SignInViewModel é uma subclasse do ViewModel
class SignInViewModel: ViewModel() {

    //criado um novo SignInState (um dataset que fiz)
    //o MutableStateFlow so garante que os observer e listeners sabem
    // das alterações aplicadas à variavel
    private val _state = MutableStateFlow(SignInState())
    //pata não expo a versão mutável de cima para o UI
    //ajuda a prevenir mudificações indesejadas fora do view model
    //o asStateFlow cria uma view imutável do state, convertendo-o para um "StateFlow"
    val state = _state.asStateFlow()

    //chamada depois de sign in com sucesso e de obter o respetivo SignInResult do mesmo
    fun onSignInResult(result: SignInResult) {
        //vamos atualizar o nosso estado
        //o "it" refere-se ao _state
        _state.update { it.copy(
            //o sign in é um sucesso se a data do SignInResult (result)
            //não for nula
            isSignInSuccessful = result.data != null,
            //mensagem de erro
            signInError = result.errorMessage
        ) }
    }

    //faz reset ao state: se navegarmos para outro screen para além no screen
    //de login queremos ter a certeza que quando voltarmos para o login screen
    //que não estejamos loged in (login sem sucesso)
    fun resetState() {
        //o update assim põe o "_state" no estado default do SignInState
        //que lá dentro tem o "isSignInSuccessful" = false
        _state.update { SignInState() }
    }
}