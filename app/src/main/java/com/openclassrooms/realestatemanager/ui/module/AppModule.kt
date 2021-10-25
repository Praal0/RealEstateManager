package com.openclassrooms.realestatemanager.ui.module

import android.app.Application
import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDAO
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.LocationDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule  : Application() {
    @Provides
    @Singleton
    fun provideAppDatabase (@ApplicationContext appContext : Context) : RealEstateDatabase {
        return RealEstateDatabase.getInstance(appContext as Application)
    }

    @Provides
    fun provideExecutor() = Executors.newFixedThreadPool(4)

    @Provides
    fun provideEstateDao(realEstateDatabase: RealEstateDatabase): EstateDAO = realEstateDatabase.estateDao()

    @Provides
    fun provideLocationDao(realEstateDatabase: RealEstateDatabase): LocationDao = realEstateDatabase.locationDao()

    @Singleton
    @Provides
    fun provideEstateRepository(localDataSource: EstateDAO) = EstateDataRepository(localDataSource)

    @Singleton
    @Provides
    fun provideLocationRepository(localDataSource: LocationDao) = LocationDataRepository(localDataSource)

}