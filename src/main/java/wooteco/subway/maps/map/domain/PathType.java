package wooteco.subway.maps.map.domain;

import java.util.function.Function;
import wooteco.subway.maps.line.domain.LineStation;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration),
    ARRIVAL(LineStation -> 0);

    private Function<LineStation, Integer> expression;

    PathType(Function<LineStation, Integer> expression) {
        this.expression = expression;
    }

    public int findWeightOf(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
