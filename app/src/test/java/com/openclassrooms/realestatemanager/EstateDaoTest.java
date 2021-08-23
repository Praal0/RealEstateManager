package com.openclassrooms.realestatemanager;


import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.model.Estate;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class EstateDaoTest {

    //For Data
    private RealEstateDatabase estateDatabase;
    //DATA SET for test
    private static long MANDATE_NUMBER_ID = 1;


    private static Estate ESTATE_HOUSE = new Estate(1, "house", 100000.00,
            50000, 2, 1, 2, "true",
            true, true,false,false,"false",null
            ,null,"John Doe");
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        try {
            this.estateDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                    RealEstateDatabase.class)
                    .allowMainThreadQueries()
                    .build();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    @After
    public void closeDb() throws Exception {
        estateDatabase.close();
    }

    @Test
    public void insertAndGetEstate() throws InterruptedException {
        //adding demo
        this.estateDatabase.estateDao().insertEstate(ESTATE_HOUSE);
        //test
        List<Estate> estateList = LiveDataTestUtil.getValue(this.estateDatabase.estateDao().getEstates());
        assertEquals(1, estateList.size());
    }

}
