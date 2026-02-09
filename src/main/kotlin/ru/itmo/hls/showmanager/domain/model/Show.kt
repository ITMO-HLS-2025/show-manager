package ru.itmo.hls.showmanager.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "\"show\"")
class Show(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performance_id")
    var performance: Performance,

    @Column(name = "hall_id")
    var hallId: Long,

    @Column(name = "show_time")
    var showTime: LocalDateTime
) {
    constructor() : this(0, Performance(), 0, LocalDateTime.now())
}
