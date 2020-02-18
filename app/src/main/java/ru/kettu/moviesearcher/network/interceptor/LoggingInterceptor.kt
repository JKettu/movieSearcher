package ru.kettu.moviesearcher.network.interceptor

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

object LoggingInterceptor {
    private val loggingInterceptor = HttpLoggingInterceptor(Logger())

    fun getLoggerInterceptor(): HttpLoggingInterceptor {
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    class Logger: HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            val logName = "OkHttp"
            if (!message.startsWith("{")) {
                Log.i(logName, message)
                return
            }
            try {
                val prettyPrintJson = GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(JsonParser().parse(message))
                Log.i(logName, prettyPrintJson)
            } catch (m: JsonSyntaxException) {
                Log.e(logName, message)
            }
        }
    }
}