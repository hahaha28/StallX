package `fun`.inaction.stallx.search.cache

import `fun`.inaction.stallx.utils.DiskCacheUtil
import android.os.Parcel
import com.baidu.mapapi.search.sug.SuggestionResult

object SearchCache {

    /**
     * 缓存点击过的搜索记录
     */
    fun cacheSearchResult(data: SuggestionResult.SuggestionInfo) {
        var searchList =
            DiskCacheUtil.getParcelableList("searchList", SuggestionResult.SuggestionInfo.CREATOR)
        if (searchList == null) {
            searchList = mutableListOf()
        }

        val newList = searchList.toMutableList()

        newList.removeIf {
            it.pt.latitude == data.pt.latitude
                    && it.pt.longitude == data.pt.longitude
        }

        newList.add(0, data)
        DiskCacheUtil.writeParcelableList("searchList", newList)

    }

    /**
     * 获取点击过的搜索记录缓存
     */
    fun getSearchResultCache(): List<SuggestionResult.SuggestionInfo>? {
        return DiskCacheUtil.getParcelableList(
            "searchList",
            SuggestionResult.SuggestionInfo.CREATOR
        )
    }

}