package `fun`.inaction.stallx.park.viewmodel

import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Park
import `fun`.inaction.network.call
import `fun`.inaction.network.service.ParkService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ParkSearchResultViewModel:ViewModel() {

    val parkList = MutableLiveData<MutableList<Park>>()

    private val service = ServiceCreator.create<ParkService>()

    fun searchNearParks(longitude:Double,latitude:Double,city:String){
        service.searchParks(city,longitude,latitude,3f).call {
            parkList.value = it.parks
        }
    }

}