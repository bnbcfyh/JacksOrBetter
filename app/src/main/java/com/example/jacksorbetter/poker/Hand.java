package com.example.jacksorbetter.poker;

import java.util.ArrayList;
import java.util.Collections;

import com.example.jacksorbetter.playing_cards.Card;


/* Hand class is used simply for holding the card array of the
 * current poker hand. It only has one private field, "cards" array.
 * More cards can be added to the hand as the game goes by in different
 * types of games with the public "addCard" method.
 */
public class Hand {

    private ArrayList<Card> cards;

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public Hand(Card[] cards) {

        this.cards = new ArrayList<>();

        for (int i = 0; i < cards.length; i++) {
            this.cards.add(cards[i]);
        }

    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void sort() {
        Collections.sort(cards);
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        String result = "";

        for (int i = 0; i < cards.size(); i++) {
            result += cards.get(i) + " ";
        }

        return result;
    }

}
