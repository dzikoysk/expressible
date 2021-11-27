package panda.std;

public final class Unit {

    public static final Unit UNIT = new Unit();

    public Unit() {}

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Unit;
    }

}
