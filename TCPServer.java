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
import java.util.Collections;


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
						String current= src.nextLine();
						currentWord = current.toUpperCase();
						searchWord = searchWord.toUpperCase();
						if (isMatchingSearchTerm(searchWord,currentWord)) {
							commonArr.add(current);
						} 
						// for(i = 0; i < currentWord.length() - 1; i++){
						// 	System.out.println("currentWord char: " + currentWord.charAt(i));
						// 	System.out.println("searchWord char: " + searchWord.charAt(i));
						// 	if(currentWord.charAt(i) != searchWord.charAt(i)){
						// 		System.out.println("currentWord: " + currentWord);
						// 		System.out.println("searchWord: " + searchWord + "\n No longer share chars.\n");
						// 		i = currentWord.length();
						// 	}
						// 	else{
						// 		if (i == searchWord.length() -1)
						// 		commonArr.add(currentWord);
						// 	}
						// }
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
	static Boolean isMatchingSearchTerm(String search, String currentWord) {
		int searchLength = search.length();
		int currentLength = currentWord.length();
		if (currentLength < searchLength) {
			return false;
		}
		String subSearch = search.substring(0,searchLength - 1);
		String subCurrent = currentWord.substring(0,searchLength - 1);
		return subCurrent.equals(subSearch);
	}

	public static void main(String argv[]) throws Exception {
		String clientSentence, lowerizerSentence, capitalizedSentence;
		ArrayList<String> wordCollection; 
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			connectionSocket.setTcpNoDelay(true);
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
					Collections.sort(wordCollection, String.CASE_INSENSITIVE_ORDER);
					System.out.print(wordCollection.toString());
				
					//searchSrc(capitalizedSentence);
					outToClient.writeBytes(wordCollection.toString().replace("[","").replace("]","") + "\n");
					outToClient.flush();
				}
			} catch (Exception e) {
				// TODO: handle exception, if client closed connection, print:
				System.out.println("Client closed connection.");
			}
		}
	}
}
