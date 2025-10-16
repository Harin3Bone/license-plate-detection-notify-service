package com.dl.detectionnotifyservice.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtil {

    private static final String NOTIFY_MSG_TEMPLATE = "พาหนะป้ายทะเบียน '%s' ถูกตรวจพบว่ามีการจอดในพื้นที่ผิดกฏหมาย ณ สถานที่ '%s' เวลา %s";
    private static final String ADDRESS_PLACEHOLDER = "%s %s %s %s";

    public static String buildNotifyMessage(
            String licensePlate, String address, String subDistrict,
            String district, String province, ZonedDateTime notifyTime,
            Clock systemClock
    ) {
        // Format address from camera details
        String formatSubDistrict;
        String formatDistrict;
        if ("กรุงเทพมหานคร".equals(province)) {
            formatSubDistrict = "แขวง" + subDistrict;
            formatDistrict = "เขต" + district;
        } else {
            formatSubDistrict = "ตำบล" + subDistrict;
            formatDistrict = "อำเภอ" + district;
        }
        String formatAddress = String.format(ADDRESS_PLACEHOLDER, address, formatSubDistrict, formatDistrict, province);

        // Clean up license plate string
        String formatLicensePlate = licensePlate
                .replaceAll("[\\[\\],']", "")
                .trim();

        // Format notification time
        String formatTime = DateFormatUtil.zonedDateTimeToString(notifyTime, systemClock.getZone());

        return String.format(NOTIFY_MSG_TEMPLATE, formatLicensePlate, formatAddress, formatTime);
    }
}
