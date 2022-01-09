package FileTransfer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ClientFile {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final File[] fileToSend = new File[1];
		JFrame jFrame = new JFrame("Client");
		jFrame.setSize(450, 450);
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		
		JLabel jTitle = new JLabel("Client");
		jTitle.setFont(new Font("Arial",Font.BOLD,25));
		jTitle.setBorder(new EmptyBorder(20,0,10,0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JLabel jFileName = new JLabel("Choose A file To Send");
		jFileName.setFont(new Font("Arial",Font.BOLD,25));
		jFileName.setBorder(new EmptyBorder(20,0,10,0));
		jFileName.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jpButton = new JPanel();
		jpButton.setBorder(new EmptyBorder(75,0,10,0));
		
		JButton jbSendFile = new JButton("Send File");
		jbSendFile.setPreferredSize(new Dimension(150,75));
		jbSendFile.setFont(new Font("Arial",Font.BOLD,20));
		
		JButton jbChooseFile = new JButton("Choose File");
		jbChooseFile.setPreferredSize(new Dimension(150,75));
		jbChooseFile.setFont(new Font("Arial",Font.BOLD,20));
		
		jpButton.add(jbSendFile);
		jpButton.add(jbChooseFile);
		
		jbChooseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Choose a File To send");
				
				if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					fileToSend[0] = jFileChooser.getSelectedFile();
					jFileName.setText("The file You want to send is: " + fileToSend[0].getName());
					
				}
			}
		});
		jbSendFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fileToSend[0] == null) {
					jFileName.setText("Please choose a file first ");
					
				}
				else {
					try {
					FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());//reading in the file
					Socket socket = new Socket("localhost",1234);
					
					DataOutputStream dataOutputStream  = new DataOutputStream(socket.getOutputStream());//writing in the file
					
					String fileName = fileToSend[0].getName();
					byte[] fileNameBytes = fileName.getBytes();
					
					byte[] fileContentBytes = new byte[(int)fileToSend[0].length()];
					fileInputStream.read(fileContentBytes);
					
					dataOutputStream.writeInt(fileNameBytes.length);//getting the bytes length
					dataOutputStream.write(fileNameBytes);//sending actual file name
					
					dataOutputStream.writeInt(fileContentBytes.length);//getting the actual file's contents length
					dataOutputStream.write(fileContentBytes);//sending the actual file contents
					}catch(IOException e1) {
						e1.printStackTrace();
					}
					
					
				}
			}
		});
		
		jFrame.add(jTitle);
		jFrame.add(jFileName);
		jFrame.add(jpButton);
		jFrame.setVisible(true);
		
		
		
		
	}

}
