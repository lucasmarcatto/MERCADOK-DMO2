package br.com.lucasmarcatto.microrslucasmarcartto.dao

import br.com.lucasmarcatto.microrslucasmarcartto.model.Post
import com.google.firebase.firestore.FirebaseFirestore

class PostDAO {

    private val db = FirebaseFirestore.getInstance()

    // salva post na coleçao posts
    fun savePost(post: Post, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao salvar post")
            }
    }

    fun getPostsPaginated(
        limit: Long = 5,
        startAfterTimestamp: Long? = null,
        onSuccess: (List<Post>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        var query = db.collection("posts")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)

        if (startAfterTimestamp != null) {
            query = query.startAfter(startAfterTimestamp)
        }

        query.get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                }
                onSuccess(posts)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Erro ao carregar posts")
            }
    }

    fun getPostsByCity( //buscar por cidade
        cidade: String,
        limit: Long = 20,
        onSuccess: (List<Post>) -> Unit,
        onFailure: (String) -> Unit
    ) {
        db.collection("posts")
            .whereEqualTo("cidade", cidade)
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val posts = querySnapshot.documents.mapNotNull { doc ->
                    doc.toObject(Post::class.java)?.copy(id = doc.id)
                }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Erro na busca")
            }
    }
}