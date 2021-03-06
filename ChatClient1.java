import java.net.*;
import java.io.*;
public class ChatClient1 extends Thread {
	public Socket clientSocket;
	public BufferedReader inFromServer;
	public BufferedReader inFromUser;
	public PrintWriter outToServer;
	public boolean sender_mode;
	public static boolean operation;
	public String inputLine, outputLine;
	public ChatClient1(Socket clientSocket, BufferedReader inFromServer) {
		this. clientSocket = clientSocket;
		this.inFromServer = inFromServer;
		sender_mode = false;
		operation = true;
	}
	public ChatClient1(Socket clientSocket, BufferedReader inFromUser,PrintWriter outToServer) {
		this.clientSocket = clientSocket;
		this.inFromUser = inFromUser;
		this.outToServer = outToServer;
		sender_mode = true;
		operation = true;
	}
	public void run() {

			if (sender_mode == true) {
				try {		
					while (true) {
						if (operation == false) {			
							break;
						}
						if ((outputLine = inFromUser.readLine())!=null){
						System.out.println("Client: " + outputLine);
						outToServer.println(outputLine);
						if (outputLine.equals("Bye.")) {
							operation = false;
							System.out.println("true mode break");
							break;
						}
					}
					}
				}
				catch (IOException ex) {
					System.out.println(ex);
				}
			}
			else {
				try {		
					while ((inputLine = inFromServer.readLine()) != null) {
						if (inputLine.length() > 0) {
							System.out.println("Server: " + inputLine);
						}
						if (inputLine.equals("Bye.")) {
							operation = false;
							System.out.println("false mode break");
							System.exit(1);
							break;
						}
						}
					if (operation == false) {
						System.out.println(" socket close");
						System.exit(1);
						clientSocket.close();
					}
					}
				catch (IOException ex) {
					System.out.println(ex);
				}
				
				
			}
	}
	


	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.err.println("Usage: java ChatServer <port number>");
			System.exit(1);
		}
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		Socket clientSocket = new Socket(hostName, portNumber);
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in)); //for user input
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
		ChatClient sthread = new ChatClient(clientSocket, inFromUser,
				outToServer);
		ChatClient rthread = new ChatClient(clientSocket, inFromServer);
		sthread.start();
		rthread.start();
	}
}