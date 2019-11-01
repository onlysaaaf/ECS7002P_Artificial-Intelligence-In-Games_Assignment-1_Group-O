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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    File deathFile = new File(".");


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

            copyState = currentState.copy(); //copy current state for game simulation
            Types.ACTIONS pickedAction = actions[0]; //set default picked action
            Vector2d bestCord = new Vector2d(gs.getPosition());
            GameState bestState = copyState; //get best state
            Vector2d bestStatePos = bestState.getPosition(); //get pos for best state
            Vector2d bestPair = new Vector2d(bestStatePos.x,bestStatePos.y); // create vector for best state pos
            int notMovedFor = 0;

            for(Types.ACTIONS a : actions){
                GameState next = policy.roll(copyState, a);
//                System.out.println(" Current pos " + copyState.getPosition());
//                System.out.println("Next pos " + next.getPosition());
                if((next.getPosition().dist(optimalCord) < bestCord.dist(optimalCord))){ //compare distances
                    bestCord = next.getPosition();
                    pickedAction = a;
                }
                Vector2d nextPos = next.getPosition();
                Vector2d newPair = new Vector2d(nextPos.x,nextPos.y);
                if(policy.evaluate(next, RLLearner.qVals.get(newPair), Double.MAX_VALUE) > policy.evaluate(bestState, RLLearner.qVals.get(bestPair),Double.MAX_VALUE )){
                    bestState = next;
                    pickedAction = a;
                    bestStatePos = bestState.getPosition();
                    bestPair = new Vector2d(bestStatePos.x,bestStatePos.y);


                }

                if(next.getPosition() == copyState.getPosition()){
                    notMovedFor ++;
                    if(notMovedFor > 5) {
                        pickedAction = actions[random.nextInt(actions.length)];
                       // GameState next2 = policy.roll(copyState, pickedAction);

                        //condition to make so player doesn't kill itself
//                        while (pickedAction == Types.ACTIONS.ACTION_BOMB && next2.getPosition() == next.getPosition()) {
//                            pickedAction = actions[random.nextInt(actions.length)];
//                        }
                    }

                }
//                if(pickedAction == Types.ACTIONS.ACTION_BOMB){
//                    System.out.println("RL PLAYER PLANTED BOMB");
//                }


                //Log deaths in file

                Types.TILETYPE[] aliveAgentIDs = currentState.getAliveAgentIDs();
                boolean isDead = false;
                for(Types.TILETYPE agent: aliveAgentIDs){

                }
                try(FileWriter fw=new FileWriter(deathFile))
                {
                    fw.write("INSERT ME WHERE MY JAR IS");
                    fw.flush();
                    fw.close();
                }catch(IOException ex)
                {
                    ex.printStackTrace();
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


}
