package com.example.jacksorbetter.poker;

import com.example.jacksorbetter.playing_cards.Card;


/* This class is used for identifying a given poker hand.
 * Also you can check whether a given poker hand is a certain type
 * or not with the public boolean methods below. Or you can simply
 * check the hand's type by calling the identifyHand method with giving
 * the hand as a parameter.
 *
 * Also, this HandIdentifier can be used for Texas Hold'em Poker and Five
 * Card Draw and of course, Video Poker, because it works for both 5 card hands
 * and 7 card hands. It works accordingly.
 */
public final class HandIdentifier {

    private HandIdentifier() {} // cannot instantiate any
                                // object from HandIdentifier class


    // returns the type as a concatenated String of the given Hand paramater
    public static String identifyHand(Hand hand) {

        String result = "";

        if (isPair(hand))
            result += "Pair";
        else if (isTwoPair(hand))
            result += "Two Pair";
        else if (isThreeKind(hand))
            result += "Three of a Kind";
        else if (isStraight(hand))
            result += "Straight";
        else if (isFlush(hand))
            result += "Flush";
        else if (isFullHouse(hand))
            result += "Full House";
        else if (isFourKind(hand))
            result += "Four of a Kind";
        else if (isStraightFlush(hand))
            result += "Straight Flush";
        else if (isRoyalFlush(hand))
            result += "Royal Flush";
        else
            result += "High Card";

        return result;
    }


    public static boolean isPair(Hand hand) {

        if (isTwoPair(hand) || isThreeKind(hand) || isStraight(hand) || isFlush(hand) || isFullHouse(hand)
                || isFourKind(hand) || isStraightFlush(hand) || isRoyalFlush(hand))
            return false;

        for (int i = 0; i < hand.getCards().size() - 1; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            for (int j = i + 1; j < hand.getCards().size(); j++) {

                int valueSecond = hand.getCards().get(j).getValue();

                if (valueFirst == valueSecond)
                    return true;
            }
        }

        return false;
    }

    public static boolean isTwoPair(Hand hand) {

        if (isThreeKind(hand) || isStraight(hand) || isFlush(hand) || isFullHouse(hand) || isFourKind(hand)
                || isStraightFlush(hand) || isRoyalFlush(hand))
            return false;

        int matchedPairs = 0;

        for (int i = 0; i < hand.getCards().size() - 1; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            for (int j = i + 1; j < hand.getCards().size(); j++) {

                int valueSecond = hand.getCards().get(j).getValue();

                if (valueFirst == valueSecond)
                    matchedPairs++;
            }
        }

        if (matchedPairs >= 2)
            return true;

        return false;
    }

    public static boolean isThreeKind(Hand hand) {

        if (isStraight(hand) || isFlush(hand) || isFullHouse(hand) || isFourKind(hand) || isStraightFlush(hand)
                || isRoyalFlush(hand))
            return false;

        for (int i = 0; i < hand.getCards().size() - 2; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            for (int j = i + 1; j < hand.getCards().size() - 1; j++) {

                int valueSecond = hand.getCards().get(j).getValue();

                if (valueFirst == valueSecond) {

                    for (int k = j + 1; k < hand.getCards().size(); k++) {

                        int valueThird = hand.getCards().get(k).getValue();

                        if (valueThird == valueSecond)
                            return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean isStraight(Hand hand) {
        if (isFlush(hand) || isFullHouse(hand) || isFourKind(hand) || isStraightFlush(hand) || isRoyalFlush(hand))
            return false;

        hand.sort();

        if (hand.getCards().size() == 5) {
            return straightCheckFiveCards(hand);
        }


        Card card0 = hand.getCards().get(0);
        Card card1 = hand.getCards().get(1);
        Card card2 = hand.getCards().get(2);
        Card card3 = hand.getCards().get(3);
        Card card4 = hand.getCards().get(4);
        Card card5 = hand.getCards().get(5);
        Card card6 = hand.getCards().get(6);

        Card[] set1 = {card0, card1, card2, card3, card4};
        Card[] set2 = {card1, card2, card3, card4, card5};
        Card[] set3 = {card2, card3, card4, card5, card6};
        Card[] set4 = {card6, card0, card1, card2, card3};

        Hand hand1 = new Hand(set1);
        Hand hand2 = new Hand(set2);
        Hand hand3 = new Hand(set3);
        Hand hand4 = new Hand(set4);

        boolean result = straightCheckFiveCards(hand1) || straightCheckFiveCards(hand2) || straightCheckFiveCards(hand3)
                || straightCheckFiveCards(hand4);

        return result;
    }

    // checks whether the given five cards build a straight hand
    private static boolean straightCheckFiveCards(Hand hand) {
        int i, testRank;

        hand.sort(); // Sort the poker hand by the rank of each card

        if (hand.getCards().get(4).getValue() == 14) { // hand has an ace

            boolean a = hand.getCards().get(0).getValue() == 2 && hand.getCards().get(1).getValue() == 3
                    && hand.getCards().get(2).getValue() == 4 && hand.getCards().get(3).getValue() == 5;
            boolean b = hand.getCards().get(0).getValue() == 10 && hand.getCards().get(1).getValue() == 11
                    && hand.getCards().get(2).getValue() == 12 && hand.getCards().get(3).getValue() == 13;

            return (a || b);
        } else { // hand doesn't have an ace

            testRank = hand.getCards().get(0).getValue() + 1;

            for (i = 1; i < 5; i++) {
                if (hand.getCards().get(i).getValue() != testRank)
                    return (false); // Straight failed...
                testRank++; // Next card in hand
            }

            return (true); // Straight found !
        }
    }

    public static boolean isFlush(Hand hand) {
        if (isFullHouse(hand) || isFourKind(hand) || isStraightFlush(hand) || isRoyalFlush(hand))
            return false;

        return containsFlush(hand);
    }

    // checks whether the given hand contains flush
    private static boolean containsFlush(Hand hand) {
        int countClubs = 0;
        int countDiamonds = 0;
        int countHearts = 0;
        int countSpades = 0;

        for (int i = 0; i < hand.getCards().size(); i++) {

            if (hand.getCards().get(i).getSuit().equals("clubs"))
                countClubs++;

            else if (hand.getCards().get(i).getSuit().equals("diamonds"))
                countDiamonds++;

            else if (hand.getCards().get(i).getSuit().equals("hearts"))
                countHearts++;

            else
                countSpades++;

        }

        if (countClubs >= 5 || countDiamonds >= 5 || countHearts >= 5 || countSpades >= 5)
            return true;
        return false;
    }

    public static boolean isFullHouse(Hand hand) {
        if (isFourKind(hand) || isStraightFlush(hand) || isRoyalFlush(hand))
            return false;

        int tripleValue = 0;
        boolean foundTriple = false;
        boolean foundPair = false;

        searchTriple:
        for (int i = 0; i < hand.getCards().size() - 2; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            for (int j = i + 1; j < hand.getCards().size() - 1; j++) {

                int valueSecond = hand.getCards().get(j).getValue();

                if (valueFirst == valueSecond) {

                    for (int k = j + 1; k < hand.getCards().size(); k++) {

                        int valueThird = hand.getCards().get(k).getValue();

                        if (valueThird == valueSecond) {
                            tripleValue = valueThird;
                            foundTriple = true;
                            break searchTriple;
                        }
                    }
                }
            }
        }

        searchPair:
        for (int i = 0; i < hand.getCards().size() - 1; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            if (valueFirst != tripleValue) {
                for (int j = i + 1; j < hand.getCards().size(); j++) {

                    int valueSecond = hand.getCards().get(j).getValue();

                    if (valueFirst == valueSecond) {
                        foundPair = true;
                        break searchPair;
                    }
                }
            }
        }

        return foundTriple && foundPair;
    }

    public static boolean isFourKind(Hand hand) {
        if (isStraightFlush(hand) || isRoyalFlush(hand))
            return false;

        for (int i = 0; i < hand.getCards().size() - 3; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            for (int j = i + 1; j < hand.getCards().size() - 2; j++) {

                int valueSecond = hand.getCards().get(j).getValue();

                if (valueFirst == valueSecond) {

                    for (int k = j + 1; k < hand.getCards().size() - 1; k++) {

                        int valueThird = hand.getCards().get(k).getValue();

                        if (valueThird == valueSecond) {

                            for (int l = k + 1; l < hand.getCards().size(); l++) {

                                int valueFourth = hand.getCards().get(l).getValue();

                                if (valueFourth == valueFirst)
                                    return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public static boolean isStraightFlush(Hand hand) {
        if (isRoyalFlush(hand))
            return false;

        return containsStraightFlush(hand);
    }

    // checks whether the given hand containts straight flush
    private static boolean containsStraightFlush(Hand hand) {
        hand.sort();

        if (hand.getCards().size() == 5) {
            if (containsFlush(hand) && straightCheckFiveCards(hand)) {
                return true;
            } else {
                return false;
            }
        }

        Card card0 = hand.getCards().get(0);
        Card card1 = hand.getCards().get(1);
        Card card2 = hand.getCards().get(2);
        Card card3 = hand.getCards().get(3);
        Card card4 = hand.getCards().get(4);
        Card card5 = hand.getCards().get(5);
        Card card6 = hand.getCards().get(6);

        Card[] set1 = {card0, card1, card2, card3, card4};
        Card[] set2 = {card1, card2, card3, card4, card5};
        Card[] set3 = {card2, card3, card4, card5, card6};
        Card[] set4 = {card6, card0, card1, card2, card3};

        Hand hand1 = new Hand(set1);
        Hand hand2 = new Hand(set2);
        Hand hand3 = new Hand(set3);
        Hand hand4 = new Hand(set4);

        if (containsFlush(hand1) && straightCheckFiveCards(hand1)) {
            return true;
        }

        if (containsFlush(hand2) && straightCheckFiveCards(hand2)) {
            return true;
        }

        if (containsFlush(hand3) && straightCheckFiveCards(hand3)) {
            return true;
        }

        if (containsFlush(hand4) && straightCheckFiveCards(hand4)) {
            return true;
        }

        return false;
    }

    public static boolean isRoyalFlush(Hand hand) {

        if (!containsStraightFlush(hand))
            return false;

        boolean aceFound = false;
        boolean kingFound = false;
        boolean queenFound = false;
        boolean jackFound = false;
        boolean tenFound = false;

        for (int i = 0; i < hand.getCards().size(); i++) {
            if (hand.getCards().get(i).getValue() == 10)
                tenFound = true;
            else if (hand.getCards().get(i).getValue() == 11)
                jackFound = true;
            else if (hand.getCards().get(i).getValue() == 12)
                queenFound = true;
            else if (hand.getCards().get(i).getValue() == 13)
                kingFound = true;
            else if (hand.getCards().get(i).getValue() == 14)
                aceFound = true;
        }

        return aceFound && kingFound && queenFound && jackFound && tenFound;
    }

}
