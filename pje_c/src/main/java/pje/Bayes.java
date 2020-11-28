package pje;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Bayes {
	private String file;
	public Bayes(String base) {
		this.file=base;
	}
	
	public String getTweet(String s) {
		String res= s.split(",")[2];
		return (String) res.substring(2, res.length()-2).trim();
	}
	
	public int getClass(String s) {
		return Integer.parseInt(s.split(",")[5].substring(0, 1)); 
	}
	
	
	
	public int nbOfOccurences(String mot, int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		int cpt = 0;
		while((line = reader.readLine()) != null) {
			if (classe == this.getClass(line)) {
				String tweet = getTweet(line);
				String[] words = tweet.split(" ");
				//System.out.print(Arrays.asList(words));
				//System.out.print(words.length);
				for(int i = 0; i<words.length;i++) {
					if (words[i].compareToIgnoreCase(mot)==0) {
						cpt++;
					}
				}
			}
		}
		//System.out.print(cpt);
		return cpt;
	}
	
	public int nbOfWords(int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		Set<String> words = new HashSet<String>();
		String line;
		while((line = reader.readLine()) != null) {
			if (classe == this.getClass(line)) {
				String tweet = getTweet(line);
				words.addAll(Arrays.asList(tweet.split(" ")));
			}
		}
		return words.size();
	}
	
	public int allWords() throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		Set<String> words = new HashSet<String>();
		while((line = reader.readLine()) != null) {
			String tweet = getTweet(line);
			words.addAll(Arrays.asList(tweet.split(" ")));
		}
		return words.size();
	}
	public float probaMot(String mot, int classe) throws IOException {
		int nc = this.nbOfWords(classe); 
		int nmc = this.nbOfOccurences(mot, classe);
		int all = this.allWords();
		//System.out.print(nmc+" "+nc+" "+all+ " ");
		return (float) (nmc+1)/(nc+all);
	}
	
	public float probaTweet(String tweet, int classe) throws IOException {
		float cpt=1;
		float pc = this.probaClasse(classe);
		String[] mots = tweet.split(" ");
		for(int i = 0;i<mots.length;i++) {
			cpt=cpt*this.probaMot(mots[i], classe);
		}
		float pclasse = this.probaClasse(classe);
		return (float) cpt*pclasse;
	}
	
	public float probaClasse(int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		int total=0;
		int cpt=0;
		while((line = reader.readLine()) != null) {
			if (classe == this.getClass(line)) {
				cpt++;
			}
			total++;
		}
		return (float) cpt/total;
	}
	
	public float result (String tweet) throws IOException {
		float pos = this.probaTweet(tweet, 4);
		float neu = this.probaTweet(tweet, 2);
		float neg = this.probaTweet(tweet, 0);
		System.out.print(pos+" ");
		System.out.print(neu+" ");
		System.out.print(neg+" ");
		float res = Math.max(pos, Math.max(neu, neg));
		if (res==pos) {
			return 4;
		}
		else if(res==neu) {
			return 2;
		}
		else if(res==neg) {
			return 0;
		}
		return -1;
	}

	public static void main(String args[]) throws IOException {
		Bayes b = new Bayes("requests.csv");
		FileReader myReader = new FileReader("requests.csv");
		BufferedReader reader = new BufferedReader(myReader);
		String thisLine = null;
		String line = null;
		reader.readLine();
		while((line = reader.readLine()) != null) {
			String tweet = b.getTweet(line);
			//System.out.println(tweet);
		}
		float res = b.result("cool bien heureux incroyable");
		//System.out.print(Math.max(6.8060704E-35, 4.2763974E-35)+" f ");
		System.out.print(res);
	}
}