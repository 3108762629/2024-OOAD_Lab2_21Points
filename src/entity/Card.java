package entity;
import controller.Game;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;


public class Card {
    // 牌对应的数值
    public int cardValue;
    // 牌面
    public String cardFace;
    // 花色
    public CardSuit cardSuit;

    private static int totalCardsWidth = 950;
    private static int totalCardsHeight = 392;
    private static int individualCardWidth = totalCardsWidth / 13;
    private static int individualCardHeight = totalCardsHeight / 4;

    private int positionX = 0;
    private int positionY = 0;

    private static BufferedImage cardSpriteSheet;
    private BufferedImage cardImage;

    // 初始化牌面到数值的映射
    private static HashMap<String, Integer> valueMap;
    static {
        valueMap = new HashMap<>();
        for (int i = 2; i <= 10; i++) {
            valueMap.put(Integer.toString(i), i);
        }
        valueMap.put("A", 1);
        valueMap.put("J", 11);
        valueMap.put("Q", 12);
        valueMap.put("K", 13);

        try {
            cardSpriteSheet = ImageIO.read(new File("Image/cardSpriteSheet.png"));
        } catch (Exception e) {
            System.out.println("读取图片错误");
        }
    }

    // 打印卡牌，庄家牌和玩家牌处于不同的位置
    public void drawCard(Graphics2D graphics, boolean isDealerTurn, int cardIndex, int playerId) {
        if (isDealerTurn) {
            positionY = Game.DEALER_START_Y+50; // 如果是 dealerTurn，那么就放在 100
            positionX = Game.DEALER_START_X+530 + (individualCardWidth + 5) * (cardIndex); // 所有牌的横坐标都是 400 +
        } else {
            positionY =  180+Game.PLAYER_START_Y+ individualCardHeight * (cardIndex / 3) + 5;
            positionX = Game.PLAYER_START_X + Game.PLAYER_WIDTH * playerId + (individualCardWidth + 5) * (cardIndex % 3)-18;
        }
        graphics.drawImage(cardImage, positionX, positionY, null); // 打印这张牌
    }

    public Card(String cardFace, String cardSuit) {
        this.cardFace = cardFace.toUpperCase();
        if (this.cardFace.equals("A")) {
            this.cardValue = 11;
        } else {
            this.cardValue = valueMap.get(this.cardFace) > 10 ? 10 : valueMap.get(this.cardFace);
        }
        this.cardSuit = CardSuit.valueOf(cardSuit);
        this.cardImage = cardSpriteSheet.getSubimage((valueMap.get(this.cardFace) - 1) * individualCardWidth, this.cardSuit.ordinal() * individualCardHeight, individualCardWidth, individualCardHeight);
    }

    enum CardSuit {
        CLUB, SPADE, HEART, DIAMOND;
    }
}
