package panda.std.stream;

import java.util.function.BinaryOperator;

public final class PandaCollectors {

    private PandaCollectors() {}

    public static <T> BinaryOperator<T> throwingMerger() {
        return (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); };
    }

}
