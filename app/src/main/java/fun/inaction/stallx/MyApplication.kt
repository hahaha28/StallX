package `fun`.inaction.stallx

import android.app.Application
import android.content.Context
import android.util.Log
import com.baidu.mapapi.SDKInitializer
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory
import com.baidu.navisdk.adapter.IBaiduNaviManager
import com.baidu.navisdk.adapter.struct.BNTTsInitConfig
import com.baidu.navisdk.adapter.struct.BNaviInitConfig
import com.tencent.mmkv.MMKV

class MyApplication : Application() {

    private val TAG = "MyApplication"
    private val BAIDU_FOLDER_NAME = "BaiduMap"


    override fun onCreate() {
        super.onCreate()

        _context = applicationContext

        MMKV.initialize(this)

        SDKInitializer.initialize(this)

        initNavi()

    }

    private fun initNavi() {
        BaiduNaviManagerFactory.getBaiduNaviManager().init(
            this,
            BNaviInitConfig.Builder()
                .sdcardRootPath(getExternalFilesDir(null)!!.absolutePath)
                .appFolderName(BAIDU_FOLDER_NAME)
                .naviInitListener(object : IBaiduNaviManager.INaviInitListener {
                    override fun onAuthResult(status: Int, msg: String?) {
                        val result: String
                        result = if (0 == status) {
                            "key校验成功!"
                        } else {
                            "key校验失败, $msg"
                        }
                        Log.e(TAG, result)
                    }

                    override fun initStart() {

                    }

                    override fun initSuccess() {
                        Log.e(TAG, "initSuccess")
                        initTTS()
                    }

                    override fun initFailed(errCode: Int) {
                        Log.e(TAG, "initFailed-$errCode")
                    }
                })
                .build()
        )
    }

    private fun initTTS() {

        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(
            BNTTsInitConfig.Builder()
                .context(this)
                .sdcardRootPath(getExternalFilesDir(null)!!.absolutePath)
                .appFolderName(BAIDU_FOLDER_NAME)
                .appId("23856362")
                .secretKey("ML4e9YGytHNRvv0z4StLyj5lT3Koqvlo")
                .appKey("ZcHnqe3zePbpI9tq55Ap5cCm")
                .build()


        )

    }

    companion object {
        private lateinit var _context: Context
        public val context: Context
            get() = _context
    }
}