package com.blink.snapgame.player;

import com.blink.snapgame.card.Card;

public class ComputerPlayer extends Player {

    public ComputerPlayer(String name) {
        super(name);
    }

    @Override
    public Card playCard() {
        return getHand().removeFirst();
    }
}
