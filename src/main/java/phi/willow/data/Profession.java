package phi.willow.data;

public enum Profession {
    MINING("Mining"),
    WOODCUTTING("Woodcutting"),
    FARMING("Farming"),
    FIGHTING("Fighting");

    public final String label;

    private Profession(String label)
    {
        this.label = label;
    }
}
