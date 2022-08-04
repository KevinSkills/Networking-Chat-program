import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JTextArea;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.CardLayout;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import java.awt.Font;

public class ChatProgram extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPanel StartPanel;
	private JTextField connect_Ip;
	private JButton btnNewButton_2;
	private JTextField host_Port;
	private JLabel lblPort;
	private JLabel lblPort_1;
	private JTextField connect_Port;
	private JPanel chatPanel;
	private ChatProgram thisClass;
	private JEditorPane textBox;
	private JTextField nameField;
	public boolean isServer;
	Networking networkingObj;
	public String theText = "";
	String happy ="happy.png", sad = "sad.png", neutral = "neutral.png";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//start the server class

		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					ChatProgram frame = new ChatProgram();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	public void printMessage(String message, String name) {

			// add message to theText
		    theText = theText + name + ": " + message  +  "<br>";;
		    
		    //smileys
		    theText = replaceWithSmileys(theText);
		    
		    //write to textBox
			textBox.setText("<p style='font-size:15px'>" + theText + "<p>" );

	}
	
	
	public String replaceWithSmileys(String t) {
		t = t.replaceAll(":\\)", "<img src='file:resource\\\\"+ happy +"' width=23 height=23>");
		t = t.replaceAll(":\\(", "<img src='file:resource\\\\"+ sad +"' width=23 height=23>");
		t = t.replaceAll(":I", "<img src='file:resource\\\\"+ neutral +"' width=23 height=23>");
		
		
		return t;
	}
	

	public ChatProgram() {
		thisClass = this;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		//make the layout
		CardLayout cl = new CardLayout(0, 0);
		
		//set to the layout
		contentPane.setLayout(cl);
		
		//add panels as "Cards" to the layout
		
		chatPanel = new JPanel();
		contentPane.add(chatPanel, "chatPanel");
		chatPanel.setLayout(null);
		
		StartPanel = new JPanel();
		contentPane.add(StartPanel, "startPanel");
		StartPanel.setLayout(null);
		
		//show the start screen
		cl.show(contentPane, "startPanel");

		
		
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					String message = textField.getText();
					
					networkingObj.sendText(message);
					printMessage(message, "You");
					

					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		sendButton.setBounds(315, 569, 89, 55);
		chatPanel.add(sendButton);
		
		textField = new JTextField();
		
		textField.setBounds(10, 569, 305, 55);
		chatPanel.add(textField);
		textField.setColumns(10);
		
		JButton FileChooseBtn = new JButton("File");
		FileChooseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.showOpenDialog(null);
				
				File f = chooser.getSelectedFile();
				try {
					System.out.println("sending: " + Networking.fileString);
					networkingObj.sendText(Networking.fileString);
					networkingObj.sendFile(f);
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		FileChooseBtn.setBounds(315, 535, 89, 23);
		chatPanel.add(FileChooseBtn);
		
		textBox = new JEditorPane();;
		textBox.setContentType("text/html");
		
		textBox.setBounds(59, 62, 288, 410);
		chatPanel.add(textBox);
		


		
		connect_Ip = new JTextField();
		connect_Ip.setBounds(101, 186, 151, 20);
		StartPanel.add(connect_Ip);
		connect_Ip.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Ip");
		lblNewLabel.setBounds(123, 161, 46, 14);
		StartPanel.add(lblNewLabel);
		
		
		JButton btnNewButton_1 = new JButton("Connect to Server");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				networkingObj = new Networking(connect_Ip.getText(), Integer.parseInt( connect_Port.getText()), thisClass, nameField.getText());

				networkingObj.start();
				cl.show(contentPane, "chatPanel");

				
				
			}
		});
		btnNewButton_1.setBounds(261, 213, 153, 23);
		StartPanel.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Host Server");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				networkingObj = new Networking(Integer.parseInt( host_Port.getText() ), thisClass, nameField.getText());

				networkingObj.start();
				cl.show(contentPane, "chatPanel");
				

				
			}
		});
		btnNewButton_2.setBounds(274, 356, 133, 23);
		StartPanel.add(btnNewButton_2);
		
		host_Port = new JTextField();
		host_Port.setColumns(10);
		host_Port.setBounds(101, 357, 60, 20);
		StartPanel.add(host_Port);
		
		lblPort = new JLabel("Port");
		lblPort.setBounds(123, 332, 46, 14);
		StartPanel.add(lblPort);
		
		lblPort_1 = new JLabel("Port");
		lblPort_1.setBounds(123, 217, 46, 14);
		StartPanel.add(lblPort_1);
		
		connect_Port = new JTextField();
		connect_Port.setColumns(10);
		connect_Port.setBounds(101, 242, 60, 20);
		StartPanel.add(connect_Port);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(123, 32, 46, 14);
		StartPanel.add(lblName);
		
		nameField = new JTextField();
		nameField.setColumns(10);
		nameField.setBounds(101, 57, 151, 20);
		StartPanel.add(nameField);

	}
}
