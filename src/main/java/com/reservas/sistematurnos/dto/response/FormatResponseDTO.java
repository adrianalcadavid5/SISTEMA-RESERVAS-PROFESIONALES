package com.reservas.sistematurnos.dto.response;

public record FormatResponseDTO<T>(
        String timeStamp,
        String message,
        Boolean encrypted,
        T data
) {
}
