package `fun`.inaction.stallx.mark.viewmodel

import `fun`.inaction.network.OkCallback
import `fun`.inaction.network.ServiceCreator
import `fun`.inaction.network.bean.Mark
import `fun`.inaction.network.bean.MarkHistoryResult
import `fun`.inaction.network.bean.Result
import `fun`.inaction.network.call
import `fun`.inaction.network.service.MarkService
import `fun`.inaction.stallx.mark.cache.MarkListCache
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MarkListViewModel:ViewModel() {

    private val markService = ServiceCreator.create<MarkService>()

    val markHistory = MutableLiveData<MutableList<Mark>>()

    init {
        getCacheMarkHistory()
        getMarkHistory()
    }

    /**
     * 发送网络请求获取标记列表
     */
    fun getMarkHistory(){
        markService.getMarkHistory().call {
            markHistory.value = it.markList
            MarkListCache.cacheMarkList(it.markList)
        }
    }

    fun getCacheMarkHistory(){
        markHistory.value = MarkListCache.getMarkList().toMutableList()
    }

    fun addMark(mark:Mark){
        MarkListCache.addMark(mark)
        getCacheMarkHistory()
    }

}