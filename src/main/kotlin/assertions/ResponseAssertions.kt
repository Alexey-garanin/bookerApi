package org.example.assertions

import io.restassured.common.mapper.TypeRef
import io.restassured.response.Response
import org.assertj.core.api.Assertions.assertThat
import io.restassured.response.ValidatableResponse

fun Response.expectStatus(expected: Int, description: String = ""): Response {
    val desc = if (description.isNotBlank()) "$description: " else ""
    assertThat(this.statusCode)
        .describedAs("${desc}expected $expected, but got ${this.statusCode}")
        .isEqualTo(expected)
    return this
}

inline fun <reified T> Response.expectStatusAndParse(expectedStatus: Int, description: String = ""): T {
    this.expectStatus(expectedStatus, description)
    return this.`as`(T::class.java)
}

inline fun <reified T> Response.expectStatusAndParseList(
    status: Int,
    description: String = ""
): T {
    val desc = if (description.isNotBlank()) "$description: " else ""
    assertThat(this.statusCode)
        .describedAs("${desc}expected $status, but got ${this.statusCode}")
        .isEqualTo(status)
    return this.`as`(object : io.restassured.common.mapper.TypeRef<T>() {})
}