package com.blink.snapgame.game;

import com.blink.snapgame.card.Card;
import com.blink.snapgame.card.Suit;
import com.blink.snapgame.card.Value;
import com.blink.snapgame.match.MatchType;
import com.blink.snapgame.player.ComputerPlayer;
import com.blink.snapgame.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class GameManager {
    private final List<Player> players;
    private final MatchType matchType;
    private final List<Stack<Card>> stacks;
    private final Random random;
    private final int maxCardsToPlay;

    public GameManager(int numDecks, int numPlayers, MatchType matchType, int maxCardsToPlay) {
        this.matchType = matchType;
        this.random = new Random();
        this.maxCardsToPlay = maxCardsToPlay;
        this.stacks = createPlayerStacks(numPlayers);
        this.players = createAndDistributeCards(numDecks, numPlayers);
    }

    public void startGame() {
        int cardsPlayed = 0;
        while (isGameOn(cardsPlayed)) {
            playRound();
            cardsPlayed++;
        }

        var remainingPlayers = players.stream().filter(p -> !p.getHand().isEmpty()).toList();
        if(remainingPlayers.size() == 1) {
            System.out.println(players.getFirst().getName() + " wins!");
        } else {
            System.out.println("Game ended after " + cardsPlayed + " cards played.");
        }

    }

    private boolean isGameOn(int cardsPlayed) {
        return players.stream().filter(p -> !p.getHand().isEmpty()).count() > 1 &&
                (maxCardsToPlay == 0 || cardsPlayed < maxCardsToPlay);
    }

    private void playRound() {
        for (int i = 0; i < players.size(); i++) {
            Player currentPlayer = players.get(i);
            if (currentPlayer.getHand().isEmpty()) {
                continue;
            }
            Card card = currentPlayer.playCard();
            stacks.get(i).add(card);

            checkSnap(currentPlayer);
        }
    }

    private void checkSnap(Player currentPlayer) {
        for (int i = 0; i < stacks.size(); i++) {
            if (stacks.get(i).isEmpty()) {
                continue;
            }
            for (int j = i + 1; j < stacks.size(); j++) {
                if (stacks.get(j).isEmpty()) {
                    continue;
                }
                if (isMatch(stacks.get(i).peek(), stacks.get(j).peek())) {
                    if (random.nextDouble() < 0.8) {
                        Player snapper = players.get(random.nextInt(players.size()));
                        System.out.println(snapper.getName() + " calls Snap!");
                        snapper.takeStack(stacks.get(i));
                        snapper.takeStack(stacks.get(j));
                        stacks.get(j).clear();
                        stacks.get(j).clear();
                        return;
                    } else {
                        System.out.println("Snap missed on this turn!");
                    }
                }
            }
        }
    }

    private boolean isMatch(Card card1, Card card2) {
        return switch (matchType) {
            case SUIT -> card1.getSuit() == card2.getSuit();
            case VALUE -> card1.getValue() == card2.getValue();
            case BOTH -> card1.getSuit() == card2.getSuit() && card1.getValue() == card2.getValue();
        };
    }

    private List<Player> createAndDistributeCards(int numDecks, int numPlayers) {
        List<Card> deck = createAndShuffleDecks(numDecks);
        List<Player> players = createPlayers(numPlayers);
        distributeCards(deck, players);
        return players;
    }

    private List<Card> createAndShuffleDecks(int numDecks) {
        List<Card> deck = new ArrayList<>();
        for (int i = 0; i < numDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Value value : Value.values()) {
                    deck.add(new Card(suit, value));
                }
            }
        }
        Collections.shuffle(deck);
        return deck;
    }

    private List<Player> createPlayers(int numPlayers) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            players.add(new ComputerPlayer("Player " + (i + 1))); //or human player
        }
        return players;
    }

    private void distributeCards(List<Card> deck, List<Player> players) {
        int numPlayers = players.size();
        int cardsPerPlayer = deck.size() / numPlayers;
        int cardIndex = 0;

        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.addCard(deck.get(cardIndex++));
            }
        }
    }

    private List<Stack<Card>> createPlayerStacks(int numPlayers) {
        List<Stack<Card>> stack = new ArrayList<>(numPlayers);
        while (numPlayers-- > 0) {
            stack.add(new Stack<>());
        }
        return stack;
    }
}