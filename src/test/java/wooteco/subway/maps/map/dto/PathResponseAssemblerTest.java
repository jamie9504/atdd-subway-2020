package wooteco.subway.maps.map.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PathResponseAssemblerTest {

    @DisplayName("0 ~ 10까지 계산 테스트")
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void calculateOverFare_0To10_Return0(int input) {
        assertThat(PathResponseAssembler.calculateOverFare(input)).isEqualTo(0);
    }

    @DisplayName("11 ~ 50까지 계산 테스트")
    @ParameterizedTest
    @ValueSource(ints = {11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 49, 50})
    void calculateOverFare_11To50_Return100의배수(int input) {
        int expected = ((input - 10) / 5) * 100;

        assertThat(PathResponseAssembler.calculateOverFare(input)).isEqualTo(expected);
    }

    @DisplayName("55 ~ 계산 테스트")
    @ParameterizedTest
    @ValueSource(ints = {51, 52, 53, 54, 55, 56, 100})
    void calculateOverFare_50To_Return100의배수(int input) {
        int expected = (((50 - 10) / 5) * 100) + (((input - 50) / 8) * 100);
        assertThat(PathResponseAssembler.calculateOverFare(input)).isEqualTo(expected);
    }
}
