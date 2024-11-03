package controller;

import entity.abs.Participate;
import entity.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Game extends JComponent implements ActionListener {
    public static final int player_Count = 3;  //玩家数量
    public static final int PLAYER_WIDTH = 400;
    public static final int PLAYER_START_X = 200;
    public static final int PLAYER_START_Y = 300;
    public static String SHOW_RESULT = "";

    public static final int DEALER_START_X = 70;
    public static final int DEALER_START_Y = 30;

    private List<Participate> Player_List;
    private List<Integer> Result_List;
    private Dealer dealer;
    private BufferedImage Background_Img;
    private BufferedImage Result_Img;

    private int pointer;
    private boolean Is_Over;
    Participate Current_Player;

    private JButton btn_Hit = new JButton("HIT");
    private JButton btn_Stand = new JButton("STAND");
    private JButton btn_Bet = new JButton("BET");
    private JButton btn_Next = new JButton("NEXT ROUND");
    private JButton btn_Quit = new JButton("QUIT");

    public Game(){
        btn_Hit.addActionListener(this);
        btn_Stand.addActionListener(this);
        btn_Bet.addActionListener(this);
        btn_Next.addActionListener(this);
        btn_Quit.addActionListener(this);

        Player_List = new ArrayList<>();
        dealer = new Dealer(new Hand(),100000,new Deck());

        Result_List = new ArrayList<>();
        for (int i=0;i<player_Count;i++){
            Participate player = new Player(new Hand(),1000,i);
            Player_List.add(player);
        }

        Restart();
        repaint();

    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g_2d = (Graphics2D) g;

        g_2d.setFont(new Font("Arial", Font.PLAIN, 30));
        g_2d.setColor(Color.BLACK);
        setButton();

        try{
            Background_Img = ImageIO.read(new File("Image/Game_Background.jpg"));
        }
        catch(IOException e){
            System.out.println(e);
        }

        g_2d.drawImage(Background_Img,0,0,Panel.GAME_BG_WIDTH,Panel.GAME_BG_HEIGHT,null);
        if(pointer < Player_List.size()){
            Current_Player = Player_List.get(pointer);
        }
        else{
            Current_Player = dealer;
        }

        if(pointer >= Player_List.size()){
            g_2d.setColor(Color.RED);
        }
        else{
            g_2d.setColor(Color.BLACK);
        }

        for(int i=0;i<Player_List.size();i++) {
            g_2d.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
            if (i == Current_Player.id) {
                g_2d.setColor(Color.RED);
            } else {
                g_2d.setColor(Color.BLACK);
            }
            BufferedImage player_img;
            try {
                player_img = ImageIO.read(new File(Player.PLAYER_IMG_PATH[i]));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            g_2d.drawImage(player_img, PLAYER_START_X + PLAYER_WIDTH * i, PLAYER_START_Y, null);
            g_2d.drawString("SCORE: " + Player_List.get(i).getScore(), PLAYER_START_X + 15 + PLAYER_WIDTH * i, PLAYER_START_Y + 125);
            g_2d.drawString("LEFT MONEY:" + Player_List.get(i).getMoney(), PLAYER_START_X + 15 + PLAYER_WIDTH * i, PLAYER_START_Y + 155);
            List<Card> Current_Hands = Player_List.get(i).getCardList();
            for (int j =0;j<Current_Hands.size();j++){
                Current_Hands.get(j).drawCard(g_2d, false,j,Player_List.get(i).id);
            }
        }

        List<Card> Dealer_Hands = dealer.getCardList();
        if (pointer >= Player_List.size()){
            g_2d.setColor(Color.RED);
            g_2d.drawString("DEALER'S SCORE IS: "+dealer.getScore(),DEALER_START_X+150,DEALER_START_Y+100);

            for(int i=0;i<Dealer_Hands.size();i++){
                Dealer_Hands.get(i).drawCard(g_2d, true,i,-1);
            }

        }
        else{
            g_2d.setColor(Color.BLACK);
            g_2d.setFont(new Font("Arial", Font.BOLD, 23));
            BufferedImage dealer_img = null;
            try
            {
                dealer_img = ImageIO.read(new File(Dealer.DEALER_IMG_PATH));
            }
            catch(IOException e)
            {
                System.out.println(e);
            }
            g_2d.drawImage(dealer_img,DEALER_START_X,DEALER_START_Y,null);
            g_2d.drawString("DEALER'S SCORE IS HIDDEN: xxx",DEALER_START_X+150,DEALER_START_Y+100);
            BufferedImage Hidden_poker_img = null;
            try{
                Hidden_poker_img = ImageIO.read(new File("Image/Hidden_Poker.png") );
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            g_2d.drawImage(Hidden_poker_img,DEALER_START_X+530,DEALER_START_Y+50,null);
            Dealer_Hands.get(1).drawCard(g_2d,true,1,-1);
        }


        if(Is_Over)
        {
            for(int index = 0;index<Player_List.size();index++)
            {
                String path;
                switch (Result_List.get(index))
                {
                    case 0:
                        path  = "Image/draw.png";
                        break;
                    case 1:
                        path = "Image/win.png";
                        break;
                    case 2:
                        path = "Image/lose.png";
                        break;
                    case 3:
                        path = "Image/bomb.png";
                    default:
                        throw new IllegalStateException("Unexpected value: "+Result_List.get(index));

                }
                try{
                    Result_Img = ImageIO.read(new File(path));
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }

                System.out.println(Integer.toString(Player_List.get(index).getScore()));
                g_2d.drawImage(Result_Img,120+PLAYER_START_X+PLAYER_WIDTH*index,PLAYER_START_Y,null);
            }


        }

        super.add(btn_Hit);
        super.add(btn_Stand);
        super.add(btn_Bet);
        super.add(btn_Next);
        super.add(btn_Quit);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton selected = (JButton) e.getSource();

        if (selected == btn_Quit) {
            Restart();

            for(Participate player: Player_List){
                player.resetMoney(1000);

            }
            Panel.GAME_FRAME.dispose();
            Panel.PANEL_STATE = Panel.STATE.OPTION;
            Panel.Init_Option();
        }

        if(selected == btn_Next)
        {
            if(this.Is_Over)
            {
                Restart();
                repaint();
            }

            else {
                JOptionPane.showMessageDialog(null, "Please complete the Game first!");
                return;
            }
        }

        if(selected == btn_Hit && pointer < Player_List.size())
        {
            if(Current_Player.isBet()){
                dealer.distributeCard(Current_Player);
                repaint();
                Current_Player.flag = false;
                if(Current_Player.isBomb())
                {
                    JOptionPane.showMessageDialog(null, "Player "+Current_Player.id+" is bombed!");
                    dealer.settleScores(Current_Player);
                    Current_Player.flag = true;
                    pointer++;

                    Check_Dealer_Turn();
                }
            }else{
                JOptionPane.showMessageDialog(null, "You should bet fist!!");

            }

            repaint();
        }

        if (selected == btn_Stand) {
            Current_Player.flag = true;

            if(pointer >= Player_List.size())
            {
                JOptionPane.showMessageDialog(null, "所有玩家摸牌完毕，开始结算筹码--------");
                for(Participate player: Player_List){
                    if (!player.isEnd)
                    {
                        int Game_result = dealer.settleScores(player);
                        Result_List.add(Game_result);
                    }
                    else{
                        Result_List.add(3);
                        SHOW_RESULT = SHOW_RESULT + "Player"+player.id+"is out of the game"+"\n";
                    }
                }

                this.Is_Over = true;
                JOptionPane.showMessageDialog(null, "The Result is" + '\n'+SHOW_RESULT);
                repaint();
            }else if (Current_Player.isBet()){
                Current_Player.flag = false;
                pointer++;
                Check_Dealer_Turn();
            }
            else{
                JOptionPane.showMessageDialog(null, "You should bet fist!!");
            }
            repaint();
        }
        if(selected == btn_Bet && pointer <= Player_List.size()) {
            if(Current_Player.flag == false){
                JOptionPane.showMessageDialog(null, "You can only bet once per round!");
            }else{
                String[] Options = new String[]{"50","80","100","250","500"};
                int response = JOptionPane.showOptionDialog(null,"Please select the bet amount:","BETTING",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,Options,Options[0]);


                if (response == 0 && Current_Player.flag) {
                    if (Current_Player.money >= 50) {
                        Current_Player.money -= 50;
                        Current_Player.moneyToBet = 50;
                        Current_Player.flag = false;
                    }
                }

                else if (response == 1 && Current_Player.flag) {
                    if (Current_Player.money >= 80) {
                        Current_Player.money -= 80;
                        Current_Player.moneyToBet = 80;
                        Current_Player.flag = false;
                    }
                }

                else if (response == 2 && Current_Player.flag) {
                    if(Current_Player.money >= 100){
                        Current_Player.money -= 100;
                        Current_Player.moneyToBet = 100;
                        Current_Player.flag = false;
                    }
                }
                else if (response == 3 && Current_Player.flag) {
                    if(Current_Player.money >= 250){
                        Current_Player.money -= 250;
                        Current_Player.moneyToBet = 250;
                        Current_Player.flag = false;
                    }
                }
                else if (response == 4 && Current_Player.flag) {
                    if(Current_Player.money >= 500){
                        Current_Player.money -= 500;
                        Current_Player.moneyToBet = 500;
                        Current_Player.flag = false;
                    }
                }
                else {
                    JOptionPane.showMessageDialog(null, "输入不合法！请重新选择");
                }
                repaint();


            }

        }

    }

    private void Check_Dealer_Turn()
    {
        if (pointer == Player_List.size()){
            while(((Participate) dealer).checkIfDrawCard()){
                dealer.distributeCard(dealer);
            }
            JOptionPane.showMessageDialog(null,"It's the Dealer's turn to draw cards!");
        }

        repaint();
    }




    private void Restart(){
        this.Is_Over = false;
        System.out.println("Restart");

        dealer.clear();

        for(Participate player:Player_List){
            player.clear();
            player.flag = true;
        }

        dealer.shuffleCards();

        dealer.distributeCard((Participate) dealer);
        dealer.distributeCard((Participate) dealer);

        for (int i=0;i<Player_List.size();i++){
            dealer.distributeCard(Player_List.get(i));
            dealer.distributeCard(Player_List.get(i));

        }

        pointer = 0;
        Result_List.clear();
        SHOW_RESULT = "";

    }
    private void setButton(){
        btn_Bet.setBounds(60,750,80,40);
        btn_Hit.setBounds(200,750,80,40);
        btn_Stand.setBounds(400,750,120,40);
        btn_Next.setBounds(700,750,180,40);
        btn_Quit.setBounds(1400,750,70,40);




        btn_Bet. setFont(new Font("Arial",Font.BOLD,15));
        btn_Hit.setFont(new Font("Arial",Font.BOLD,15));
        btn_Stand.setFont(new Font("Arial",Font.BOLD,15));
        btn_Stand.setFont(new Font("Arial",Font.BOLD,15));
        btn_Next.setFont(new Font("Arial",Font.BOLD,15));


    }



}
