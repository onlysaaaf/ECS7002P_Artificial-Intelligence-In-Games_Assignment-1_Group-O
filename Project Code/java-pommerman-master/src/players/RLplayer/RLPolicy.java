package players.RLplayer;

import core.GameState;
import utils.Types;

public class RLPolicy {
    int[] dimSize;
    double[] qValues;
    private Object qValuesTable;
    GameState[] states;
    Types.ACTIONS[] actions;
}
