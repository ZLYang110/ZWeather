package com.zlyandroid.basic.common.app

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.widget.Toast
import com.zlyandroid.basic.common.util.log.L
import java.util.*

open class App : Application() , Application.ActivityLifecycleCallbacks  {

    protected val FLAG_CLEAR_TOP = 0
    protected val FLAG_CLEAR_OLD = 1



    override fun onCreate() {
        super.onCreate()
        application = this
        registerActivityListener()
    }

    companion object {
        private lateinit var application: Application
        private val activities = Collections.synchronizedList(LinkedList<Activity>())
        private val singleInstanceActivities =
            Collections.synchronizedMap(HashMap<Class<out Activity?>, Int>())

        fun getApp(): Application  {
            return application
        }
        fun getAppContext(): Context {
            return  getApp().getApplicationContext()
        }

    /**
     * 判断Android程序是否在前台运行
     */
      fun isAppOnForeground(): Boolean {
        val activityManager = getAppContext().getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName =  getAppContext().getPackageName()
        for (appProcess in appProcesses) {
            if (appProcess.processName == packageName && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    /**
     * APP从后台切换回前台
     */
      fun returnToForeground() {
        if (!isAppOnForeground()) {
            val currentActivity: Activity? =  currentActivity()
            if (currentActivity != null) {
                val intent = Intent(getAppContext(), currentActivity.javaClass)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                 getAppContext().startActivity(intent)
            }
        }
    }


    /**
     * 获取当前Activity
     */
    fun currentActivity(): Activity? {
        return if (activities.isEmpty()) {
            null
        } else  activities.get(activities.size - 1)
    }

    /**
     * 按照指定类名找到activity
     */
      fun findActivity(cls: Class<*>?): Activity? {
        if (cls == null) {
            return null
        }
        if (activities.isEmpty()) {
            return null
        }
        for (activity in activities) {
            if (activity.javaClass == cls) {
                return activity
            }
        }
        return null
    }
      fun exitApp() {
        finishAllActivity()
         killProcess()
    }
      fun killProcess() {
        Process.killProcess(Process.myPid())
    }
        fun recreate() {
            if (!activities.isEmpty()) {
                for (activity in activities) {
                    activity.recreate()
                }
            }
        }
      fun restartApp() {
        val intent: Intent? =  getApp().getPackageManager()
            .getLaunchIntentForPackage(getApp().getPackageName())
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            getApp().startActivity(intent)
            killProcess()
        } else {
             finishActivityWithoutCount(1)
            if (! activities.isEmpty()) {
                 activities.get(0).recreate()
            }
        }
    }
      fun finishActivityWithoutCount(count: Int) {
        if (activities.isEmpty()) {
            return
        }
        if (count <= 0) {
             finishAllActivity()
            return
        }
        for (i in  activities.size - 1 downTo count) {
             finishActivity(activities.get(i))
        }
    }

    /**
     * 结束指定的Activity
     */
      fun finishActivity(activity: Activity) {
        if (activities.isEmpty()) {
            return
        }
         activities.remove(activity)
        activity.finish()
    }

    /**
     * 结束指定类名的Activity
     */
      fun finishActivity(cls: Class<out Activity?>?) {
        if (cls == null) {
            return
        }
        if (activities.isEmpty()) {
            return
        }
        for (i in  activities.indices.reversed()) {
            val activity: Activity =  activities.get(i)
            if (cls == activity.javaClass) {
                finishActivity(activity)
            }
        }
    }
    /**
     * 结束所有Activity
     */
      fun finishAllActivity() {
        if (activities.isEmpty()) {
            return
        }
        for (i in  activities.indices.reversed()) {
            val activity: Activity =  activities.get(i)
            if (!activity.isFinishing) {
                activity.finish()
            }
        }
          activities.clear()
    }


    }
    override fun attachBaseContext(context: Context?) {
        super.attachBaseContext(context)
          //MultiDex.install(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val cls: Class<out Activity> = activity.javaClass
        if (! singleInstanceActivities.containsKey(cls)) {
            activities.add(activity)
            return
        }
        val flag: Int? =  singleInstanceActivities.get(cls)
        when (flag) {
            FLAG_CLEAR_TOP -> {
                var oldPos = -1
                var i = 0
                while (i < activities.size) {
                    if (cls == activities.get(i).javaClass) {
                        oldPos = i
                    }
                    i++
                }
                if (oldPos >= 0) {
                    var i: Int = activities.size - 1
                    while (i >= oldPos) {
                        val top: Activity = activities.get(i)
                        if (!top.isFinishing) {
                            top.finish()
                        }
                        i--
                    }
                }
            }
            FLAG_CLEAR_OLD -> {
                var i: Int = activities.size - 1
                while (i >= 0) {
                    val old: Activity = activities.get(i)
                    if (cls == old.javaClass && !old.isFinishing) {
                        old.finish()
                    }
                    i--
                }
            }
            else -> throw UnsupportedOperationException("Flag not find")
        }
        activities.add(activity)
        L.i(activities)
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activities.isEmpty()) {
            return
        }
        if ( activities.contains(activity)) {
            activities.remove(activity)
        }
      ///  L.i(activities)

    }
    open fun registerActivityListener() {
        registerActivityLifecycleCallbacks(this)
    }
}