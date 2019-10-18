package players.RLplayer;

import core.GameState;
import utils.Types;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RLPolicy {
    int[] dimSize;
    double[] qValues;
    private Object qValuesTable;
    GameState[] states;
    Types.ACTIONS[] actions;

    private double alpha = 0.1; //Learning rate
    private double gamma = 0.3; //Eagerness

    private int reward = 100;

    private double getPolicyFromState(GameState g){
        ArrayList<Types.ACTIONS> actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        double maxVal = Double.MAX_VALUE;

        GameState copy = g.copy();

        //pick action to move to state that has maximum Q value
        //TODO

        return maxVal;
    }





}
