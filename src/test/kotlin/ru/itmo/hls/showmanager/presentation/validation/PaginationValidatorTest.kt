package ru.itmo.hls.showmanager.presentation.validation

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PaginationValidatorTest {
    private val validator = PaginationValidator()

    @Test
    fun `validateSize accepts valid size`() {
        validator.validateSize(10)
    }

    @Test
    fun `validateSize rejects invalid size`() {
        assertThrows(IllegalArgumentException::class.java) {
            validator.validateSize(0)
        }
    }
}
