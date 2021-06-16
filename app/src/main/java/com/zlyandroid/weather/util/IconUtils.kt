package com.zlyandroid.weather.util

import com.zlyandroid.weather.R

object IconUtils {

    fun getDayIconDark(weather: String?): Int {
        val imageId: Int
        imageId = when (weather) {
            "100" -> R.mipmap.icon_100d
            "101" -> R.mipmap.icon_101d
            "102" -> R.mipmap.icon_102d
            "103" -> R.mipmap.icon_103d
            "104" -> R.mipmap.icon_104d
            "150" -> R.mipmap.icon_150d
            "153" -> R.mipmap.icon_153d
            "154" -> R.mipmap.icon_154d
            "300" -> R.mipmap.icon_300d
            "301" -> R.mipmap.icon_301d
            "302" -> R.mipmap.icon_302d
            "303" -> R.mipmap.icon_303d
            "304" -> R.mipmap.icon_304d
            "305" -> R.mipmap.icon_305d
            "306" -> R.mipmap.icon_306d
            "307" -> R.mipmap.icon_307d
            "308" -> R.mipmap.icon_308d
            "309" -> R.mipmap.icon_309d
            "310" -> R.mipmap.icon_310d
            "311" -> R.mipmap.icon_311d
            "312" -> R.mipmap.icon_312d
            "313" -> R.mipmap.icon_313d
            "314" -> R.mipmap.icon_314d
            "315" -> R.mipmap.icon_315d
            "316" -> R.mipmap.icon_316d
            "317" -> R.mipmap.icon_317d
            "318" -> R.mipmap.icon_318d
            "350" -> R.mipmap.icon_350d
            "351" -> R.mipmap.icon_351d
            "399" -> R.mipmap.icon_399d
            "400" -> R.mipmap.icon_400d
            "401" -> R.mipmap.icon_401d
            "402" -> R.mipmap.icon_402d
            "403" -> R.mipmap.icon_403d
            "404" -> R.mipmap.icon_404d
            "405" -> R.mipmap.icon_405d
            "406" -> R.mipmap.icon_406d
            "407" -> R.mipmap.icon_407d
            "408" -> R.mipmap.icon_408d
            "409" -> R.mipmap.icon_409d
            "410" -> R.mipmap.icon_410d
            "546" -> R.mipmap.icon_456d
            "457" -> R.mipmap.icon_457d
            "499" -> R.mipmap.icon_499d
            "500" -> R.mipmap.icon_500d
            "501" -> R.mipmap.icon_501d
            "502" -> R.mipmap.icon_502d
            "503" -> R.mipmap.icon_503d
            "504" -> R.mipmap.icon_504d
            "507" -> R.mipmap.icon_507d
            "508" -> R.mipmap.icon_508d
            "509" -> R.mipmap.icon_509d
            "510" -> R.mipmap.icon_510d
            "511" -> R.mipmap.icon_511d
            "512" -> R.mipmap.icon_512d
            "513" -> R.mipmap.icon_513d
            "514" -> R.mipmap.icon_514d
            "515" -> R.mipmap.icon_515d
            "900" -> R.mipmap.icon_900d
            "901" -> R.mipmap.icon_901d
            "999" -> R.mipmap.icon_999d
            else -> R.mipmap.icon_100d
        }
        return imageId
    }


}