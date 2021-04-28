package `fun`.inaction.stallx.nav

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.baidu.mapapi.walknavi.WalkNavigateHelper
import com.baidu.mapapi.walknavi.adapter.IWNaviStatusListener
import com.baidu.mapapi.walknavi.adapter.IWTTSPlayer
import com.baidu.mapapi.walknavi.model.WalkNaviDisplayOption
import com.baidu.platform.comapi.walknavi.WalkNaviModeSwitchListener

class WalkNavActivity : AppCompatActivity() {

    private lateinit var walkNavHelper: WalkNavigateHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        walkNavHelper = WalkNavigateHelper.getInstance()
        val view = walkNavHelper.onCreate(this)
        view.fitsSystemWindows = true

        val options = WalkNaviDisplayOption()
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ALPHA_8)
        options.imageToAr(bitmap)
        walkNavHelper.setWalkNaviDisplayOption(options)

        walkNavHelper.setTTsPlayer { s, b ->
            // 语音播报
            Log.e("tag", "msg:${s}，是否抢先播报：$b")
            0
        }

        walkNavHelper.setWalkNaviStatusListener(object : IWNaviStatusListener {
            override fun onWalkNaviModeChange(mode: Int, listener: WalkNaviModeSwitchListener?) {
                walkNavHelper.switchWalkNaviMode(this@WalkNavActivity, mode, listener)
            }

            override fun onNaviExit() {
            }
        })

        walkNavHelper.startWalkNavi(this)

        setContentView(view)
    }

    override fun onResume() {
        super.onResume()
        walkNavHelper.resume()
    }

    override fun onPause() {
        super.onPause()
        walkNavHelper.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        walkNavHelper.quit()
    }
}