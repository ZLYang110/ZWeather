package com.zlyandroid.basic.common.base

 interface  Presenter {
    /**
     *
     */
    fun getLayoutID(): Int

    /**
     * UI显示方法(操作UI，但不存在数据获取或处理代码，也不存在事件监听代码)
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    fun initView()

    /**
     * Data数据方法(存在数据获取或处理代码，但不存在事件监听代码)
     * @must Activity-在子类onCreate方法内初始化View(setContentView)后调用；Fragment-在子类onCreateView方法内初始化View后调用
     */
    fun initData()

    /**
     * 是否存活(已启动且未被销毁)
     */
    fun isAlive(): Boolean

    /**
     * 是否在运行
     */
    fun isRunning(): Boolean

 }