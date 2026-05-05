package br.com.lucasmarcatto.microrslucasmarcartto.dao

import br.com.lucasmarcatto.microrslucasmarcartto.model.User
import com.google.firebase.firestore.FirebaseFirestore

class UserDAO {

    private val db = FirebaseFirestore.getInstance()

    //salva ou atualiza o documento do user na coleção "usuarios"
    fun saveUser(user: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("usuarios").document(user.email)
            .set(user)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao salvar")
            }
    }

    fun getUserByEmail(email: String, onResult: (User?) -> Unit) { //buscar um usuário pelo e-mail
        db.collection("usuarios").document(email)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val user = document.toObject(User::class.java)
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    //atualiza nome e/ou foto de perfil
    fun updateProfile(
        email: String,
        nomeCompleto: String,
        fotoPerfil: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val updates = hashMapOf<String, Any>(
            "nomeCompleto" to nomeCompleto,
            "fotoPerfil" to fotoPerfil
        )

        db.collection("usuarios").document(email)
            .update(updates)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e.message ?: "Erro ao atualizar perfil") }
    }
}