package `fun`.inaction.stallx.utils

import com.baidu.mapapi.search.sug.SuggestionResult
import com.baidu.mapapi.search.sug.SuggestionSearch
import com.baidu.mapapi.search.sug.SuggestionSearchOption

object MapUtil {

    /**
     * 地点输入提示检索
     */
    private val suggestionSearch by lazy { SuggestionSearch.newInstance() }

    /**
     * 地点输入提示检索
     */
    fun suggestionSearch(city:String,keyword:String,listener:(List<SuggestionResult.SuggestionInfo>)->Unit){

        suggestionSearch.setOnGetSuggestionResultListener {
            if(it.allSuggestions == null){
                return@setOnGetSuggestionResultListener
            }
            listener(it.allSuggestions)
        }

        suggestionSearch.requestSuggestion(
            SuggestionSearchOption()
            .city(city)
            .citylimit(true)
            .keyword(keyword))
    }


}