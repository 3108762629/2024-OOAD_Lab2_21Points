package entity;

import entity.abs.IDeck;
import entity.abs.IDealer;
import entity.abs.Participate;

import static controller.Game.SHOW_RESULT;

public class Dealer extends Participate implements IDealer {
    private IDeck cardDeck;
    public static final String DEALER_IMG_PATH = "Image/Dealer.png";

    @Override
    public void distributeCard(Participate participant) {
        // 从牌堆里摸牌
        Card cardToDistribute = cardDeck.deal();
        // 牌发光了，重新洗牌
        if (cardToDistribute == null) {
            cardDeck.shuffle();
            cardToDistribute = cardDeck.deal();
        }
        // 发牌
        participant.drawCard(cardToDistribute);
    }

    @Override
    public void shuffleCards() {
        cardDeck.shuffle();
    }

    @Override
    public int settleScores(Participate participant) {
        if (participant.getScore() <= 21 && this.getScore() < participant.getScore()) {
            this.giveFunds(participant, 1);
            System.out.println("庄家点数小于玩家，玩家" + participant.id + "胜利，玩家余额:" + participant.money);
            SHOW_RESULT = SHOW_RESULT + "庄家点数小于玩家，玩家" + participant.id + "胜利，玩家余额:" + participant.money + '\n';
            return 1;
        } else if (this.getScore() <= 21 && this.getScore() > participant.getScore()) {
            this.receiveFunds(participant);
            System.out.println("庄家点数大于玩家，玩家" + participant.id + "失败，玩家余额:" + participant.money);
            SHOW_RESULT = SHOW_RESULT + "庄家点数大于玩家，玩家" + participant.id + "失败，玩家余额:" + participant.money + '\n';
            return 2;
        } else if (this.getScore() == 21 && participant.getScore() == 21) {
            if (participant.isBlackJack() && this.isBlackJack()) {
                this.giveFunds(participant, 0);
                System.out.println("庄家和玩家" + participant.id + "都为BlackJack" + "平局，玩家余额:" + participant.money);
                SHOW_RESULT = SHOW_RESULT + "庄家和玩家" + participant.id + "都为BlackJack" + "平局，玩家余额:" + participant.money + '\n';
                return 0;
            } else if (this.getHandSize() == participant.getHandSize()) {
                this.giveFunds(participant, 0);
                System.out.println("庄家和玩家" + participant.id + "都为21点且牌数相等" + "平局，玩家余额:" + participant.money);
                SHOW_RESULT = SHOW_RESULT + "庄家和玩家" + participant.id + "都为21点且牌数相等" + "平局，玩家余额:" + participant.money + '\n';
                return 0;
            } else if (participant.isBlackJack() || participant.getHandSize() < this.getHandSize()) {
                this.giveFunds(participant, 1);
                System.out.println("庄家和玩家都为21点，玩家" + participant.id + "的牌更好，胜利，玩家余额:" + participant.money);
                SHOW_RESULT = SHOW_RESULT + "庄家和玩家都为21点，玩家" + participant.id + "的牌更好，胜利，玩家余额:" + participant.money + '\n';
                return 1;
            } else {
                this.receiveFunds(participant);
                System.out.println("庄家和玩家" + participant.id + "都为21点，庄家的牌更好，胜利，玩家余额:" + participant.money);
                SHOW_RESULT = SHOW_RESULT + "庄家和玩家" + participant.id + "都为21点，庄家的牌更好，胜利，玩家余额:" + participant.money + '\n';
                return 2;
            }
        } else if (this.isBomb()) {
            if(participant.isBomb()) {
                this.giveFunds(participant, 0);
                System.out.println("庄家和玩家" + participant.id + "爆掉" + "平局，玩家余额:" + participant.money);
                SHOW_RESULT = SHOW_RESULT + "庄家和玩家" + participant.id + "爆掉" + "平局，玩家余额:" + participant.money + '\n';
                return 0;
            }
            this.giveFunds(participant, 1);
            System.out.println("庄家爆掉，玩家" + participant.id + "胜利，玩家余额:" + participant.money);
            SHOW_RESULT = SHOW_RESULT + "庄家爆掉，玩家" + participant.id + "胜利，玩家余额:" + participant.money + '\n';
            return 1;
        } else if (participant.isBomb()) {
            if(this.isBomb())
            {
                this.giveFunds(participant, 0);
                System.out.println("庄家和玩家" + participant.id + "爆掉" + "平局，玩家余额:" + participant.money);
                SHOW_RESULT = SHOW_RESULT + "庄家和玩家" + participant.id + "爆掉" + "平局，玩家余额:" + participant.money + '\n';
                return 0;
            }
            this.receiveFunds(participant);
            System.out.println("玩家" + participant.id + "爆掉，庄家胜利，玩家余额:" + participant.money);
            return 2;
        } else if (this.getScore() == participant.getScore()) {
            this.giveFunds(participant, 0);
            System.out.println("庄家和玩家" + participant.id + "点数相等，" + "平局，玩家余额:" + participant.money);
            SHOW_RESULT = SHOW_RESULT + "庄家和玩家" + participant.id + "点数相等，" + "平局，玩家余额:" + participant.money + '\n';
            return 0;
        } else {
            System.out.println("清算时出现了处理不了的状况");
            return -1;
        }
    }

    private void receiveFunds(Participate participant) {
        this.money += participant.loseMoney();
    }

    private void giveFunds(Participate participant, int res) {
        if (res == 0) {
            this.money -= participant.earnMoney(res);
        }
        if (res == 1) {
            this.money -= participant.earnMoney(res) / 2;
        }
    }

    @Override
    public boolean checkIfDrawCard() {
        return this.getScore() < 17;
    }

    @Override
    public int earnMoney(int res) {
        return 0;
    }

    @Override
    public int loseMoney() {
        return 0;
    }

    @Override
    public boolean isBet() {
        return false;
    }

    public Dealer(Hand hand, int money, IDeck deck) {
        this.hand = hand;
        this.money = money;
        this.cardDeck = deck;
    }
}

