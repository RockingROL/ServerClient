package org.owasp.crypto;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.net.ssl.SSLSocket;

public class ClientHandler extends Thread { 

	private final SSLSocket sslSocket;
	private final BufferedReader in;
	private final PrintWriter out;
	        
	// Constructor 
	public ClientHandler(SSLSocket sslSocket_, BufferedReader in_, PrintWriter out_) { 
		sslSocket = sslSocket_; 
	    in = in_; 
	    out = out_; 
	} 
	  
	@Override
	public void run() { 
		String received; 
		try { 
			while (true) { 
	  
	    		// Ask user what he wants 
	    		out.println("DO you want to say something?");
	                  
	            // receive the answer from client 
	    		received = in.readLine();
	                  
	    		if(received.equals("quit")) {  
	    			System.out.println("Client " + sslSocket + " sends exit..."); 
	                System.out.println("Closing this connection."); 
	                sslSocket.close(); 
	                System.out.println("Connection closed"); 
	                break; 
	    		} 

	            System.out.println("Received input from client "+sslSocket+":"+received);
	            out.println("Response from server");
			}
		} catch (Exception e) { 
			e.printStackTrace();  
		} finally {     
			try { 
				// closing resources 
				in.close(); 
				out.close();       
			} catch(Exception e){ 
				e.printStackTrace(); 
			} 	
		}
	}
	
} 
	
