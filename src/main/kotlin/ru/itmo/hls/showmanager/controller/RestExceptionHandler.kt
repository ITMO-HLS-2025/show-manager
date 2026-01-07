package ru.itmo.hls.showmanager.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import ru.itmo.hls.showmanager.exception.ShowNotFoundException

@RestControllerAdvice
class RestExceptionHandler {
    @ExceptionHandler(ShowNotFoundException::class)
    fun handleShowNotFound(ex: ShowNotFoundException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("message" to ex.message.orEmpty()))

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("message" to ex.message.orEmpty()))
}
