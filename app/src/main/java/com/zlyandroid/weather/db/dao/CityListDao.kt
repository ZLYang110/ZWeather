package com.zlyandroid.weather.db.dao

import androidx.room.*
import com.zlyandroid.weather.db.model.CityModel

/**
 * @author zhangliyang
 * @date 2020/12/15
 * GitHub: https://github.com/ZLYang110
 * desc:爱好标签 Dao
 */

@Dao
interface CityListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg mode: CityModel)

    @Update
    suspend fun updata(mode: CityModel)

    @Query("DELETE FROM CityModel WHERE  cityName= :cityName")
    suspend fun delete(cityName: String)

    @Query("DELETE FROM CityModel")
    suspend fun deleteAll()

    @Query("SELECT * FROM CityModel WHERE  cityName= :cityName")
    suspend fun findById(cityName: String): CityModel

    @Query("SELECT * FROM CityModel WHERE  isSelect= :isSelect ORDER BY addTime ASC")
    suspend fun findBySelect(isSelect: Boolean): List<CityModel>

    @Query("SELECT * FROM CityModel ORDER BY time DESC LIMIT (:offset), (:count)")
    suspend fun findAll(offset: Int, count: Int): List<CityModel>

    @Query("SELECT * FROM CityModel ORDER BY time ASC")
    suspend fun findAll(): List<CityModel>
}