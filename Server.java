//Shane O' Rourke - 12361351
//Lab Assignment 11 - Programming

/**
 * This class contains the methods that handle the running of the server. It makes use of the
 * org.apache.commons.io.* classes which allow the user to copy files to their own directories.
 * Uploads and takes files from a Server folder.
 */

package Lab113rdYear;

import java.io.*;
import java.net.*;
import org.apache.commons.io.*;

public class Server {
	
	private ServerSocket server;
	private Socket connection;
	private ObjectInputStream input;//Stream stores data from client.
	private ObjectOutputStream output;//Stream stores data to be sent to the client.
	private File serverFile;
	private int count;
	private String dir = "C:\\Users\\Shane O' Rourke\\Desktop\\Server\\";
	//File path above is where I store the files received.
	private String choice;//Stores the choice of client. Either server or client.
	private String fileRequest;//Stores the filepath requested from the client.
	
	public Server(){
		try {
			server = new ServerSocket(2015, 10);//Initialises the server with port number and no. connections.
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Run class that starts all the methods required to run the server.
	public void run() throws ClassNotFoundException{
		
		try{
			while(true)
			{
				try{
					awaitConnection();
					getStreams();
					clientChoiceGetter();
					
					if (choice.equals("1")){//If client wishes to upload.
						inputFile();
					}
					else if (choice.equals("2")){//If client wishes to download..
						serverFileRequestGetter();
						outputFile(fileRequest);//Passes file path requested by client.
					}
				}
				catch(EOFException e){
					e.printStackTrace();
				}
				finally{
					closeConnection();
					count++;
				}
			}//end while
		}//end try
		
		catch(IOException e){
			e.printStackTrace();
		}
	}//End of run.
	
	//Reads in the file uploaded by the client.
	private void inputFile() throws IOException{
		try {	
			serverFile = (File) input.readObject();//Stores uploaded file.
			File destFile = new File(dir);//File path where uploaded file is to be stored on the server.
			FileUtils.copyFileToDirectory(serverFile, destFile);//Copies file to this filepath.
			System.out.println("FILE UPLOADED TO SERVER: "+serverFile.getName());
				
		}catch (ClassNotFoundException e) {
			System.out.println("Server - Error In Upload.");
			return;
		}
		
	}//end inputFile().
	
	//Sends off the file requested to be downloaded by the client.
	private void outputFile(String fileRequest) throws IOException{
		try{
			String filePath = "C:\\Users\\Shane O' Rourke\\Desktop\\Server\\"+fileRequest;
			serverFile = new File(filePath);//Stores the file to be sent off.
			output.writeObject(serverFile);//Writes this file to the output stream.
			output.flush();//Forces any data in output stream to be written out.
			System.out.println("Server File Output Written: "+serverFile.getName());
		
		}catch(IOException e){
			System.out.println("Server Error writing to output: "+e);
		}
	}//end of outputfile().
	
	//Waits for a client connection.
	private void awaitConnection() throws IOException{
		
		System.out.println("Server Waiting For Request.\n");
		connection = server.accept();//Waits in standby for client connection request.
		count++;//Counts the number of connections to the server.
		System.out.println("Server Connection No: "+count+"\nRequest from: "
		+connection.getInetAddress().getHostName());
		
	}//end awaitConnection.
	
	//Obtains the sockets streams and uses them to initialise the input and output streams.
	private void getStreams() throws IOException{
		
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		System.out.println("Server Received I/O Streams.");
		
	}//end getStreams().
	
	//Closes off the connections and streams to the socket.
	private void closeConnection() throws IOException{
		System.out.println("Server Terminating Connection");
		
		try{
			output.close();
			input.close();
			connection.close(); 
			count--;
			System.out.println("Server Output/Input/Connection closed");
		}
		catch (IOException e){
			e.printStackTrace();
		}
		
	}//end close connection().
	
	//Receives the client choice e.g. Upload or Download. Represented as a 1 or 2.
	private void clientChoiceGetter(){
		try {
			choice = (String) input.readObject();//Stores the client choice.
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found: "+e);
		} catch (IOException e) {
			System.out.println("IO error: "+e);
		}
	}
	
	//Receives and stores the file name that the client wishes to download.
	private void serverFileRequestGetter(){
		try {
			fileRequest = (String) input.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Class Not Found: "+e);
		} catch (IOException e) {
			System.out.println("IO error: "+e);
		}
	}
	
}//End of class.
