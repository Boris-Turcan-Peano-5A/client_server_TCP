/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author hudoj
 */
public final class Client extends JFrame {

    private Socket client = null;

    private JPanel contentPane;
    private JTextField txtfieldip;
    private JTextField txtfield;
    private JTextArea textArea;

    public Client(Socket s) {
        this.client = s;
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);

        JLabel lblIpServer = new JLabel("IP SERVER");
        panel.add(lblIpServer);

        txtfieldip = new JTextField();
        txtfieldip.setText("127.0.0.1");
        panel.add(txtfieldip);
        txtfieldip.setColumns(10);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1, BorderLayout.SOUTH);

        txtfield = new JTextField();
        txtfield.setText("Hello World");
        panel_1.add(txtfield);
        txtfield.setColumns(25);

        JButton sendbtn = new JButton("SEND");
        sendbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                PrintWriter out = null;
                //BufferedReader tastiera = null;
                try {
                    out = new PrintWriter(client.getOutputStream(), true);
                    //tastiera = new BufferedReader(new InputStreamReader(System.in));
                    out.println("["+client.getLocalPort()+"]>"+txtfield.getText());
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        panel_1.add(sendbtn);
        start();
    }

    public void start() {
        //Thread writer = new Thread(new Writer(client));
        Thread listener = new Thread(new Listener(client));
        listener.start();
        //writer.start();
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        String ip = "127.0.0.1";
        String s = "ciao";
        Socket socket = new Socket(InetAddress.getByName(ip), 1234);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Client client = new Client(socket);
                    client.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        //client.start();
        //PrintWriter out= new PrintWriter(socket.getOutputStream(), true);
        //out.println(s);
        //BufferedReader in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //System.out.println(in.readLine());
        //out.close();
        //in.close();
    }

    public class Listener implements Runnable {

        private Socket client = null;

        public Listener(Socket s) {
            this.client = s;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            try {
                String line = "";
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                while ((line = in.readLine()) != null) {
                    textArea.append(line + "\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.exit(-1);
            }
        }
    }

    public class Writer implements Runnable {

        private Socket client = null;

        public Writer(Socket s) {
            this.client = s;
        }

        @Override
        public void run() {
            PrintWriter out = null;
            BufferedReader tastiera = null;
            String input = "";
            try {
                out = new PrintWriter(client.getOutputStream(), true);
                tastiera = new BufferedReader(new InputStreamReader(System.in));
                while ((input = tastiera.readLine()) != null) {
                    out.println(input);
                }
            } catch (IOException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    tastiera.close();
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
