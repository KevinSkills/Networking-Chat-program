import java.net.*;

import javax.swing.JFileChooser;

import java.io.*;

public class Networking extends Thread {
	
	public static final String fileString = "asdasdgasd asd XxFileTransfebjhiklwerguhiretuhigrxX------asdghaweasr";

	//for client
	private String toIP;
		
	//for server
	ServerSocket serverSock;
	
	//for both
	Socket sock;
	private boolean isServer;
	private DataInputStream dataIn;
	private DataOutputStream dataOut;	
	private ChatProgram cp;
	private int port;
	
	
	private boolean running;
	
	private String otherName;
	private String yourName;
	
	
	
	
	public Networking(int p, ChatProgram c, String yourName) { //server
		isServer = true;
		this.port = p;
		this.cp = c;
		this.yourName = yourName;
		
		
	}

	public Networking(String ip, int port, ChatProgram c, String yourName) { //client
		isServer = false;
		this.toIP = ip;
		this.port = port;
		this.cp = c;
		this.yourName = yourName;

		
	}
	
	
	
	
	public void run() {
		try {
			if(isServer) {
				serverSock = new ServerSocket(port);
				System.out.println("Hosting on port " + port);
				sock = serverSock.accept(); //venter til at der er noget at acceptere :D
			
			}else {
				if(toIP.isEmpty()) toIP = "localhost";
				
				sock = new Socket(toIP , port);
				System.out.println("Connecting to " + toIP + " on port " + port);
			}
			

			
			running = true;
			dataIn = new DataInputStream(sock.getInputStream());
			dataOut = new DataOutputStream(sock.getOutputStream());
			
			
			dataOut.writeUTF(yourName);
			otherName = dataIn.readUTF();
			
			String t = "";

			

			while(running) {
				//while loop is for recieving things. Sending things can happen in the normal thread
				
				t = RecieveText();
				
				if(t.equals(Networking.fileString)) {

					//open filechooser
					
					JFileChooser c = new JFileChooser();
					c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					c.showOpenDialog(null);
					File f = c.getSelectedFile();
					
					//run readfile method
					try {
						File myFile = readFile(f.getPath());
						Runtime.getRuntime().exec("explorer.exe /select," + myFile.getPath());
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else if(t!= null) {
					
					 cp.printMessage(t, otherName);
				}

				if(t.equals("QUIT")) running = false;
			}
			
			dataIn.close();
			dataOut.close();
			sock.close();
			if(isServer) serverSock.close();
			System.exit(0);
		
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Problem with connection.");
		}
		System.out.println("Program terminating...");
	}
	
	
	
	
	public String RecieveText() {
		try {
			String incomming = "";
			
			incomming = dataIn.readUTF();
			System.out.println("recieved:" +  incomming);
			return incomming;
			
				
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "not working";
		}
		
	}
	
	public void sendText(String message) throws IOException {
		dataOut.writeUTF(message); 
		dataOut.flush();
		if(message.equals("QUIT")) running = false;
	}
	
	


	
	
	public void sendFile(File file) throws IOException {
		int bytesInBuffer = 0;
		
		FileInputStream fileStream = new FileInputStream(file);
		//write file name to server
		dataOut.writeUTF(file.getName());
		dataOut.flush();
		//write file size to server
		dataOut.writeLong(file.length());
		dataOut.flush();
		//break up the file into segments (socket can only handle max 65k data)
		byte[] fileBuffer = new byte[1024];
		//continue looping through data as long as data can be read from the FileInputStream
		while ((bytesInBuffer=fileStream.read(fileBuffer)) != -1) {
			System.out.println("4");
			dataOut.write(fileBuffer, 0, bytesInBuffer);
			dataOut.flush();
		}
		fileStream.close();
	}
	
	public File readFile(String directoryPath) throws IOException {
		int bytesInBuffer = 0;
		String fileName;
		long fileSize = 0;
		
		//get file name from client
		fileName = dataIn.readUTF();
		//get file size from the client
		fileSize = dataIn.readLong();
		//prepare file output
		FileOutputStream fileStream = new FileOutputStream(directoryPath + "\\recieved_" + fileName);
		//set up buffer for file segments
		byte[] fileBuffer = new byte[1024];
		//run the loop to read from the socket
		while (fileSize > 0 && (bytesInBuffer=dataIn.read(fileBuffer, 0, (int)Math.min(fileBuffer.length, fileSize))) != -1) {
			fileStream.write(fileBuffer, 0, bytesInBuffer);
			fileSize -= bytesInBuffer;
		}
		fileStream.close();
		
		return new File(directoryPath + "\\recieved_" + fileName);
	}

}









