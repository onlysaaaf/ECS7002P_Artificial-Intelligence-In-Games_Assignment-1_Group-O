package players.RLplayer;

import core.GameState;
import players.Player;
import players.heuristics.AdvancedHeuristic;
import players.heuristics.CustomHeuristic;
import utils.Types;
import java.util.Random;

public class RLPlayer extends Player {

    private Random random;
    private GameState currentState;
    /**
     * Default constructor, to be called in subclasses (initializes player ID and random seed for this agent.
     *     * @param seed - random seed for this player.
     * @param pId  - this player's ID.
     */
    protected RLPlayer(long seed, int pId) {
        super(seed, pId);
        random = new Random();
    }

    @Override
    public Types.ACTIONS act(GameState gs) {
        return null;
    }

    @Override
    public int[] getMessage() {
        return new int[0];
    }

    @Override
    public Player copy() {
        return null;
    }

    private int calculateReward(){
        int newQ = random.nextInt();
        //TODO

        //Get current state, execute action randomly or with neural net
        //Get reward, get new state prime, update Q value with bellman equation
        //Keep original state, action, state reached after action, reward and if game ended or not in storage
        return newQ;


    }

    void setCurrentGameState(GameState gs)
    {
        //TODO
    }
}
