package com.example.jacksorbetter.job_cards;

import java.util.ArrayList;

import com.example.jacksorbetter.playing_cards.*;

public class JacksOrBetterDeck extends Deck {


    // constructs a deck made of JacksOrBetterCard objects
    public JacksOrBetterDeck() {

        super.cards = new ArrayList<>();

        for (int i = 2; i <= 14; i++) {
            JacksOrBetterCard c = new JacksOrBetterCard(i, "clubs");
            cards.add(c);
        }

        for (int i = 2; i <= 14; i++) {
            JacksOrBetterCard c = new JacksOrBetterCard(i, "diamonds");
            cards.add(c);
        }

        for (int i = 2; i <= 14; i++) {
            JacksOrBetterCard c = new JacksOrBetterCard(i, "hearts");
            cards.add(c);
        }

        for (int i = 2; i <= 14; i++) {
            JacksOrBetterCard c = new JacksOrBetterCard(i, "spades");
            cards.add(c);
        }

    }

    public JacksOrBetterDeck(boolean shuffled) {
        this();
        if (shuffled)
            shuffle();
    }

    public JacksOrBetterCard draw() {
        Card topCard = this.cards.get(0);
        topCard.deal();

        this.cards.remove(0);

        return (JacksOrBetterCard) topCard;
    }

}
