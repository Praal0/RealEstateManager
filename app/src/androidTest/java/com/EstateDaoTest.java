package com;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.PhotoDescription;
import com.openclassrooms.realestatemanager.models.UriList;

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

    private static UriList uriListTest = new UriList();
    private static PhotoDescription descriptionTest = new PhotoDescription();

    private static Estate ESTATE_HOUSE = new Estate(1,1, "house", 200, 4, 2, 1, 200, 100000.00, "Très belle maison", "2 rue du Pont", 66000, "Perpignan", true, false,
            false, true, true, 1601510400000L,"", "Karine Danjard",uriListTest,descriptionTest,uriListTest);

    private static Estate ESTATE_FLAT = new Estate(2,2, "flat", 80, 2, 1, 1, 0, 50000.00, "Very nice flat", "5 rue longue", 66000, "Perpignan", false, true,
            true, true, true,1601510400000L,"","John Doe", uriListTest,descriptionTest,uriListTest);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
       estateDatabase = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
               RealEstateDatabase.class)
               .allowMainThreadQueries()
               .build();
    }

    @After
    public void closeDatabase() {
        this.estateDatabase.close();
    }

    @Test
    public void insertAndGetEstate() throws InterruptedException {
        //adding demo
        //estateDatabase.estateDao().insertEstate(ESTATE_HOUSE);
        //estateDatabase.estateDao().insertEstate(ESTATE_FLAT);
        //test
        List<Estate> estateList = LiveDataTestUtil.getValue(this.estateDatabase.estateDao().getEstates());
        assertEquals(2, estateList.size());
    }

    @Test
    public void getEstateWhenNoItemInserted() throws InterruptedException {
        //test
        List<Estate> estatesList = LiveDataTestUtil.getValue(this.estateDatabase.estateDao().getEstates());
        assertTrue(estatesList.isEmpty());
    }

}
