package players.RLplayer;

import core.GameState;
import players.heuristics.CustomHeuristic;
import players.heuristics.WinScoreHeuristic;
import utils.Types;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class RLPolicy {
    Types.ACTIONS[] actions;
    Random r = new Random();

    private double alpha = 0.1; //Learning rate
    private double gamma = 0.3; //Eagerness
    private double reward = 0;
    int actionToPick = 0;
    private ArrayList<Double> qValues = new ArrayList<>();
    private ArrayList<GameState> states = new ArrayList<>();


    public double getPolicyFromState(GameState g){
        ArrayList<Types.ACTIONS> actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        double maxQVal = Double.MAX_VALUE;
        double Qval = Double.MIN_VALUE;
        GameState sPrime = null;
        GameState copy = g.copy();

        for(int i =0; i<actions.length; ++i){
            GameState next = (roll(copy,actions[i]));
            states.add(next);
            Qval = evaluate(next,Qval,maxQVal);
            qValues.add(Qval);
            if(Qval > maxQVal)
                maxQVal = Qval;
            if (next.isTerminal()){
                sPrime = next;
                break;
            }

        }


        //pick action to move to state that has maximum Q value
        //TODO

        return maxQVal;
    }

    public GameState roll(GameState gs, Types.ACTIONS act)
    {
        //Simple, all random first, then my position.
        int nPlayers = 4;
        Types.ACTIONS[] actionsAll = new Types.ACTIONS[4];
        int playerId = gs.getPlayerId() - Types.TILETYPE.AGENT0.getKey();

        for(int i = 0; i < nPlayers; ++i)
        {
            if(playerId == i)
            {
                actionsAll[i] = act;
            }else {
                int actionIdx = r.nextInt(gs.nActions());
                actionsAll[i] = Types.ACTIONS.all().get(actionIdx);
            }
        }

        gs.next(actionsAll);

        return gs;

    }

    public Double evaluate(GameState gs, double Qval, double maxQval){
        CustomHeuristic heuristic = new CustomHeuristic(gs);
        reward = heuristic.evaluateState(gs);

        return Qval + alpha*(reward+ gamma * maxQval - Qval) ;
    }

    public int getActionToPick(){
        return actionToPick;
    }




}
