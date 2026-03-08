package WagonFortShootout.java.framework.ai;

import java.util.Arrays;

public class StateMachine {

    private final State[] accepted_states;
    private State state;

    public StateMachine(State[] accepted_states)  {
        this(accepted_states, accepted_states[0]);
    }

    public StateMachine(State[] accepted_states, State state)  {
        if(accepted_states.length < 2) {
            throw new RuntimeException("State machine must have at least 2 acceptable states.");
        }
        this.accepted_states = accepted_states;
        this.state = state;
    }

    public boolean is(State state) {
        return this.state == state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if(!Arrays.asList(accepted_states).contains(state)) {
            throw new RuntimeException("Attempted to enter illegal state " + state.toString() + ".");
        }
        this.state = state;
    }
}
