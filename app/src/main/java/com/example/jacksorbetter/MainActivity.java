package com.example.jacksorbetter;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.example.jacksorbetter.job_cards.*;
import com.example.jacksorbetter.poker.*;


public class MainActivity extends AppCompatActivity {


    private final float HOLD_ALPHA_CONSTANT = (float) 0.444444444; // set alpha of cards when held
    private final int DEFAULT_BALANCE = 50; // default balance


    private JacksOrBetterDeck deck;

    private JacksOrBetterCard[] cards;
    private Hand hand;
    private ImageView[] cardImages;

    private ImageView handType;

    private int balance;
    private int betAmount;

    // images of decimal values of balance
    private ImageView onesDecimal;
    private ImageView tensDecimal;
    private ImageView hundredsDecimal;


    private ImageView betImage; // bet amount image

    // images of increase and decrease buttons
    private ImageButton incBetButton;
    private ImageButton decBetButton;

    private ImageButton maximumBetButton;

    private ImageButton dealButton;

    // these are shown when the balance hits zero
    private ImageButton claimFiftyButton;
    private ImageView youLost;
    private ImageView takeThis;


    // true if the round is finished, i.e. second deal is made
    private boolean isFinished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deck = new JacksOrBetterDeck(true);

        cards = new JacksOrBetterCard[5];
        isFinished = false;

        cardImages = new ImageView[5];

        cardImages[0] = findViewById(R.id.card1);
        cardImages[1] = findViewById(R.id.card2);
        cardImages[2] = findViewById(R.id.card3);
        cardImages[3] = findViewById(R.id.card4);
        cardImages[4] = findViewById(R.id.card5);


        // closed cards are not clickable
        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i].setClickable(false);
        }

        handType = findViewById(R.id.handType);

        // do not show the hand type until the game is over
        handType.setVisibility(View.INVISIBLE);

        dealButton = findViewById(R.id.dealButton);

        this.balance = DEFAULT_BALANCE;

        this.onesDecimal = findViewById(R.id.onesDecimal);
        this.tensDecimal = findViewById(R.id.tensDecimal);
        this.hundredsDecimal = findViewById(R.id.hundredsDecimal);

        this.betAmount = 1;

        this.betImage = findViewById(R.id.betImage);
        this.decBetButton = findViewById(R.id.decBetButton);
        this.incBetButton = findViewById(R.id.incBetButton);

        this.claimFiftyButton = findViewById(R.id.claimFiftyButton);
        this.youLost = findViewById(R.id.youLost);
        this.takeThis = findViewById(R.id.takeThis);

        this.maximumBetButton = findViewById(R.id.maximumBetButton);

        updateCredit(); // update the balance digits images

    }


    // these methods are called when the first, second,..., fifth card is held
    public void hold1(View v) {

        if (cards[0].isHeld())
            cards[0].release();

        else
            cards[0].hold();

        updateCard(cards[0], 0);

        playHoldSound();

    }

    public void hold2(View v) {
        if (cards[1].isHeld())
            cards[1].release();

        else
            cards[1].hold();

        updateCard(cards[1], 1);

        playHoldSound();
    }

    public void hold3(View v) {
        if (cards[2].isHeld())
            cards[2].release();

        else
            cards[2].hold();

        updateCard(cards[2], 2);

        playHoldSound();

    }

    public void hold4(View v) {
        if (cards[3].isHeld())
            cards[3].release();

        else
            cards[3].hold();

        updateCard(cards[3], 3);

        playHoldSound();
    }

    public void hold5(View v) {
        if (cards[4].isHeld())
            cards[4].release();

        else
            cards[4].hold();

        updateCard(cards[4], 4);

        playHoldSound();

    }


    // called whenever the deal button is clicked
    public void deal(View v) {
        playDealSound();

        // first deal
        if (isFinished) {
            bet();
            handType.setVisibility(View.INVISIBLE);
            isFinished = false;
            JacksOrBetterDeck deck = new JacksOrBetterDeck(true);
            this.deck = deck;
            for (int i = 0; i < cards.length; i++) {
                cards[i] = deck.draw();
            }

            this.hand = new Hand(cards);

            for (int i = 0; i < cardImages.length; i++) {
                cardImages[i].setClickable(true);
            }

            decBetButton.setClickable(false);
            incBetButton.setClickable(false);
            maximumBetButton.setClickable(false);

        }

        // opening deal or second deal
        else {

            // opening deal of the game
            if (cards[0] == null) {
                bet();

                for (int i = 0; i < cards.length; i++) {
                    cards[i] = this.deck.draw();
                }

                this.hand = new Hand(cards);

                for (int i = 0; i < cardImages.length; i++) {
                    cardImages[i].setClickable(true);
                }

                decBetButton.setClickable(false);
                incBetButton.setClickable(false);
                maximumBetButton.setClickable(false);

            }

            // second deal
            else {
                for (int i = 0; i < cards.length; i++) {
                    if (!cards[i].isHeld()) {
                        cards[i] = deck.draw();
                    }
                    this.hand = new Hand(cards);
                }


                for (int i = 0; i < cardImages.length; i++) {
                    cardImages[i].setClickable(false);
                }

                for (int i = 0; i < cards.length; i++) {
                    cards[i].release();
                }

                decBetButton.setClickable(true);
                incBetButton.setClickable(true);
                maximumBetButton.setClickable(true);


                isFinished = true;
                conclude();
                handType.setVisibility(View.VISIBLE);
                changeCardViews();
                if (checkIfBalanceZero())
                    return;
            }
        }

        changeCardViews();
    }


    /* This private method is used to decide whether the given "Pair" hand is
     * a "Jacks Or Better Pair" or not. Returns true if the pair is consisted of
     * Jacks, Queens, Kings, or Aces.
     */
    private boolean isPairJacksOrBetter() {

        for (int i = 0; i < hand.getCards().size() - 1; i++) {

            int valueFirst = hand.getCards().get(i).getValue();

            for (int j = i + 1; j < hand.getCards().size(); j++) {

                int valueSecond = hand.getCards().get(j).getValue();

                if (valueFirst == valueSecond) {

                    if (valueFirst >= 11)
                        return true;

                    else
                        return false;

                }
            }
        }

        return false;
    }


    // update the images of all cards
    private void changeCardViews() {
        for (int i = 0; i < cards.length; i++) {
            updateCard(cards[i], i);
        }
    }

    // update a given card at a given index
    private void updateCard(JacksOrBetterCard card, int index) {
        cardImages[index].setImageResource(convertCardToImage(card));

        if (card.isHeld()) {
            cardImages[index].setAlpha(HOLD_ALPHA_CONSTANT);
        } else {
            cardImages[index].setAlpha((float) 1);
        }

    }


    // returns the corresponding image for a JacksOrBetterCard object
    private int convertCardToImage(JacksOrBetterCard card) {

        String cardToString = card.toString();

        int result = 0;

        if (cardToString.equals("Ac")) {
            result = R.drawable.acec;
        } else if (cardToString.equals("As")) {
            result = R.drawable.aces;
        } else if (cardToString.equals("Ah")) {
            result = R.drawable.aceh;
        } else if (cardToString.equals("Ad")) {
            result = R.drawable.aced;

        } else if (cardToString.equals("Kc")) {
            result = R.drawable.kingc;

        } else if (cardToString.equals("Ks")) {
            result = R.drawable.kings;

        } else if (cardToString.equals("Kh")) {
            result = R.drawable.kingh;

        } else if (cardToString.equals("Kd")) {
            result = R.drawable.kingd;

        } else if (cardToString.equals("Qc")) {
            result = R.drawable.queenc;

        } else if (cardToString.equals("Qs")) {
            result = R.drawable.queens;

        } else if (cardToString.equals("Qh")) {
            result = R.drawable.queenh;

        } else if (cardToString.equals("Qd")) {
            result = R.drawable.queend;

        } else if (cardToString.equals("Jc")) {
            result = R.drawable.jackc;

        } else if (cardToString.equals("Js")) {
            result = R.drawable.jacks;

        } else if (cardToString.equals("Jh")) {
            result = R.drawable.jackh;

        } else if (cardToString.equals("Jd")) {
            result = R.drawable.jackd;

        } else if (cardToString.equals("10c")) {
            result = R.drawable.tenc;

        } else if (cardToString.equals("10s")) {
            result = R.drawable.tens;

        } else if (cardToString.equals("10h")) {
            result = R.drawable.tenh;

        } else if (cardToString.equals("10d")) {
            result = R.drawable.tend;

        } else if (cardToString.equals("9c")) {
            result = R.drawable.ninec;

        } else if (cardToString.equals("9s")) {
            result = R.drawable.nines;

        } else if (cardToString.equals("9h")) {
            result = R.drawable.nineh;

        } else if (cardToString.equals("9d")) {
            result = R.drawable.nined;

        } else if (cardToString.equals("8c")) {
            result = R.drawable.eightc;

        } else if (cardToString.equals("8s")) {
            result = R.drawable.eights;

        } else if (cardToString.equals("8h")) {
            result = R.drawable.eighth;

        } else if (cardToString.equals("8d")) {
            result = R.drawable.eightd;

        } else if (cardToString.equals("7c")) {
            result = R.drawable.sevenc;

        } else if (cardToString.equals("7s")) {
            result = R.drawable.sevens;

        } else if (cardToString.equals("7h")) {
            result = R.drawable.sevenh;

        } else if (cardToString.equals("7d")) {
            result = R.drawable.sevend;

        } else if (cardToString.equals("6c")) {
            result = R.drawable.sixc;

        } else if (cardToString.equals("6s")) {
            result = R.drawable.sixs;

        } else if (cardToString.equals("6h")) {
            result = R.drawable.sixh;

        } else if (cardToString.equals("6d")) {
            result = R.drawable.sixd;

        } else if (cardToString.equals("5c")) {
            result = R.drawable.fivec;

        } else if (cardToString.equals("5s")) {
            result = R.drawable.fives;

        } else if (cardToString.equals("5h")) {
            result = R.drawable.fiveh;

        } else if (cardToString.equals("5d")) {
            result = R.drawable.fived;

        } else if (cardToString.equals("4c")) {
            result = R.drawable.fourc;

        } else if (cardToString.equals("4s")) {
            result = R.drawable.fours;

        } else if (cardToString.equals("4h")) {
            result = R.drawable.fourh;

        } else if (cardToString.equals("4d")) {
            result = R.drawable.fourd;

        } else if (cardToString.equals("3c")) {
            result = R.drawable.threec;

        } else if (cardToString.equals("3s")) {
            result = R.drawable.threes;

        } else if (cardToString.equals("3h")) {
            result = R.drawable.threeh;

        } else if (cardToString.equals("3d")) {
            result = R.drawable.threed;

        } else if (cardToString.equals("2c")) {
            result = R.drawable.twoc;

        } else if (cardToString.equals("2s")) {
            result = R.drawable.twos;

        } else if (cardToString.equals("2h")) {
            result = R.drawable.twoh;

        } else if (cardToString.equals("2d")) {
            result = R.drawable.twod;
        }

        return result;

    }


    // called when the credit hits zero and the last game is lost
    private boolean checkIfBalanceZero() {

        if (this.balance == 0) {

            for (int i = 0; i < cardImages.length; i++) {
                cardImages[i].setAlpha((float) 0.166);
            }

            handType.setAlpha((float) 0.166);

            incBetButton.setClickable(false);
            decBetButton.setClickable(false);
            dealButton.setClickable(false);
            maximumBetButton.setClickable(false);

            youLost.setVisibility(View.VISIBLE);
            takeThis.setVisibility(View.VISIBLE);
            claimFiftyButton.setVisibility(View.VISIBLE);

            playGameOverSound();
            return true;

        } else return false;
    }

    // called when the button for claiming 50 extra credit to continue playing is pushed
    public void claimCredit(View v) {

        playHoldSound();

        for (int i = 0; i < cardImages.length; i++) {
            cardImages[i].setAlpha((float) 1);
            cardImages[i].setImageResource(R.drawable.b1fv);
        }

        handType.setAlpha((float) 1);
        handType.setVisibility(View.INVISIBLE);

        incBetButton.setClickable(true);
        decBetButton.setClickable(true);
        dealButton.setClickable(true);
        maximumBetButton.setClickable(true);

        youLost.setVisibility(View.INVISIBLE);
        takeThis.setVisibility(View.INVISIBLE);
        claimFiftyButton.setVisibility(View.INVISIBLE);

        this.balance += 50;
        updateCredit();

    }


    // when the game ends
    private void conclude() {

        String str = HandIdentifier.identifyHand(hand);

        if (str.equals("High Card")) {

            handType.setImageResource(R.drawable.high_card);
            playLoseSound();


        } else if (str.equals("Pair")) {
            if (isPairJacksOrBetter()) {
                handType.setImageResource(R.drawable.jacks_or_better);
                playShortWinSound();
                win(1);
            } else {
                handType.setImageResource(R.drawable.small_pair);
                playLoseSound();
            }
        } else if (str.equals("Two Pair")) {
            handType.setImageResource(R.drawable.two_pair);
            playMediumWinSound();
            win(2);

        } else if (str.equals("Three of a Kind")) {
            handType.setImageResource(R.drawable.three_of_a_kind);
            playMediumWinSound();
            win(3);

        } else if (str.equals("Straight")) {
            handType.setImageResource(R.drawable.straight);
            playLongWinSound();
            win(4);

        } else if (str.equals("Flush")) {
            handType.setImageResource(R.drawable.flush);
            playLongWinSound();
            win(6);
        } else if (str.equals("Full House")) {
            handType.setImageResource(R.drawable.full_house);
            playLongWinSound();
            win(9);
        } else if (str.equals("Four of a Kind")) {
            handType.setImageResource(R.drawable.four_of_a_kind);
            playCrazyWinSound();
            win(25);

        } else if (str.equals("Straight Flush")) {
            handType.setImageResource(R.drawable.straight_flush);
            playCrazyWinSound();
            win(50);

        } else if (str.equals("Royal Flush")) {
            handType.setImageResource(R.drawable.royal_flush);
            playCrazyWinSound();
            win(250);
        }
    }

    private void win(int coefficient) {
        balance += betAmount * coefficient;
        updateCredit();
    }

    private void updateCredit() {

        int credit = this.balance;

        if (credit > 999) {
            this.onesDecimal.setImageResource(R.drawable.dollar);
            this.tensDecimal.setImageResource(R.drawable.dollar);
            this.hundredsDecimal.setImageResource(R.drawable.dollar);
            return;
        }

        int onesDecimal;
        int tensDecimal;
        int hundredsDecimal;

        hundredsDecimal = credit / 100;
        credit -= hundredsDecimal * 100;

        tensDecimal = credit / 10;
        credit -= tensDecimal * 10;

        onesDecimal = credit;

        switch (onesDecimal) {
            case 0:
                this.onesDecimal.setImageResource(R.drawable.zero);
                break;
            case 1:
                this.onesDecimal.setImageResource(R.drawable.one);
                break;
            case 2:
                this.onesDecimal.setImageResource(R.drawable.two);
                break;
            case 3:
                this.onesDecimal.setImageResource(R.drawable.three);
                break;
            case 4:
                this.onesDecimal.setImageResource(R.drawable.four);
                break;
            case 5:
                this.onesDecimal.setImageResource(R.drawable.five);
                break;
            case 6:
                this.onesDecimal.setImageResource(R.drawable.six);
                break;
            case 7:
                this.onesDecimal.setImageResource(R.drawable.seven);
                break;
            case 8:
                this.onesDecimal.setImageResource(R.drawable.eight);
                break;
            case 9:
                this.onesDecimal.setImageResource(R.drawable.nine);
                break;
        }

        switch (tensDecimal) {
            case 0:
                this.tensDecimal.setImageResource(R.drawable.zero);
                break;
            case 1:
                this.tensDecimal.setImageResource(R.drawable.one);
                break;
            case 2:
                this.tensDecimal.setImageResource(R.drawable.two);
                break;
            case 3:
                this.tensDecimal.setImageResource(R.drawable.three);
                break;
            case 4:
                this.tensDecimal.setImageResource(R.drawable.four);
                break;
            case 5:
                this.tensDecimal.setImageResource(R.drawable.five);
                break;
            case 6:
                this.tensDecimal.setImageResource(R.drawable.six);
                break;
            case 7:
                this.tensDecimal.setImageResource(R.drawable.seven);
                break;
            case 8:
                this.tensDecimal.setImageResource(R.drawable.eight);
                break;
            case 9:
                this.tensDecimal.setImageResource(R.drawable.nine);
                break;
        }

        switch (hundredsDecimal) {
            case 0:
                this.hundredsDecimal.setImageResource(R.drawable.zero);
                break;
            case 1:
                this.hundredsDecimal.setImageResource(R.drawable.one);
                break;
            case 2:
                this.hundredsDecimal.setImageResource(R.drawable.two);
                break;
            case 3:
                this.hundredsDecimal.setImageResource(R.drawable.three);
                break;
            case 4:
                this.hundredsDecimal.setImageResource(R.drawable.four);
                break;
            case 5:
                this.hundredsDecimal.setImageResource(R.drawable.five);
                break;
            case 6:
                this.hundredsDecimal.setImageResource(R.drawable.six);
                break;
            case 7:
                this.hundredsDecimal.setImageResource(R.drawable.seven);
                break;
            case 8:
                this.hundredsDecimal.setImageResource(R.drawable.eight);
                break;
            case 9:
                this.hundredsDecimal.setImageResource(R.drawable.nine);
                break;
        }
    }


    // makes the necessary controls then decreases the balance by betAmount
    private void bet() {

        if (betAmount > balance) {
            betAmount = balance;
            updateBetAmount();
            balance -= betAmount;
        } else {
            balance -= betAmount;
        }
        updateCredit();
    }

    //adjusting the bet amount
    public void increaseBetAmount(View v) {
        playHoldSound();
        if (this.betAmount >= 5 || this.betAmount >= this.balance) {
            return;
        }
        this.betAmount++;
        updateBetAmount();
    }

    public void decreaseBetAmount(View v) {
        playHoldSound();
        if (betAmount <= 1) {
            return;
        }
        this.betAmount--;
        updateBetAmount();
    }

    public void betMaximum(View v) {

        playHoldSound();

        if (balance <= 5) {
            betAmount = balance;
        } else {
            betAmount = 5;
        }

        updateBetAmount();
    }


    // updates the images whenever the betAmount is changed
    private void updateBetAmount() {

        switch (betAmount) {
            case 1:
                betImage.setImageResource(R.drawable.one);
                break;
            case 2:
                betImage.setImageResource(R.drawable.two);
                break;
            case 3:
                betImage.setImageResource(R.drawable.three);
                break;
            case 4:
                betImage.setImageResource(R.drawable.four);
                break;
            case 5:
                betImage.setImageResource(R.drawable.five);
                break;
        }


    }


    // methods for playing the sound effects
    private void playHoldSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.hold);
        song.start();
    }

    private void playDealSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.deal);
        song.start();
    }

    private void playLoseSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.lose);
        song.start();
    }

    private void playCrazyWinSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.crazy_win);
        song.start();
    }

    private void playLongWinSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.long_win);
        song.start();
    }

    private void playMediumWinSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.medium_win);
        song.start();
    }

    private void playShortWinSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.short_win);
        song.start();
    }

    private void playGameOverSound() {
        MediaPlayer song = MediaPlayer.create(this, R.raw.balance_zero);
        song.start();
    }

}
