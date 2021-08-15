package co.com.ceiba.mobile.pruebadeingreso.modules.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.databinding.FragmentMainBinding
import co.com.ceiba.mobile.pruebadeingreso.father.FatherActivity
import co.com.ceiba.mobile.pruebadeingreso.modules.FatherFragment
import co.com.ceiba.mobile.pruebadeingreso.modules.main.adapters.RecyclerViewMainAdapter
import kotlinx.coroutines.launch
import repository.remote_data_source.api_test.UserDTO

class MainFragment : FatherFragment(), View.OnClickListener {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return try {
            super.onCreateView(inflater, container, savedInstanceState)
            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_main,
                container,
                false
            )
            binding.lifecycleOwner = this
            viewModel =
                ViewModelProvider(
                    this,
                    MainVMFactory(
                        (activity as FatherActivity).dbService,
                        (activity as FatherActivity).remoteService,
                        (activity as FatherActivity).constants
                    )
                ).get(MainViewModel::class.java)
            binding.root
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
            null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            super.onViewCreated(view, savedInstanceState)
            setObservers()
            binding.recyclerViewSearchResults.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.editTextSearch.doOnTextChanged { text, start, before, count ->
                onKeyPressed(text, count)
            }
            job = lifecycleScope.launch {
                //To delete the local DB uncomment the following:
                //viewModel.deleteAllRecordsDB()
                getUserList()
            }
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }

    //Returns a user list from local DB if exists or fetch from the API if it doesn't
    private suspend fun getUserList() {
        try {
            (activity as FatherActivity).viewModel.sinDatosOInternet.value = false
            if (!(viewModel as MainViewModel).getUserListDB()) {
                if ((activity as FatherActivity).isInternetAvailable()) {
                    (viewModel as MainViewModel).getUserListAPI()
                } else {
                    (activity as FatherActivity).viewModel.sinDatosOInternet.value = true
                }
            }
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        } finally {
            (viewModel as MainViewModel).vProgressBar.value = View.GONE
        }
    }

    override fun setObservers() {
        try {
            super.setObservers()
            (viewModel as MainViewModel).userList.observe(viewLifecycleOwner) {
                //Sets the recyclerview adapter and restores its previous state if exists
                if (it != null) {
                    if ((viewModel as MainViewModel).filteredUserList.value == null) {
                        binding.recyclerViewSearchResults.adapter =
                            RecyclerViewMainAdapter(
                                it,
                                this,
                                (activity as FatherActivity).constants
                            )
                    } else {
                        binding.recyclerViewSearchResults.adapter =
                            RecyclerViewMainAdapter(
                                (viewModel as MainViewModel).filteredUserList.value!!,
                                this,
                                (activity as FatherActivity).constants
                            )
                    }
                    if ((viewModel as MainViewModel).recyclerViewState != null) {
                        (binding.recyclerViewSearchResults.layoutManager as LinearLayoutManager).onRestoreInstanceState(
                            (viewModel as MainViewModel).recyclerViewState
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }

    //Triggered when a key is pressed on the keyboard
    private fun onKeyPressed(text: CharSequence?, count: Int) {
        try {
            val userList: List<UserDTO>
            if (count == 0) {
                (viewModel as MainViewModel).filteredUserList.value = null
                userList = (viewModel as MainViewModel).userList.value ?: emptyList<UserDTO>()
            } else {
                (viewModel as MainViewModel).filteredUserList.value =
                    (viewModel as MainViewModel).userList.value?.filter { it ->
                        it.name.contains(
                            text!!,
                            ignoreCase = true
                        )
                    } ?: emptyList<UserDTO>()

                userList = (viewModel as MainViewModel).filteredUserList.value!!
            }
            (activity as FatherActivity).viewModel.sinDatosOInternet.value =
                userList.isEmpty()
            binding.recyclerViewSearchResults.adapter =
                RecyclerViewMainAdapter(
                    userList,
                    this,
                    (activity as FatherActivity).constants
                )
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }

    //Open the posts for the user selected
    override fun onClick(v: View?) {
        try {
            when (v!!.id) {
                //CLICK ON RECYCLERVIEW ITEM TO OPEN A USER POST
                R.id.btn_view_post -> {
                    var user: UserDTO? = null
                    user = if ((viewModel as MainViewModel).filteredUserList.value == null) {
                        (viewModel as MainViewModel).userList.value!![v.tag as Int]
                    } else {
                        (viewModel as MainViewModel).filteredUserList.value!![v.tag as Int]
                    }
                    val action = MainFragmentDirections.actionNavMainToNavPost()
                    action.userSelected = user
                    (viewModel as MainViewModel).recyclerViewState =
                        binding.recyclerViewSearchResults.layoutManager!!.onSaveInstanceState()
                    (activity as FatherActivity).navController.navigate(action)

                }
            }
        } catch (e: Exception) {
            Log.i((activity as FatherActivity).constants.TAG_ERROR, e.message!!)
        }
    }
}