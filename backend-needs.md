## 😌 레벨2 최종 미션을 임하는 자세

레벨2 과정을 스스로의 힘으로 구현했다는 것을 증명하는데 집중해라

- [ ] 기능 목록을 잘 작성한다.  
- [ ] 자신이 구현한 기능에 대해 인수 테스트를 작성한다.
- [ ] 자신이 구현한 코드에 대해 단위 테스트를 작성한다.
- [ ] TDD 사이클 이력을 볼 수 있도록 커밋 로그를 잘 작성한다.
- [ ] 사용자 친화적인 예외처리를 고민한다.
- [ ] 읽기 좋은 코드를 만든다.

## 🎯 요구사항

- [x] 1. 경로 조회 응답 결과에 `요금` 정보 추가(필수)
- [ ] 2. `가장 빠른 경로 도착` 경로 타입 추가(선택)

## 📑 기능 요구사항 상세

### 1. 경로 조회 응답 결과에 요금 정보 추가

1. 인수 테스트 작성
   - 최단 거리 경로 조회시 요금 추가
   - 최소 시간 경로 조회시 요금 추가
2. 문서화
   - 최단 거리 경로 조회 문서화
   - 최소 시간 경로 조회 문서화
3. TDD로 개발

### 2. 가장 빠른 도착 경로 타입 추가

1. 인수 테스트 작성
2. 문서화
3. TDD로 개발

#### 기능 제약사항

- 노선의 첫차/막차 시간은 24:00을 넘기지 못하며 첫차 시간은 막차 시간 보다 항상 이르다.
- 막차가 끊길 경우 다음날 첫차 기준으로 계산한다.
- 이동 시간과 승하차 시간은 고려하지 않는다. 1호선과 2호선이 교차하는 C역에서 1호선에서 2호선으로 환승해야 하는 경우 1호선 도착 시간이 14:30이고 2호선 출발 시간이 14:30일 경우에도 환승할 수 있음

#### 가장 빠른 도착 경로 계산 방법

ex) 14:00에 A역에서 D역으로 이동할 때 A-B-D와 A-C-D 경로가 존재하는 경우

- A-B 구간의 노선을 조회하고 상행/하행을 판단
- 노선의 첫차 시간과 간격을 활용하여 14:00 이후 A-B 방향으로 A역에 가장 빨리 도착하는 시간 계산
- A-B의 소요시간을 조회하여 B역 도착 시간을 계산
- B역 도착 시간 이후 B-D 구간의 노선을 조회하고 상행/하행을 판단
- 노선의 첫차 시간과 간격을 활용하여 B역 도착 시간 이후 B-D 방향으로 B역에 가장 빨리 도착하는 시간 계산
- B-D의 소요시간을 조회하여 D역 도착 시간을 계산
- 같은 방법으로 A-C-D 경로의 도착 시간을 계산하여 빨리 도착하는 경로를 응답

#### 기존 코드 설명 - Line(노선)

- 노선은 시간과 관련된 첫차 시간, 막차 시간, 간격의 정보를 가지고 있음
- 각 노선의 첫 역에서 마지막 역으로 가는 방향(하행)과 마지막 역에서 첫 역으로 가는 방향(상행)이 존재함
- 첫차 시간은 첫 역과 마지막 역에서 하루 중 처음 지하철이 출발하는 시간을 의미
- 막차 시간은 첫 역과 마지막 역에서 하루 중 마지막으로 지하철이 출발하는 시간을 의미



``` java
package wooteco.subway.maps.line.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class LineStations {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "line_id", foreignKey = @ForeignKey(name = "fk_line_station_to_line"))
    private List<LineStation> lineStations = new ArrayList<>();

    public List<LineStation> getLineStations() {
        return lineStations;
    }

    public List<LineStation> getStationsInOrder() {
        // 출발지점 찾기
        Optional<LineStation> preLineStation = lineStations.stream()
            .filter(it -> it.getPreStationId() == null)
            .findFirst();

        List<LineStation> result = new ArrayList<>();
        while (preLineStation.isPresent()) {
            LineStation preStationId = preLineStation.get();
            result.add(preStationId);
            preLineStation = lineStations.stream()
                .filter(it -> it.getPreStationId().equals(preStationId.getStationId()))
                .findFirst();
        }
        return result;
    }

    public boolean isForward(LineStation startLineStation, LineStation endLineStation) {
        List<LineStation> stationsInOrder = getStationsInOrder();
        int startLineStationIndex = stationsInOrder.indexOf(startLineStation);
        int endLineStationIndex = stationsInOrder.indexOf(endLineStation);

        return startLineStationIndex < endLineStationIndex;
    }

    public void add(LineStation lineStation) {
        checkValidation(lineStation);

        lineStations.stream()
            .filter(it -> it.getPreStationId().equals(lineStation.getPreStationId()))
            .findFirst()
            .ifPresent(it -> it.updatePreStationTo(lineStation.getStationId()));

        lineStations.add(lineStation);
    }

    private void checkValidation(LineStation lineStation) {
        if (lineStation.getStationId() == null) {
            throw new RuntimeException();
        }

        if (lineStations.stream().anyMatch(it -> it.isSame(lineStation))) {
            throw new RuntimeException();
        }
    }

    public void removeByStationId(Long stationId) {
        LineStation lineStation = lineStations.stream()
            .filter(it -> it.getStationId().equals(stationId))
            .findFirst()
            .orElseThrow(RuntimeException::new);

        lineStations.stream()
            .filter(it -> it.getPreStationId().equals(stationId))
            .findFirst()
            .ifPresent(it -> it.updatePreStationTo(lineStation.getPreStationId()));

        lineStations.remove(lineStation);
    }
}

```

