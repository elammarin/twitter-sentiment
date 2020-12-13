package pje;

import java.io.*;
import java.util.*;


public class Classification {

        private String file;

        public Classification(String base) {
        	this.file=base;
		}

		public float distance(String t1,String t2) {
                float[] param = mots(t1,t2);
                return ((( param[2]+param[1])-  param[0])/(param[2]+param[1]));
        }

        public float[] mots(String t1, String t2) {
                String[] mots1=t1.split(" ");
                String[] mots2=t2.split(" ");
                List<String> list = Arrays.asList(mots1);
                //System.out.println(mots1[1]);
                //System.out.println(mots2[1]);
                int commun=0;
                for (int i=0;i<mots2.length;i++) {
                                if (list.contains(mots2[i])) {
                                        commun++;
                                }
                }
                float[] result= {0,0,0};
                result[0] = commun;
                result[1] = mots1.length;
                result[2] = mots2.length;
                return result;
        }

        
        public int knn(String tweet, String base ,int nb_voisins) throws IOException {
    		//File tweets = new File("requests.csv");
    		FileReader myReader = new FileReader(base);
    		BufferedReader reader = new BufferedReader(myReader);
    		List<CoupleTweetDistance> proches_voisins = new ArrayList<CoupleTweetDistance>(); 
    		String thisLine = null;
    		String line = null;
    		reader.readLine(); 
    		for(int i=0;i<nb_voisins;i++) {
    			if((thisLine = reader.readLine()) != null) {
    				proches_voisins.add(new CoupleTweetDistance(thisLine, distance(tweet, thisLine.split(",")[2])));
    			}
    		}
    		while((line = reader.readLine()) != null) {
    			float distance = distance(tweet, line.split(",")[2]);
    			boolean t = inferieurAuneDistance(distance, proches_voisins);
    			if (t) {
    				//proches_voisins.remove(t);
    				//System.out.println("je remplace");
    				proches_voisins.add(new CoupleTweetDistance(line, distance));
    			}
    			else {    				
    				//System.out.println("je ne remplace pas");
    				}
    			}
    		
    		reader.close();
    		
    		return vote(proches_voisins);
    	}


	private boolean inferieurAuneDistance(float distance, List<CoupleTweetDistance> proches_voisins) {	
		int cpt = 0;
		int minindex = -1;
		float min = proches_voisins.get(0).getD();
		//CoupleTweetDistance ctdmin = proches_voisins.get(0);
		for (CoupleTweetDistance ctd : proches_voisins) {
				float distance_ctd = ctd.getD();
				if (distance_ctd>distance) {
					if (min<distance_ctd) {
						min = distance_ctd;
						minindex = cpt;
					}
				}
				cpt++;
			}
		if (minindex == -1) {return false;}
		else {
			proches_voisins.remove(minindex);
			return true;
			}
		}

	private int vote(List<CoupleTweetDistance> proches_voisins) {
		//System.out.println("size"+proches_voisins.size());
		Map<String,Integer> monDico = new HashMap<String,Integer>();
		monDico.put("0", 0);
		monDico.put("2", 0);
		monDico.put("4", 0);
		monDico.put("-1",0);
		for(CoupleTweetDistance element : proches_voisins) {
			String s = element.getT();
			String polarite = s.split(",")[5];
			monDico.put(polarite.trim(), (monDico.get(polarite.trim())+1));
		}
		//System.out.println(monDico.get("0"));
		int max = 0;
		
		if (monDico.get("2")>monDico.get("0") && monDico.get("2")>monDico.get("4")) {
			max = 2;
		}
		else if (monDico.get("4")>monDico.get("0") && monDico.get("4")>monDico.get("2")) {
			max = 4;
		}
		return max;
		
	}
	
	public static void main(String args[]) throws IOException {
		Classification c = new Classification("requests.csv");
		System.out.println(c.kWords("peur sympa cool horrible irritation","negative.txt","positive.txt"));
		System.out.println(c.CorrectRateKeywords());
		System.out.println(c.CorrectRateKnn());
	}
	
	private float CorrectRateKeywords() throws IOException {
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
			int pol2 = (int) this.kWords(tweet, "negative.txt", "positive.txt");
			if (pol == pol2) equal++;
			total++;}
			start++;
		}
		b.close();
		return (float) equal/total;
	}

	private float CorrectRateKnn() throws IOException {
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
				int pol2 = (int) this.knn(tweet, this.file, 27);
				if (pol == pol2) equal++;
				total++;
			}
			start++;
		}
		b.close();
		return (float) equal/total;
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

	/*
	// Class representing a couple : ( Distance, Tweet )
		private class DTCouple {

			private float distance;

			private Tweet tweet;

			public DTCouple ( float distance, Tweet tweet ) {
				this.distance = distance;
				this.tweet = tweet;
			}

			public float getDistance () {
				return this.distance;
			}

			public Tweet getTweet () {
				return this.tweet;
			}

		}

*/
	 public int kWords(String t, String filePathNeg, String filePathPos) throws IOException {
	        String[] array = t.split(" ");
	        //System.out.println(Arrays.toString(array));
	        int neg = 0;
	        int pos = 0;
	        for (String s : array) {
	            if (compareFile(filePathNeg, s.trim())) {
	                neg++;
	            } else if (compareFile(filePathPos, s.trim())) {
	                pos++;
	            } 
	        }
	        //System.out.println("Avant changement :" +t.getPolarity()); // verif
	        if (neg==pos) return 2;
	        else if (neg> pos) return 0;
	        else return 4;
	        //System.out.println("Apres changement :" + t.getPolarity()); // verif
	        //return -1;
	    }
	
	private static boolean compareFile(String filePath, String search) throws IOException {
		File file = new File(filePath);
        String[] words;
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String str;
        while((str = br.readLine()) != null)
        {
            words = str.split(", ");  //les mots sont séparés par des virgules
            for (String word : words)
            {
                //Cherche le mot
                if (word.equals(search))
                {
                    // On a trouvé le mot dans le fichier
                    fr.close();
                    br.close();
                    return true;
                }
            }
        }
        fr.close();
        return false;
	}


	private class CoupleTweetDistance{
		private String t;
		private float d;
		
		public CoupleTweetDistance(String t, float d) {
			this.t = t;
			this.d = d;
		}

		public String getT() {
			return t;
		}

		@SuppressWarnings("unused")
		public void setT(String t) {
			this.t = t;
		}

		public float getD() {
			return d;
		}

		@SuppressWarnings("unused")
		public void setD(int d) {
			this.d = d;
		}
		
	}
	
}
