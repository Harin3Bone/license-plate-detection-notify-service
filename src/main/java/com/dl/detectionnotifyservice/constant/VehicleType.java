package com.dl.detectionnotifyservice.constant;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum VehicleType {
    CAR("Car"),
    TRUCK("Truck"),
    MOTORCYCLE("Motorcycle"),
    BUS("Bus"),
    OTHER("Other");

    private final String value;

    VehicleType(String value) {
        this.value = value;
    }

    private static final Map<String, VehicleType> VEHICLE_TYPE_MAP = new HashMap<>();

    static {
        for (VehicleType vt : VehicleType.values()) {
            VEHICLE_TYPE_MAP.put(vt.name().toLowerCase(), vt);
        }
    }

    public static boolean isValidType(String type) {
        return type != null && VEHICLE_TYPE_MAP.get(type.toLowerCase()) != null;
    }

    public static VehicleType fromString(String type) {
        if (type != null) {
            VehicleType vt = VEHICLE_TYPE_MAP.get(type.toLowerCase());
            if (vt != null) {
                return vt;
            }
        }
        return OTHER; // Default to OTHER if no match found
    }
}
