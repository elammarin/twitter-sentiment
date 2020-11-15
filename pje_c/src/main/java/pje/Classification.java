package fil.coo;

import java.io.*;
import java.util.*;


public class Classification {
	
	public float distance(String t1,String t2) {
		float[] param = mots(t1,t2);
		return ((( param[2]+param[1])-  param[0])/(param[2]+param[1]));
	}
	
	public float[] mots(String t1, String t2) {
		String[] mots1=t1.split(" ");
		String[] mots2=t2.split(" ");
		List<String> list = Arrays.asList(mots1);
		System.out.println(mots1[1]);
		System.out.println(mots2[1]);
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
		FileReader myReader = null;
		myReader = new FileReader(base);
		BufferedReader reader = new BufferedReader(myReader);
		List<String> voisins = new ArrayList<String>(); 
		List<String> proches_voisins = new ArrayList<String>(); 
		String thisLine = null;
		String line = null;
		int cpt = -1;
		while((line = reader.readLine()) != null) {
			voisins.add(thisLine);
			cpt++;
		}
		reader.readLine();
		List<Float> distances = new ArrayList<Float>(); 
		for(int i=0;i<nb_voisins;i++) {
			if((thisLine = reader.readLine()) != null) {
				proches_voisins.add(i,thisLine);
				distances.add(distance(tweet,proches_voisins.get(i).split(",")[2]));
			}
		}
		
		for (int i=nb_voisins+1; i<cpt;i++) {
			float d = distance(tweet,voisins.get(i).split(",")[2]);
			//boolean condition = distances.stream().anyMatch( num ->  d < num);
			for(String tw : proches_voisins) {
				if (d<distance(tweet,tw.split(",")[2])) {
					proches_voisins.add(voisins.get(i));
					proches_voisins.remove(tw);
					voisins.remove(voisins.get(i));
					voisins.add(tw);
				}
			}
		}
		return vote(proches_voisins);
	}

	private int vote(List<String> proches_voisins) {
		Map<String,Integer> monDico = new HashMap<String,Integer>();
		for(String element : proches_voisins) {
			String polarite = element.split(",")[5];
			if (!monDico.containsKey(polarite)) {
				monDico.put(polarite, 1);
			}
			else {
				monDico.put(polarite, (monDico.get(polarite)+1));
			}
		}
		
		String max = monDico.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
		
		return Integer.parseInt(max);
		
	}
	
	public static void main(String args[]) {
		Classification c = new Classification();
		System.out.println(c.distance("hello la famille", "bonjour la famille"));
	}

}
