package wooteco.subway.maps.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private int extraFare;

    protected Fare() {
    }

    Fare(int extraFare) {
        validate(extraFare);
        this.extraFare = extraFare;
    }

    private void validate(int extraFare) {
        if (extraFare < 0) {
            throw new IllegalArgumentException("이용 금액 [" + extraFare + "]은 0원보다 적을 수 없습니다.");
        }
    }

    public int getExtraFare() {
        return extraFare;
    }

    public void setExtraFare(int extraFare) {
        validate(extraFare);
        this.extraFare = extraFare;
    }
}
