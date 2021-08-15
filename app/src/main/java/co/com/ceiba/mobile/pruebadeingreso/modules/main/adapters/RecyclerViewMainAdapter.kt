package co.com.ceiba.mobile.pruebadeingreso.modules.main.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import co.com.ceiba.mobile.pruebadeingreso.R
import repository.remote_data_source.api_test.UserDTO
import utilities.Constants

class RecyclerViewMainAdapter(
    private var listaUsersDTO: List<UserDTO>,
    private var listener:Fragment,
    private var constants: Constants
) :
    RecyclerView.Adapter<RecyclerViewMainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        var itemView: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listaUsersDTO.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.name.text = listaUsersDTO[position].name
            holder.phone.text = listaUsersDTO[position].phone
            holder.email.text = listaUsersDTO[position].email
            holder.button.tag = position
            holder.button.setOnClickListener(listener as View.OnClickListener)
        } catch (e: Exception) {
            Log.i(constants.TAG_ERROR, e.message!!)
        }
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.findViewById<TextView>(R.id.name)
        val phone = v.findViewById<TextView>(R.id.phone)
        val email = v.findViewById<TextView>(R.id.email)
        val button = v.findViewById<Button>(R.id.btn_view_post)
    }
}