package FileTransfer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class ServerFile{
	static ArrayList<MyFile> myFiles = new ArrayList<>();
	public static void main(String[] args) throws IOException{
		int fileId = 0;
		JFrame jFrame = new JFrame("Server");
		jFrame.setSize(400, 400);
		jFrame.setLayout(new BoxLayout(jFrame.getContentPane(),BoxLayout.Y_AXIS));
		jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
		
		JPanel jPanel = new JPanel(); 
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
		
		JScrollPane jScrollPane = new JScrollPane (jPanel);
		jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel jTitle = new JLabel("Server");
		jTitle.setFont(new Font("Arial",Font.BOLD,25));
		jTitle.setBorder(new EmptyBorder(20,0,10,0));
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		jFrame.add(jTitle);
		jFrame.add(jScrollPane);
		jFrame.setVisible(true);
		
		
		ServerSocket serverSocket = new ServerSocket(1234) ;
		
		while(true) {
			try {
				
				Socket socket = serverSocket.accept();
				
				DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());//getting the file from the client sent through socket
				int fileNameLength = dataInputStream.readInt();//reading filename sent by client
				//if the file is sent
				if(fileNameLength > 0) {
					byte[] fileNameBytes = new byte[fileNameLength];
					dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);//reading the file name from start to end
					String fileName = new String(fileNameBytes);//accepting the array and saving it filename
					
					//now doing the same thing for contents of the file
					int fileContentLength = dataInputStream.readInt();
					if(fileContentLength > 0) {
						byte[] fileContentBytes = new byte[fileContentLength];
						dataInputStream.readFully(fileContentBytes, 0, fileContentLength);//reading the file contents from start to end
						//now creating the row that will accept all the files
						JPanel jpFileRow = new JPanel(); 
						jpFileRow.setLayout(new BoxLayout(jpFileRow,BoxLayout.Y_AXIS));
						
						JLabel jFileName = new JLabel(fileName);
						jFileName.setFont(new Font("Arial",Font.BOLD,20));
						jFileName.setBorder(new EmptyBorder(10,0,10,0));
						
						if(getFileExtension(fileName).equalsIgnoreCase("txt")) {
							jpFileRow.setName(String.valueOf(fileId));
							jpFileRow.addMouseListener(getMyMouseListener());
							
							jpFileRow.add(jFileName);
							jPanel.add(jpFileRow);
							jFrame.validate();
						}
						else {
							jpFileRow.setName(String.valueOf(fileId));
							jpFileRow.addMouseListener(getMyMouseListener());
							
							jpFileRow.add(jFileName);
							jpFileRow.validate();
						}
						//adding the stacks of file
						myFiles.add(new MyFile(fileId,fileName,fileContentBytes,getFileExtension(fileName)));			
				}
				
			}
				
			}catch(IOException error) {
				error.printStackTrace();
			}
		}
		
	}
	public static MouseListener getMyMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				JPanel jPanel = (JPanel)e.getSource();
				int fileId = Integer.parseInt(jPanel.getName());
				
				for (MyFile myFile:myFiles) {
					if(myFile.getId() == fileId) {
						JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
						jfPreview.setVisible(true);
					}
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}
	
	public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension ) {
		JFrame jFrame = new JFrame("File Downloader");
		jFrame.setSize(400, 400);
		
		JPanel jPanel = new JPanel(); 
		jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));
		
		JLabel jTitle = new JLabel("File Downloader");
		jTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
		jTitle.setFont(new Font("Arial",Font.BOLD,25));
		jTitle.setBorder(new EmptyBorder(20,0,10,0));
		
		JLabel jlPrompt = new JLabel("Are you sure you want to download " + fileName);
		jlPrompt.setFont(new Font("Arial",Font.BOLD,25));
		jlPrompt.setBorder(new EmptyBorder(20,0,10,0));
		jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
		

		JButton jbYes = new JButton("Yes");
		jbYes.setPreferredSize(new Dimension(150,75));
		jbYes.setFont(new Font("Arial",Font.BOLD,20));
		
		JButton jbNo = new JButton("No");
		jbNo.setPreferredSize(new Dimension(150,75));
		jbNo.setFont(new Font("Arial",Font.BOLD,20));
		
		JLabel jFileContent = new JLabel("File Downloader");
		jFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JPanel jpButtons = new JPanel();
		jpButtons.setBorder(new EmptyBorder(20, 0 , 20 , 0));
		jpButtons.add(jbYes);
		jpButtons.add(jbNo);
		
		//logic for text/image file
		if(fileExtension.equalsIgnoreCase("txt")) {
			jFileContent.setText("<html>" + new String(fileData) + "</html>");
		}
		else {
			jFileContent.setIcon(new ImageIcon(fileData));
		}
		//logic for yes/no btn
		jbYes.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			File fileToDownload = new File (fileName);
			try {
				FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
				fileOutputStream.write(fileData);
				fileOutputStream.close();
				jFrame.dispose();
				
			}catch(IOException e1) {
				e1.printStackTrace();
			}
		}
		});
		
		jbNo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				jFrame.dispose();
			}
			
		});	
		jPanel.add(jTitle);
		jPanel.add(jlPrompt);
		jPanel.add(jFileContent);
		jPanel.add(jpButtons);
		jFrame.add(jPanel);
		return jFrame;
		
	}
	public static String getFileExtension(String fileName) {
		int i = fileName.lastIndexOf('.');
		//if there is an extension
		if(i > 0) {
			return fileName.substring(i + 1);
		}
		else {
			return "No extensions found";
		}
	}
}