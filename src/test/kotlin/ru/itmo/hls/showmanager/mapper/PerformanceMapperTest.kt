package ru.itmo.hls.showmanager.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.itmo.hls.showmanager.entity.Performance

class PerformanceMapperTest {
    @Test
    fun `toDto maps fields`() {
        val performance = Performance(
            id = 1,
            title = "Hamlet",
            description = "Desc",
            durationMinutes = 120,
            theatreIds = mutableSetOf(200, 201)
        )

        val dto = performance.toDto()

        assertEquals(1, dto.id)
        assertEquals("Hamlet", dto.title)
        assertEquals("Desc", dto.description)
        assertEquals(120, dto.durationMinutes)
        assertEquals(2, dto.theatreIds.size)
    }
}
