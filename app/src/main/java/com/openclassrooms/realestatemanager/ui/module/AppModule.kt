package com.openclassrooms.realestatemanager.ui.module

import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateDatabase
import com.openclassrooms.realestatemanager.database.dao.EstateDao
import com.openclassrooms.realestatemanager.database.dao.ImageDao
import com.openclassrooms.realestatemanager.database.dao.LocationDao
import com.openclassrooms.realestatemanager.repositories.EstateDataRepository
import com.openclassrooms.realestatemanager.repositories.ImageDataRepository
import com.openclassrooms.realestatemanager.repositories.LocationDataRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideAppDatabase (@ApplicationContext appContext : Context) : RealEstateDatabase {
        return RealEstateDatabase.getInstance(appContext)
    }

    @Provides
    fun provideEstateDao(realEstateDatabase: RealEstateDatabase): EstateDao = realEstateDatabase.estateDao()

    @Provides
    fun provideImageDao(realEstateDatabase: RealEstateDatabase): ImageDao = realEstateDatabase.imageDao()

    @Provides
    fun provideLocationDao(realEstateDatabase: RealEstateDatabase): LocationDao = realEstateDatabase.locationDao()

    @Singleton
    @Provides
    fun provideEstateRepository(localDataSource: EstateDao) = EstateDataRepository(localDataSource)

    @Singleton
    @Provides
    fun provideImageRepository(localDataSource: ImageDao) = ImageDataRepository(localDataSource)

    @Singleton
    @Provides
    fun provideLocationRepository(localDataSource: LocationDao) = LocationDataRepository(localDataSource)
}