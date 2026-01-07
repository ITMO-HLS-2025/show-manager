package ru.itmo.hls.showmanager.validation

import org.springframework.stereotype.Component

@Component
class PaginationValidator {
    fun validateSize(pageSize: Int) {
        require(pageSize in 1..100) { "Invalid page size" }
    }
}
