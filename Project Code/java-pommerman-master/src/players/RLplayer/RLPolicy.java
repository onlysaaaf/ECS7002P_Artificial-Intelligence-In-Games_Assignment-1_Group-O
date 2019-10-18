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

    private double[] myQValues( GameState state ) {


        return null;
    }

    public double[] getQValuesAt( int[] state ) {

        int i;
        Object curTable = qValuesTable;
        double[] returnValues;


        return null;
    }


    public void setQValue( int[] state, int action, double newQValue ) {

        qValues = myQValues( state );
        Array.setDouble( qValues, action, newQValue );
    }

    public double getMaxQValue( int[] state ) {

        double maxQ = -Double.MAX_VALUE;

        return maxQ;
    }

    public double getQValue( int[] state, int action ) {

        double qValue = 0;


        return qValue;
    }

}
