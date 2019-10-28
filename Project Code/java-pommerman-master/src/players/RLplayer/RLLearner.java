package players.RLplayer;

import core.GameState;
import players.Player;
import players.heuristics.AdvancedHeuristic;
import players.heuristics.CustomHeuristic;
import players.mcts.SingleTreeNode;
import utils.ElapsedCpuTimer;
import utils.Pair;
import utils.Types;
import utils.Vector2d;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class   RLLearner {
    private GameState currentState;
    private Random random;
    private Types.ACTIONS[] actions;
    private RLParams params;
    private ArrayList<Double> qValues;
    private ArrayList<GameState> states;
     static HashMap<Pair,Double> qVals;
     static Pair<Integer,Integer> positions;
    ArrayList<Types.ACTIONS> actionsList;


    private RLPolicy policy;

    RLLearner (GameState g){
        currentState = g;
        random = new Random();
        actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        policy = new RLPolicy();
         params = new RLParams();
         qValues = new ArrayList<>();
         states = new ArrayList<>();
         qVals = new HashMap();


    }

    static void initialiseMap(){
        for (int x =0; x<Types.BOARD_SIZE;x++){
            for (int y = 0; y<Types.BOARD_SIZE; y++){
                positions = new Pair<>(x,y);
                qVals.put(positions,Double.MIN_VALUE);
            }
        }


    }


    public int learn(){
        //get action to return
        GameState copyState = currentState.copy();
        Vector2d currentpos = copyState.getPosition();
        int currenty = currentpos.y;
        int currentx = currentpos.x;
        Pair currentPair = new Pair(currentx,currenty);
        double Qval = qVals.get(currentPair);

        double newQ = policy.evaluate(copyState, Qval, Double.MAX_VALUE );
        for(Types.ACTIONS a: actionsList){
            GameState next = policy.roll(copyState, a);
            Vector2d nextPos = next.getPosition();
            Pair newPair = new Pair(nextPos.x,nextPos.y);
            double q = policy.evaluate(next, qVals.get(newPair), Double.MAX_VALUE);
            qVals.put(newPair,q);
        }
        //for time limit
            //Evaluate current state

            // (loop) evaluate coridinates in the line of sight
            //Move to best value based on current and cordinates around it
            //if every value is same choose random state
                //calculate q val based on action and store it




        return 0;
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
        int actionToPick = 0;


        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        ect.setMaxTimeMillis(40);


        while (!stop) {
            states.add(state);
            newQ = policy.getPolicyFromState(state);
            ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();
            qValues.add(newQ);

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


        }
        return newQ;



    }
}
