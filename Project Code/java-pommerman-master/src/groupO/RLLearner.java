package groupO;

import core.GameState;
import utils.ElapsedCpuTimer;
import utils.Types;
import utils.Vector2d;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class   RLLearner {
    private GameState currentState;
    private Random random;
    private Types.ACTIONS[] actions;
    private RLParams params;
   // private ArrayList<Double> qValues;
    //private ArrayList<GameState> states;
     static HashMap<Vector2d,Double> qVals;
     static Vector2d positions;
    ArrayList<Types.ACTIONS> actionsList;
    double bestQ = Double.MIN_VALUE;


    File deathFileDir = new File(".");
    File deathFile = new File(deathFileDir, "death.txt");
    private RLPolicy policy;

    RLLearner (GameState g){
        currentState = g;
        random = new Random();
        actionsList = Types.ACTIONS.all();
        actions = new Types.ACTIONS[actionsList.size()];
        policy = new RLPolicy();
         params = new RLParams();
         //qValues = new ArrayList<>();
       //  states = new ArrayList<>();
         qVals = new HashMap();


    }

    static void initialiseMap(){
        for (int x =0; x<Types.BOARD_SIZE;x++){
            for (int y = 0; y<Types.BOARD_SIZE; y++){
                positions = new Vector2d(x,y);
                qVals.put(positions,Double.MIN_VALUE);
            }
        }


    }


    public void learn(GameState copyState){
        //get action to return
        Vector2d currentpos = copyState.getPosition();
        int currenty = currentpos.y;
        int currentx = currentpos.x;
        Vector2d currentPair = new Vector2d(currentx,currenty);
        double Qval = qVals.get(currentPair);
        boolean stop = false;
        int numIters = 0;
        int acumTimeTaken = 0;
        int avgTimeTaken = 0;
        long remaining = 0;
        int remainingLimit = 10;

        ElapsedCpuTimer ect = new ElapsedCpuTimer();
        ect.setMaxTimeMillis(100);
     ElapsedCpuTimer elapsedTimerIteration = new ElapsedCpuTimer();

        //System.out.println("enter learn ");
        //int bestAction = 0;
    Types.ACTIONS bestAction = actionsList.get(random.nextInt(actionsList.size()));
        while (!stop) {
            for (Types.ACTIONS a : actionsList) {
                GameState next = policy.roll(copyState, a);
                Vector2d nextPos = next.getPosition();
                Vector2d newPair = new Vector2d(nextPos.x, nextPos.y);
                double q = policy.evaluate(next, qVals.get(newPair), Double.MAX_VALUE); //Eval new state
                if (q>bestQ){
                    bestQ = q;
                    bestAction = a;
                }
                qVals.put(newPair, q);

            }


            if(Types.DEFAULT_VISION_RANGE ==-1) { //if full visibility then can search through whole board
                for (int x = 0; x < Types.BOARD_SIZE; x++) {
                    for (int y = 0; y < Types.BOARD_SIZE; y++) {
                        Vector2d currentStatePos = new Vector2d(x, y);
                        double q = policy.evaluate(policy.roll(copyState, actionsList.get(random.nextInt(actionsList.size()))), qVals.get(currentStatePos), Double.MAX_VALUE); //Update all qvalues in map by rolling and evaluating random action
                        qVals.put(currentpos, q);
                        if (q > bestQ) {
                            bestQ = q;
                        }

                    }
                }
            }else{
                for (int x = copyState.getPosition().x; x < Types.DEFAULT_VISION_RANGE + copyState.getPosition().x; x++) { //for partial visbility only search through vision range
                    for (int y = copyState.getPosition().y; y < Types.DEFAULT_VISION_RANGE +copyState.getPosition().y; y++) {
                        Vector2d currentStatePos = new Vector2d(x, y);
                        double q = policy.evaluate(policy.roll(copyState, actionsList.get(random.nextInt(actionsList.size()))), qVals.get(currentStatePos), Double.MAX_VALUE); //Update all qvalues in map by rolling and evaluating random action
                        qVals.put(currentpos, q);
                        if (q > bestQ) {
                            bestQ = q;
                        }

                    }
                }
                outerloop:
                for (int x = copyState.getPosition().x; x < Types.DEFAULT_VISION_RANGE - copyState.getPosition().x; x--) { //for partial visbility only search through vision range
                    for (int y = copyState.getPosition().y; y < Types.DEFAULT_VISION_RANGE -copyState.getPosition().y; y--) {
                        Vector2d currentStatePos = new Vector2d(x, y);
                        if(x>=0 &&y>=0) {
                            double q = policy.evaluate(policy.roll(copyState, actionsList.get(random.nextInt(actionsList.size()))), qVals.get(currentStatePos), Double.MAX_VALUE); //Update all qvalues in map by rolling and evaluating random action
                            qVals.put(currentpos, q);
                            if (q > bestQ) {
                                bestQ = q;
                            }
                        }else {
                            break outerloop;
                        }


                    }
                }
            }
            copyState = policy.roll(copyState,bestAction); //get best state to learn from based on action

            if (params.stop_type == params.STOP_TIME) {
                numIters++;
                acumTimeTaken += (elapsedTimerIteration.elapsedMillis());
                avgTimeTaken = acumTimeTaken / numIters;
                remaining = ect.remainingTimeMillis();
                stop = remaining <= 2 * avgTimeTaken || remaining <= remainingLimit;
            }
            if(copyState.isTerminal()){
                    try {
                        FileWriter fw = new FileWriter(deathFile);
                        fw.write("Death of player at " + copyState.getPosition().toString());
                        System.out.println("Death of player at " + copyState.getPosition().toString());
                   } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

            }

        }







    }


}
