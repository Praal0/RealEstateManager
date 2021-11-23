package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils.*
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilTest {

    @Test
    fun convertDollarToEuroTest() {
        val dollars = 100
        val euros: Int = convertDollarToEuro(dollars)
        Assert.assertEquals(81, euros)
    }

    @Test
    fun convertEuroToDollarTest() {
        val euros = 81
        val dollars: Int = convertEurosToDollar(euros)
        Assert.assertEquals(95, dollars)
    }

   @Test
   fun getTodayDateTest(){
       val sdf = SimpleDateFormat("dd/M/yyyy")
       val currentDateTest = sdf.format(Date())
       val date = getTodayDate()
       Assert.assertEquals(currentDateTest, date)
   }


}