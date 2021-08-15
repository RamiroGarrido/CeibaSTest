package co.com.ceiba.mobile.pruebadeingreso.modules.post.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.com.ceiba.mobile.pruebadeingreso.R
import repository.remote_data_source.api_test.PostDTO
import utilities.Constants

class RecyclerViewPostAdapter(
    private var postListDTO: List<PostDTO>,
    private var constants: Constants
) :
    RecyclerView.Adapter<RecyclerViewPostAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postListDTO.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.title.text = postListDTO[position].title
            holder.body.text = postListDTO[position].body
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val title = v.findViewById<TextView>(R.id.title)
        val body = v.findViewById<TextView>(R.id.body)
    }
}