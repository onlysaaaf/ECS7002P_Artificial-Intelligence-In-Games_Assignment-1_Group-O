package players.RLplayer;

import core.GameState;
import players.Player;
import players.heuristics.AdvancedHeuristic;
import players.heuristics.CustomHeuristic;
import players.mcts.SingleTreeNode;
import utils.ElapsedCpuTimer;
import utils.Types;

import java.util.ArrayList;
import java.util.Random;

public class RLLearner {
    private GameState currentState;
    private Random random;
    private Types.ACTIONS[] actions;



    private RLPolicy policy;

    RLLearner (GameState g){
        currentState = g;
        random = new Random();
        ArrayList<Types.ACTIONS> actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        policy = new RLPolicy();
    }

    public void learn(){
        //apply qlearning algorithm here
    }

    //calculate reward for state here
    private double calculateQ(){
        int reward = 0;

        boolean stop = false;

        GameState state = currentState.copy();
        ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
        int numOfActions = actions.length;
        int index = random.nextInt(numOfActions);
        double newQ =  RLPolicy.getPolicyFromState(currentState);
            //stop = true;
            //Get reward, get new state prime, update Q value with bellman equation
            //Keep original state, action, state reached after action, reward and if game ended or not in storage
            //stop after X amount of time
            //TODO
        return newQ;


    }
}
