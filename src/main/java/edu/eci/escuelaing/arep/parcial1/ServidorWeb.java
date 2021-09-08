/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.escuelaing.arep.parcial1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 *
 * @author jgarc
 */

public class ServidorWeb {
	public static void main(String[] args) throws IOException, WeatherServiceException {
		
		while (true) {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket(getPort());
			} catch (IOException e) {
				System.err.println("Could not listen on port: 8080.");
				System.exit(1);
			}
			Socket clientSocket = null;
			try {
				System.out.println("Listo para recibir ...");
				clientSocket = serverSocket.accept();
			} catch (IOException e) {
				System.exit(1);
			}

			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String inputLine, outputLine;
			String[] arch = null;
			String[] elem = null;

			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("GET")) {
					arch = inputLine.split("/");
					elem = arch[1].split(" ");
                                        System.out.print(elem[0]);
                                        if(elem[0].equals("clima")){
                                            file("clima", clientSocket.getOutputStream(), out);
                                        }else{
                                            file(getCityWeather(elem[0]), clientSocket.getOutputStream(), out);
                                        }					
				}
				if (!in.ready()) {
					break;
				}
			}
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
		}
	}
	 public static String getCityWeather(String city) throws WeatherServiceException {
        try {
            HttpURLConnection connection;
            StringBuffer response = null;
            BufferedReader in;
            int responseCode;
            String inputLine;
            URL url;
            url = new URL("http://api.openweathermap.org/data/2.5"+"/weather?q="+city+"&appid=bcb69238dbb54b09e975adeba8ea99cd");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            responseCode = connection.getResponseCode();
            if (responseCode==HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                inputLine = in.readLine();
                response = new StringBuffer();

                while (inputLine!=null) {
                    response.append(inputLine);
                    inputLine = in.readLine();
                }
                in.close();
            }
            return String.valueOf(response);
        } catch (Exception e) {
            throw new WeatherServiceException(e.getMessage());
        }
    }

	static int getPort() {
		if (System.getenv("PORT") != null) {
			return Integer.parseInt(System.getenv("PORT"));
		}

		return 8080; // returns default port if heroku-port isn't set(i.e. on localhost) }
	}
        public static void file(String contenido , OutputStream clientOutput, PrintWriter out ) throws FileNotFoundException, IOException{  
            System.out.println(contenido); 
            if(!contenido.equals("clima")){
                String outputLine = "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n"
                        + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <title>index file</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<div class=\"container\">\n"
                        + "  <div class=\"title\">\n"
                        + "    <h1 class=\"text-title\">SERVER</h1>\n"
                        + "  </div>\n"
                        + "  <div class=\"container-input\">\n"
                        + contenido
                        + "  </div>\n"
                        + "</div>\n"
                        + "</body>\n"
                        + "</html>";
                out.write(("HTTP/1.1 404 Not Found \r\n" + "Content-Type: text/html; charset=\"utf-8\" \r\n" + "\r\n"
						+ outputLine));
            }else{
                String outputLine = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n"
                        + "<head>\n"
                        + "    <meta charset=\"UTF-8\">\n"
                        + "    <title>index file</title>\n"
                        + "</head>\n"
                        + "<body>\n"
                        + "<div class=\"container\">\n"
                        + "  <div class=\"title\">\n"
                        + "    <h1 class=\"text-title\">SERVER</h1>\n"
                        + "  </div>\n"
                        + "  <div class=\"container-input\">\n"
                        + "    <p>para buscar la informacion de una ciudad en especifico grega al path : /'nombre de la ciudad' </b></p>\n"
                        + "  </div>\n"
                        + "</div>\n"
                        + "</body>\n"
                        + "</html>";
                out.write(("HTTP/1.1 404 Not Found \r\n" + "Content-Type: text/html; charset=\"utf-8\" \r\n" + "\r\n"
						+ outputLine));
            }
    }
}
