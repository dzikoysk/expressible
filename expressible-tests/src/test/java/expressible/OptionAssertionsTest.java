package expressible;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import expressible.Option;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static expressible.OptionAssertions.assertEmpty;
import static expressible.OptionAssertions.assertIsDefined;
import static expressible.OptionAssertions.assertOptionEquals;

public class OptionAssertionsTest {

    @Test
    void shouldAssertIsDefined() {
        assertIsDefined(Option.of("test"));
        assertThrows(AssertionFailedError.class, () -> assertIsDefined(Option.none()));
    }

    @Test
    void shouldAssertOptionEquals() {
        assertOptionEquals("test", Option.of("test"));

        Option<? extends String> value = Option.of("test");
        assertOptionEquals("test", value);

        assertThrows(AssertionFailedError.class, () -> assertOptionEquals("test", Option.none()));
        assertThrows(AssertionFailedError.class, () -> assertOptionEquals("test", Option.of("err")));
    }

    @Test
    void shouldAssertEmpty() {
        assertEmpty(Option.none());
        assertThrows(AssertionFailedError.class, () -> assertEmpty(Option.of("test")));
    }

}
