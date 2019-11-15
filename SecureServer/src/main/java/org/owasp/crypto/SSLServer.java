package org.owasp.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Security;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * @author Joe Prasanna Kumar
 * This program simulates an SSL Server listening on a specific port for client requests
 * 
 * Algorithm:
 * 1. Regsiter the JSSE provider
 * 2. Set System property for keystore by specifying the keystore which contains the server certificate
 * 3. Set System property for the password of the keystore which contains the server certificate
 * 4. Create an instance of SSLServerSocketFactory
 * 5. Create an instance of SSLServerSocket by specifying the port to which the SSL Server socket needs to bind with
 * 6. Initialize an object of SSLSocket
 * 7. Create InputStream object to read data sent by clients
 * 8. Create an OutputStream object to write data back to clients.
 * 
 */ 


public class SSLServer {

	/**
	 * @param args
	 */
	//private static final String USER_AGENT = "Mozilla/5.0";
	//private static final String GET_URL = "http://localhost:8080/homepage.html?user=rolan.crasto93@gmail.com";
	private static final String GET_URL = "http://localhost:8080/login";
	
	private static final String POST_URL = "http://localhost:8080/homepage.html?user=rolan.crasto93@gmail.com";
	
	private static final String POST_PARAMS = "userName=rolan.crasto93@gmail.com&password=Grettamymother@31";
	
	private static void sendGET() throws IOException {
		URL obj = new URL(GET_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		//con.setRequestProperty("User-Agent", USER_AGENT);
		int responseCode = con.getResponseCode();
		System.out.println("GET Response Code :: " + responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) { // success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("GET request not worked");
		}

	}

	private static void sendPOST() throws IOException {
		URL obj = new URL(POST_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		//con.setRequestProperty("User-Agent", USER_AGENT);

		// For POST only - START
		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
			in.close();

			// print result
			System.out.println(response.toString());
		} else {
			System.out.println("POST request not worked");
		}
	}
	
	
	public static void main(String[] args) {
		
		try {
			//sendGET();
			sendPOST();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
		/*
		int intSSLport = 3112; // Port where the SSL Server needs to listen for new requests from the client

		// Registering the JSSE provider
		Security.addProvider(Security.getProvider("SunJSSE"));

		//Specifying the Keystore details
		System.setProperty("javax.net.ssl.keyStore","C:\\Users\\QRC\\keystore\\server\\server.jks");
		System.setProperty("javax.net.ssl.keyStorePassword","changeit");

		// Enable debugging to view the handshake and communication which happens between the SSLClient and the SSLServer
		//System.setProperty("javax.net.debug","all");

		// Initialize the Server Socket
		SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory)SSLServerSocketFactory.getDefault();
		SSLServerSocket sslServerSocket = null;
		try {
			sslServerSocket = (SSLServerSocket)sslServerSocketfactory.createServerSocket(intSSLport);
			System.out.println("SSL ServerSocket started");
			SSLSocket sslSocket = null;
			try {
				while(true) {
					sslSocket = (SSLSocket)sslServerSocket.accept();
					System.out.println("ServerSocket accepted");
					
					// Create Input / Output Streams for communication with the client
					PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
					BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
												
					System.out.println("Assigning new thread for this client"); 
						
					// create a new thread object
					ClientHandler clHndlr = new ClientHandler(sslSocket, in, out);
					Thread th =  clHndlr;
							
					// Invoking the start() method 
		            th.start();
		                
				}		
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sslSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				sslServerSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		*/
	}
	
}