package com.example.couponstohospitalbot.telegram.model;

import com.example.couponstohospitalbot.ApplicationContextHolder;
import com.example.couponstohospitalbot.telegram.Bot;
import com.example.couponstohospitalbot.telegram.exception.SiteFailException;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.example.couponstohospitalbot.telegram.keyboards.Constants.ANSWER_MESSAGE;
import static com.example.couponstohospitalbot.telegram.keyboards.Constants.STOP_ALARM;
import static com.example.couponstohospitalbot.telegram.keyboards.ParsingJson.*;

@Service
@RequiredArgsConstructor
public class TrackingService {
    private final TrackingRepository trackingRepository;
    private AlarmThread alarm;
    private static final Logger logger = Logger.getLogger(TrackingService.class.getName());
    private final List<Long> events = new LinkedList<>();
    private List<Long> toDeleteEvents = new ArrayList<>();

    private static final String url = "https://gorzdrav.spb.ru/service-free-schedule#%5B%7B%22district%22:" +
            "%22DISTRICTID%22%7D,%7B%22lpu%22:%22HOSPITALID%22%7D,%7B%22speciality%22:%22DIRECTIONID%22%7D%5D";
    private static final String urlWithDoctor = "https://gorzdrav.spb.ru/service-free-schedule#%5B%7B%22district%22:" +
            "%22DISTRICTID%22%7D,%7B%22lpu%22:%22HOSPITALID%22%7D,%7B%22speciality%22:" +
            "%22DIRECTIONID%22%7D,%7B%22doctor%22:%22DOCTORID%22%7D%5D";

    public Long initTracking(State state) {//проверить, есть ли уже с такими данными
        Tracking tracking = new Tracking(state);
        //проверяем по chatId, regionId, hospitalId, directionId, doctorId
        Optional<Tracking> equal = trackingRepository.findEqual(tracking.getChatId(),
                tracking.getRegionId(), tracking.getHospitalId(),
                tracking.getDirectionId(), tracking.getDoctorId());
        if (equal.isPresent()) {
            tracking = equal.get();
            tracking.setIsFinished(false);
        }
        trackingRepository.save(tracking);
        return tracking.getTrackId();
    }

    public Runnable waitCoupons() throws InterruptedException {
        List<Tracking> list = trackingRepository.getActiveRequests();
        for (Tracking elem : list) { //для корректного возобновления работы после перезапуска приложения
            events.add(elem.getTrackId());
        }
        while (true) {
            TimeUnit.SECONDS.sleep(60); //проверяем с интервалом в минуту
            for (Long trackId : events) {
                try {
                    Tracking tracking = findById(trackId);
                    JSONArray result;
                    try {
                        result = getDoctorsList(tracking.getHospitalId(), tracking.getDirectionId());
                    } catch (SiteFailException e) {
                        logger.warning(e.getMessage());
                        TimeUnit.SECONDS.sleep(60);
                        continue;
                    }
                    boolean flag = false;
                    if (result != null) {
                        for (int i = 0; i < result.length(); i++) {
                            if (Objects.equals(tracking.getDoctorId(), "-1") ||
                                    result.getJSONObject(i).get("id").equals(tracking.getDoctorId())) {
                                flag = true; //чтобы не удалилось из коллекции
                                if ((int) result.getJSONObject(i).get("freeTicketCount") > 0) {
                                    logger.info("Coupon found");
                                    String url = getLink(trackId);
                                    String mess;
                                    try {
                                        mess = ANSWER_MESSAGE + "\n" + getRequestInfo(trackId) +
                                                "\nСсылка для записи: " + url + "\n\n" + STOP_ALARM;
                                    } catch (SiteFailException e) {
                                        logger.warning(e.getMessage());
                                        break;
                                    }
                                    ApplicationContextHolder.getContext().getBean(Bot.class)
                                            .notifyUser(tracking.getChatId().toString(), mess);
                                    alarmStart(tracking.getChatId().toString());
                                    setFinished(trackId);
                                    toDeleteEvents.add(trackId);
                                    break;
                                }
                            }
                        }
                    }
                    if (!flag) {
                        String mess = "К сожалению, данный выбор больше не доступен.\n" +
                                "\nПожалуйста, перезапишитесь";
                        ApplicationContextHolder.getContext().getBean(Bot.class)
                                .notifyUser(tracking.getChatId().toString(), mess);
                        toDeleteEvents.add(trackId);
                        ApplicationContextHolder.getContext().getBean(CollectionService.class)
                                .deleteItem(trackId);
                    }
                } catch (URISyntaxException | IOException ignored) {
                }
            }
            if (!toDeleteEvents.isEmpty()) {
                for (Long trackId : toDeleteEvents) { //очищаем список событий
                    events.remove(trackId);
                }
                toDeleteEvents = new ArrayList<>();
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

    public String getRequestInfo(Long trackId) throws IOException, URISyntaxException, SiteFailException {
        Tracking tracking = findById(trackId);
        StringBuilder sb = new StringBuilder("Район: ");
        sb.append(findRegionNameById(tracking.getRegionId())).append("\nБольница: ");
        sb.append(findHospitalNameById(tracking.getHospitalId().toString(), tracking.getRegionId())).append("\nНаправление: ");
        sb.append(findDirectionNameById(tracking.getDirectionId(), tracking.getHospitalId())).append("\nДоктор: ");
        if (Objects.equals(tracking.getDoctorId(), "-1")) {
            sb.append("без разницы");
        } else {
            sb.append(findDoctorNameById(tracking.getDoctorId(), tracking.getHospitalId(), tracking.getDirectionId()));
        }
        return sb.toString();
    }

    private String getLink(Long trackId) {
        Tracking tracking = findById(trackId);
        if (tracking.getDoctorId().equals("-1")) {
            return url
                    .replace("DISTRICTID", tracking.getRegionId())
                    .replace("HOSPITALID", tracking.getHospitalId().toString())
                    .replace("DIRECTIONID", tracking.getDirectionId());
        } else {
            return urlWithDoctor
                    .replace("DISTRICTID", tracking.getRegionId())
                    .replace("HOSPITALID", tracking.getHospitalId().toString())
                    .replace("DIRECTIONID", tracking.getDirectionId())
                    .replace("DOCTORID", tracking.getDoctorId());
        }
    }

    private void alarmStart(String chatId) {
        alarm = new AlarmThread(chatId);
        alarm.start();
    }

    public void alarmStop() {
        if (alarm != null) {
            alarm.interrupt();
        }
    }
}
