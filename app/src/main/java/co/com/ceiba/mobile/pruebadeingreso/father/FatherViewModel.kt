package co.com.ceiba.mobile.pruebadeingreso.father

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FatherViewModel: ViewModel() {

    var vProgressBar = MutableLiveData<Int>()
    var sinDatosOInternet = MutableLiveData<Boolean>()

    init {
        vProgressBar.value = View.VISIBLE
        sinDatosOInternet.value = false
    }

}