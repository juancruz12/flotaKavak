package com.kavak.flota.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum Estado {
    COMPLETADO(new HashSet<>()), // No puede transicionar a ningún estado
    CANCELADO(new HashSet<>()), // No puede transicionar a ningún estado
    EN_PROCESO(new HashSet<>(Arrays.asList(Estado.COMPLETADO, Estado.CANCELADO))),
    PENDIENTE(new HashSet<>(Arrays.asList(Estado.EN_PROCESO, Estado.CANCELADO)));


    private final Set<Estado> estadosPermitidos;

    Estado(Set<Estado> estadosPermitidos) {
        this.estadosPermitidos = estadosPermitidos;
    }

    /**
     * Verifica si es posible transicionar desde el estado actual a otro estado
     * @param nuevoEstado Estado destino
     * @return true si la transición es válida
     */
    public boolean puedeTransicionarA(Estado nuevoEstado) {
        return estadosPermitidos.contains(nuevoEstado);
    }

    /**
     * Obtiene el conjunto de estados a los que puede transicionar
     * @return Set con estados permitidos
     */
    public Set<Estado> getEstadosPermitidos() {
        return new HashSet<>(estadosPermitidos);
    }

    /**
     * Verifica si el estado es terminal (no puede cambiar)
     * @return true si es COMPLETADO o CANCELADO
     */
    public boolean esTerminal() {
        return this == COMPLETADO || this == CANCELADO;
    }

    /**
     * Verifica si el estado es activo (no finalizado)
     * @return true si es PENDIENTE o EN_PROCESO
     */
    public boolean esActivo() {
        return this == PENDIENTE || this == EN_PROCESO;
    }

    /**
     * Obtiene la lista de estados activos (no finalizados)
     * Estados activos: PENDIENTE, EN_PROCESO
     * @return Lista de estados activos
     */
    public static List<Estado> getEstadosActivos() {
        return Arrays.asList(PENDIENTE, EN_PROCESO);
    }
}

