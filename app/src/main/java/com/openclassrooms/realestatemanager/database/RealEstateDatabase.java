package com.openclassrooms.realestatemanager.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.database.dao.EstateDAO;
import com.openclassrooms.realestatemanager.database.dao.LocationDao;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.Location;

import java.util.concurrent.Executor;

@Database(entities = {Estate.class, Location.class}, version = 1, exportSchema = false)
public abstract class RealEstateDatabase extends RoomDatabase {

    // --- SINGLETON ---
    public static volatile RealEstateDatabase INSTANCE;

    // --- DAO ---
    public abstract EstateDAO estateDao();
    public abstract LocationDao locationDao();

    public static RealEstateDatabase getInstance(
            @NonNull Application application,
            @NonNull Executor ioExecutor
    ) {
        if (INSTANCE == null) {
            synchronized (RealEstateDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = create(application, ioExecutor);
                }
            }
        }
        return INSTANCE;
    }

    private static RealEstateDatabase create(Application application, Executor ioExecutor) {
        Builder<RealEstateDatabase> builder = Room.databaseBuilder(application, RealEstateDatabase.class, "RealEstateDatabase.db");
        builder.addCallback(new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                Log.d("DatabaseCreate","TOTO");
                ioExecutor.execute(() -> {
                EstateDAO estateDAO = RealEstateDatabase.getInstance(application, ioExecutor).estateDao();
                });
            }
        });
        return builder.build();
    }
}
