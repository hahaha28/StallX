package `fun`.inaction.stallx.utils

import android.os.Parcel
import android.os.Parcelable
import android.util.Base64
import android.view.View

fun Parcelable.toBase64String():String{
    val parcel = Parcel.obtain()
    writeToParcel(parcel,0)
    val bytes = parcel.marshall()
    parcel.recycle()
    return Base64.encodeToString(bytes,Base64.DEFAULT)
}

fun <T> decodeParcelString(str:String,creator:Parcelable.Creator<T>):T{
    val bytes = Base64.decode(str,Base64.DEFAULT)
    val parcel = Parcel.obtain()
    parcel.unmarshall(bytes,0,bytes.size)
    parcel.setDataPosition(0)
    return creator.createFromParcel(parcel)
}

fun View.show(){
    this.visibility = View.VISIBLE
}

fun View.hide(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}