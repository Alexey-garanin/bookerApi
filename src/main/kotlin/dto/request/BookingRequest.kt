package org.example.dto.request

import com.fasterxml.jackson.annotation.JsonInclude

data class BookingRequest(
    val firstname: String,
    val lastname: String,
    val totalprice: Int,
    val depositpaid: Boolean,
    val bookingdates: BookingDates,
    val additionalneeds: String?,
)
data class BookingDates(
    val checkin: String,
    val checkout: String
)
@JsonInclude(JsonInclude.Include.NON_NULL)
data class BookingPatchRequest(
    val firstname: String? = null,
    val lastname: String? = null
)