package `fun`.inaction.stallx.search.viewmodel

import `fun`.inaction.stallx.search.cache.SearchCache
import `fun`.inaction.stallx.utils.MapUtil
import `fun`.inaction.stallx.utils.loge
import `fun`.inaction.stallx.utils.logi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.baidu.mapapi.search.sug.SuggestionResult

class SearchViewModel:ViewModel() {

    private val TAG = "SearchViewModel"

    /**
     * 搜索结果数据
     */
    val searchResult: MutableLiveData<MutableList<SuggestionResult.SuggestionInfo>> = MutableLiveData()

    init {
        getCache()
    }

    /**
     * 根据key获得搜索建议
     */
    fun search(key:String,city:String){
        if(key == ""){
            getCache()
            return
        }
        MapUtil.suggestionSearch(city,key){
            val data = it.toMutableList()
            logi(TAG,"search ${key})")
            data.forEach {
                logi(TAG,"add:${it.address},(${it.pt?.longitude},${it.pt?.latitude})")
            }
            data.removeIf {
                it.pt == null
            }

            searchResult.value = data
        }
    }

    /**
     * 获取”点击过的搜索记录“缓存
     */
    fun getCache(){
        SearchCache.getSearchResultCache()?.let { searchResult.value = it.toMutableList() }
    }

    /**
     * 缓存点击过的搜索结果
     */
    fun cache(suggestionInfo: SuggestionResult.SuggestionInfo){
        SearchCache.cacheSearchResult(suggestionInfo)
    }

}