package entity.abs;
import entity.Card;

public interface IDeck {
    // 抽出一张牌
    Card deal();

    // 洗牌
    void shuffle();
}
