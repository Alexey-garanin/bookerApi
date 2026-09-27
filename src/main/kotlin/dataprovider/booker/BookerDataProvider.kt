package org.example.dataprovider.booker

import org.example.dto.request.BookingDates
import org.example.dto.request.BookingRequest
import java.time.LocalDate
import kotlin.random.Random

object BookerDataProvider {

    private val LIST_FIRSTNAME =
        listOf("James", "Mary", "Michael", "Patricia", "John", "Jennifer", "Robert", "Linda", "David", "Elizabeth")
    private val LIST_LASTNAME =
        listOf("Smith", "Williams", "Brown", "Young", "Harris", "Anderson", "Martinez", "Scott", "Hill", "Gonzalez")
    private val LIST_ADDITIONALNEEDS =
        listOf("Breakfast", "Animal service", "Early check-in", "Late check-out", "Kid-friendly utensils", "Specific balcony view", null)


    fun createBookingInstance(): BookingRequest {
        val checkin = LocalDate.now()
        val checkout = checkin.plusDays(Random.nextLong(4, 8))
        return BookingRequest(
        firstname = LIST_FIRSTNAME.random(),
        lastname = LIST_LASTNAME.random(),
        totalprice = Random.nextInt(23, 101) * 100,
        depositpaid = Random.nextBoolean(),
            bookingdates = BookingDates(
                checkin = checkin,
                checkout = checkout
            ),
        additionalneeds = LIST_ADDITIONALNEEDS.random(),
    )
    }

}