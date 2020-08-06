package wooteco.subway.maps.line.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import wooteco.subway.common.domain.BaseEntity;

@Entity
public class Line extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String color;
    private LocalTime startTime;
    private LocalTime endTime;
    private int intervalTime;

    @Embedded
    private Fare extraFare;

    @Embedded
    private LineStations lineStations = new LineStations();

    public Line() {
    }

    public Line(String name, String color, LocalTime startTime, LocalTime endTime, int intervalTime,
        int extraFare) {
        this.name = name;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.intervalTime = intervalTime;
        this.extraFare = new Fare(extraFare);
    }

    public void update(Line line) {
        this.name = line.name;
        this.startTime = line.startTime;
        this.endTime = line.endTime;
        this.intervalTime = line.intervalTime;
        this.color = line.color;
        this.extraFare = line.extraFare;
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.add(lineStation);
    }

    public void removeLineStationById(Long stationId) {
        lineStations.removeByStationId(stationId);
    }

    int calculatorWaitMinuteFirstStation(LocalTime foundTime, LineStation firstStation,
        LineStation nextStation) {
        int currentMinute = calculatorCurrentMinuteFirstStation(firstStation, nextStation);
        LocalTime startPointStartTime = foundTime.minusMinutes(currentMinute);
        if (startPointStartTime.isAfter(endTime)) {
            return calculatorAfterEndTrainAtPoint(currentMinute, foundTime);
        }
        return calculatorBeforeEndTrainAtPoint(currentMinute, foundTime);
    }

    int calculatorBeforeEndTrainAtPoint(int currentMinute, LocalTime foundTime) {
        LocalTime startTimeAtPoint = foundTime.minusMinutes(currentMinute);
        List<LocalTime> startTrainTimes = new ArrayList<>();

        LocalTime temp = LocalTime.of(startTime.getHour(), startTime.getMinute());

        while (!temp.isBefore(startTime) && !temp.isAfter(endTime)) {
            startTrainTimes.add(temp);
            temp = temp.plusMinutes(intervalTime);
        }

        LocalTime realStartTime = startTrainTimes.stream()
            .filter(startTrainTime -> !startTrainTime.isBefore(startTimeAtPoint))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        System.out.println(realStartTime);

        return realStartTime.minusMinutes(startTimeAtPoint.getMinute()).getMinute();
    }

    int calculatorAfterEndTrainAtPoint(int currentMinute, LocalTime foundTime) {
        LocalTime startTimeAtPoint = startTime.plusMinutes(currentMinute);
        int startMinutesAtPoint = calculateTimeToMinute(startTimeAtPoint);
        return startMinutesAtPoint + (24 * 60) - calculateTimeToMinute(foundTime);
    }

    private int calculateTimeToMinute(LocalTime localTime) {
        return (localTime.getHour() * 60) + localTime.getMinute();
    }

    int calculatorCurrentMinuteFirstStation(LineStation firstStation, LineStation nextStation) {
        boolean forward = lineStations.isForward(firstStation, nextStation);
        if (forward) {
            return calculatorCurrentMinuteStartStationToPointStation(firstStation);
        }
        return calculatorCurrentMinuteEndStationToPointStation(firstStation);
    }

    int calculatorCurrentMinuteStartStationToPointStation(LineStation pointStation) {
        int currentMinute = 0;
        List<LineStation> stationsInOrder = lineStations.getStationsInOrder();
        for (LineStation lineStation : stationsInOrder) {
            currentMinute += lineStation.getDuration();
            if (lineStation.equals(pointStation)) {
                break;
            }
        }
        return currentMinute;
    }

    public int calculatorCurrentMinuteEndStationToPointStation(LineStation pointStation) {
        int currentMinute = 0;
        List<LineStation> stationsInOrder = lineStations.getStationsInOrder();
        Collections.reverse(stationsInOrder);
        for (LineStation lineStation : stationsInOrder) {
            if (lineStation.equals(pointStation)) {
                break;
            }
            currentMinute += lineStation.getDuration();
        }
        return currentMinute;
    }

    public List<LineStation> getStationInOrder() {
        return lineStations.getStationsInOrder();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public int getIntervalTime() {
        return intervalTime;
    }

    public LineStations getLineStations() {
        return lineStations;
    }

    public int getExtraFare() {
        return extraFare.getExtraFare();
    }
}
