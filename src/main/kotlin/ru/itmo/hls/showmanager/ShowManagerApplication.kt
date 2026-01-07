package ru.itmo.hls.showmanager

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class ShowManagerApplication

fun main(args: Array<String>) {
    runApplication<ShowManagerApplication>(*args)
}
