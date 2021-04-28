package `fun`.inaction.stallx.mark.cache

import `fun`.inaction.network.bean.Mark
import `fun`.inaction.stallx.utils.DiskCacheUtil

object MarkListCache {

    fun cacheMarkList(markList:List<Mark>){
        DiskCacheUtil.writeSerializableList("markList",markList)
    }

    fun getMarkList():List<Mark>{
        return DiskCacheUtil.getSerializableList<Mark>("markList")?: listOf()
    }

    fun addMark(mark:Mark){
        val list = DiskCacheUtil.getSerializableList<Mark>("markList")?.toMutableList()?: mutableListOf()
        list.add(0,mark)
        DiskCacheUtil.writeSerializableList("markList",list)
    }

}