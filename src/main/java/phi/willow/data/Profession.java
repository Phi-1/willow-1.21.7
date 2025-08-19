package phi.willow.data;

public enum Profession {
    // TODO: instance xp, basically mult on how much xp each instance of xp gain gives, because it makes sense you'd have to mine much more stone than chop logs for the same level
    MINING("Mining", 1),
    WOODCUTTING("Woodcutting", 3),
    FARMING("Farming", 6),
    FIGHTING("Fighting", 6);

    public final String label;
    public final int instanceXP;

    Profession(String label, int instanceXP)
    {
        this.label = label;
        this.instanceXP = instanceXP;
    }
}
