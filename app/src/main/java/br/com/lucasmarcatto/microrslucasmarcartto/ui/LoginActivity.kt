package br.com.lucasmarcatto.microrslucasmarcartto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.lucasmarcatto.microrslucasmarcartto.auth.AuthHelper
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authHelper = AuthHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //se estiver logado  vai direto para Home
        if (authHelper.getCurrentUser() != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Preencha e-mail e senha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authHelper.loginUser(email, password,
                onSuccess = {
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                },
                onFailure = { errorMessage ->
                    Toast.makeText(this, "Erro: $errorMessage", Toast.LENGTH_LONG).show()
                }
            )
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}