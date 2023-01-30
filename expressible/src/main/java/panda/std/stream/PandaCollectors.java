package panda.std.stream;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class PandaCollectors {

    private PandaCollectors() {}

    public static <T> BinaryOperator<T> throwingMerger() {
        return (u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

    public static <T> Collector<T, Object, List<T>> shufflingCollector(Random random) {
        return Collectors.collectingAndThen(Collectors.toList(), list -> {
            Collections.shuffle(list, random);
            return list;
        });
    }

}
