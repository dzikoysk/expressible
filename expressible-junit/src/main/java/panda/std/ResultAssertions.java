package panda.std;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static panda.std.Result.error;
import static panda.std.Result.ok;

public final class ResultAssertions {

    public static void assertOk(Result<?, ?> result) {
        if (result.isErr()) {
            fail("Expected success, but result errored with: <" + result.getError() + ">");
        }
    }

    public static <VALUE> void assertOk(VALUE value, Result<VALUE, ?> result) {
        assertOk(result);
        assertEquals(ok(value), result);
    }

    public static void assertError(Result<?, ?> result) {
        if (result.isOk()) {
            fail("Expected failure, but result succeeded with: <" + result.get() + ">");
        }
    }

    public static <ERROR> void assertError(ERROR error, Result<?, ERROR> result) {
        assertError(result);
        assertEquals(error(error), result);
    }

}
