package pje;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Arthur Assim & Nordine El Ammari
 * Bayes Classifier
 */

public class Bayes {
	private String file;
	public Bayes(String base) {
		this.file=base;
	}
	
	/**
	 * @return the number of lines in the source file
	 * @throws IOException
	 */
	public int getNbLinesFile() throws IOException {
		int res = 0;
		FileReader f = new FileReader(this.file);
		BufferedReader b = new BufferedReader(f);
		while(b.readLine()!= null) res+=1;
		return res-1;
	}
	
	/**
	 * @param s the tweet's content
	 * @return the tweet's content
	 */
	public String getTweet(String s) {
		String res= s.split(",")[2];
		return (String) res.substring(2, res.length()-2).trim();
	}
	
	/**
	 * @param s the tweet's class
	 * @return the tweet's class
	 */
	public int getClass(String s) {
		//System.out.println(s.split(",")[5]);
		return Integer.parseInt(s.split(",")[5].substring(0, 1)); 
	}
	
	
	
	/**
	 * @param mot the word whose we want to know the number of occurences in the class 
	 * @param classe the class in which we want to know the numbers of words
	 * @return the numbers of words in the class
	 * @throws IOException
	 */
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
	
	/**
	 * @param bgrm the bi-gram whose we want to know the number of occurences in the class 
	 * @param classe the class in which we want to know the numbers of bi-grams
	 * @return the numbers of bi-grams in the class
	 * @throws IOException
	 */
	public int nbOfOccurencesBigramme(String bgrm, int classe) throws IOException {
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
					if (m.compareToIgnoreCase(bgrm)==0) {
						cpt++;
					}
				}
			}
		}
		reader.close();
		//System.out.print(cpt);
		return cpt;
	}

	/**
	 * @param classe the class in which we want to know the numbers of words
	 * @return the number of words in the whole class
	 * @throws IOException
	 */
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
	
	/**
	 * @return the number of words in the whole learning base
	 * @throws IOException
	 */
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
	
	/**
	 * @param mot the bi-gram for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the word
	 * @return the probability of the word belonging to the class
	 * @throws IOException
	 */
	public float probaMot(String mot, int classe) throws IOException {
		int nc = this.nbOfWords(classe); 
		int nmc = this.nbOfOccurences(mot, classe);
		int all = this.allWords();
		//System.out.print(nmc+" "+nc+" "+all+ " ");
		return (float) (nmc+1)/(nc+all);
	}
	
	/**
	 * @param bgrm the bi-gram for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the bi-gram
	 * @return the probability of the bi-gram belonging to the class
	 * @throws IOException
	 */
	public float probaBigramme(String bgrm, int classe) throws IOException {
		int nc = this.nbOfWords(classe); 
		int nmc = this.nbOfOccurencesBigramme(bgrm, classe);
		int all = this.allWords();
		//System.out.print(nmc+" "+nc+" "+all+ " ");
		return (float) (nmc+1)/(nc+all);
	}
	
	/**
	 * @param tweet the tweet for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the tweet
	 * @return the probability of the tweet belonging to the class calculated by the uni-gram presence bayes classification
	 * @throws IOException
	 */
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
	
	/**
	 * @param tweet the tweet for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the tweet
	 * @return the probability of the tweet belonging to the class calculated by the bi-gram presence bayes classification
	 * @throws IOException
	 */
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
	
	/**
	 * @param tweet the tweet for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the tweet
	 * @return the probability of the tweet belonging to the class calculated by the uni and bi-gram presence bayes classification
	 * @throws IOException
	 */
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
	
	/**
	 * @param tweet the tweet for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the tweet
	 * @return the probability of the tweet belonging to the class calculated by the uni-gram frequency bayes classification
	 * @throws IOException
	 */
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
	
	/**
	 * @param tweet the tweet for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the tweet
	 * @return the probability of the tweet belonging to the class calculated by the bi-gram frequency bayes classification
	 * @throws IOException
	 */
	public float probaTweetByFrequencyBigramme(String tweet, int classe) throws IOException {
		float cpt=1;
		List<String> dejaUtilise = new ArrayList<String>(); 
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
	
	/**
	 * @param tweet the tweet for which we want to know the probability of belonging to the class
	 * @param classe the class of which we want to know the probability of containing the tweet
	 * @return the probability of the tweet belonging to the class calculated by the uni and bi-gram frequency bayes classification
	 * @throws IOException
	 */
	public float probaTweetByFrequencyMotAndBigramme(String tweet, int classe) throws IOException {
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
	
	/**
	 * @param t the tweet whose we want to know the occurences of bi-grams
	 * @return every bi-gram in the tweet and his number of occurences
	 */
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

	/**
	 * @param mots the words to transform in bi-gram
	 * @return the bi-gram from the words
	 */
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
	
	/**
	 * @param t the tweet whose we want to know the occurences of words
	 * @return every word in the tweet and his number of occurences
	 */
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
	
	/**
	 * @param classe the class whose rate we want to know 
	 * @return the class' rate
	 * @throws IOException
	 */
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
	
	/**
	 * @param tweet the tweet we want to classify
	 * @return the tweet's class calculated by the presence uni-gram bayes classification
	 * @throws IOException
	 */
	public float result (String tweet) throws IOException {
		float pos = this.probaTweet(tweet, 4);
		float neu = this.probaTweet(tweet, 2);
		float neg = this.probaTweet(tweet, 0);
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
	
	/**
	 * @param tweet the tweet we want to classify
	 * @return the tweet's class calculated by the presence bi-gram bayes classification
	 * @throws IOException
	 */
	public float resultBigramme (String tweet) throws IOException {
		float pos = this.probaBigramme(tweet, 4);
		float neu = this.probaBigramme(tweet, 2);
		float neg = this.probaBigramme(tweet, 0);
		//System.out.print(pos+" ");
		//System.out.print(neu+" ");
		//System.out.print(neg+" ");
		float res = Math.max(pos, Math.max(neu, neg));
		//System.out.println("ok");
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
	
	/**
	 * @param tweet the tweet we want to classify
	 * @return the tweet's class calculated by the presence uni and bi-gram bayes classification
	 * @throws IOException
	 */
	public float resultMotAndBigramme (String tweet) throws IOException {
		float pos = this.probaTweetMotAndBigramme(tweet, 4);
		float neu = this.probaTweetMotAndBigramme(tweet, 2);
		float neg = this.probaTweetMotAndBigramme(tweet, 0);
		//System.out.print(pos+" ");
		//System.out.print(neu+" ");
		//System.out.print(neg+" ");
		float res = Math.max(pos, Math.max(neu, neg));
		//System.out.println("ok");
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
	
	/**
	 * @param tweet the tweet we want to classify
	 * @return the tweet's class calculated by the frequency uni-gram bayes classification
	 * @throws IOException
	 */
	public float resultByFrequency (String tweet) throws IOException {
		float pos = this.probaTweetByFrequency(tweet, 4);
		float neu = this.probaTweetByFrequency(tweet, 2);
		float neg = this.probaTweetByFrequency(tweet, 0);
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
	
	/**
	 * @param tweet the tweet we want to classify
	 * @return the tweet's class calculated by the frequency bi-gram bayes classification
	 * @throws IOException
	 */
	public float resultByFrequencyBigramme (String tweet) throws IOException {
		float pos = this.probaTweetByFrequencyBigramme(tweet, 4);
		float neu = this.probaTweetByFrequencyBigramme(tweet, 2);
		float neg = this.probaTweetByFrequencyBigramme(tweet, 0);
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
	

	/**
	 * @param tweet the tweet we want to classify
	 * @return the tweet's class calculated by the frequency uni and bi-gram bayes classification
	 * @throws IOException
	 */
	public float resultByFrequencyMotAndBigramme (String tweet) throws IOException {
		float pos = this.probaTweetMotAndBigramme(tweet, 4);
		float neu = this.probaTweetMotAndBigramme(tweet, 2);
		float neg = this.probaTweetMotAndBigramme(tweet, 0);
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
	
	/**
	 * @return the rate of similarity between the learning base and the presence uni-gram bayes classification
	 * @throws IOException
	 */
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
	
	/**
	 * @return the rate of similarity between the learning base and the presence bi-gram bayes classification
	 * @throws IOException
	 */
	public float CorrectRateBayesBigramme() throws IOException {
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
			int pol2 = (int) this.resultBigramme(tweet);
			if (pol == pol2) equal++;
			total++;}
			start++;
		}
		b.close();
		return (float) equal/total;
	} 
	
	/**
	 * @return the rate of similarity between the learning base and the presence uni and bi-gram bayes classification
	 * @throws IOException
	 */
	public float CorrectRateBayesMotAndBigramme() throws IOException {
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
			int pol2 = (int) this.resultMotAndBigramme(tweet);
			if (pol == pol2) equal++;
			total++;}
			start++;
		}
		b.close();
		return (float) equal/total;
	} 

	/**
	 * @return the rate of similarity between the learning base and the frequency uni-gram bayes classification
	 * @throws IOException
	 */
	public float CorrectRateBayesByFrequency() throws IOException {
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
			int pol2 = (int) this.resultByFrequency(tweet);
			if (pol == pol2) equal++;
			total++;}
			start++;
		}
		b.close();
		return (float) equal/total;
	} 

	/**
	 * @return the rate of similarity between the learning base and the bayes by frequency and bi-gram classification
	 * @throws IOException
	 */
	public float CorrectRateBayesByFrequencyBigramme() throws IOException {
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
			int pol2 = (int) this.resultByFrequencyBigramme(tweet);
			if (pol == pol2) equal++;
			total++;}
			start++;
		}
		b.close();
		return (float) equal/total;
	} 
	
	/**
	 * @return the rate of similarity between the learning base and the bayes by frequency uni and bi-gram classification
	 * @throws IOException
	 */
	public float CorrectRateBayesByFrequencyMotAndBigramme() throws IOException {
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
			int pol2 = (int) this.resultByFrequencyMotAndBigramme(tweet);
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
		float res = b.resultByFrequencyBigramme("mauvais nul pas");
		
		System.out.println(b.CorrectRateSimpleBayes());
		System.out.println(b.CorrectRateBayesBigramme());
		System.out.println(b.CorrectRateBayesMotAndBigramme());
		System.out.println(b.CorrectRateBayesByFrequency());
		System.out.println(b.CorrectRateBayesByFrequencyBigramme());
		System.out.println(b.CorrectRateBayesByFrequencyMotAndBigramme());
		System.out.print(res);
		
		
	}
}

