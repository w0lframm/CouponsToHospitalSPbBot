package com.example.couponstohospitalbot.telegram.keyboards;

import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ParsingJsonTest {
    static JSONArray arrayReg;

    @BeforeAll
    static void setUp() throws IOException, SiteFailException {
        arrayReg = ParsingJson.getRegionsList();
    }

    @Test
    void findRegionIdByName() throws IOException, JSONException, SiteFailException {
        String reg = ParsingJson.findRegionIdByName(arrayReg.getJSONObject(0).get("name").toString());
        assertEquals(arrayReg.getJSONObject(0).get("id"), reg);
    }

    @Test
    void findRegionIdByInvalidName() throws IOException, SiteFailException {
        String reg = ParsingJson.findRegionIdByName("wrong-region");
        assertNull(reg);
    }

    @Test
    void findRegionNameById() throws JSONException, IOException, URISyntaxException, SiteFailException {
        String reg = ParsingJson.findRegionNameById(arrayReg.getJSONObject(0).get("id").toString());
        assertEquals(arrayReg.getJSONObject(0).get("name"), reg);
    }

    @Test
    void findRegionNameByInvalidId() throws IOException, URISyntaxException, SiteFailException {
        String reg = ParsingJson.findRegionNameById("wrongId");
        assertNull(reg);
    }

    @Test
    void findHospitalIdByName() throws JSONException, IOException, SiteFailException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        Integer hospId = ParsingJson.findHospitalIdByName(regId, arrayHosp.getJSONObject(0).get("lpuFullName").toString());
        assertEquals(arrayHosp.getJSONObject(0).get("id"), hospId);
    }

    @Test
    void findHospitalIdByInvalidName() throws JSONException, IOException, SiteFailException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        Integer hospId = ParsingJson.findHospitalIdByName(regId, "wrong-name");
        assertEquals(-1, hospId);
    }

    @Test
    void findDirectionIdByName() throws JSONException, IOException, SiteFailException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        JSONArray arrayDir = ParsingJson.getDirectionsList((Integer) arrayHosp.getJSONObject(0).get("id"));
        String dirId = ParsingJson.findDirectionIdByName((Integer) arrayHosp.getJSONObject(0).get("id"),
                arrayDir.getJSONObject(0).get("name").toString());
        assertEquals(arrayDir.getJSONObject(0).get("id"), dirId);
    }

    @Test
    void findDirectionIdByInvalidName() throws JSONException, IOException, SiteFailException {
        String regId = arrayReg.getJSONObject(0).get("id").toString();
        JSONArray arrayHosp = ParsingJson.getHospitalList(regId);
        String dirId = ParsingJson.findDirectionIdByName((Integer) arrayHosp.getJSONObject(0).get("id"),
                "wrong-name");
        assertNull(dirId);
    }

    @Test
    void findDoctorIdByName() throws JSONException, IOException, URISyntaxException, SiteFailException {
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