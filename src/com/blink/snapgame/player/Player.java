package com.blink.snapgame.player;

import com.blink.snapgame.card.Card;

import java.util.LinkedList;
import java.util.Stack;

public abstract class Player {
    private final String name;
    private final LinkedList<Card> hand;

    public abstract Card playCard();

    public Player(String name) {
        this.name = name;
        this.hand = new LinkedList<>();
    }

    public final String getName() {
        return name;
    }

    public final LinkedList<Card> getHand() {
        return hand;
    }

    public final void addCard(Card card) {
        hand.add(card);
    }

    public final void takeStack(Stack<Card> stack) {
        while (!stack.isEmpty()) {
            hand.addLast(stack.pop());
        }
    }
}
