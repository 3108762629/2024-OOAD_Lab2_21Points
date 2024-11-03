package controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Option extends JComponent implements ActionListener {
    private JButton btnStart = new JButton("Start Game");
    private JButton btnExit = new JButton("Exit Game");


    private static BufferedImage backgroundImage;
    private BufferedImage OptionBackgroundImage;

    public Option() {
        btnStart.addActionListener(this);
        btnExit.addActionListener(this);
        try {
            OptionBackgroundImage = ImageIO.read(new File("Image/OptionBackground.png"));
        }
        catch (IOException e) {
            System.out.println("无法读取背景图片");
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g_2 = (Graphics2D) g;

        g_2.setFont(new Font("Comic Sans MS", Font.BOLD, 100));
        g_2.setColor(Color.BLACK);

        g_2.drawImage(OptionBackgroundImage, 0, 0,1530,765, null);

        btnStart.setBounds(400,350,230,90);
        btnExit.setBounds(800,350,230,90);

        btnStart.setFont(new Font("Arial", Font.PLAIN, 25));
        btnExit.setFont(new Font("Arial", Font.PLAIN, 25));

        btnStart.setBorder(BorderFactory.createRaisedBevelBorder());
        btnExit.setBorder(BorderFactory.createRaisedBevelBorder());



        super.add(btnStart);
        super.add(btnExit);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton selected = (JButton) e.getSource();

        if( selected == btnExit )
        {
            System.exit(0);
        }

        else if( selected == btnStart )
        {
            Panel.PANEL_STATE = Panel.STATE.IN_GAME;
            Panel.OPTION_FRAME.dispose();
            Panel.Init_Game();

        }

    }

}
