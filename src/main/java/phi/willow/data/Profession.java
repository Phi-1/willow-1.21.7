package phi.willow.data;

public enum Profession {
    MINING("Mining", 1),
    WOODCUTTING("Woodcutting", 3),
    FARMING("Farming", 3),
    FIGHTING("Fighting", 6);

    public final String label;
    public final int instanceXP;

    Profession(String label, int instanceXP)
    {
        this.label = label;
        this.instanceXP = instanceXP;
    }
}
