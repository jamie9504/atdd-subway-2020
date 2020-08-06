package wooteco.subway.maps.map.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class DiscountRoleTest {

    @DisplayName("어린이 Role 생성")
    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    void from_6To12_ReturnChild(Integer age) {
        DiscountRole discountRole = DiscountRole.from(age);
        assertThat(discountRole).isEqualTo(DiscountRole.CHILD);
    }


    @DisplayName("청소년 Role 생성")
    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18})
    void from_12To18_ReturnYouth(Integer age) {
        DiscountRole discountRole = DiscountRole.from(age);
        assertThat(discountRole).isEqualTo(DiscountRole.YOUTH);
    }

    @DisplayName("그 외 일반 Role 생성")
    @ParameterizedTest
    @NullSource
    @ValueSource(ints = {5, 19, 100})
    void from_Not6To18_ReturnNormal(Integer age) {
        DiscountRole discountRole = DiscountRole.from(age);
        assertThat(discountRole).isEqualTo(DiscountRole.NORMAL);
    }

    @DisplayName("할인 계산")
    @ParameterizedTest
    @ValueSource(ints = {1550, 2550, 3550})
    void calculateDiscount(int price) {
        int expectedChild = (int) ((price - 350) * 0.5);
        int expectedYouth = (int) ((price - 350) * 0.8);

        assertThat(DiscountRole.CHILD.calculateDiscount(price)).isEqualTo(expectedChild);
        assertThat(DiscountRole.YOUTH.calculateDiscount(price)).isEqualTo(expectedYouth);
        assertThat(DiscountRole.NORMAL.calculateDiscount(price)).isEqualTo(price);
    }
}