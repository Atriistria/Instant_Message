package com.example.instant_message.domain.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

object DebounceHelper {
    private var lastClickTimes = mutableMapOf<String, Long>()
    private const val DEFAULT_CLICK_INTERVAL = 1000L // 1 second
    private val cleanupJob: Job

    init {
        cleanupJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(10 * 60 * 1000L)
                cleanExpiredClicks()
            }
        }
    }

    /**
     * 判断是否允许执行某个动作
     * @param key 动作的唯一标识
     * @param interval 动作允许执行的最小间隔时间，单位为毫秒
     * @param onProceed 允许执行时执行的回调函数
     * @param onDebounce 动作执行时执行的回调函数
     */
    fun shouldProceed(
        key: String,
        interval: Long = DEFAULT_CLICK_INTERVAL,
        onProceed: () -> Unit,
        onDebounce: (() -> Unit)? = null
    ){
        val currentTime = System.currentTimeMillis()
        val lastClickTime = lastClickTimes[key] ?: 0
        if (currentTime - lastClickTime >= interval){
            lastClickTimes[key] = currentTime
            onProceed()
        } else {
            onDebounce?.invoke()
        }
    }

    //手动清除过期的点击记录
    private fun cleanExpiredClicks() {
        val expirationTime = System.currentTimeMillis() - (10 * 60 * 1000L)
        lastClickTimes.entries.removeIf { it.value < expirationTime }
    }

    fun release() {
        cleanupJob.cancel()
    }
}
