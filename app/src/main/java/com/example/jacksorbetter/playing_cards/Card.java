package com.example.jacksorbetter.playing_cards;

public class Card implements Comparable<Card> {

    protected int value; // (J = 11, Q = 12, K = 13, A = 14)
    protected String suit;
    protected boolean isDealt;

    public Card(int value, String suit) {

        this.value = value;
        this.suit = suit;

    }

    public void deal() {
        isDealt = true;
    }

    public int getValue() {
        return value;
    }

    public String getSuit() {
        return suit;
    }

    public boolean isDealt() {
        return isDealt;
    }

    @Override
    public String toString() {

        String valueToPrint = this.value + "";

        if (this.value == 11) {
            valueToPrint = "J";
        } else if (this.value == 12) {
            valueToPrint = "Q";
        } else if (this.value == 13) {
            valueToPrint = "K";
        } else if (this.value == 14) {
            valueToPrint = "A";
        }

        String suitToPrint = "";

        switch (this.suit) {

            case "clubs":
                suitToPrint = "c";
                break;
            case "diamonds":
                suitToPrint = "d";
                break;
            case "hearts":
                suitToPrint = "h";
                break;
            case "spades":
                suitToPrint = "s";
                break;

        }

        return valueToPrint + suitToPrint;
    }

    /* compare the card to another card, cards are compared according to their values
     * Ace is the biggest card, not counted as one.
     */

    @Override
    public int compareTo(Card card) {

        if (this.getValue() > card.getValue())
            return 1;
        else if (this.getValue() < card.getValue())
            return -1;
        else
            return 0;
    }

}
