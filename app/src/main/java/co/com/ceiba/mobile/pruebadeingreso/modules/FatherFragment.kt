package co.com.ceiba.mobile.pruebadeingreso.modules

import android.util.Log
import androidx.fragment.app.Fragment
import co.com.ceiba.mobile.pruebadeingreso.R
import co.com.ceiba.mobile.pruebadeingreso.father.FatherActivity
import kotlinx.coroutines.Job

open class FatherFragment:Fragment() {
    lateinit var job: Job
    lateinit var viewModel:FatherViewModel

    open fun setObservers() {
        try {
            viewModel.vProgressBar.observe(viewLifecycleOwner) {
                if (it != null) {
                    (activity as FatherActivity).viewModel.vProgressBar.value = it
                }
            }
            viewModel.errorMessage.observe(viewLifecycleOwner) {
                if (it != null) {
                    when (it) {
                        (activity as FatherActivity).constants.HTTP_UNAUTHORIZED -> {
                            (activity as FatherActivity).showMessage(resources.getString(R.string.errorUnauthorized))
                        }
                        (activity as FatherActivity).constants.HTTP_NOT_FOUND -> {
                            (activity as FatherActivity).showMessage(resources.getString(R.string.errorNotFound))
                        }
                        else -> {
                            (activity as FatherActivity).showMessage(resources.getString(R.string.errorInternet))

                        }
                    }
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