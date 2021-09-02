package com.openclassrooms.realestatemanager.ui.module

import android.app.Application
import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.database.dao.PictureDao
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.PictureDataRepository
import com.openclassrooms.realestatemanager.repositories.LocationDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@HiltAndroidApp
@Module
@InstallIn(SingletonComponent::class)
class AppModule  : Application() {
    @Provides
    @Singleton
    fun provideAppDatabase (@ApplicationContext appContext : Context) : RealEstateDatabase {
        return RealEstateDatabase.getInstance(appContext)
    }

    @Provides
    fun provideEstateDao(realEstateDatabase: RealEstateDatabase): EstateDao = realEstateDatabase.estateDao()

    @Provides
    fun provideImageDao(realEstateDatabase: RealEstateDatabase): PictureDao = realEstateDatabase.pictureDao()

    @Provides
    fun provideLocationDao(realEstateDatabase: RealEstateDatabase): LocationDao = realEstateDatabase.locationDao()

    @Singleton
    @Provides
    fun provideEstateRepository(localDataSource: EstateDao) = EstateDataRepository(localDataSource)

    @Singleton
    @Provides
    fun provideImageRepository(localDataSource: PictureDao) = PictureDataRepository(localDataSource)

    @Singleton
    @Provides
    fun provideLocationRepository(localDataSource: LocationDao) = LocationDataRepository(localDataSource)
}