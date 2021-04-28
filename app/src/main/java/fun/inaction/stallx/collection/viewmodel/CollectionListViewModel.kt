package `fun`.inaction.stallx.collection.viewmodel

import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Collection
import `fun`.inaction.network.call
import `fun`.inaction.network.service.CollectionService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CollectionListViewModel:ViewModel() {

    val dataList = MutableLiveData<MutableList<Collection>>()
    val service = ServiceCreator.create<CollectionService>()

    init {
        getCollection()
    }

    fun getCollection(){
        service.getCollections().call {
            dataList.value = it.collectionData
        }
    }

    fun cancelCollected(collectionId:String){
        service.deleteCollection(collectionId).call {

        }
    }

}