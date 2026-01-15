package ru.itmo.hls.showmanager

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@OpenAPIDefinition(info = Info(title = "Show Manager API", version = "v1"))
@SpringBootApplication
@EnableFeignClients
class ShowManagerApplication

fun main(args: Array<String>) {
    runApplication<ShowManagerApplication>(*args)
}
