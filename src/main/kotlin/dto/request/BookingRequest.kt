package org.example.dto.request

import java.time.LocalDate

data class BookingRequest(
    val firstname: String,
    val lastname: String,
    val totalprice: Int,
    val depositpaid: Boolean,
    val bookingdates: BookingDates,
    val additionalneeds: String?,
)
data class BookingDates(
    val checkin: LocalDate,
    val checkout: LocalDate
)