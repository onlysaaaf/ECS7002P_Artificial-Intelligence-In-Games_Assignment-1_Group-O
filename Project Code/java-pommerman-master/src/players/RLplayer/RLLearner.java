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
    private RLParams params;
    private ArrayList<Double> qValues;
    private ArrayList<GameState> states;


    private RLPolicy policy;

    RLLearner (GameState g){
        currentState = g;
        random = new Random();
        ArrayList<Types.ACTIONS> actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        policy = new RLPolicy();
         params = new RLParams();
         qValues = new ArrayList<>();
         states = new ArrayList<>();

    }

    public int learn(){
        //get action to return
        int actionToPick = 0;
        GameState copyState = currentState.copy();



        return actionToPick;
    }

    //calculate reward for state here
    private double calculateQ(GameState state) {
        int index = 0;
        int numIters = 0;
        boolean stop = false;
        int acumTimeTaken = 0;
        int avgTimeTaken = 0;
        long remaining = 0;
        int remainingLimit = 5;
        int fmCallsCount = 0;
        double newQ = Double.MIN_VALUE;

        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        ect.setMaxTimeMillis(40);


        while (!stop) {
            states.add(state);
            newQ = policy.getPolicyFromState(state);
            ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
            //TODO

            if (params.stop_type == params.STOP_TIME) {
                numIters++;
                acumTimeTaken += (elapsedTimerIteration.elapsedMillis());
                avgTimeTaken = acumTimeTaken / numIters;
                remaining = ect.remainingTimeMillis();
                stop = remaining <= 2 * avgTimeTaken || remaining <= remainingLimit;
            } else if (params.stop_type == params.STOP_ITERATIONS) {
                numIters++;
                stop = numIters >= params.num_iterations;
            } else if (params.stop_type == params.STOP_FMCALLS) {
                fmCallsCount += params.rollout_depth;
                stop = (fmCallsCount + params.rollout_depth) > params.num_fmcalls;
            }
            qValues.add(newQ);


        }
        return newQ;



    }
}
