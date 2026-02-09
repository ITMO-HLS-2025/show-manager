package ru.itmo.hls.showmanager.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "performance")
class Performance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    var title: String,
    var description: String? = null,
    var durationMinutes: Int? = null,

    @OneToMany(mappedBy = "performance")
    var shows: MutableList<Show> = mutableListOf(),
) {
    constructor() : this(0, "", null, null)
}
