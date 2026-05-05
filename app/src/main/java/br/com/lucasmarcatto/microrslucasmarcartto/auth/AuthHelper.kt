package br.com.lucasmarcatto.microrslucasmarcartto.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthHelper {
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser //retorna o usuário logado ou nul

    //-------ergistro com email e senha
    fun registerUser(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(firebaseAuth.currentUser)
                } else {
                    onFailure(task.exception?.message ?: "Erro desconhecido")
                }
            }
    }

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess(firebaseAuth.currentUser)
                } else {
                    onFailure(task.exception?.message ?: "Erro desconhecido")
                }
            }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun updatePassword(
        newPassword: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        onFailure(task.exception?.message ?: "Erro ao alterar senha")
                    }
                }
        } else {
            onFailure("Usuário não autenticado")
        }
    }
}