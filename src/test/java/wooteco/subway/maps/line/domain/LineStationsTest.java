package wooteco.subway.maps.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 단위 테스트")
public class LineStationsTest {

    protected LineStation FIRST_LINE_STATION;
    protected LineStation SECOND_LINE_STATION;
    protected LineStation THIRD_LINE_STATION;
    private LineStations lineStations;

    @BeforeEach
    void setUp() {
        FIRST_LINE_STATION = new LineStation(1L, null, 0, 0);
        SECOND_LINE_STATION = new LineStation(2L, 1L, 10, 4);
        THIRD_LINE_STATION = new LineStation(3L, 2L, 10, 5);
        // given
        lineStations = new LineStations();
        lineStations.add(FIRST_LINE_STATION);
        lineStations.add(SECOND_LINE_STATION);
        lineStations.add(THIRD_LINE_STATION);
    }

    @DisplayName("지하철 노선에 역을 마지막에 등록한다.")
    @Test
    void add1() {
        // when
        lineStations.add(new LineStation(4L, 3L, 10, 10));

        // then
        List<Long> stationIds = lineStations.getStationsInOrder().stream()
            .map(LineStation::getStationId)
            .collect(Collectors.toList());
        assertThat(stationIds).containsExactly(1L, 2L, 3L, 4L);
    }

    @DisplayName("지하철 노선에 역을 중간에 등록한다.")
    @Test
    void add2() {
        // when
        lineStations.add(new LineStation(4L, 1L, 10, 10));

        // then
        List<Long> stationIds = lineStations.getStationsInOrder().stream()
            .map(LineStation::getStationId)
            .collect(Collectors.toList());
        assertThat(stationIds).containsExactly(1L, 4L, 2L, 3L);
    }

    @DisplayName("이미 등록되어 있던 역을 등록한다.")
    @Test
    void add3() {
        // when
        assertThatThrownBy(() -> lineStations.add(new LineStation(2L, 1L, 10, 10)))
            .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에 등록된 마지막 지하철역을 제외한다.")
    @Test
    void removeLineStation1() {
        // when
        lineStations.removeByStationId(3L);

        // then
        List<Long> stationIds = lineStations.getStationsInOrder().stream()
            .map(LineStation::getStationId)
            .collect(Collectors.toList());
        assertThat(stationIds).containsExactly(1L, 2L);
    }

    @DisplayName("지하철 노선에 등록된 중간 지하철역을 제외한다.")
    @Test
    void removeLineStation2() {
        // when
        lineStations.removeByStationId(2L);

        // then
        List<Long> stationIds = lineStations.getStationsInOrder().stream()
            .map(it -> it.getStationId())
            .collect(Collectors.toList());
        assertThat(stationIds).containsExactly(1L, 3L);
    }

    @DisplayName("지하철 노선의 출발점을 제외한다.")
    @Test
    void removeLineStation3() {
        // when
        assertThatThrownBy(() -> lineStations.removeByStationId(null))
            .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("지하철 노선에서 등록되지 않는 역을 제외한다.")
    @Test
    void removeLineStation4() {
        // when
        assertThatThrownBy(() -> lineStations.removeByStationId(100L))
            .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("정방향인지 체크 - 정방향")
    @Test
    void isForward_Forward_True() {
        assertThat(lineStations.isForward(FIRST_LINE_STATION, SECOND_LINE_STATION)).isTrue();
        assertThat(lineStations.isForward(FIRST_LINE_STATION, THIRD_LINE_STATION)).isTrue();
        assertThat(lineStations.isForward(SECOND_LINE_STATION, THIRD_LINE_STATION)).isTrue();
    }

    @DisplayName("정방향인지 체크 - 역방향")
    @Test
    void isForward_Reverse_False() {
        assertThat(lineStations.isForward(SECOND_LINE_STATION, FIRST_LINE_STATION)).isFalse();
        assertThat(lineStations.isForward(THIRD_LINE_STATION, FIRST_LINE_STATION)).isFalse();
        assertThat(lineStations.isForward(THIRD_LINE_STATION, SECOND_LINE_STATION)).isFalse();
    }
}
