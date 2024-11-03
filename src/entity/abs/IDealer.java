package entity.abs;

public interface IDealer {
    // 给玩家发牌
    void distributeCard(Participate gameParticipate);

    // 洗牌
    void shuffleCards();

    // 游戏结束时，清算玩家得分，返回1玩家胜利，返回2玩家失败，返回0平局
    int settleScores(Participate gameParticipate);
}
