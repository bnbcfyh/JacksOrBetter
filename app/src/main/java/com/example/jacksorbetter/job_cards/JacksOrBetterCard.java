package com.example.jacksorbetter.job_cards;

import com.example.jacksorbetter.playing_cards.Card;

/* A JacksOrBetterCard has only one different feature than the
 * normal Card object, that is isHeld field and methods to hold
 * and release the card.
 */
public class JacksOrBetterCard extends Card {

    private boolean isHeld;

    public JacksOrBetterCard(int value, String suit) {
        super(value, suit);
        this.isHeld = false;
    }

    public void hold() {
        isHeld = true;
    }

    public void release() {
        isHeld = false;
    }

    public boolean isHeld() {
        return isHeld;
    }
}
