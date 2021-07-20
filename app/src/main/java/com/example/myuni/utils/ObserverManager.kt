package com.example.myuni.utils

class ObserverManager : SubjectListener {
    /**
     * 观察者集合
     */
    private val list = ArrayList<ObserverListener>()

    override fun add(observerListener: ObserverListener) {
        // 加入队列
        list.add(observerListener)
    }

    override fun notifyObserver(count: String) {
        // 通知观察者刷新数据
        for (observerListener in list) {
            observerListener.observerUpData(count)
        }
    }

    override fun remove(observerListener: ObserverListener) {
        // 从监听队列删除
        list.remove(observerListener)
    }

    companion object {
        val instance: ObserverManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ObserverManager()
        }
    }
}

interface SubjectListener {
    /**
     * 添加
     */
    fun add(observerListener: ObserverListener)

    /**
     * 通知的内容
     */
    fun notifyObserver(count: String)

    /**
     * 删除
     */
    fun remove(observerListener: ObserverListener)
}

interface ObserverListener {
    /**
     * 刷新操作
     * @param count 传输的内容
     */
    fun observerUpData(count: String)
}