package com.rolan;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//import javax.net.ssl.HttpsURLConnection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class UrlConnection {

	private List<String> cookies;
	private HttpURLConnection conn;

	private final String USER_AGENT = "Mozilla/5.0";
  
	public List<String> getCookies() {
			return cookies;
	}
	
	public void setCookies(List<String> cookies) {
		this.cookies = cookies;
	}
	
	
  	private String GetPageContent(String url) throws Exception {

		URL obj = new URL(url);
		conn = (HttpURLConnection) obj.openConnection();

		// default is GET
		conn.setRequestMethod("GET");

		conn.setUseCaches(false);

		// act like a browser
		conn.setRequestProperty("User-Agent", USER_AGENT);
		conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if (cookies != null) {
			for (String cookie : this.cookies) {
				conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
			}
		}
		int responseCode = conn.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// Get the response cookies
		setCookies(conn.getHeaderFields().get("Set-Cookie"));

		return response.toString();

  	}
  
  
  	public String getFormParams(String html, String username, String password,String code) throws UnsupportedEncodingException {

  		System.out.println("Extracting form's data...");

  		Document doc = Jsoup.parse(html);

  		// Google form id
  		Element loginform = doc.getElementById("f");
  		Elements inputElements = loginform.getElementsByTag("input");
  		List<String> paramList = new ArrayList<String>();
  		for (Element inputElement : inputElements) {
  			String key = inputElement.attr("name");
  			String value = inputElement.attr("value");

  			if (key.equals("username"))
  				value = username;
  			else if (key.equals("password"))
  				value = password;
  			else if (key.equals("code"))
  				value = code;
  			paramList.add(key + "=" + URLEncoder.encode(value, "UTF-8"));
  		}

  		// build parameters list
  		StringBuilder result = new StringBuilder();
  		for (String param : paramList) {
  			if (result.length() == 0) {
  				result.append(param);
  			} else {
  				result.append("&" + param);
  			}
  		}
  		return result.toString();
  	}
  	
  	private void sendPost(String url, String postParams) throws Exception {

  		URL obj = new URL(url);
  		conn = (HttpURLConnection) obj.openConnection();

  		// Acts like a browser
  		conn.setUseCaches(false);
  		conn.setRequestMethod("POST");
  		conn.setRequestProperty("User-Agent", USER_AGENT);
  		conn.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
  		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
  		if(this.cookies != null) {
  			for (String cookie : this.cookies) {
  	  			conn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
  	  		}
  		}
  		
  		conn.setRequestProperty("Connection", "keep-alive");
  		conn.setRequestProperty("Referer",url);
  		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
  		conn.setRequestProperty("Content-Length", Integer.toString(postParams.length()));

  		conn.setDoOutput(true);
  		conn.setDoInput(true);

  		// Send post request
  		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
  		wr.writeBytes(postParams);
  		wr.flush();
  		wr.close();

  		int responseCode = conn.getResponseCode();
  		System.out.println("\nSending 'POST' request to URL : " + url);
  		System.out.println("Post parameters : " + postParams);
  		System.out.println("Response Code : " + responseCode);

  		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
  		String inputLine;
  		StringBuffer response = new StringBuffer();

  		while ((inputLine = in.readLine()) != null) {
  			response.append(inputLine);
  		}
  		in.close();
  		// System.out.println(response.toString());

  	}

  	public static void main(String[] args) throws Exception {
  		
  		Scanner sc = new Scanner(System.in);
  		
  		System.out.print("Enter your email  ");
  		String username = sc.nextLine();
  		username = username.trim();
  		
  		Console cnsl = System.console();
		char[] pwd = cnsl.readPassword("Enter your password  ");
		String password = String.valueOf(pwd);
  		
  		System.out.print("Enter google authenticator verification code  ");
  		int code = sc.nextInt();
  		
  		String url = "http://localhost:8080/login";
  		String postUrl = "http://localhost:8080/homepage.html?user="+username;

  		UrlConnection http = new UrlConnection();

  		// make sure cookies is turn on
  		CookieHandler.setDefault(new CookieManager());

  		// 1. Send a "GET" request, so that you can extract the form's data.
  		String page = http.GetPageContent(url);
  		//System.out.println(page);
  		
  		String postParams = http.getFormParams(page, username, password, Integer.toString(code));
  		
  		// 2. Construct above post's content and then send a POST request for
  		// authentication
  		http.sendPost(url, postParams);
  		
  		// 3. success then go to gmail.
  		String result = http.GetPageContent(postUrl);
  		System.out.println(result);
  		
  	}
  	
  	
  	
}