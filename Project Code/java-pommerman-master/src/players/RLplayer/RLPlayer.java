package players.RLplayer;

import core.GameState;
import players.Player;
import utils.Types;

public class RLPlayer extends Player {

    /**
     * Default constructor, to be called in subclasses (initializes player ID and random seed for this agent.
     *
     * @param seed - random seed for this player.
     * @param pId  - this player's ID.
     */
    protected RLPlayer(long seed, int pId) {
        super(seed, pId);
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
}
