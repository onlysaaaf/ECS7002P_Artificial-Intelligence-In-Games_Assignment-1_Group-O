package players.RLplayer;

import core.GameState;
import utils.Types;

import java.lang.reflect.Array;

public class RLPolicy {
    int[] dimSize;
    double[] qValues;
    private Object qValuesTable;
    GameState[] states;
    Types.ACTIONS[] actions;

    private double alpha = 0.1; //Learning rate
    private double gamma = 0.3; //Eagerness

    private int reward = 100;

    Types.ACTIONS getBestAction(){
        return null;
    }

    GameState getNextState(){
        return null;
    }





}
