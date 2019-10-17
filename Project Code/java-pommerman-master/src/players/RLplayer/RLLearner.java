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

    RLLearner (GameState g){
        currentState = g;
        random = new Random();
        ArrayList<Types.ACTIONS> actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
    }

    public void learn(){
        //apply qlearning algorithm here
    }

    //calculate reward for state here
    private int calculateReward(){
        int newQ = random.nextInt();
        int selectedAction = random.nextInt(actions.length);   //Get current state, execute action randomly or with neural net
        int reward = 0;

        boolean stop = false;

        while(!stop){

            GameState state = currentState.copy();
            ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
            stop = true;
            //Get reward, get new state prime, update Q value with bellman equation
            //Keep original state, action, state reached after action, reward and if game ended or not in storage
            //stop after X amount of time
            //TODO
        }

        return newQ;


    }
}
