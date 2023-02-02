package com.example.couponstohospitalbot.telegram.keyboards;

import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class ParsingJson {
    private static final Logger logger = Logger.getLogger(ParsingJson.class.getName());

    public static JSONArray getRegionsList() throws IOException, SiteFailException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet("https://gorzdrav.spb.ru/_api/api/v2/shared/districts");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 502) { //если упал сайт, а не запрос стал невалидным, то исключение SiteFailException
                    logger.warning(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                    throw new SiteFailException();
                }
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return new JSONObject(responseBody).getJSONArray("result");
            }
        }
    }

    //https://gorzdrav.spb.ru/_api/api/v2/shared/district/4/lpus
    public static JSONArray getHospitalList(String regionId) throws IOException, SiteFailException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet("https://gorzdrav.spb.ru/_api/api/v2/shared/district/" + regionId + "/lpus");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 502) { //если упал сайт, а не запрос стал невалидным, то исключение SiteFailException
                    logger.warning(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                    throw new SiteFailException();
                }
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return new JSONObject(responseBody).getJSONArray("result");
            }
        }
    }

    public static JSONArray getDirectionsList(int hospitalId) throws IOException, SiteFailException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet("https://gorzdrav.spb.ru/_api/api/v2/schedule/lpu/" + hospitalId + "/specialties");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 502) { //если упал сайт, а не запрос стал невалидным, то исключение SiteFailException
                    logger.warning(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                    throw new SiteFailException();
                }
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return new JSONObject(responseBody).getJSONArray("result");
            }
        }
    }

    public static JSONArray getDoctorsList(int hospitalId, String dirId) throws IOException, URISyntaxException, SiteFailException {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpGet = new HttpGet("https://gorzdrav.spb.ru/_api/api/v2/schedule/lpu/" + hospitalId +
                    "/speciality/" + dirId + "/doctors");

            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == 502) { //если упал сайт, а не запрос стал невалидным, то исключение SiteFailException
                    logger.warning(statusLine.getStatusCode() + " " + statusLine.getReasonPhrase());
                    throw new SiteFailException();
                }
                String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                return new JSONObject(responseBody).getJSONArray("result");
            }
        }
    }

    public static String findRegionIdByName(String region) throws IOException, SiteFailException {
        JSONArray arrayReg = getRegionsList();
        for (int i = 0; i < arrayReg.length(); i++) {
            if (arrayReg.getJSONObject(i).get("name").equals(region)) {
                return arrayReg.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    public static String findRegionNameById(String regionId) throws IOException, URISyntaxException, SiteFailException {
        JSONArray arrayReg = getRegionsList();
        for (int i = 0; i < arrayReg.length(); i++) {
            if (arrayReg.getJSONObject(i).get("id").equals(regionId)) {
                return arrayReg.getJSONObject(i).get("name").toString();
            }
        }
        return null;
    }

    public static Integer findHospitalIdByName(String regionId, String hospName) throws IOException, SiteFailException {
        JSONArray arrayHosp = getHospitalList(regionId);
        for (int i = 0; i < arrayHosp.length(); i++) {
            if (arrayHosp.getJSONObject(i).get("lpuFullName").equals(hospName)) {
                return (int) arrayHosp.getJSONObject(i).get("id");
            }
        }
        return -1;
    }

    public static String findHospitalNameById(String hospId, String regionId) throws URISyntaxException, IOException, SiteFailException {
        Integer hospitalId = Integer.parseInt(hospId);
        JSONArray arrayHosp = getHospitalList(regionId);
        for (int i = 0; i < arrayHosp.length(); i++) {
            if (arrayHosp.getJSONObject(i).get("id").equals(hospitalId)) {
                return arrayHosp.getJSONObject(i).get("lpuFullName").toString();
            }
        }
        return null;
    }

    public static String findDirectionIdByName(int hospId, String direction) throws IOException, SiteFailException {
        JSONArray arrayDir = getDirectionsList(hospId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("name").equals(direction)) {
                return arrayDir.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    public static String findDirectionNameById(String directionId, Integer hospitalId) throws URISyntaxException, IOException, SiteFailException {
        JSONArray arrayDir = getDirectionsList(hospitalId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("id").equals(directionId)) {
                return arrayDir.getJSONObject(i).get("name").toString();
            }
        }
        return null;
    }

    public static String findDoctorIdByName(int hospId, String dirId, String nameDoctor) throws IOException, URISyntaxException, SiteFailException {
        JSONArray arrayDir = getDoctorsList(hospId, dirId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("name").equals(nameDoctor)) {
                return arrayDir.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }

    public static String  findDoctorNameById(String doctorId, int hospitalId, String directionId) throws IOException, URISyntaxException, SiteFailException {
        JSONArray arrayDoctor = getDoctorsList(hospitalId, directionId);
        for (int i = 0; i < arrayDoctor.length(); i++) {
            if (arrayDoctor.getJSONObject(i).get("id").equals(doctorId)) {
                return arrayDoctor.getJSONObject(i).get("name").toString();
            }
        }
        return null;
    }
}
