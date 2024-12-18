package serverSide;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.swing.*;
import java.net.*;

public class server implements ActionListener {
    static JFrame f = new JFrame();
    JTextField text;
    static JPanel area;
    static DataOutputStream dout;
    static Box vertical = Box.createVerticalBox(); // Create a vertical box to hold messages
     static JScrollPane scrollPane; // ScrollPane for chat area

    server() {
        f.setTitle("Chat Application");

        // Top Panel
        JPanel p = new JPanel();
        p.setBackground(new Color(7, 94, 84));
        p.setBounds(0, 0, 450, 70);
        p.setLayout(null);
        f.add(p);

        JLabel title = new JLabel("Server");
        title.setBounds(170, 20, 200, 35);
        title.setForeground(Color.RED);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        p.add(title);

        // Chat Area
        area = new JPanel();
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        area.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(area); // Wrap chat area in a scrollpane
        scrollPane.setBounds(5, 75, 425, 540);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        f.add(scrollPane);

        // Input Field
        text = new JTextField();
        text.setBounds(5, 620, 310, 30);
        text.setForeground(Color.BLACK);
        text.setFont(new Font("Arial", Font.BOLD, 20));
        f.add(text);

        // Send Button
        JButton button = new JButton("Send");
        button.setBounds(320, 620, 108, 30);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.RED);
        button.setBorder(null);
        button.setBackground(new Color(7, 94, 84));
        button.addActionListener(this);
        f.add(button);

        // Frame Settings
        f.setUndecorated(false);
        f.setLayout(null);
        f.setSize(450, 700);
        f.setLocation(200, 40);
        f.setVisible(true);
    }
//Sending message 
    
    @Override
 

    public void actionPerformed(ActionEvent e) {
        String out = text.getText().trim(); // Get the input text and trim whitespace

        if (!out.isEmpty()) {
            JPanel p2 = formatLabel(out);
          
            
            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align right
            rightPanel.setBackground(Color.WHITE);
            rightPanel.add(p2);
            
            JPanel pan=new JPanel(new BorderLayout());
            //pan.add(p2,BorderLayout.PAGE_START);
            pan.setBackground(Color.WHITE);

             area.add(pan,BorderLayout.EAST);
             
            

            vertical.add(rightPanel);
            vertical.add(Box.createVerticalStrut(10)); // Add spacing

            area.add(vertical);
            f.revalidate();
            f.repaint();

            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // Auto-scroll

            try {
                dout.writeUTF(out); // Send the message to the client
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            text.setText(""); // Clear the input field
        }
    }


    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style='width: 150px;border-radius :20px;'>" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.BOLD, 16));
        output.setBackground(new Color(180, 220, 180)); // Light green background for messages
        output.setOpaque(true);
        output.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding for the label
        panel.add(output);
        panel.repaint();
        panel.validate();
        panel.invalidate();
        //panel.revalidate();

        return panel;
    }

    public static void main(String[] args) {
        new server();
        int port = 33888;

        try {
            try (ServerSocket socket = new ServerSocket(port)) {
				while (true) {
				    Socket s = socket.accept();
				    DataInputStream din = new DataInputStream(s.getInputStream());
				    dout = new DataOutputStream(s.getOutputStream());
//receiving message 

				    while (true) {
				        String msg = din.readUTF();
				        JPanel panel = formatLabel(msg);

				        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Align left
				        leftPanel.setBackground(Color.WHITE);
				        leftPanel.add(panel);

				        vertical.add(leftPanel);
				        vertical.add(Box.createVerticalStrut(10)); // Add spacing

				        f.validate();
				        scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()); // Auto-scroll
				    }
				}
			
        }
        }catch (Exception e) {
            e.printStackTrace();
        
        }
    }
}
   
      
    

    

