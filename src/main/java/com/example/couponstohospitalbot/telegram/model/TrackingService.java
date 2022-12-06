package com.example.couponstohospitalbot.telegram.model;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Bot;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.ANSWER_MESSAGE;
import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.*;

@Service
@RequiredArgsConstructor
public class TrackingService {
    private final TrackingRepository trackingRepository;
    private static final Logger logger = Logger.getLogger(TrackingService.class.getName());
    private final List<Long> events = new LinkedList<>();
    private List<Long> toDeleteEvents = new ArrayList<>();

    private static final String DRIVER = "webdriver.chrome.driver";
    private static final String LOCATION = ".\\src\\main\\resources\\chromedriver.exe";
    private static final String URL = "https://gorzdrav.spb.ru/service-free-schedule";
    private static final String DISTRICT = "//*[@id='serviceAnotherMan']//li[@data-district-id='";
    private static final String HOSPITAL = "//*[@type='button' and @data-lpu-id='";
    private static final String SPECIALITY = "//*/button[@data-speciality-id='";

    public Long initTracking(State state) {
        Tracking tracking = new Tracking(state);
        trackingRepository.save(tracking);
        return tracking.getTrackId();
    }

    public Runnable waitCoupons() throws InterruptedException {
        while (true) {
            TimeUnit.SECONDS.sleep(1); //проверяем с интервалом в минуту
            if (!events.isEmpty()) {
                for (Long trackId : events) {
                    try {
                        Tracking tracking = findById(trackId);
                        JSONArray result = getDoctorsList(tracking.getHospitalId(), tracking.getDirectionId());

                        for (int i = 0; i < result.length(); i++) {
                            if ((Objects.equals(tracking.getDoctorId(), "-1") || result.getJSONObject(i).get("id").equals(tracking.getDoctorId())) &&
                                    (int) result.getJSONObject(i).get("freeTicketCount") > 0) {
                                String url = getUrl(trackId);
                                String mess = ANSWER_MESSAGE + "\n" + getRequestInfo(trackId) + "\nСсылка для записи: " + url; // добавить ссылку на регистрацию
                                ApplicationContextHolder.getContext().getBean(Bot.class).notifyUser(tracking.getChatId().toString(), mess);
                                setFinished(trackId);
                                toDeleteEvents.add(trackId);
                                break;
                            }
                        }
                    } catch (URISyntaxException | IOException ignored) {}
                }
                if (!toDeleteEvents.isEmpty()) {
                    for (Long trackId : toDeleteEvents) { //очищаем список событий
                        events.remove(trackId);
                    }
                    toDeleteEvents = new ArrayList<>();
                }
            }
        }
    }


    private Tracking findById(Long trackId) {
        Optional<Tracking> optionalTracking = trackingRepository.findById(trackId);
        return optionalTracking.orElse(null);
    }

    private void setFinished(Long trackId) {
        Tracking tracking = findById(trackId);
        tracking.setIsFinished(true);
        trackingRepository.save(tracking);
    }

    public void addEvent(Long trackId) {
        events.add(trackId);
    }

    private String getRequestInfo(Long trackId) throws IOException, URISyntaxException {
        Tracking tracking = findById(trackId);
        StringBuilder sb = new StringBuilder("Район: ");
        sb.append(findRegionNameById(tracking.getRegionId())).append("\nБольница: ");
        sb.append(findHospitalNameById(tracking.getChatId(), tracking.getHospitalId().toString())).append("\nНаправление: ");
        sb.append(findDirectionNameById(tracking.getChatId(), tracking.getDirectionId())).append("\nДоктор: ");
        if (Objects.equals(tracking.getDoctorId(), "-1")) { //или переделать на другую логику
            sb.append("без разницы");
        } else {
            sb.append(findDoctorNameById(tracking.getChatId(), tracking.getDoctorId()));
        }
        return sb.toString();
    }

    private String getUrl(Long trackId) throws InterruptedException {
        Tracking tracking = findById(trackId);
        String url;
        System.setProperty(DRIVER, LOCATION);
        WebDriver driver = new ChromeDriver();
        driver.get(URL);
        Thread.sleep(1000);
        driver.findElement(By.xpath(DISTRICT + tracking.getRegionId() + "']")).click();
        Thread.sleep(1000);
        driver.findElement(By.xpath(HOSPITAL + tracking.getHospitalId() + "']")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath(SPECIALITY + tracking.getDirectionId() + "']")).click();
        url = driver.getCurrentUrl();
        driver.close();
        return url;
    }
}
