package wooteco.subway.maps.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    protected LineStation FIRST_LINE_STATION;
    protected LineStation SECOND_LINE_STATION;
    protected LineStation THIRD_LINE_STATION;
    private Line line;

    @BeforeEach
    void setUp() {
        FIRST_LINE_STATION = new LineStation(1L, null, 0, 0);
        SECOND_LINE_STATION = new LineStation(2L, 1L, 10, 4);
        THIRD_LINE_STATION = new LineStation(3L, 2L, 10, 5);

        line = new Line("Name", "Color", LocalTime.of(14, 0), LocalTime.of(19, 0), 10, 1000);
        line.addLineStation(FIRST_LINE_STATION);
        line.addLineStation(SECOND_LINE_STATION);
        line.addLineStation(THIRD_LINE_STATION);
    }

    @DisplayName("정방향 해당 역까지 소요 시간")
    @Test
    void calculatorCurrentMinuteStartStationToPointStation() {
        assertThat(line.calculatorCurrentMinuteStartStationToPointStation(FIRST_LINE_STATION))
            .isEqualTo(0);
        assertThat(line.calculatorCurrentMinuteStartStationToPointStation(SECOND_LINE_STATION))
            .isEqualTo(4);
        assertThat(line.calculatorCurrentMinuteStartStationToPointStation(THIRD_LINE_STATION))
            .isEqualTo(9);
    }

    @DisplayName("역방향 해당 역까지 소요 시간")
    @Test
    void calculatorCurrentMinuteEndStationToPointStation() {
        assertThat(line.calculatorCurrentMinuteEndStationToPointStation(FIRST_LINE_STATION))
            .isEqualTo(9);
        assertThat(line.calculatorCurrentMinuteEndStationToPointStation(SECOND_LINE_STATION))
            .isEqualTo(5);
        assertThat(line.calculatorCurrentMinuteEndStationToPointStation(THIRD_LINE_STATION))
            .isEqualTo(0);
    }

    @DisplayName("두 역이 있을 때, 맨 처음 역에서 해당 역까지 소요 시간")
    @Test
    void calculatorCurrentMinuteFirstStation() {
        assertThat(
            line.calculatorCurrentMinuteFirstStation(FIRST_LINE_STATION, SECOND_LINE_STATION))
            .isEqualTo(0);
        assertThat(line.calculatorCurrentMinuteFirstStation(FIRST_LINE_STATION, THIRD_LINE_STATION))
            .isEqualTo(0);
        assertThat(
            line.calculatorCurrentMinuteFirstStation(SECOND_LINE_STATION, FIRST_LINE_STATION))
            .isEqualTo(5);
        assertThat(
            line.calculatorCurrentMinuteFirstStation(SECOND_LINE_STATION, THIRD_LINE_STATION))
            .isEqualTo(4);
        assertThat(line.calculatorCurrentMinuteFirstStation(THIRD_LINE_STATION, FIRST_LINE_STATION))
            .isEqualTo(0);
        assertThat(
            line.calculatorCurrentMinuteFirstStation(THIRD_LINE_STATION, SECOND_LINE_STATION))
            .isEqualTo(0);
    }

    @DisplayName("막차 이후 첫차까지 대기 시간 - 분")
    @Test
    void calculatorAfterEndTrainAtPoint() {
        int minute = line.calculatorAfterEndTrainAtPoint(20, LocalTime.of(22, 0));
        int expected = (14 + 24 - 22) * 60 + 20;
        assertThat(minute).isEqualTo(expected);
    }

    @DisplayName("출발 대기 시간 분")
    @Test
    void calculatorWaitMinuteFirstStation() {
        assertThat(line.calculatorWaitMinuteFirstStation(LocalTime.of(22, 0), SECOND_LINE_STATION,
            FIRST_LINE_STATION)).isEqualTo((14 + 24 - 22) * 60 + 5);
        assertThat(line.calculatorWaitMinuteFirstStation(LocalTime.of(17, 0), SECOND_LINE_STATION,
            FIRST_LINE_STATION)).isEqualTo(5);
        assertThat(line.calculatorWaitMinuteFirstStation(LocalTime.of(17, 5), SECOND_LINE_STATION,
            FIRST_LINE_STATION)).isEqualTo(0);
    }
}