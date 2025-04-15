package com.p1nero.smc.util;

public class VersonCheck {
    public static boolean isGreaterThan(String version1, String version2) {
        String[] v1Parts = version1.split("\\.");
        String[] v2Parts = version2.split("\\.");

        int length = Math.max(v1Parts.length, v2Parts.length);
        for (int i = 0; i < length; i++) {
            int v1 = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2 = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;

            // 比较两个数字
            if (v1 > v2) {
                return true;
            } else if (v1 < v2) {
                return false;
            }
        }

        // 如果所有部分都相等，说明版本号相等
        return false;
    }
}
