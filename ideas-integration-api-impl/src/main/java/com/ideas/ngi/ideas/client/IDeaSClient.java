package com.ideas.ngi.ideas.client;

import com.ideas.ngi.ideas.client.reservation.IDeaSReservationV1ApiClient;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum IDeaSClient {

    RESERVATION_CLIENT(IDeaSDataType.RESERVATION, IDeaSReservationV1ApiClient.class);

    private final IDeaSDataType iDeaSDataType;
    private final Class<?> clazz;

    public IDeaSDataType getIDeaSDataType() {
        return iDeaSDataType;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    // Reverse-lookup map for getting a day from an abbreviation
    private static final Map<IDeaSDataType, IDeaSClient> LOOKUP;

    static {
        LOOKUP = Arrays.stream(IDeaSClient.values())
                .collect(Collectors.toMap(
                        IDeaSClient::getIDeaSDataType,
                        Function.identity(),
                        (left, right) -> {
                            String errorMsg = String.format("Duplicate keys: %s and %s", left.getIDeaSDataType(),
                                    right.getIDeaSDataType());
                            throw new IllegalArgumentException(errorMsg);
                        },
                        () -> new EnumMap<>(IDeaSDataType.class)
                ));
    }

    IDeaSClient(IDeaSDataType iDeaSDataType, Class<?> clazz) {
        this.iDeaSDataType = iDeaSDataType;
        this.clazz = clazz;
    }

    public static IDeaSClient get(IDeaSDataType getIDeaSDataType) {
        return LOOKUP.get(getIDeaSDataType);
    }
}
