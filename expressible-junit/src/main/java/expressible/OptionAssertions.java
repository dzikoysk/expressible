package expressible;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public final class OptionAssertions {

    private OptionAssertions() {}

    public static void assertIsDefined(Option<?> option) {
        option.onEmpty(() -> fail("Option is empty"));
    }

    public static <VALUE> void assertOptionEquals(VALUE value, Option<? extends VALUE> option) {
        assertIsDefined(option);
        assertEquals(value, option.get());
    }

    public static void assertEmpty(Option<?> option) {
        option.peek(value -> fail("Option " + option + " is not empty"));
    }

}
