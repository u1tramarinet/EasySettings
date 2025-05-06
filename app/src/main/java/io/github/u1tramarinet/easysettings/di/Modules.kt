package io.github.u1tramarinet.easysettings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.u1tramarinet.easysettings.infra.SystemSettingHandler
import io.github.u1tramarinet.easysettings.infra.SystemSettingHandlerImpl

@Module
@InstallIn(SingletonComponent::class)
interface Modules {
    @Binds
    fun bindSystemSettingHandler(impl: SystemSettingHandlerImpl): SystemSettingHandler
}