package pje;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Bayes {
	private String file;
	public Bayes(String base) {
		this.file=base;
	}
	
	public int getNbLinesFile() throws IOException {
		int res = 0;
		FileReader f = new FileReader(this.file);
		BufferedReader b = new BufferedReader(f);
		while(b.readLine()!= null) res+=1;
		return res-1;
	}
	
	public String getTweet(String s) {
		String res= s.split(",")[2];
		return (String) res.substring(2, res.length()-2).trim();
	}
	
	public int getClass(String s) {
		//System.out.println(s.split(",")[5]);
		return Integer.parseInt(s.split(",")[5].substring(0, 1)); 
	}
	
	
	
	public int nbOfOccurences(String mot, int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		int cpt = 0;
		reader.readLine();
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
		reader.close();
		//System.out.print(cpt);
		return cpt;
	}
	
	public int nbOfOccurencesBigramme(String mot, int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		int cpt = 0;
		reader.readLine();
		while((line = reader.readLine()) != null) {
			if (classe == this.getClass(line)) {
				String tweet = getTweet(line);
				String[] words = tweet.split(" ");
				Object[] mots = this.toBigramme(words);
				//System.out.print(Arrays.asList(words));
				//System.out.print(words.length);
				for(int i = 0; i<mots.length;i++) {
					String m = (String) mots[i];
					if (m.compareToIgnoreCase(mot)==0) {
						cpt++;
					}
				}
			}
		}
		reader.close();
		//System.out.print(cpt);
		return cpt;
	}

	public int nbOfWords(int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		Set<String> words = new HashSet<String>();
		String line;
		reader.readLine();
		while((line = reader.readLine()) != null) {
			if (classe == this.getClass(line)) {
				String tweet = getTweet(line);
				words.addAll(Arrays.asList(tweet.split(" ")));
			}
		}
		reader.close();
		return words.size();
	}
	
	public int allWords() throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		Set<String> words = new HashSet<String>();
		reader.readLine();
		while((line = reader.readLine()) != null) {
			String tweet = getTweet(line);
			words.addAll(Arrays.asList(tweet.split(" ")));
		}
		reader.close();
		return words.size();
	}
	
	public float probaMot(String mot, int classe) throws IOException {
		int nc = this.nbOfWords(classe); 
		int nmc = this.nbOfOccurences(mot, classe);
		int all = this.allWords();
		//System.out.print(nmc+" "+nc+" "+all+ " ");
		return (float) (nmc+1)/(nc+all);
	}
	
	public float probaBigramme(String mot, int classe) throws IOException {
		int nc = this.nbOfWords(classe); 
		int nmc = this.nbOfOccurencesBigramme(mot, classe);
		int all = this.allWords();
		//System.out.print(nmc+" "+nc+" "+all+ " ");
		return (float) (nmc+1)/(nc+all);
	}
	
	public float probaTweet(String tweet, int classe) throws IOException {
		float cpt=1;
		//float pc = this.probaClasse(classe);
		String[] mots = tweet.split(" ");
		List<String> dejaUtilise = new ArrayList<String>(); 
		for(int i = 0;i<mots.length;i++) {
			if (!dejaUtilise.contains(mots[i]) && mots[i].length()>2) {
			cpt=cpt*this.probaMot(mots[i], classe);
			dejaUtilise.add(mots[i]);
			}
		}
		float pclasse = this.probaClasse(classe);
		return (float) cpt*pclasse;
	}
	
	public float probaTweetBigramme(String tweet, int classe) throws IOException {
		float cpt=1;
		Object[] mots = this.toBigramme((tweet.split(" ")));
		List<String> dejaUtilise = new ArrayList<String>(); 
		for(int i = 0;i<mots.length;i++) {
			String m = (String) mots[i];
			if (!dejaUtilise.contains(m) && m.length()>3) {
			cpt=cpt*this.probaBigramme(m, classe);
			dejaUtilise.add(m);
			}
		}
		float pclasse = this.probaClasse(classe);
		return (float) cpt*pclasse;
	}
	
	public float probaTweetMotAndBigramme(String tweet, int classe) throws IOException {
		float cpt=1;
		String[] words = tweet.split(" ");
		List<String> dejaUtilise = new ArrayList<String>(); 
		for(int i = 0;i<words.length;i++) {
			if (!dejaUtilise.contains(words[i]) && words[i].length()>2) {
			cpt=cpt*this.probaMot(words[i], classe);
			dejaUtilise.add(words[i]);
			}
		}
		Object[] mots = this.toBigramme((tweet.split(" "))); 
		for(int i = 0;i<mots.length;i++) {
			String m = (String) mots[i];
			if (!dejaUtilise.contains(m) && m.length()>2) {
			cpt=cpt*this.probaBigramme(m, classe);
			dejaUtilise.add(m);
			}
		}
		float pclasse = this.probaClasse(classe);
		return (float) cpt*pclasse;
	}
	
	public float probaTweetByFrequency(String tweet, int classe) throws IOException {
		float cpt=1;
		String[] mots = tweet.split(" ");
		HashMap<String, Integer> dico = this.OccurenceDeMotsDansUnTweet(tweet);
		List<String> dejaUtilise = new ArrayList<String>(); 
		for(int i = 0;i<mots.length;i++) {
			if (!dejaUtilise.contains(mots[i]) && mots[i].length()>2) {
				cpt=(float) (cpt*(Math.pow(this.probaMot(mots[i], classe), dico.get(mots[i]))));
				dejaUtilise.add(mots[i]);
			}
		}
		float pclasse = this.probaClasse(classe);
		return (float) cpt*pclasse;
	}
	
	public float probaTweetByFrequencyBigramme(String tweet, int classe) throws IOException {
		float cpt=1;
		String[] mots = tweet.split(" ");
		HashMap<String, Integer> dico = this.OccurenceDeMotsDansUnTweet(tweet);
		List<String> dejaUtilise = new ArrayList<String>(); 
		for(int i = 0;i<mots.length;i++) {
			if (!dejaUtilise.contains(mots[i]) && mots[i].length()>3) {
				cpt=(float) (cpt*(Math.pow(this.probaMot(mots[i], classe), dico.get(mots[i]))));
				dejaUtilise.add(mots[i]);
			}
		}
		HashMap<String, Integer> dico2 = this.OccurenceDeBigrammeDansUnTweet(tweet);
		Object[] words = this.toBigramme((tweet.split(" "))); 
		for(int i = 0;i<words.length;i++) {
			String m = (String) words[i];
			if (!dejaUtilise.contains(m) && m.length()>3) {
			cpt=(float) (cpt*(Math.pow(this.probaBigramme(m, classe), dico2.get(m))));
			dejaUtilise.add(m);
			}
		}
		float pclasse = this.probaClasse(classe);
		return (float) cpt*pclasse;
	}
	
	private HashMap<String, Integer> OccurenceDeBigrammeDansUnTweet(String t) {
		Object[] mots = this.toBigramme(t.split(" "));
		HashMap<String, Integer> dico = new HashMap<String, Integer>();
		for(int i = 0;i<mots.length;i++) {
			String m = (String) mots[i];
			if (dico.containsKey(m)) {
				dico.put(m, dico.get(mots[i])+1);
			}
			else dico.put(m, 1);
		}
		return dico;
	}

	public Object[] toBigramme(String[] mots) {
		ArrayList<String> bigramme = new ArrayList<String>();
		int n = mots.length;
		for (int i = 0; i<n; i++) {
			if(i!= (n-1)) {
			bigramme.add((mots[i].trim())+" "+(mots[i+1].trim()));
			}
		}
		Object[] res = bigramme.toArray();
		return res;
	}
	
	public HashMap<String, Integer> OccurenceDeMotsDansUnTweet(String t){
		String[] mots = t.split(" ");
		HashMap<String, Integer> dico = new HashMap<String, Integer>();
		for(int i = 0;i<mots.length;i++) {
			if (dico.containsKey(mots[i])) {
				dico.put(mots[i], dico.get(mots[i])+1);
			}
			else dico.put(mots [i], 1);
		}
		return dico;
	}
	
	public float probaClasse(int classe) throws IOException {
		FileReader myReader = new FileReader(file);
		BufferedReader reader = new BufferedReader(myReader);
		String line;
		int total=0;
		int cpt=0;
		reader.readLine();
		while((line = reader.readLine()) != null) {
			if (classe == this.getClass(line)) {
				cpt++;
			}
			total++;
		}
		reader.close();
		return (float) cpt/total;
	}
	
	public float result (String tweet) throws IOException {
		float pos = this.probaTweet(tweet, 4);
		float neu = this.probaTweet(tweet, 2);
		float neg = this.probaTweet(tweet, 0);
		//System.out.print(pos+" ");
		//System.out.print(neu+" ");
		//System.out.print(neg+" ");
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
	
	public float resultByFrequency (String tweet) throws IOException {
		float pos = this.probaTweetByFrequency(tweet, 4);
		float neu = this.probaTweetByFrequency(tweet, 2);
		float neg = this.probaTweetByFrequency(tweet, 0);
		//System.out.print(pos+" ");
		//System.out.print(neu+" ");
		//System.out.print(neg+" ");
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
	
	public float resultByFrequencyAndBigramme (String tweet) throws IOException {
		float pos = this.probaTweetByFrequencyBigramme(tweet, 4);
		float neu = this.probaTweetByFrequencyBigramme(tweet, 2);
		float neg = this.probaTweetByFrequencyBigramme(tweet, 0);
		//System.out.print(pos+" ");
		//System.out.print(neu+" ");
		//System.out.print(neg+" ");
		float res = Math.max(pos, Math.max(neu, neg));
		System.out.println("ok");
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
	
	public float CorrectRateSimpleBayes() throws IOException {
		int nblines = this.getNbLinesFile();
		int deuxTiers = (nblines/3)*2;
		int equal = 0;
		int total = 0;
		int start = 0;
		FileReader f = new FileReader(this.file);
		BufferedReader b = new BufferedReader(f);
		String line = null;
		while ((line = b.readLine())!=null ) {
			if (start>= deuxTiers) {
			String tweet =this.getTweet(line);
			int pol = this.getClass(line);
			int pol2 = (int) this.result(tweet);
			if (pol == pol2) equal++;
			total++;}
			start++;
		}
		b.close();
		return (float) equal/total;
	} 


	public static void main(String args[]) throws IOException {
		Bayes b = new Bayes("requests.csv");
		System.out.println(b.getNbLinesFile());
		FileReader myReader = new FileReader("requests.csv");
		BufferedReader reader = new BufferedReader(myReader);
		String thisLine = null;
		String line = null;
		reader.readLine();
		while((line = reader.readLine()) != null) {
			String tweet = b.getTweet(line);
			//System.out.println(tweet);
		}
		reader.close();
		float res = b.resultByFrequencyAndBigramme("trop mauvais trop nul aime pas");
		
		System.out.println(b.CorrectRateSimpleBayes());
		System.out.print(res);
		
		
	}
}

