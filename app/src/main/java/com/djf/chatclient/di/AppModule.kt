package com.djf.chatclient.di

import android.content.Context
import com.djf.chatclient.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.offline.plugin.factory.StreamOfflinePluginFactory
import io.getstream.chat.android.state.plugin.config.StatePluginConfig
import io.getstream.chat.android.state.plugin.factory.StreamStatePluginFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

class AppModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object RetrofitModule {
        @Singleton
        @Provides
        fun provideApplicationContext(@ApplicationContext context: Context): Context {
            return context
        }
        @Singleton
        @Provides
        fun provideChatClient(applicationContext: Context): ChatClient {
            val offlinePluginFactory = StreamOfflinePluginFactory(appContext = applicationContext)
            val statePluginFactory =
                StreamStatePluginFactory(
                    config = StatePluginConfig(),
                    appContext = applicationContext
                )

            // 2 - Set up the client for API calls and with the plugin for offline storage
            return ChatClient.Builder(com.djf.chatclient.BuildConfig.API_KEY, applicationContext)
                .withPlugins(offlinePluginFactory, statePluginFactory)
                .logLevel(ChatLogLevel.ALL) // Set to NOTHING in prod
                .build()
        }


        @Provides
        fun provideBaseUrl(): String = "https://chat-backend-latest.onrender.com"

        @Provides
        @Singleton
        fun provideRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideApiService(retrofit: Retrofit): ApiService =
            retrofit.create(ApiService::class.java)

    }
}