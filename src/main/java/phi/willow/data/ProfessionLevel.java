package phi.willow.data;

public enum ProfessionLevel {
    NOVICE(3 * 64, "Novice"),
    APPRENTICE(3 * 27 * 64, "Apprentice"),
    EXPERT(15 * 27 * 64, "Expert"),
    MASTER(0, "Master");

    public final String label;
    public final int xpToNext;

    ProfessionLevel(int xpToNext, String label)
    {
        this.label = label;
        this.xpToNext = xpToNext;
    }
}
