package phi.willow.data;

public enum ProfessionLevel {
    NOVICE(1, "Novice"),
    APPRENTICE(1, "Apprentice"),
    EXPERT(1, "Expert"),
    MASTER(0, "Master");

    public final String label;
    public final int xpToNext;

    ProfessionLevel(int xpToNext, String label)
    {
        this.label = label;
        this.xpToNext = xpToNext;
    }
}
