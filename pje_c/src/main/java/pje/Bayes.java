package pje;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Bayes {
	private String file;
	public Bayes(String base) {
		this.file=base;
	}
	
	public int nbOfOccurences(String mot, int classe) throws FileNotFoundException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);

		return 0;
	}
	
	public int nbOfWords(int classe) throws FileNotFoundException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		return 0;
	}
	public int allWords() throws FileNotFoundException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		return 0;
	}
	public int probabilité(String tweet, int classe) {
		return 0;
	}
	
	public static void main(String args[]) throws IOException {
	
		FileReader myReader = new FileReader("requests.csv");
		BufferedReader reader = new BufferedReader(myReader);
		String thisLine = null;
		String line = null;
		reader.readLine();
		while((line = reader.readLine()) != null) {
			String[] tweet = line.split(",");
			System.out.println(tweet[2]);
		}
		
	}
}
