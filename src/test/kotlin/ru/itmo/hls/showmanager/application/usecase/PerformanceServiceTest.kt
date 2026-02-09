package ru.itmo.hls.showmanager.application.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import ru.itmo.hls.showmanager.domain.model.Performance

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = ["/sql/cleanup.sql", "/sql/show_data.sql"])
class PerformanceServiceTest {
    @Autowired
    private lateinit var performanceService: PerformanceService

    @Test
    fun `findById returns performance`() {
        val performance = performanceService.findById(1)

        assertNotNull(performance)
        assertEquals("Hamlet", performance?.title)
    }

    @Test
    @Sql(scripts = ["/sql/cleanup.sql"])
    fun `save stores performance`() {
        val saved = performanceService.save(
            Performance(
                title = "New Performance",
                description = "Desc",
                durationMinutes = 80
            )
        )

        assertNotNull(saved.id)
        assertEquals("New Performance", performanceService.findById(saved.id)?.title)
    }
}
