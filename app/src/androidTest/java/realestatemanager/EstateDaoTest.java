package realestatemanager;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.realestatemanager.database.RealEstateDatabase;
import com.openclassrooms.realestatemanager.models.Estate;
import com.openclassrooms.realestatemanager.models.Location;
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

    private static Location LOCATION_HOUSE = new Location(1,0L,0L,"11 street de senter","Lyon","69000");
    private static Location LOCATION_FLAT = new Location(1,0L,0L,"11 street de senter","Lyon","69000");

    private static Estate ESTATE_HOUSE = new Estate(1L, "house", 200, 4, 2, 1, 200, 100000.00, "Très belle maison", true, false,
            false, true, true, "23/01/2021","", "Karine Danjard",uriListTest,descriptionTest,uriListTest,LOCATION_HOUSE);

    private static Estate ESTATE_FLAT = new Estate(2L, "flat", 80, 2, 1, 1, 0, 50000.00, "Very nice flat", false, true,
            true, true, true,"23/01/2021","","John Doe", uriListTest,descriptionTest,uriListTest,LOCATION_FLAT);

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
        estateDatabase.estateDao().insertEstateTest(ESTATE_HOUSE);
        estateDatabase.estateDao().insertEstateTest(ESTATE_FLAT);
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
