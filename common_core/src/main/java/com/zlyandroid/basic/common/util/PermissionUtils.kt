package com.zlyandroid.basic.common.util

import android.Manifest
import android.content.Context
import com.zlylib.mypermissionlib.MyPermission
import com.zlylib.mypermissionlib.RequestListener
import com.zlylib.mypermissionlib.RuntimeRequester


/**
 * @author zhangliyang
 * @date 2020/11/17
 * GitHub: https://github.com/ZLYang110
 */
object PermissionUtils {

    object PermissionGroup {
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        val PERMISSIONS_CAME = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }


    fun request(
        listener: RequestListener?,
        context: Context?,
        vararg permissions: String
    ): RuntimeRequester? {
        return MyPermission.with(context!!)
            .runtime(1)
            .permissions(*permissions)
            .onBeforeRequest { data, executor -> // TODO 在每个权限申请之前调用，多次回调。可弹窗向用户说明下面将进行某个权限的申请。
                executor.execute()
            }
            .onBeenDenied { data, executor -> // TODO 在每个权限被拒后调用，多次回调。可弹窗向用户说明为什么需要该权限，否则用户可能在下次申请勾选不再提示。
                executor.execute()
            }
            .onGoSetting { data, executor -> // TODO 在每个权限永久被拒后调用（即用户勾选不再提示），多次回调。可弹窗引导用户前往设置打开权限，调用executor.execute()会自动跳转设置。
                executor.execute()
            }
            .request(listener!!)
    }

}