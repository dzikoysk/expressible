package panda.std;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static panda.std.Result.error;
import static panda.std.Result.ok;

public final class ResultAssertions {

    public static void assertOk(Result<?, ?> result) {
        assertTrue(result.isOk());
    }

    public static <VALUE> void assertOk(VALUE value, Result<VALUE, ?> result) {
        assertEquals(ok(value), result);
    }

    public static void assertError(Result<?, ?> result) {
        assertTrue(result.isErr());
    }

    public static <ERROR> void assertError(ERROR error, Result<?, ERROR> result) {
        assertEquals(error(error), result);
    }

}
