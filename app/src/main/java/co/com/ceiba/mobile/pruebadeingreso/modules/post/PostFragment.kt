package co.com.ceiba.mobile.pruebadeingreso.modules.post

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.databinding.FragmentPostBinding
import co.com.ceiba.mobile.pruebadeingreso.father.FatherActivity
import co.com.ceiba.mobile.pruebadeingreso.modules.FatherFragment
import co.com.ceiba.mobile.pruebadeingreso.modules.post.adapters.RecyclerViewPostAdapter
import kotlinx.coroutines.launch

class PostFragment : FatherFragment() {

    private lateinit var binding: FragmentPostBinding
    private val args: PostFragmentArgs by navArgs()

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
            viewModel =
                ViewModelProvider(
                    this,
                    PostVMFactory(
                        (activity as FatherActivity).dbService,
                        (activity as FatherActivity).remoteService,
                        (activity as FatherActivity).constants
                    )
                ).get(PostViewModel::class.java)
            (viewModel as PostViewModel).userObtained = args.userSelected
            binding.root
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            binding.recyclerViewPostsResults.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.name.text = (viewModel as PostViewModel).userObtained!!.name
            binding.phone.text = (viewModel as PostViewModel).userObtained!!.phone
            binding.email.text = (viewModel as PostViewModel).userObtained!!.email
            job = lifecycleScope.launch {
                getPostList()
            }
            setObservers()
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }

    //Returns a user list from local DB if exists or fetch from the API if it doesn't
    private suspend fun getPostList() {
        try {
            (activity as FatherActivity).viewModel.sinDatosOInternet.value = false
            if (!(viewModel as PostViewModel).getPostByUserIdDB()) {
                if ((activity as FatherActivity).isInternetAvailable()) {
                    (viewModel as PostViewModel).getPostByUserIdAPI()
                }
                else{
                    (activity as FatherActivity).viewModel.sinDatosOInternet.value = true
                }
            }
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
        finally {
            (viewModel as PostViewModel).vProgressBar.value = View.GONE
        }
    }

    override fun setObservers() {
        try {
            super.setObservers()
            (viewModel as PostViewModel).postList.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.recyclerViewPostsResults.adapter =
                        RecyclerViewPostAdapter(
                            it,
                            (activity as FatherActivity).constants
                        )
                }
            }
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }

    //Cancel all pending tasks running on the background
    override fun onDestroy() {
        try {
            super.onDestroy()
            job.cancel()
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }

}