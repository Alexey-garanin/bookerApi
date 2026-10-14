package org.example.dto.response

import org.example.dto.request.BookingRequest

data class BookingResponse(
    val bookingid: Long,
    val booking: BookingRequest
)

data class BookingIdResponse(
    val bookingid: Long
)