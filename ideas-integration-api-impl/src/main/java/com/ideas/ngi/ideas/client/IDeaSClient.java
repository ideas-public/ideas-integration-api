package com.ideas.ngi.ideas.client;

import com.ideas.ngi.ideas.client.group.IDeaSGroupBlockMasterV1ApiClient;
import com.ideas.ngi.ideas.client.inventory.IDeaSInventoryV1ApiClient;
import com.ideas.ngi.ideas.client.reservation.IDeaSReservationV1ApiClient;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum IDeaSClient {

    GROUP_CLIENT(IDeaSDataType.GROUP, IDeaSGroupBlockMasterV1ApiClient.class),
    INVENTORY_CLIENT(IDeaSDataType.INVENTORY, IDeaSInventoryV1ApiClient.class),
    RESERVATION_CLIENT(IDeaSDataType.RESERVATION, IDeaSReservationV1ApiClient.class),
    ROOM_TYPE_CLIENT(IDeaSDataType.ROOM_TYPE, IDeaSReservationV1ApiClient.class),
    ROOM_TYPE_ACTIVITY_CLIENT(IDeaSDataType.ROOM_TYPE_ACTIVITY, IDeaSReservationV1ApiClient.class),
    ROOM_TYPE_MARKET_ACTIVITY_CLIENT(IDeaSDataType.ROOM_TYPE_MARKET_ACTIVITY, IDeaSReservationV1ApiClient.class),
    TOTAL_HOTEL_ACTIVITY_CLIENT(IDeaSDataType.TOTAL_HOTEL_ACTIVITY, IDeaSReservationV1ApiClient.class),;

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
