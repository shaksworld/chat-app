package models;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Client implements ActionListener {

    JTextField text;
    static JPanel panel1;
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream dataOutputStream;
    static JFrame frame = new JFrame();
    Client() {
        frame.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(128, 128,128));
        panel.setBounds(0, 0, 450, 70);
        panel.setLayout(null);
        frame.add(panel);

        //linking panel to the icons

        ImageIcon icon = new ImageIcon("src/main/java/icon/arrow-1.png"); //back arrow icon
        Image image = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH); //resizing the icon
        ImageIcon scaledIcon = new ImageIcon(image); // scaledIcon
        JLabel back1 = new JLabel(scaledIcon);
        back1.setBounds(5, 15, 25, 25);
        panel.add(back1);


        back1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.exit(0);
            }
        });

        //creating the profile icon

        ImageIcon icon1 = new ImageIcon("src/main/java/icon/jays-profile-2.png"); //profile icon
        Image image1 = icon1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH); //resizing the icon
        ImageIcon scaledIcon1 = new ImageIcon(image1); // scaledIcon
        JLabel profile1 = new JLabel(scaledIcon1);
        profile1.setBounds(400, 10, 40, 40);
        panel.add(profile1);

        //creating the name of the user
        JLabel name = new JLabel("Jay");
        name.setFont(new Font("SAN_SERIF", Font.BOLD, 22));
        name.setForeground(Color.WHITE);
        name.setBounds(170, 20, 100, 25);
        panel.add(name);

        //creating the status of the user
        JLabel status = new JLabel("Active Now");
        status.setFont(new Font("SAN_SERIF", Font.PLAIN, 12));
        status.setForeground(Color.GREEN);
        status.setBounds(170, 45, 100, 25);
        panel.add(status);

        panel1 = new JPanel();
        panel1.setBounds(10, 75, 430, 570);
        frame.add(panel1);

        //creating the text area
        text = new JTextField();
        text.setBounds(5, 655,310, 40);
        text.setBackground(Color.WHITE);
        text.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        frame.add(text);

        //creating the send button
        JButton send = new JButton("Send");
        send.setBounds(320, 655, 123, 40);
        send.setBackground(new Color(96, 243, 116));
        send.setForeground(Color.GREEN);
        send.addActionListener(this);
        send.setFont(new Font("SAN_SERIF", Font.PLAIN, 14));
        frame.add(send);

        frame.setSize(450, 700);
        frame.setLocation(800, 50);
        frame.setUndecorated(true);
        frame.getContentPane().setBackground(Color.WHITE);


        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try{
            String out =  text.getText();
            JPanel panel2 = formatLabel(out);

            panel1.setLayout(new BorderLayout());

            JPanel right = new JPanel(new BorderLayout());
            right.add(panel2, BorderLayout.LINE_END);
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));

            panel1.add(vertical, BorderLayout.PAGE_START);

            dataOutputStream.writeUTF(out);

            text.setText("");

            frame.repaint();
            frame.invalidate();
            frame.validate();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

    }
    public static JPanel formatLabel(String out) {
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        JLabel output = new JLabel("<html><p style = \"width: 150px\">" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.PLAIN, 16)); //font of the text
        output.setBackground(new Color(203, 203, 203)); //background color of the text
        output.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 50)); //border of the text

        output.setOpaque(true);
        p1.add(output);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        p1.add(time);

        return p1;


    }

    public static void main(String[] args) {
        new Client();
        try{
            Socket socket = new Socket("127.0.0.1", 6001);
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            while (true){
                panel1.setLayout(new BorderLayout());
                String msg = dataInputStream.readUTF();
                JPanel panel2 = formatLabel(msg);

                JPanel left = new JPanel(new BorderLayout());
                left.add(panel2, BorderLayout.LINE_START);
                vertical.add(left);
                vertical.add(Box.createVerticalStrut(15));
                panel1.add(vertical, BorderLayout.PAGE_START);
                frame.validate();
            }

        } catch (Exception e) {
        e.printStackTrace();
    }
    }
}
