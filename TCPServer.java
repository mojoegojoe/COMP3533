import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.File;
import java.lang.module.ModuleDescriptor.Opens;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;


public class TCPServer {
	static ArrayList<String> searchSrc(String searchWord) throws Exception{
		File srcFile = new File("words.txt");
		String currentWord;
		int i;
		ArrayList<String> commonArr = new ArrayList<String>();
		try{
			if(srcFile.exists()){
				Scanner src = new Scanner(srcFile);
				try{
					while(src.hasNext()){
						currentWord = src.nextLine().toUpperCase();
						searchWord = searchWord.toUpperCase();
						for(i = 0; i < currentWord.length(); i++){
							//System.out.println("currentWord char: " + currentWord.charAt(i));
							//System.out.println("searchWord char: " + searchWord.charAt(i));
							if(currentWord.charAt(i) != searchWord.charAt(i)){
							//	System.out.println("currentWord: " + currentWord);
							//	System.out.println("searchWord: " + searchWord + "\n No longer share chars.\n");
								i = currentWord.length();
							}
							else{
								commonArr.add(currentWord);
							}
						}
					}
				}
				catch(Exception e){
						// handle
						System.out.print("err" + e);
				}
			}
		}
		catch(Exception e) {
			System.out.println("No access to file");
		}
		return commonArr;
	} 
	public static void main(String argv[]) throws Exception {
		String clientSentence, lowerizerSentence, capitalizedSentence;
		ArrayList<String> wordCollection; 
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			System.out.println("Accepted TCP connection from" 
					+ connectionSocket.getInetAddress() 
					+ ":" + connectionSocket.getPort());
			try {
				while (true) {
					clientSentence = inFromClient.readLine();
					lowerizerSentence = clientSentence.toLowerCase() + '\n';
					capitalizedSentence = clientSentence.toUpperCase() + '\n';
					wordCollection = searchSrc(lowerizerSentence);
					System.out.print(wordCollection);
					//searchSrc(capitalizedSentence);
					outToClient.writeBytes(capitalizedSentence);
				}
			} catch (Exception e) {
				// TODO: handle exception, if client closed connection, print:
				System.out.println("Client closed connection.");
			}
		}
	}
}
