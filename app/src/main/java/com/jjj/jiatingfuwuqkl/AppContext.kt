package com.jjj.jiatingfuwuqkl

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Process
import com.jjj.jiatingfuwuqkl.api.ApiService
import com.parkingwang.okhttp3.LogInterceptor.LogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * App 上下文环境
 *
 *
 * Created by Tony on 9/30/15.
 */
class AppContext : Application() {
    private val PROTOCOL = "http://"
    //    ip地址或者 域名
    private val IP_DOMAIN = PROTOCOL + "192.168.1.41:80/"
    //    端口部分(可省略)：跟在域名后面的是端口，域名和端口之间使用“:”作为分隔符。端口不是一个URL必须的部分，如果省略端口部分，将采用默认端口
    private val HOST = IP_DOMAIN + "SwmSignPhp/public/index/"


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        handler = Handler()
        //  		import android.os.Process;  有两个包需要导入这个包
        mainThreadId = Process.myTid()
        //        初始化  网络 请求的
        initOkHttpAndRetrofit()
    }

    private fun initOkHttpAndRetrofit() {
        //        初始化  和 配置 okhttp
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(LogInterceptor())
                .connectTimeout(5L, TimeUnit.SECONDS)
                .readTimeout(5L, TimeUnit.SECONDS)
                //其他配置
                .build()
        //        配置 retrofit
        val retrofit = Retrofit.Builder()
                //                添加 okhttp
                .client(okHttpClient)
                //                基本网址
                .baseUrl(HOST)
                //                 请求数据 转换器 gson
                .addConverterFactory(GsonConverterFactory.create())
                //                下面的不太懂。。。
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        //        创建服务器接口， 通过 ApiService类实现
        apiService = retrofit.create(ApiService::class.java)
    }

    companion object {

        var handler: Handler? = null
        var mainThreadId: Int = 0
        var context: Context? = null
        //    返回 创建好的 ApiService
        var apiService: ApiService? = null
    }
}
