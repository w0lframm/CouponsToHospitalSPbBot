package com.example.couponstohospitalbot.telegram.keyboards;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.getRegionsList;
import static org.junit.jupiter.api.Assertions.*;

class ParsingJsonTest {
    static JSONArray arrayReg;

    @BeforeAll
    static void setUp() throws IOException, URISyntaxException {
        arrayReg = ParsingJson.getRegionsList();
    }

    @Test
    void findRegionIdByName() throws IOException, URISyntaxException, JSONException {
        String reg = ParsingJson.findRegionIdByName(arrayReg.getJSONObject(0).get("name").toString());
        assertEquals(arrayReg.getJSONObject(0).get("id"), reg);
    }

    @Test
    void findRegionIdByInvalidName() throws IOException, URISyntaxException {
        String reg = ParsingJson.findRegionIdByName("wrong-region");
        assertNull(reg);
    }

    @Test
    void findRegionNameById() throws JSONException, IOException, URISyntaxException {
        String reg = ParsingJson.findRegionNameById(arrayReg.getJSONObject(0).get("id").toString());
        assertEquals(arrayReg.getJSONObject(0).get("name"), reg);
    }

    @Test
    void findRegionNameByInvalidId() throws IOException, URISyntaxException {
        String reg = ParsingJson.findRegionNameById("wrongId");
        assertNull(reg);
    }

    @Test
    void findHospitalIdByName() throws JSONException, IOException, URISyntaxException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        Integer hospId = ParsingJson.findHospitalIdByName(regId, arrayHosp.getJSONObject(0).get("lpuFullName").toString());
        assertEquals(arrayHosp.getJSONObject(0).get("id"), hospId);
    }

    @Test
    void findHospitalIdByInvalidName() throws JSONException, IOException, URISyntaxException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        Integer hospId = ParsingJson.findHospitalIdByName(regId, "wrong-name");
        assertEquals(-1, hospId);
    }

    //    JSONArray arrayDir = getDirectionsList(hospId);
//        for (int i = 0; i < arrayDir.length(); i++) {
//        if (arrayDir.getJSONObject(i).get("name").equals(direction)) {
//            return arrayDir.getJSONObject(i).get("id").toString();
//        }
//    }
//        return null;
    @Test
    void findDirectionIdByName() throws JSONException, IOException, URISyntaxException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        JSONArray arrayDir = ParsingJson.getDirectionsList((Integer) arrayHosp.getJSONObject(0).get("id"));
        String dirId = ParsingJson.findDirectionIdByName((Integer) arrayHosp.getJSONObject(0).get("id"),
                arrayDir.getJSONObject(0).get("name").toString());
        assertEquals(arrayDir.getJSONObject(0).get("id"), dirId);
    }

    @Test
    void findDirectionIdByInvalidName() throws JSONException, IOException, URISyntaxException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        String dirId = ParsingJson.findDirectionIdByName((Integer) arrayHosp.getJSONObject(0).get("id"),
                "wrong-name");
        assertNull(dirId);
    }

    @Test
    void findDoctorIdByName() throws JSONException, IOException, URISyntaxException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();

        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        Integer hospId = (Integer) arrayHosp.getJSONObject(0).get("id");

        JSONArray arrayDir = ParsingJson.getDirectionsList(hospId);
        String dirId = arrayDir.getJSONObject(0).get("id").toString();

        JSONArray arrayDoc = ParsingJson.getDoctorsList(hospId, dirId);

        String docId = ParsingJson.findDoctorIdByName(hospId, dirId, arrayDoc.getJSONObject(0).get("name").toString());

        assertEquals(arrayDoc.getJSONObject(0).get("id"), docId);
    }

}