package pje;

import java.io.*;
import java.util.*;

import pje.utils.*;

public class Classification {

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
    				System.out.println("je remplace");
    				proches_voisins.add(new CoupleTweetDistance(line, distance));
    			}
    			else {    				
    				System.out.println("je ne remplace pas");}
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
		System.out.println("size"+proches_voisins.size());
		Map<String,Integer> monDico = new HashMap<String,Integer>();
		monDico.put("0", 0);
		monDico.put("2", 0);
		monDico.put("4", 0);
		for(CoupleTweetDistance element : proches_voisins) {
			String s = element.getT();
			String polarite = s.split(",")[5];
			monDico.put(polarite.trim(), (monDico.get(polarite)+1));
		}
		System.out.println(monDico.get("0"));
		int max = 0;
		
		if (monDico.get("2")>monDico.get("0") && monDico.get("2")>monDico.get("4")) {
			max = 2;
		}
		else if (monDico.get("4")>monDico.get("0") && monDico.get("4")>monDico.get("2")) {
			max = 4;
		}
		return max;
		
	}
	
	public static void main(String args[]) {
		Classification c = new Classification();
		//System.out.println(c.distance("bonjour", "bonjour la famille"));
		try {
			System.out.println(c.knn("François LOOS ancien Ministre est élu à l’unanimité Président de Brasseurs de France","requests.csv", 50));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

		public void setT(String t) {
			this.t = t;
		}

		public float getD() {
			return d;
		}

		public void setD(int d) {
			this.d = d;
		}
		
		
		
	}
	
}
