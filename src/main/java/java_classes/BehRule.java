package java_classes;

public class BehRule {
    public final boolean hasInt;
    public final boolean hasAva;
    public final boolean hasSec;
    public final String rule;

    public BehRule(String rule, boolean hasInt, boolean hasAva, boolean hasSec) {
        this.hasInt = hasInt;
        this.hasAva = hasAva;
        this.hasSec = hasSec;
        this.rule = rule;
    }
}
