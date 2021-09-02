package panda.std;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static panda.std.Result.error;
import static panda.std.Result.ok;

public final class ResultAssertions {

    public static <VALUE> VALUE assertOk(Result<VALUE, ?> result) {
        if (result.isErr()) {
            fail("Expected success, but result errored with: <" + result.getError() + ">");
        }

        return result.get();
    }

    public static <VALUE> void assertOk(VALUE value, Result<VALUE, ?> result) {
        assertOk(result);
        assertEquals(ok(value), result);
    }

    public static <ERROR> ERROR assertError(Result<?, ERROR> result) {
        if (result.isOk()) {
            fail("Expected failure, but result succeeded with: <" + result.get() + ">");
        }

        return result.getError();
    }

    public static <ERROR> void assertError(ERROR error, Result<?, ERROR> result) {
        assertError(result);
        assertEquals(error(error), result);
    }

}
