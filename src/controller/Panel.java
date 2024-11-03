package controller;


import javax.swing.*;
public class Panel {
    enum STATE{
        IN_GAME,OPTION
    }
    public static int GAME_BG_WIDTH = 1800;
    public static int GAME_BG_HEIGHT = 856;
    public static STATE PANEL_STATE = STATE.OPTION;

    public static JFrame OPTION_FRAME = new JFrame();
    public static JFrame GAME_FRAME = new JFrame();

    public static void main(String[] args) {
        if(PANEL_STATE == STATE.OPTION)
        {
            Init_Option();
        }
    }

    public static void Init_Option(){
        OPTION_FRAME.setTitle("Welcome to BLACK JACK");
        OPTION_FRAME.setSize(1530,765);
        OPTION_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        OPTION_FRAME.setVisible(false);
        OPTION_FRAME.setLocationRelativeTo(null);

        Option Op = new Option();
        OPTION_FRAME.add(Op);
        OPTION_FRAME.setVisible(true);

    }
    public static void Init_Game(){
        GAME_FRAME.setTitle("BLACK JACK");
        GAME_FRAME.setSize(GAME_BG_WIDTH,GAME_BG_HEIGHT);
        GAME_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GAME_FRAME.setVisible(false);
        GAME_FRAME.setLocationRelativeTo(null);

        Game game = new Game();
        GAME_FRAME.add(game);
        GAME_FRAME.setVisible(true);


    }


}
