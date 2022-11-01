package com.example.couponstohospitalbot.telegram.keyboards;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Shortener {
    static Set<String> optionalHospWords = new HashSet<>(Arrays.asList("городская", "городской", "поликлиническое", "спб", "гбуз",
            "невского", "петроградского", "московского", "кронштадтского", "адмиралтейского", "василеостровского",
            "выборгского", "калининского", "кировского", "колпинского", "красногвардейского", "красносельского",
            "курортного", "приморского", "пушкинского", "фрунзенского", "центрального", "петродворцового", "района"));

    public static String shortHospName(String hospName) {
        String[] arr1 = hospName.split("\"");
        if (arr1.length == 2 && !arr1[1].equals("") || arr1.length == 3 && arr1[2].equals("")) {
            if (arr1[1].getBytes().length <= 64) {
                return arr1[1];
            }
            return shortestHospName(arr1[1]);
        }
        return shortestHospName(hospName);
    }


    private static String shortestHospName(String hospName) {
        hospName = hospName.toLowerCase(Locale.ROOT).trim().replace("\"", "");
        String[] arr = hospName.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String i : arr) {
            if (optionalHospWords.contains(i)) {
                continue;
            }
            sb.append(i).append(" ");
        }
        return sb.toString();
    }


    public static String shortDirectionName(String directionName) {
        return substringToBytes(directionName, 64); //доработать
    }

    public static String shortDoctorName(String doctorName, int maxBytes) {
        String[] array = doctorName.split(" ");
        StringBuilder sb = new StringBuilder();
        if (array.length == 3) {
            sb.append(array[0]).append(array[1].charAt(0)).append(".").append(array[2].charAt(0)).append(".");
            if (sb.toString().getBytes().length <= maxBytes) {
                return sb.toString();
            }
            return substringToBytes(sb.toString(), maxBytes);
        }
        return substringToBytes(doctorName, maxBytes);
    }

    private static String substringToBytes(String name, int maxBytes) {
        int b = 0;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            int skip = 0;
            int more;
            if (c <= 0x007f) {
                more = 1;
            }
            else if (c <= 0x07FF) {
                more = 2;
            } else if (c <= 0xd7ff) {
                more = 3;
            } else if (c <= 0xDFFF) {
                more = 4;
                skip = 1;
            } else {
                more = 3;
            }

            if (b + more > maxBytes) {
                return name.substring(0, i);
            }
            b += more;
            i += skip;
        }
        return name;
    }
}
