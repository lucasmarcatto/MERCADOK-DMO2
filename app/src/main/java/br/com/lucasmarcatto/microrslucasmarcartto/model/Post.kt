package br.com.lucasmarcatto.microrslucasmarcartto.model

data class Post(
    val id: String = "",
    val imageString: String = "",
    val descricao: String = "",
    val cidade: String = "",
    val autor: String = "",
    val autorNome: String = "",
    val autorFoto: String = "",
    val timestamp: Long = System.currentTimeMillis()
)