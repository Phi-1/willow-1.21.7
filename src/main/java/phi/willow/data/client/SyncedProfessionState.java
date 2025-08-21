package phi.willow.data.client;

import phi.willow.data.PlayerProfessionState;

import java.util.ArrayList;

public class SyncedProfessionState {

    /**
     * ONLY ACCESS ON LOGICAL CLIENT
     */
    public static PlayerProfessionState state = new PlayerProfessionState(new ArrayList<>());

}
