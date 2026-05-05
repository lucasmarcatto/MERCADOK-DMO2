package br.com.lucasmarcatto.microrslucasmarcartto.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.lucasmarcatto.microrslucasmarcartto.auth.AuthHelper
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding
    private val authHelper = AuthHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSalvarSenha.setOnClickListener {
            val novaSenha = binding.etNovaSenha.text.toString()
            val confirmarSenha = binding.etConfirmarNovaSenha.text.toString()

            if (novaSenha.isEmpty() || confirmarSenha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (novaSenha.length < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (novaSenha != confirmarSenha) {
                Toast.makeText(this, "Senhas não conferem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authHelper.updatePassword(
                newPassword = novaSenha,
                onSuccess = {
                    runOnUiThread {
                        Toast.makeText(this, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()  //volta para a tela de perfil
                    }
                },
                onFailure = { erro ->
                    runOnUiThread {
                        Toast.makeText(this, "Erro: $erro", Toast.LENGTH_LONG).show()
                    }
                }
            )
        }
    }
}