package ru.itmo.hls.showmanager.presentation.rest

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import ru.itmo.hls.showmanager.domain.exception.ShowNotFoundException

class RestExceptionHandlerTest {
    private val handler = RestExceptionHandler()

    @Test
    fun `handleShowNotFound returns 404`() {
        val response = handler.handleShowNotFound(ShowNotFoundException("missing"))

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertEquals("missing", response.body?.get("message"))
    }

    @Test
    fun `handleBadRequest returns 400`() {
        val response = handler.handleBadRequest(IllegalArgumentException("bad"))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("bad", response.body?.get("message"))
    }
}
