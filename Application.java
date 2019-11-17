package WebSocketClient;

import java.net.URI;
import java.net.URISyntaxException;

class Application {
	
	public static void main (String args[]) {
		
		try {
			 // open websocket
			final DesktopEndpoint desktopEndpoint = new DesktopEndpoint(new URI("ws://localhost:8080/login/"));

            // add listener
            desktopEndpoint.addMessageHandler(new DesktopEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                }
            });
            
            desktopEndpoint.sendMessage("Talking from client");
            
            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}