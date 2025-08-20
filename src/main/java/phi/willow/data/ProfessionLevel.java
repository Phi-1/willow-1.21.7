package phi.willow.data;

public enum ProfessionLevel {
    NOVICE(3 * 64, "Novice", 10),
    APPRENTICE(3 * 27 * 64, "Apprentice", 45),
    EXPERT(15 * 27 * 64, "Expert", 100),
    MASTER(0, "Master", 0);

    public final String label;
    public final int xpToNext;
    public final int playerLevelsToNext;

    ProfessionLevel(int xpToNext, String label, int playerLevelsToNext)
    {
        this.label = label;
        this.xpToNext = xpToNext;
        this.playerLevelsToNext = playerLevelsToNext;
    }
}
