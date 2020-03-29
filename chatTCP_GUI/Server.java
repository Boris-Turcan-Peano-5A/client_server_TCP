/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author hudoj
 */
public class Server extends JFrame {

    ServerSocket server = null;
    private static final List<Socket> clients = new ArrayList<>();

    private JPanel contentPane;
    private JTextArea textArea;

    public Server(ServerSocket s) {
        this.server = s;
        setTitle("Server");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 451, 301);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JScrollPane scrollPane = new JScrollPane();
        contentPane.add(scrollPane, BorderLayout.CENTER);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.SOUTH);

        JButton startbtn = new JButton("START");
        panel.add(startbtn);
        startbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    start();
                    startbtn.setEnabled(false);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });
        this.addWindowListener(new WindowListener(){
            @Override
            public void windowOpened(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowClosing(WindowEvent we) {
                try {
                    server.close();
                    JOptionPane.showMessageDialog(null, "SERVER CLOSED!", "INFO", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void windowClosed(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowIconified(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowDeiconified(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowActivated(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void windowDeactivated(WindowEvent we) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    public void start() throws IOException {
        Thread serverth = new Thread(new Runnable() {
            @Override
            public void run() {
                Socket c = null;
                while (true) {
                    try {
                        c = server.accept();
                        clients.add(c);
                        System.out.println("Creo un worker!");
                        textArea.append("Client collegato: "+c.getLocalAddress()+"\n");
                        Thread w = new Thread(new Worker(c));
                        w.start();
                    } catch (IOException ex) {
                        Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                        System.exit(-1);
                    }
                }
            }

        });
        serverth.start();
    }

    public static void sendMessage(String m) throws IOException {
        synchronized (clients) {
            PrintWriter out = null;
            for (Socket s : clients) {
                if (!s.isClosed()) {
                    out = new PrintWriter(s.getOutputStream(), true);
                    out.println(s.getLocalAddress().toString() + "> " + m);
                    //out.close();
                }
            }
        }
    }

    public static void deleteClient(Socket c) throws IOException {
        synchronized (clients) {
            clients.remove(c);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(1234);
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Server serverGUI = new Server(server);
                    serverGUI.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //s.start();
    }

    public class Worker implements Runnable {

        private Socket client;

        public Worker(Socket s) {
            this.client = s;
        }

        @Override
        public void run() {
            BufferedReader in = null;
            PrintWriter out = null;
            String message = "";
            try {
                in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
                out = new PrintWriter(this.client.getOutputStream(), true);
                while ((message = in.readLine()) != null) {
                    System.err.println(message);
                    //out = new PrintWriter(this.client.getOutputStream(), true);
                    //out.println("Server> " + message);
                    Server.sendMessage(message);
                }
            } catch (IOException ex) {
                System.out.println("!!!Connessione terminata dal client!!!");
                try {
                    System.out.println("delClient: " + client.getLocalAddress());
                    textArea.append("Client "+client.getLocalAddress()+" e' scollegato!\n");
                    Server.deleteClient(client);
                } catch (IOException ex1) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex1);
                }
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    if (!client.isClosed()) {
                        in.close();
                        out.close();
                    }
                    client.close();
                } catch (IOException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
