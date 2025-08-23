package phi.willow.data;

import phi.willow.util.TickTimers;

public enum ProfessionLevel {
    NOVICE(3 * 64, "Novice", 10),
    APPRENTICE(3 * 27 * 64, "Apprentice", 30),
    EXPERT(15 * 27 * 64, "Expert", 100),
    MASTER(0, "Master", 0);

    public final String label;
    public final int xpToNext;
    public final int playerLevelsToNext;
    public int totalXPForNext;

    ProfessionLevel(int xpToNext, String label, int playerLevelsToNext)
    {
        this.label = label;
        this.xpToNext = xpToNext;
        this.playerLevelsToNext = playerLevelsToNext;
        TickTimers.schedule(this::calculateTotalXPForNext, this.ordinal() + 1);
    }

    private void calculateTotalXPForNext()
    {
        int totalPrevious = 0;
        for (int i = 0; i < this.ordinal(); i++)
        {
            totalPrevious += ProfessionLevel.values()[i].xpToNext;
        }
        this.totalXPForNext = totalPrevious + this.xpToNext;
    }

}
