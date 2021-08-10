package panda.std;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static panda.std.Option.of;

public final class OptionAssertions {

    public static void assertIsDefined(Option<?> option) {
        option.onEmpty(() -> fail("Option is empty"));
    }

    public static <VALUE> void assertOptionEquals(VALUE value, Option<VALUE> option) {
        assertEquals(of(value), option);
    }

    public static void assertEmpty(Option<?> option) {
        option.peek(value -> fail("Option " + option + " is not empty"));
    }

}
