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

import javax.swing.*;
import java.security.Policy;
import java.util.*;
import java.util.stream.Collectors;


public class RLPlayer extends Player {

    private Random random;
    private GameState currentState;
    private Types.ACTIONS[] actions;
    RLParams params;
    RLLearner learner;
    RLPolicy policy;

    /**
     * Default constructor, to be called in subclasses (initializes player ID and random seed for this agent.
     *     * @param seed - random seed for this player.
     * @param pId  - this player's ID.
     */
    public RLPlayer(long seed, int pId) {
        super(seed, pId);
        random = new Random();
        ArrayList<Types.ACTIONS> actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        params = new RLParams();
        learner = new RLLearner(this.currentState);
        policy = new RLPolicy();
        //System.out.println(RLLearner.qVals.isEmpty());
        if(RLLearner.qVals.isEmpty() ) {
            RLLearner.initialiseMap();
        }
        int i = 0;
        for (Types.ACTIONS act : actionsList) {
            actions[i++] = act;
        }

    }

    @Override
    public Types.ACTIONS act(GameState gs) {
        //TODO

        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        ect.setMaxTimeMillis(params.num_time);

        // Number of actions available
        int num_actions = actions.length;
        currentState = gs;
        GameState copyState = currentState.copy();

        learner.learn(copyState);

        if(Types.DEFAULT_VISION_RANGE ==-1){
            //everything visible
            //sort list
//            for(Map.Entry<Pair,Double> e :qvalues){
//                System.out.println(e.getKey() + "=>" + e.getValue());
//            }

            Double bestQVal = Double.MIN_VALUE;
            Vector2d optimalCord = new Vector2d(0,0);
           // Map<Vector2d, Double> sorted_qvals = convertToTreeMap(RLLearner.qVals);
            for (Map.Entry<Vector2d, Double> entry : RLLearner.qVals.entrySet()) {
               // System.out.println(entry.getKey() + " = " + entry.getValue());
                if(entry.getValue() > bestQVal){
                    bestQVal = entry.getValue();
                    optimalCord = entry.getKey();
                   // System.out.println(optimalCord);
                }
            }

            copyState = currentState.copy();
            Types.ACTIONS pickedAction = actions[0];
            Vector2d bestCord = new Vector2d(gs.getPosition());
            for(Types.ACTIONS a : actions){
                GameState next = policy.roll(copyState, a);
//                System.out.println(" Current pos " + copyState.getPosition());
//                System.out.println("Next pos " + next.getPosition());
                if((next.getPosition().dist(optimalCord) < bestCord.dist(optimalCord))){ //compare distances
                    bestCord = next.getPosition();
                    pickedAction = a;
                }
            }

            return pickedAction;




//            System.out.println(bestQVal);
//            System.out.println(optimalCord);

//            Set<Map.Entry<Vector2d, Double>> qvalues = sorted_qvals.entrySet();
//            Comparator<Map.Entry<Vector2d,Double>> comparator = new Comparator<Map.Entry<Vector2d, Double>>() {
//                @Override
//                public int compare(Map.Entry<Vector2d, Double> pairDoubleEntry, Map.Entry<Vector2d, Double> t1) {
//                    Vector2d cord1 = pairDoubleEntry.getKey();
//                    Double qVal = pairDoubleEntry.getValue();
//                    Vector2d cord2 = t1.getKey();
//                    Double qVal2 = t1.getValue();
//                    System.out.println(qVal);
//                    System.out.println(qVal2);
//                    System.out.println(Double.compare(qVal,qVal2));
//
//
//                    return Double.compare(qVal,qVal2);
//                }
//            };



        }else if(Types.DEFAULT_VISION_RANGE > 1) {
            //Types.whatever  = vision range
        }else{
            return null;
        }

        return actions[1];
    }

    @Override
    public int[] getMessage() {
        return new int[0];
    }

    @Override
    public Player copy() {

        RLPlayer copy =  new RLPlayer(this.seed, this.playerID);
        copy.setCurrentGameState(this.currentState);
        return copy;
    }



    private void setCurrentGameState(GameState gs)
    {
        currentState = gs;
    }

    private GameState getCurrentState(){
        return  currentState;
    }


    public <K, V> Map<K, V> convertToTreeMap(Map<K, V> hashMap)
    {
        Map<K, V>
                treeMap = hashMap
                // Get the entries from the hashMap
                .entrySet()

                // Convert the map into stream
                .stream()

                // Now collect the returned TreeMap
                .collect(
                        Collectors

                                // Using Collectors, collect the entries
                                // and convert it into TreeMap
                                .toMap(
                                        Map.Entry::getKey,
                                        Map.Entry::getValue,
                                        (oldValue,
                                         newValue)
                                                -> newValue,
                                        TreeMap::new));

        // Return the TreeMap
        return treeMap;
    }
}
