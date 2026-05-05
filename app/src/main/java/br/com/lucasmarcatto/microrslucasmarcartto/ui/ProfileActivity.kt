package br.com.lucasmarcatto.microrslucasmarcartto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import br.com.lucasmarcatto.microrslucasmarcartto.auth.AuthHelper
import br.com.lucasmarcatto.microrslucasmarcartto.dao.UserDAO
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ActivityProfileBinding
import br.com.lucasmarcatto.microrslucasmarcartto.model.User
import br.com.lucasmarcatto.microrslucasmarcartto.util.Base64Converter

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val authHelper = AuthHelper()
    private val userDAO = UserDAO()

    private var currentUser: User? = null


    private val galeriaLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            binding.imgProfilePhoto.setImageURI(uri)
        } else {
            Toast.makeText(this, "Nenhuma foto selecionada", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarDadosUsuario()

        binding.btnAlterarFoto.setOnClickListener {
            galeriaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }


        binding.btnAlterarSenha.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }

        binding.btnSalvarPerfil.setOnClickListener {
            salvarAlteracoes()
        }

        binding.btnVoltar.setOnClickListener {
            finish()  //volta para tela de login
        }
    }

    private fun carregarDadosUsuario() {
        val email = authHelper.getCurrentUser()?.email ?: ""
        if (email.isEmpty()) {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        userDAO.getUserByEmail(email) { user ->
            if (user != null) {
                currentUser = user
                runOnUiThread {
                    binding.etNomeCompleto.setText(user.nomeCompleto)
                    if (user.fotoPerfil.isNotEmpty()) {
                        try {
                            val bitmap = Base64Converter.stringToBitmap(user.fotoPerfil)
                            binding.imgProfilePhoto.setImageBitmap(bitmap)
                        } catch (e: Exception) {
                            // mantém placeholder
                        }
                    }
                }
            }
        }
    }

    private fun salvarAlteracoes() {
        val email = authHelper.getCurrentUser()?.email ?: ""
        if (email.isEmpty()) return

        val novoNome = binding.etNomeCompleto.text.toString().trim()
        if (novoNome.isEmpty()) {
            Toast.makeText(this, "Informe seu nome", Toast.LENGTH_SHORT).show()
            return
        }

        val fotoBase64 = getFotoBase64()

        userDAO.updateProfile(
            email = email,
            nomeCompleto = novoNome,
            fotoPerfil = fotoBase64,
            onSuccess = {
                runOnUiThread {
                    Toast.makeText(this, "Perfil atualizado!", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { erro ->
                runOnUiThread {
                    Toast.makeText(this, "Falha ao salvar perfil: $erro", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    private fun getFotoBase64(): String {
        return try {
            val drawable = binding.imgProfilePhoto.drawable
            if (drawable is android.graphics.drawable.BitmapDrawable) {
                Base64Converter.drawableToString(drawable)
            } else {
                currentUser?.fotoPerfil ?: ""
            }
        } catch (e: Exception) {
            currentUser?.fotoPerfil ?: ""
        }
    }
}