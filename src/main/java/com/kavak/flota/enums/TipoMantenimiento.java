package com.kavak.flota.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TipoMantenimiento {
    CAMBIO_ACEITE,
    FRENOS,
    MOTOR,
    LLANTAS,
    TRANSMISION,
    GENERAL;

    /**
     * Obtener lista de todos los valores permitidos como strings
     */
    public static List<String> getValoresPermitidos() {
        return Arrays.stream(TipoMantenimiento.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}

