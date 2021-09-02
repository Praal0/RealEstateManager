package com.openclassrooms.realestatemanager

import com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro
import com.openclassrooms.realestatemanager.utils.Utils.convertEurosToDollar
import org.junit.Assert
import org.junit.Test

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

}