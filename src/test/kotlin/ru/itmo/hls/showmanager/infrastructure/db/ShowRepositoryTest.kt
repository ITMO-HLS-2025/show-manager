package ru.itmo.hls.showmanager.infrastructure.db

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = ["/sql/cleanup.sql", "/sql/show_data.sql"])
class ShowRepositoryTest {
    @Autowired
    private lateinit var showRepository: ShowJpaRepository

    @Test
    fun `findShowByIdFetch returns show with performance`() {
        val show = showRepository.findShowByIdFetch(10)

        assertNotNull(show)
        assertEquals("Hamlet", show?.performance?.title)
    }

    @Test
    fun `findAllByTheatreId returns matching shows`() {
        val now = LocalDateTime.now()
        val end = now.plusDays(14)

        val shows = showRepository.findAllByTheatreId(100, now, end)

        assertEquals(1, shows.size)
        assertEquals(10, shows.first().id)
    }
}
