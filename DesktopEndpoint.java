package WebSocketClient;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpoint;
import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

@ClientEndpoint
class DesktopEndpoint extends Endpoint{
	
	private Session userSession;
	private MessageHandler messageHandler;

	
	String login() {
		  try {
		    String loginBase = "http://localhost:8080/login";
		    //Console console = System.console();
		    //if(console == null) throw new RuntimeException("No console!");
		    //String username = console.readLine("Username: ");
		    //char[] password = console.readPassword("Password: ");
		    String username = "rolan.crasto93@gmail.com";
		    String password = "Grettamymother@31";
		    URL loginUrl = new URL(loginBase/*+"?username="+username+"&password="+new String(password)*/);
		    HttpURLConnection con = (HttpURLConnection) loginUrl.openConnection();
		    //con.setInstanceFollowRedirects(false);
		    String cookie = con.getHeaderField("Set-Cookie");
		    System.out.println("Cookie:"+cookie);
		    if(cookie!=null)
		    	return cookie.replaceAll("SESSIONID=(.*?);.*", "$1");
		    return cookie;
		  } catch (IOException e) {
		    // do something sensible
		    return null;
		  }
		}

	
	DesktopEndpoint(URI endpointURI) {
		
		final ClientEndpointConfig cec =
				ClientEndpointConfig.Builder.create().configurator(
		new ClientEndpointConfig.Configurator() {
		  @Override
		  public void beforeRequest(Map<String, List<String>> headers) {
		    super.beforeRequest(headers);
		    String sessionId = login();
		    List cookieList = headers.get("Cookie");
		    if (cookieList == null) cookieList = new ArrayList();
		    cookieList.add("SESSIONID=\""+sessionId+"\"");
		    headers.put("Cookie", cookieList);
		  }
		}).build();
		
		
		
		
		
		WebSocketContainer container = ContainerProvider.getWebSocketContainer();
		try {
			//container.connectToServer(this, endpointURI);
			container.connectToServer(this, cec, endpointURI);
		} catch (DeploymentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	   /**
     * Callback hook for Connection open events.
     *
     * @param userSession the userSession which is opened.
     */
	
	@Override
	public void onOpen(Session userSession, EndpointConfig config) {
		System.out.println("opening websocket");
        this.userSession = userSession;
		
	}
	
    /**
     * Callback hook for Connection close events.
     *
     * @param userSession the userSession which is getting closed.
     * @param reason the reason for connection close
     */
    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    /**
     * Callback hook for Message Events. This method will be invoked when a client send a message.
     *
     * @param message The text message
     */
    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }
	
    /**
     * register message handler
     *
     * @param msgHandler
     */
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }
    
    /**
     * Send a message.
     *
     * @param message
     */
    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }
    
    /**
     * Message handler.
     *
     */
    public static interface MessageHandler {

        public void handleMessage(String message);
    }


}