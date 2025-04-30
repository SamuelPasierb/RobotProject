package data;

public enum Turn {
    LEFT("Left", 1),
    STRAIGHT("Straight", 0),
    RIGHT("Right", -1);

    public final String name;
    public final int value;

    private Turn(String name, int value) {
        this.name = name;
        this.value = value;
    }
}
