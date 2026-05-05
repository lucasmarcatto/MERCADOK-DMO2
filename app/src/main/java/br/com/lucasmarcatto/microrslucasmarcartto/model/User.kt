package br.com.lucasmarcatto.microrslucasmarcartto.model

data class User(
    val nome: String = "",
    val email: String = "",
    val nomeCompleto: String = "",
    val fotoPerfil: String = ""   //base64 da imagem, string vazia se não houver
)