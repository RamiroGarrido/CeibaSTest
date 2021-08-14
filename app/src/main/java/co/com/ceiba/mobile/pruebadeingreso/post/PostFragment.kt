package co.com.ceiba.mobile.pruebadeingreso.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.databinding.FragmentPostBinding
import co.com.ceiba.mobile.pruebadeingreso.father.FatherActivity

class PostFragment : Fragment() {
    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            super.onCreateView(inflater, container, savedInstanceState)
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_post,
                container,
                false
            )
            binding.lifecycleOwner = this
            binding.root
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
            null
        }
    }
}