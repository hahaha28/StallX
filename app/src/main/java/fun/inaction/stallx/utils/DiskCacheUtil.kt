package `fun`.inaction.stallx.utils

import `fun`.inaction.stallx.MyApplication
import android.content.Intent
import android.os.Parcelable
import com.baidu.mapapi.search.sug.SuggestionResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import java.io.*

object DiskCacheUtil {

    private val rootPath: String = MyApplication.context.externalCacheDir.toString()

    private val gson:Gson = Gson()

    val mmkv: MMKV? = MMKV.mmkvWithID("cache", rootPath)

    fun write(key: String, data: String) =
        mmkv?.encode(key, data)

    /**
     * 缓存 Parcelable 对象列表
     */
    fun writeParcelableList(key:String, data: List<Parcelable>){
        val temp = mutableListOf<String>()
        data.forEach {
            temp.add(it.toBase64String())
        }
        mmkv?.encode(key,gson.toJson(temp))
    }

    fun write(key:String,obj:Serializable){
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(obj)
        val bytes = bos.toByteArray()
        mmkv?.encode(key,bytes)
        oos.close()
        bos.close()
    }


    fun getString(key: String): String? =
        mmkv?.getString(key, null)

    fun <T> getSerializableObj(key:String):T?{
        mmkv?.getBytes(key,null)?.let {
            val bais = ByteArrayInputStream(it)
            val ois = ObjectInputStream(bais)
            val obj = ois.readObject() as T
            ois.close()
            bais.close()
            return obj
        }
        return null
    }

    fun  <T:Parcelable> getParcelableList(key: String,creator:Parcelable.Creator<T>):List<T>?{
        getString(key)?.let {
            val result = mutableListOf<T>()
            val stringList = gson.fromJson<List<String>>(it, object : TypeToken<List<String>>(){}.type)
            stringList.forEach { str ->
                result.add(decodeParcelString(str,creator))
            }
            return result
        }
        return null
    }



}