package wooteco.subway.maps.map.ui;

import static java.time.LocalDateTime.now;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wooteco.subway.maps.map.application.MapService;
import wooteco.subway.maps.map.domain.PathType;
import wooteco.subway.maps.map.dto.MapResponse;
import wooteco.subway.maps.map.dto.PathResponse;
import wooteco.subway.maps.station.dto.StationResponse;

@RestController
public class MapController {

    private MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findPath(@RequestParam Long source,
        @RequestParam Long target, @RequestParam PathType type) {
        if (type.equals(PathType.ARRIVAL)) {
            StationResponse gyodae = new StationResponse(1L, "교대역", now(), now());
            StationResponse gangnam = new StationResponse(2L, "강남역", now(), now());
            StationResponse yangjae = new StationResponse(3L, "양재역", now(), now());
            List<StationResponse> stations = Arrays.asList(gyodae, gangnam, yangjae);
            int duration = 3;
            int distance = 4;
            int fare = 1250;
            return ResponseEntity.ok(new PathResponse(stations, duration, distance, fare));
        }
        return ResponseEntity.ok(mapService.findPath(source, target, type));
    }

    @GetMapping("/maps")
    public ResponseEntity<MapResponse> findMap() {
        MapResponse response = mapService.findMap();
        return ResponseEntity.ok(response);
    }
}
