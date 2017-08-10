package ir.kivee.smallwall

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var netUtil = NetUtils(this)
        var state = getPreferences()
        mainStateChanger(state)
        chaneWallpaper.setOnClickListener({ _ ->
            netUtil.mainStack()
        })

        appState.setOnCheckedChangeListener { _, state ->
            editPreferences(state)
            mainStateChanger(state)
        }

    }

    private fun broadcastStateChanger(state: Boolean) {
        val broadcastState = when (state) {
            true -> {
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            }
            false -> {
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
            }
        }

        val component = ComponentName(this@MainActivity, DateChangeReceiver::class.java)
        packageManager.setComponentEnabledSetting(component,
                broadcastState, PackageManager.DONT_KILL_APP)
    }

    private fun switchStateChanger(state: Boolean) {
        appState.isChecked = state
    }

    private fun getPreferences(): Boolean {
        preferences = getSharedPreferences("swPrefrences",
                Context.MODE_PRIVATE)
        return preferences.getBoolean("broadcastState", false)
    }

    private fun editPreferences(switchState: Boolean) {
        editor = preferences.edit()
        editor.putBoolean("broadcastState", switchState)
        editor.commit()
    }

    private fun mainStateChanger(state: Boolean) {
        broadcastStateChanger(state)
        switchStateChanger(state)
    }
}


