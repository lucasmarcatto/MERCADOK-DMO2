package br.com.lucasmarcatto.microrslucasmarcartto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.lucasmarcatto.microrslucasmarcartto.adapter.PostAdapter
import br.com.lucasmarcatto.microrslucasmarcartto.auth.AuthHelper
import br.com.lucasmarcatto.microrslucasmarcartto.dao.PostDAO
import br.com.lucasmarcatto.microrslucasmarcartto.dao.UserDAO
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ActivityHomeBinding
import br.com.lucasmarcatto.microrslucasmarcartto.model.Post

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val authHelper = AuthHelper()
    private val userDAO = UserDAO()
    private val postDAO = PostDAO()

    private var listaPosts = mutableListOf<Post>()
    private lateinit var adapter: PostAdapter


    private var ultimoTimestamp: Long? = null
    private var todosCarregados = false


    private var modoBusca = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        carregarDadosUsuario()
        configurarRecyclerView()
        carregarFeedInicial()


        binding.btnCreatePost.setOnClickListener {
            startActivity(Intent(this, CreatePostActivity::class.java))
        }


        binding.btnCarregarMais.setOnClickListener {
            if (modoBusca) {
                //sai do modo busca e recarrega o feed normal
                binding.etBuscarCidade.text.clear()
                carregarFeedInicial()
                modoBusca = false
                binding.btnCarregarMais.text = "Carregar mais"
                todosCarregados = false
            } else if (!todosCarregados) {
                carregarMaisPosts()
            } else {
                Toast.makeText(this, "Todos os posts foram carregados", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnBuscarCidade.setOnClickListener {
            val cidade = binding.etBuscarCidade.text.toString().trim()
            if (cidade.isEmpty()) {
                //vazio= volta o feed normal
                carregarFeedInicial()
                modoBusca = false
                binding.btnCarregarMais.text = "Carregar mais"
            } else {
                buscarPostsPorCidade(cidade)
            }
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            authHelper.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun configurarRecyclerView() {
        adapter = PostAdapter(listaPosts)
        binding.recyclerViewFeed.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewFeed.adapter = adapter
    }


    private fun carregarFeedInicial() {
        postDAO.getPostsPaginated(
            limit = 5,
            startAfterTimestamp = null,
            onSuccess = { novosPosts ->
                listaPosts.clear()
                listaPosts.addAll(novosPosts)
                adapter.updateList(listaPosts)
                if (novosPosts.isNotEmpty()) {
                    ultimoTimestamp = novosPosts.last().timestamp
                }
                todosCarregados = novosPosts.size < 5
                modoBusca = false
                binding.btnCarregarMais.text = "Carregar mais"
            },
            onFailure = { erro ->
                Toast.makeText(this, "Erro ao carregar feed: $erro", Toast.LENGTH_LONG).show()
            }
        )
    }


    private fun carregarMaisPosts() {
        postDAO.getPostsPaginated(
            limit = 5,
            startAfterTimestamp = ultimoTimestamp,
            onSuccess = { novosPosts ->
                if (novosPosts.isNotEmpty()) {
                    listaPosts.addAll(novosPosts)
                    adapter.updateList(listaPosts)
                    ultimoTimestamp = novosPosts.last().timestamp
                }
                todosCarregados = novosPosts.size < 5
//                if (todosCarregados) {
//                    binding.btnCarregarMais.text = "Nada mais a carregar"
//                }
            },
            onFailure = { erro ->
                Toast.makeText(this, "Erro: $erro", Toast.LENGTH_SHORT).show()
            }
        )
    }

    //b usca por cidade
    private fun buscarPostsPorCidade(cidade: String) {
        postDAO.getPostsByCity(
            cidade = cidade,
            limit = 20,
            onSuccess = { posts ->
                listaPosts.clear()
                listaPosts.addAll(posts)
                adapter.updateList(listaPosts)
                modoBusca = true
                binding.btnCarregarMais.text = "Voltar ao feed"
                todosCarregados = true // na busca n usamos paginacao
                if (posts.isEmpty()) {
                    Toast.makeText(this, "Nenhum post encontrado em $cidade", Toast.LENGTH_SHORT).show()
                }
            },
            onFailure = { erro ->
                Toast.makeText(this, "Erro na busca: $erro", Toast.LENGTH_LONG).show()
            }
        )
    }


    private fun carregarDadosUsuario() {
        val email = authHelper.getCurrentUser()?.email ?: ""
        if (email.isNotEmpty()) {
            userDAO.getUserByEmail(email) { user ->
                if (user != null) {
                    runOnUiThread {
                        binding.tvWelcome.text = "Bem-vindo(a), ${user.nomeCompleto}!"
                    }
                }
            }
        }
    }
}