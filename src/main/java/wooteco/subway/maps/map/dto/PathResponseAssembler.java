package wooteco.subway.maps.map.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.map.domain.SubwayPath;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.maps.station.dto.StationResponse;

public class PathResponseAssembler {

    protected static final int DEFAULT_FARE = 1250;
    protected static final int DEFAULT_OVER_FARE = 0;
    protected static final int OVER_FARE_FIRST_POINT = 10;
    protected static final int OVER_FARE_SECOND_POINT = 50;
    protected static final int OVER_FARE_CHARGE = 100;
    protected static final int OVER_FARE_SECOND_UNIT = 5;
    protected static final int OVER_PARE_THIRD_UNIT = 8;

    public static PathResponse assemble(SubwayPath subwayPath, Map<Long, Station> stations,
        List<Line> foundLines) {
        List<StationResponse> stationResponses = subwayPath.extractStationIds().stream()
            .map(it -> StationResponse.of(stations.get(it)))
            .collect(Collectors.toList());

        int distance = subwayPath.calculateDistance();
        int fare = calculateFare(distance, foundLines);

        return new PathResponse(stationResponses, subwayPath.calculateDuration(), distance, fare);
    }

    private static int calculateFare(int distance, List<Line> lines) {
        return DEFAULT_FARE
            + calculateOverFare(distance)
            + calculateExtraFare(lines);
    }

    static int calculateOverFare(int distance) {
        if (distance <= OVER_FARE_FIRST_POINT) {
            return DEFAULT_OVER_FARE;
        }
        if (distance <= OVER_FARE_SECOND_POINT) {
            return ((distance - OVER_FARE_FIRST_POINT) / OVER_FARE_SECOND_UNIT) * OVER_FARE_CHARGE;
        }
        return (((OVER_FARE_SECOND_POINT - OVER_FARE_FIRST_POINT) / OVER_FARE_SECOND_UNIT)
            * OVER_FARE_CHARGE)
            + (((distance - OVER_FARE_SECOND_POINT) / OVER_PARE_THIRD_UNIT) * OVER_FARE_CHARGE);
    }

    private static int calculateExtraFare(List<Line> lines) {
        return lines.stream()
            .mapToInt(Line::getExtraFare)
            .sum();
    }
}
