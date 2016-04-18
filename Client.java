//Shane O' Rourke - 12361351
//Lab Assignment 11 - Programming

/**
 * This class contains all the functionality that is required to run the client.
 * Downloads to and uploads from a client folder which stores all the files.
 */

package Lab113rdYear;

import java.io.*;
import java.net.*;
import java.util.*;
import org.apache.commons.io.FileUtils;

public class Client extends Thread{
	
	private Socket client;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private String server;
	private String directory = "C:\\Users\\Shane O' Rourke\\Desktop\\Client\\";
	//Above is where the client uploads the files from and downloads to.
	private File file;//Temporarily stores the file which is uploaded or downloaded.
	private String clientChoice;//Client choice to upload or download.
	
	public Client(){
		server = "127.0.0.1";//Address of server.
	}
	
	//Method that calls the methods of the client class.
	public void run(){
		
		try {
			connectWithServer();
			getStreams();
			clientChoice();
		} catch (IOException e) {
			System.out.println("Error connecting with server/Getting streams: "+e);
		}
		
		if(clientChoice.equals("1")){
			
			try{
				System.out.println("Upload Started.");
				chooseFile();//Prompts user to enter file they wish to upload.
				uploadFile(file);
			}
			catch (IOException e){
				System.out.println("Error Upload Client: "+e);
			}
		}
		else if (clientChoice.equals("2")){
			try{
				System.out.println("Download Started.");
				chooseFile();//Prompts user to enter file they wish to download.
				downloadFile();
			}
			catch (IOException e){
				System.out.println("Error Download Client: "+e);
			}
		}
		closeConnection();
		
	}
	
	//Initialises the connection with the server.
	private void connectWithServer() throws IOException{
		
		client = new Socket(InetAddress.getByName(server), 2015);
		//Above the creates a socket with the address and port number.
		System.out.println("Client Connected To: "+client.getInetAddress().getHostName());
	}//end of connect with server.
	
	//Obtains the sockets streams and uses them to initialise the input and output streams.
	private void getStreams() throws IOException{
		output = new ObjectOutputStream(client.getOutputStream());
		output.flush();
		
		input = new ObjectInputStream(client.getInputStream());
		System.out.println("Client Received I/O Streams.");
	}//end of get streams.
	
	//Method used to write file to output stream. To upload a file.
	private void uploadFile(File file) throws IOException{
		try{
			output.writeObject(file);
			output.flush();
			System.out.println("Client Upload Written: "+file.getName());
		}
		catch (IOException e){
			System.out.println("Client Upload Written Error: "+e);
		}
	}//end of sendData().
	
	//Method reads in the file from the input stream and then copies the file to the client folder.
	private void downloadFile() throws IOException{
		File destFile = new File(directory);
		try {
			file = (File) input.readObject(); 
			FileUtils.copyFileToDirectory(file, destFile);//Copies file to destination file.
			System.out.println("CLIENT DOWNLOADED FILE FROM SERVER: "+file.getName());
		} catch (ClassNotFoundException e) {
			System.out.println("Client Download File Error : "+e);
		}
	}//end of download file.
	
	//Prompts the user to input the file name that they want.
	private void chooseFile(){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter name of file: ");
		String fileName = in.next();
		file = new File(directory+fileName);
		if (clientChoice.equals("2")){
			clientFileRequester(fileName);
			//If the user wishes to download then send this file name to the server.
		}
	}
	
	//Closes the socket, output and input streams.
	private void closeConnection(){
		System.out.println("Client Closing Connection.");
		
		try {
			output.close();
			input.close();
			client.close();
		}
		catch(IOException e){
			System.out.println("Client Error Closing Connection: "+e);
		}
	}//end of close connection.
	
	//Sends the client choice (upload/download) to the server so the server knows what to do.
	private void clientChoiceSender(String choice){
		try{
			output.writeObject(choice);
			output.flush();
		}
		catch(IOException e){
			System.out.println("Error in client choice sender: "+e);
		}
	}
	
	//If the client wishes to download then send the file name to the server.
	private void clientFileRequester(String file){
		try {
			output.writeObject(file);
			output.flush();
		} catch (IOException e) {
			System.out.println("Client file requester error: "+e);
		}
	}
	
	//Prompts the client whether they wish to upload or download.
	private void clientChoice(){
		Scanner in = new Scanner(System.in);
		System.out.println("Enter 1 to Upload or 2 to Download: ");
		clientChoice = in.next();
		clientChoiceSender(clientChoice);//Sends client choice to server.
	}

}//End of class.
