//Shane O' Rourke - 12361351
//Lab Assignment 11 - Programming

package Lab113rdYear;

import java.io.*;

public class ServerTest {

	public static void main(String[] args) throws IOException {
		
			//Creates a server object and then calls the run method to start the server.
			Server sers = new Server();
			try {
				sers.run();
			} catch (ClassNotFoundException e) {
				System.out.println("Error starting server: "+e);
			}
		}
}
