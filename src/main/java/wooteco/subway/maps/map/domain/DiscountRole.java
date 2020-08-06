package wooteco.subway.maps.map.domain;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountRole {

    CHILD(1, age -> 6 <= age && age < 13, price -> (price - 350) * 0.5),
    YOUTH(2, age -> 13 <= age && age < 19, price -> (price - 350) * 0.8),
    NORMAL(3, age -> true, Double::valueOf);

    private final int order;
    private final Predicate<Integer> condition;
    private final Function<Integer, Double> discount;

    DiscountRole(int order, Predicate<Integer> condition, Function<Integer, Double> discount) {
        this.order = order;
        this.condition = condition;
        this.discount = discount;
    }

    public static DiscountRole from(int age) {
        return Arrays.stream(DiscountRole.values())
            .filter(discountRole -> discountRole.condition.test(age))
            .min(Comparator.comparingInt(discountRole -> discountRole.order))
            .orElseThrow(() -> new IllegalArgumentException("해당하는 DiscountRole이 없습니다."));
    }

    public static DiscountRole defaultRole() {
        return NORMAL;
    }

    public double calculateDiscount(int price) {
        return discount.apply(price);
    }
}
