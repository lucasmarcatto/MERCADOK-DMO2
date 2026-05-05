package br.com.lucasmarcatto.microrslucasmarcartto.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import br.com.lucasmarcatto.microrslucasmarcartto.auth.AuthHelper
import br.com.lucasmarcatto.microrslucasmarcartto.dao.PostDAO
import br.com.lucasmarcatto.microrslucasmarcartto.dao.UserDAO
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ActivityCreatePostBinding
import br.com.lucasmarcatto.microrslucasmarcartto.location.LocalizacaoHelper
import br.com.lucasmarcatto.microrslucasmarcartto.model.Post
import br.com.lucasmarcatto.microrslucasmarcartto.util.Base64Converter

class CreatePostActivity : AppCompatActivity(), LocalizacaoHelper.Callback {

    private lateinit var binding: ActivityCreatePostBinding
    private val authHelper = AuthHelper()
    private val postDAO = PostDAO()
    private val userDAO = UserDAO()
    private val LOCATION_PERMISSION_REQUEST = 1001

    private lateinit var galeriaLauncher: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //btnVoltar-----
        binding.btnVoltar.setOnClickListener {
            finish()
        }

        configurarGaleria()

        binding.btnSelectImage.setOnClickListener {
            galeriaLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.btnObterCidade.setOnClickListener {
            solicitarLocalizacao()
        }

        binding.btnSalvarPost.setOnClickListener {
            val descricao = binding.etDescricao.text.toString().trim()
            val cidade = binding.etCidade.text.toString().trim()

            if (descricao.isEmpty()) {
                Toast.makeText(this, "Descreva seu produto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val drawable = binding.imgPostPreview.drawable
            val imagemBase64 = if (drawable != null) {
                try {
                    Base64Converter.drawableToString(drawable)
                } catch (e: Exception) {
                    ""
                }
            } else ""

            if (imagemBase64.isEmpty()) {
                Toast.makeText(this, "Selecione uma imagem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val emailAutor = authHelper.getCurrentUser()?.email ?: ""
            if (emailAutor.isEmpty()) {
                Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userDAO.getUserByEmail(emailAutor) { user ->
                val nomeAutor = user?.nomeCompleto ?: emailAutor
                val fotoAutor = user?.fotoPerfil ?: ""
                val novoPost = Post(
                    imageString = imagemBase64,
                    descricao = descricao,
                    cidade = cidade,    // pode ficar vazia se não preenchida
                    autor = emailAutor,
                    autorNome = nomeAutor,
                    autorFoto = fotoAutor
                )

                postDAO.savePost(novoPost,
                    onSuccess = {
                        Toast.makeText(this, "Post publicado!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))
                        finish()
                    },
                    onFailure = { erro ->
                        Toast.makeText(this, "Erro: $erro", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun configurarGaleria() {
        galeriaLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) {
                binding.imgPostPreview.setImageURI(uri)
            } else {
                Toast.makeText(this, "Nenhuma imagem selecionada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ------------------ localizacao--------------
    private fun solicitarLocalizacao() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        } else {
            obterCidade()
        }
    }

    private fun obterCidade() {
        val localizacaoHelper = LocalizacaoHelper(applicationContext)
        localizacaoHelper.obterCidadeAtual(this)
    }

    override fun onCidadeRecebida(cidade: String) {
        runOnUiThread {
            binding.etCidade.setText(cidade)
            Toast.makeText(this, "Cidade: $cidade", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErro(mensagem: String) {
        runOnUiThread {
            Toast.makeText(this, "Erro: $mensagem", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                obterCidade()
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}