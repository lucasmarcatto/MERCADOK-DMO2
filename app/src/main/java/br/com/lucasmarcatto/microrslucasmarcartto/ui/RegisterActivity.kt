package br.com.lucasmarcatto.microrslucasmarcartto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.lucasmarcatto.microrslucasmarcartto.auth.AuthHelper
import br.com.lucasmarcatto.microrslucasmarcartto.dao.UserDAO
import br.com.lucasmarcatto.microrslucasmarcartto.model.User
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authHelper = AuthHelper()
    private val userDAO = UserDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val nome = binding.etNome.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (nome.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != confirmPassword) {
                Toast.makeText(this, "Senhas não conferem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.length < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authHelper.registerUser(email, password,
                onSuccess = { firebaseUser ->
                    //salva no Firestore
                    val user = User(
                        nome = nome,
                        email = email,
                        nomeCompleto = nome,
                        fotoPerfil = ""
                    )
                    userDAO.saveUser(user,
                        onSuccess = {
                            Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        },
                        onFailure = { errorMessage ->
                            Toast.makeText(this, "Conta criada, mas falha ao salvar perfil: $errorMessage", Toast.LENGTH_LONG).show()

                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                    )
                },
                onFailure = { errorMessage ->
                    Toast.makeText(this, "Erro no cadastro: $errorMessage", Toast.LENGTH_LONG).show()
                }
            )
        }

        binding.btnVoltar.setOnClickListener {
            finish()  //volta para tela de login
        }
    }
}