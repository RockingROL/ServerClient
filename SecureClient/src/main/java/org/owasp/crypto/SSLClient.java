 package org.owasp.crypto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.Security;
import java.util.Scanner;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * @author Joe Prasanna Kumar
 * This program simulates a client socket program which communicates with the SSL Server
 * 
 * Algorithm:
 * 1. Determine the SSL Server Name and port in which the SSL server is listening
 * 2. Register the JSSE provider
 * 3. Create an instance of SSLSocketFactory
 * 4. Create an instance of SSLSocket
 * 5. Create an OutputStream object to write to the SSL Server
 * 6. Create an InputStream object to receive messages back from the SSL Server
 * 
 */ 

public class SSLClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String strServerName = "localhost"; // SSL Server Name
		int intSSLport = 3112; // Port where the SSL Server is listening
		
        
		// Registering the JSSE provider
		Security.addProvider(Security.getProvider("SunJSSE"));
		
		System.setProperty("javax.net.ssl.trustStore","C:\\Users\\QRC\\keystore\\client\\clientTruststore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword","changeit");
		
		try {
			// Creating Client Sockets
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(strServerName,intSSLport);

         	// Initializing the streams for Communication with the Server
			PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
         	BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
			
         	Scanner scanner = new Scanner(System.in);
            while(true){
            	System.out.println(in.readLine());
                System.out.println("Enter something:");
                String inputLine = scanner.nextLine();
                out.println(inputLine);
                
            	if(inputLine.equals("quit")) {  
	    			System.out.println("Client " + sslSocket + " sends exit..."); 
	                System.out.println("Closing this connection."); 
	                sslSocket.close(); 
	                System.out.println("Connection closed"); 
	                break; 
	    		} 
                 
                
            }
            

            scanner.close();
            // Closing the Streams and the Socket
			out.close();
			in.close();
			//stdIn.close();
			//sslSocket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}