package com.jasonzou.retrofitdemo.eventbus;

import com.jasonzou.retrofitdemo.BuildConfig;

import org.greenrobot.eventbus.EventBus;

/**
 * 项目:  RetrofitDemo <br>
 * 类名:  com.jasonzou.retrofitdemo.eventbus.EventBusMaster<br>
 * 描述:  EventBus配置，<br>
 *        配置EventBus.getDefault()返回的实例<br>
 * 创建人: jasonzou<br>
 * 创建时间: 2018/9/29 10:13<br>
 */
public class EventBusMaster {
    private static boolean isInited = false;

    /**
     * 在Application中调用
     */
    public static void init() {
        if (isInited) {
            return;
        }else {
            /*debug版本会直接崩溃*/
            //配置默认实例:EventBus.getDefault()
            EventBus.builder()
                    .addIndex(new MyEventBusIndex())
                    .throwSubscriberException(BuildConfig.DEBUG)//事件处理过程中出现异常直接throw，不catch
                    .logNoSubscriberMessages(false)
                    .sendNoSubscriberEvent(false)
                    .installDefaultEventBus();
            isInited = true;
        }
    }
}
