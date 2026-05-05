package br.com.lucasmarcatto.microrslucasmarcartto.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.lucasmarcatto.microrslucasmarcartto.databinding.ItemPostBinding
import br.com.lucasmarcatto.microrslucasmarcartto.model.Post
import br.com.lucasmarcatto.microrslucasmarcartto.util.Base64Converter

class PostAdapter(
    private var posts: List<Post>
) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    fun updateList(newPosts: List<Post>) { //atualiza os posts e notifica mudancas
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    inner class ViewHolder(val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post) {
            binding.tvUsername.text = post.autorNome.ifEmpty { post.autorNome }
            binding.tvCidade.text = post.cidade.ifEmpty { "Localização não informada" }
            binding.tvDescricao.text = post.descricao

            if (post.imageString.isNotEmpty()) {
                try {
                    val bitmap = Base64Converter.stringToBitmap(post.imageString)
                    binding.imgPost.setImageBitmap(bitmap)
                } catch (e: Exception) { }
            }

            if (post.autorFoto.isNotEmpty()) {
                try {
                    val bitmap = Base64Converter.stringToBitmap(post.autorFoto)
                    binding.imgAutorFoto.setImageBitmap(bitmap)
                } catch (e: Exception) {
                    //nada, para poder manter a imagem padrao do android
                }
            }
        }
    }
}