package com.example.jacksorbetter.playing_cards;

import java.util.ArrayList;
import java.util.Random;

public class Deck {

    protected ArrayList<Card> cards;

    public Deck() {

        this.cards = new ArrayList<>();

        for (int i = 2; i <= 14; i++) {
            Card c = new Card(i, "clubs");
            cards.add(c);
        }

        for (int i = 2; i <= 14; i++) {
            Card c = new Card(i, "diamonds");
            cards.add(c);
        }

        for (int i = 2; i <= 14; i++) {
            Card c = new Card(i, "hearts");
            cards.add(c);
        }

        for (int i = 2; i <= 14; i++) {
            Card c = new Card(i, "spades");
            cards.add(c);
        }

    }

    public Deck(boolean shuffled) {

        this();
        if (shuffled)
            shuffle();

    }

    public void shuffle() {

        Random rand = new Random();
        int amountOfCards = this.cards.size();
        ArrayList<Card> shuffledCards = new ArrayList<>();

        for (int i = 0; i < amountOfCards; i++) {

            int index = rand.nextInt(amountOfCards - i);
            shuffledCards.add(this.cards.get(index));
            this.cards.remove(index);

        }

        this.cards.addAll(shuffledCards);

    }

    public Card draw() {

        Card topCard = this.cards.get(0);
        topCard.deal();

        this.cards.remove(0);

        return topCard;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {

        String str = "";

        for (int i = 0; i < this.cards.size(); i++) {
            str += this.cards.get(i) + " ";
        }

        return str;
    }

}
