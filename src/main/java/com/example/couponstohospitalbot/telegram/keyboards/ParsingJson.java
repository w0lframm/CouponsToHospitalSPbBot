package com.example.couponstohospitalbot.telegram.keyboards;

import net.sf.corn.httpclient.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ParsingJson {
    public static void main(String[] args) throws IOException, URISyntaxException {

        fillButtonsRegion();

        //получить ответ с кнопки
        //String region = button.response(); - название региона

        String regionId = findRegionIdByName("Калининский");
        fillButtonsHospital(regionId);

        //получить ответ с кнопки
        //String hospital = button.response(); - название поликлинники

        int hospId = findHospitalIdByName(regionId, "СПб ГБУЗ \"Городская поликлиника №112\"");
        fillButtonsDirection(hospId);

        //получить ответ с кнопки
        //String direction = button.response(); - название направления

        String dirId = findDirectionIdByName(hospId, "Инфекционист");
        fillButtonsDoctor(hospId, dirId);

        //получить ответ с кнопки
        //String doctor = button.response(); - имя доктора

        String doctorId = findDoctorIdByName(hospId, dirId, "Даева Евгения Юрьевна");
        fillDateAndTime(hospId, doctorId);

        //получить ответ с кнопки
        //String dateTime = button.response(); - время визита

        //еще не продумала как отправлять данные о себе при записи
    }

    public static JSONArray fillButtonsRegion() throws URISyntaxException, IOException {
        JSONArray array = getRegionsList();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsObj = array.getJSONObject(i);
            System.out.println(jsObj.get("name")); // заполнить кнопки
        }
        return array;
    }

    private static JSONArray fillButtonsHospital(String regionId) throws IOException, URISyntaxException {
        JSONArray arrayHosp = getHospitalList(regionId);
        for (int i = 0; i < arrayHosp.length(); i++) {
            JSONObject jsObj = arrayHosp.getJSONObject(i);
            System.out.println(jsObj.get("lpuFullName")); // заполнить кнопки
        }
        return arrayHosp;
    }

    private static JSONArray fillButtonsDirection(int hospitalId) throws URISyntaxException, IOException {
        JSONArray arrayDir = getDirectionsList(hospitalId);
        for (int i = 0; i < arrayDir.length(); i++) {
            JSONObject jsObj = arrayDir.getJSONObject(i);
            System.out.println(jsObj.get("name")); // заполнить кнопки
        }
        return arrayDir;
    }

    private static JSONArray fillButtonsDoctor(int hospitalId, String dirId) throws IOException, URISyntaxException {
        JSONArray arrayDoctor = getDoctorsList(hospitalId, dirId);
        for (int i = 0; i < arrayDoctor.length(); i++) {
            JSONObject jsObj = arrayDoctor.getJSONObject(i);
            System.out.println(jsObj.get("name")); // заполнить кнопки
        }
        return arrayDoctor;
    }

    private static JSONArray fillDateAndTime(int hospitalId, String doctorId) throws IOException, URISyntaxException {
        JSONArray arrayTimetable = getTimetableList(hospitalId, doctorId);
        for (int i = 0; i < arrayTimetable.length(); i++) {
            JSONObject jsObj = arrayTimetable.getJSONObject(i);
            System.out.println(jsObj.get("visitStart")); // заполнить кнопки
        }
        return arrayTimetable;
    }


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

    //https://gorzdrav.spb.ru/_api/api/v2/schedule/lpu/289/doctor/52/appointments
    public static JSONArray getTimetableList(int hospitalId, String doctorId) throws IOException, URISyntaxException {
        HttpClient client = new HttpClient(new URI("https://gorzdrav.spb.ru/_api/api/v2/schedule/lpu/" + hospitalId + "/doctor/" + doctorId + "/appointments"));
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

    public static int findHospitalIdByName(String regionId, String hospName) throws URISyntaxException, IOException {
        JSONArray arrayHosp = getHospitalList(regionId);
        for (int i = 0; i < arrayHosp.length(); i++) {
            if (arrayHosp.getJSONObject(i).get("lpuFullName").equals(hospName)) {
                return (int) arrayHosp.getJSONObject(i).get("id");
            }
        }
        return -1;
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

    public static String findDoctorIdByName(int hospId, String dirId, String nameDoctor) throws IOException, URISyntaxException {
        JSONArray arrayDir = getDoctorsList(hospId, dirId);
        for (int i = 0; i < arrayDir.length(); i++) {
            if (arrayDir.getJSONObject(i).get("name").equals(nameDoctor)) {
                return arrayDir.getJSONObject(i).get("id").toString();
            }
        }
        return null;
    }
}
