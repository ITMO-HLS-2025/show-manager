package ru.itmo.hls.showmanager.dto

data class SeatRawDto(
    val row: Int,
    val seats: List<SeatStatusDto>
)

data class SeatStatusDto(
    val seatId: Long,
    val number: Int,
    val status: String
)
