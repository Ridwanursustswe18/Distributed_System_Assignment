
package assignment;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.Socket;

import javax.swing.*;
public class Client extends JFrame implements ActionListener  {
	JPanel panel;
	JTextField field;
	JButton Btn;
	static JTextArea txtArea;
	static Socket skt;
	static DataInputStream din;
	static DataOutputStream dout;
	 Client() {
		// TODO Auto-generated constructor stub
		 //setting the background color using panel
		 panel = new JPanel();
		 panel.setLayout(null);
		 panel.setBackground(new Color(7,94,84));
		 panel.setBounds(0,0,450,70);
		 add(panel);
		 //setting the name of client
		 JLabel label = new JLabel("User2"); 
		 label.setFont(new Font("SAN_SERIF", Font.BOLD, 18));
	       label.setForeground(Color.WHITE);
	       label.setBounds(110, 15, 100, 18);
	       panel.add(label);//adding the panel in label
	       //Adding Text Area
	      txtArea = new JTextArea();
	      txtArea.setBounds(5, 75, 440, 570);
	      txtArea.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
	      //txtArea.setBackground(Color.BLUE);
	      txtArea.setEditable(false);
	      txtArea.setLineWrap(true);
	      txtArea.setWrapStyleWord(true);
		   add(txtArea);
		   
	       //now making the field for sending text and it's button
	       field = new JTextField();
	       field= new JTextField();
	       field.setBounds(5, 655, 310, 40);
	       field.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
	       add(field);
	       
	       Btn = new JButton();
	     Btn = new JButton("Send");
	     Btn.setBounds(320, 655, 70, 40);
	     Btn.setBackground(new Color(7, 94, 84));
	     Btn.setForeground(Color.WHITE);
	     Btn.setFont(new Font("SAN_SERIF", Font.PLAIN, 16));
	     Btn.addActionListener(this);
	       add(Btn);
	      
	       
	       
		 getContentPane().setBackground(Color.white);
		 setLayout(null);
		 setSize(450,700);//setting the frame size
		 setLocation(400,200);//setting the width of x & y coordinate 
		 
		 setVisible(true);
	 }
	 public void actionPerformed(ActionEvent ae) {
		 try {
		 String out = field.getText();
		 txtArea.setText(txtArea.getText() + "\n \t \t \t" + out);
		 //sending the data to the server
		 dout.writeUTF(out);
		 field.setText("");
		 }catch(Exception e) {
			 
		 }
	 }
	 

	public static void main(String[] args) {
		// creating server frame
		
		Client clt = new Client();
		clt.setVisible(true);
		try {
			//creating the socket object for client
			
			skt = new Socket("127.0.0.1",6001); //(ip,port no.)
			 din = new DataInputStream(skt.getInputStream()); //recieved from server
			 dout = new DataOutputStream(skt.getOutputStream());//sent to server
			 String msgInput;
			 msgInput = din.readUTF();
			 txtArea.setText(txtArea.getText() + "\n" + msgInput);
			
		}catch(Exception e){

		}
	}
	
}
