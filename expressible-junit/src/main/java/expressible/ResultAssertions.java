package expressible;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static expressible.Result.error;
import static expressible.Result.ok;

public final class ResultAssertions {

    private ResultAssertions() {}

    public static <VALUE> VALUE assertOk(Result<? extends VALUE, ?> result) {
        if (result.isErr()) {
            fail("Expected success, but result errored with: <" + result.getError() + ">");
        }

        return result.get();
    }

    public static <VALUE> void assertOk(VALUE value, Result<? extends VALUE, ?> result) {
        assertOk(result);
        Assertions.assertEquals(Result.ok(value), result);
    }

    public static <ERROR> ERROR assertError(Result<?, ERROR> result) {
        if (result.isOk()) {
            fail("Expected failure, but result succeeded with: <" + result.get() + ">");
        }

        return result.getError();
    }

    public static <ERROR> void assertError(ERROR error, Result<?, ERROR> result) {
        assertError(result);
        Assertions.assertEquals(Result.error(error), result);
    }

}
