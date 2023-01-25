package com.example.couponstohospitalbot.telegram.keyboards;

import net.sf.corn.httpclient.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ParsingJson {

    public static JSONArray getRegionsList() throws IOException, URISyntaxException {
        HttpClient client = new HttpClient(new URI("https://gorzdrav.spb.ru/_api/api/v2/shared/districts"));
        return new JSONObject(client.sendData(HttpClient.HTTP_METHOD.GET).getData()).getJSONArray("result");
    }

    //https://gorzdrav.spb.ru/_api/api/v2/shared/district/4/lpus
    public static JSONArray getHospitalList(String regionId) throws URISyntaxException, IOException {
        HttpClient client = new HttpClient(new URI("https://gorzdrav.spb.ru/_api/api/v2/shared/district/" + regionId + "/lpus"));
        return new JSONObject(client.sendData(HttpClient.HTTP_METHOD.GET).getData()).getJSONArray("result");
    }

    public static JSONArray getDirectionsList(int hospitalId) throws URISyntaxException, IOException {
        HttpClient client = new HttpClient(new URI("https://gorzdrav.spb.ru/_api/api/v2/schedule/lpu/" + hospitalId + "/specialties"));
        return new JSONObject(client.sendData(HttpClient.HTTP_METHOD.GET).getData()).getJSONArray("result");
    }

    public static JSONArray getDoctorsList(int hospitalId, String dirId) throws IOException, URISyntaxException {
        HttpClient client = new HttpClient(new URI("https://gorzdrav.spb.ru/_api/api/v2/schedule/lpu/" + hospitalId + "/speciality/" + dirId + "/doctors"));
        return new JSONObject(client.sendData(HttpClient.HTTP_METHOD.GET).getData()).getJSONArray("result");
    }

    public static String findRegionIdByName(String region) throws IOException, URISyntaxException {
        JSONArray arrayReg = getRegionsList();
        for (int i = 0; i < arrayReg.length(); i++) {
            if (arrayReg.getJSONObject(i).get("name").equals(region)) {
                return arrayReg.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    public static String findRegionNameById(String regionId) throws IOException, URISyntaxException {
        JSONArray arrayReg = getRegionsList();
        for (int i = 0; i < arrayReg.length(); i++) {
            if (arrayReg.getJSONObject(i).get("id").equals(regionId)) {
                return arrayReg.getJSONObject(i).get("name").toString();
            }
        }
        return null;
    }

    public static Integer findHospitalIdByName(String regionId, String hospName) throws URISyntaxException, IOException {
        JSONArray arrayHosp = getHospitalList(regionId);
        for (int i = 0; i < arrayHosp.length(); i++) {
            if (arrayHosp.getJSONObject(i).get("lpuFullName").equals(hospName)) {
                return (int) arrayHosp.getJSONObject(i).get("id");
            }
        }
        return -1;
    }

    public static String findHospitalNameById(String hospId, String regionId) throws URISyntaxException, IOException {
        Integer hospitalId = Integer.parseInt(hospId);
        JSONArray arrayHosp = getHospitalList(regionId);
        for (int i = 0; i < arrayHosp.length(); i++) {
            if (arrayHosp.getJSONObject(i).get("id").equals(hospitalId)) {
                return arrayHosp.getJSONObject(i).get("lpuFullName").toString();
            }
        }
        return null;
    }

    public static String findDirectionIdByName(int hospId, String direction) throws URISyntaxException, IOException {
        JSONArray arrayDir = getDirectionsList(hospId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("name").equals(direction)) {
                return arrayDir.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    public static String findDirectionNameById(String directionId, Integer hospitalId) throws URISyntaxException, IOException {
        JSONArray arrayDir = getDirectionsList(hospitalId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("id").equals(directionId)) {
                return arrayDir.getJSONObject(i).get("name").toString();
            }
        }
        return null;
    }

    public static String findDoctorIdByName(int hospId, String dirId, String nameDoctor) throws IOException, URISyntaxException {
        JSONArray arrayDir = getDoctorsList(hospId, dirId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("name").equals(nameDoctor)) {
                return arrayDir.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    public static String  findDoctorNameById(String doctorId, int hospitalId, String directionId) throws IOException, URISyntaxException {
        JSONArray arrayDoctor = getDoctorsList(hospitalId, directionId);
        for (int i = 0; i < arrayDoctor.length(); i++) {
            if (arrayDoctor.getJSONObject(i).get("id").equals(doctorId)) {
                return arrayDoctor.getJSONObject(i).get("name").toString();
            }
        }
        return null;
    }
}
