package wooteco.subway.common;

import java.time.LocalTime;
import org.springframework.test.util.ReflectionTestUtils;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.station.domain.Station;

public class TestObjectUtils {

    public static Station createStation(Long id, String name) {
        Station station = new Station(name);
        ReflectionTestUtils.setField(station, "id", id);
        return station;
    }

    public static Line createLine(Long id, String name, String color) {
        Line line1 = new Line(name, color, LocalTime.of(5, 30), LocalTime.of(23, 30), 10, 0);
        ReflectionTestUtils.setField(line1, "id", id);
        return line1;
    }
}
