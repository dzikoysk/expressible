package expressible;

/**
 * Alternative to Kotlin's Unit
 */
public final class Blank {

    public static final Blank BLANK = new Blank();

    public Blank() {}

    public Void toVoid() {
        return voidness();
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Blank;
    }

    public static Void voidness() {
        return null;
    }

}
