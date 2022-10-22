package panda.std.stream;

import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Random;

/**
 * Original code from https://stackoverflow.com/a/35846296
 */
public final class RandomComparator<T> implements Comparator<T> {

    private final Map<T, Integer> map = new IdentityHashMap<>();
    private final Random random;

    public RandomComparator() {
        this(new Random());
    }

    public RandomComparator(Random random) {
        this.random = random;
    }

    @Override
    public int compare(T t1, T t2) {
        return Integer.compare(this.valueFor(t1), this.valueFor(t2));
    }

    private int valueFor(T t) {
        synchronized (this.map) {
            return this.map.computeIfAbsent(t, ignore -> this.random.nextInt());
        }
    }

}
