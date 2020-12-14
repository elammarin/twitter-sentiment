package pje;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

import pje.Classification;

import pje.Bayes;



/**
 * @author Arthur Assima & Nordine El Ammari
 * Graphical Interface
 */
public class Interface extends JFrame implements Action {
	
	
	private static final long serialVersionUID = 1L;
	
	private static QueryResult tweets;
	//private static String recherche;

	public int remaining;
	
	int positive;
	int neutral;
	int negative;
	
    static JTextField t; 
  
    static JFrame f; 
  
    static JButton b; 
    static JButton b2;
    static JButton b3;
    static JButton b4;
    static JButton b5;
    static JButton b6;
    static JButton b7;
    static JButton b8;
    static JButton b9;
	static JButton b10;
	static JButton b11;
  
    static JLabel l;
    
    static JLabel a; 
    
    public static JPanel p;
    public static JPanel p2;
    public static JPanel p3;
    public static JPanel pb;
    
    static JTextArea tw;
    
    public static Request r;

	




    public Interface()
    { 		
    	this.positive = 0;
    	this.neutral=0;
    	this.negative = 0;
    	this.p=new JPanel(); 
    	this.p2=new JPanel(); 
    	p2.setLayout(new BoxLayout(p2, BoxLayout.PAGE_AXIS));
    	this.p3=new JPanel();
    	p3.setLayout(new BoxLayout(p3, BoxLayout.PAGE_AXIS));
    	this.pb=new JPanel();
    	pb.setLayout(new GridLayout(5, 2));
    	r= new Request();
    	 try {
			remaining = r.getRemainingRequest();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static String nettoyage(String s) {
    s = s.replaceAll("([\\n\\r]+)", " ");
    return s.replaceAll("(@[^\\s]+)|(#([^\\s]+))|((www\\.[^\\s]+)|(https?://[^\\s]+))|(\")|(\\p{Punct})", "");
    	
    }
  
    @SuppressWarnings("deprecation")
	public static void main(String[] args) { 
    	
    	final Interface in = new Interface();
    	
    	in.enable();
   
    	
    	
    	
        // create a new frame to store text field and button 
        f = new JFrame("TweetsAnalysisApp"); 

        // create a label to display text 
        l = new JLabel("Recherche Twitter"); 
  
        // create a new button 
        b = new JButton("Rechercher"); 
        
        b3 = new JButton("KnnEvaluation");
        
        b4 = new JButton("KeywordsEvaluation");
        
        b5 = new JButton("BayesUniEvaluation");
        
        b6 = new JButton("BayesBiEvaluation");
        
        b7 = new JButton("BayesUniAndBiEvaluation");
        
        b8 = new JButton("BayesUniFrequencyEvaluation");
        
        b9 = new JButton("BayesBiFrequencyEvaluation");
        
        b10 = new JButton("BayesUniAndBiFrequencyEvaluation");
        
  
        try {
			a = new JLabel("Requêtes restantes : "+r.getRemainingRequest());
        } catch (TwitterException e1) {
			e1.printStackTrace();
		}
        
  
        // addActionListener to button 
        b.addActionListener(new ActionListener() {
        

		public void actionPerformed(ActionEvent e) {
        	File myObj = new File("requests.csv");
        	String recherche;

        	recherche = t.getText()+" lang:fr"+" -filter:retweets";
        	try {
        		p2.removeAll();
        		
        		tweets = r.run(recherche);
        		int re = r.getRemainingRequest();
        		a.setText(("Requêtes restantes : "+re));
        		if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("requests.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		else {
        	        System.out.println("File already exists.");
        	    }
        		FileWriter myWriter = new FileWriter("requests.csv", true);
        		for (Status status : tweets.getTweets()) {
        			Line l = new Line(status, p2);
        			l.create();
        		}
        		myWriter.close();
        	}
        	catch(Exception i) {
        		i.printStackTrace();
        		
        		}
            pb.add(b2);
            pb.add(b3);
            pb.add(b4);
            pb.add(b5);
            pb.add(b6);
            pb.add(b8);
            pb.add(b7);
            pb.add(b8);
            pb.add(b9);
            pb.add(b10);
            pb.add(b11);
        	}
        }) ;
        
        b2 = new JButton("Ajout à la base"); 
        b2.addActionListener(new ActionListener() {

    		public void actionPerformed(ActionEvent e) {
    			Component[] list = p2.getComponents();
    			LinkedList<Integer> val = new LinkedList<Integer>();
    			String research;

            	research = t.getText();
    			FileWriter myWriter = null;
				try {
					myWriter = new FileWriter("requests.csv", true);
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				
    			for (Component button : list) {
    				if (button instanceof JRadioButton && ((JRadioButton) button).isSelected()) {
    					AbstractButton b = (AbstractButton) button;
    						try {
								switch (b.getText()) {
								case "Neutre" : val.add(2);
									break;
								case "Négatif" : val.add(0);
									break;
								case "Positif" : val.add(4);
									break;
								default : val.add(-1);
								 	break;
    							}
							} catch (Exception e1) {
								e1.printStackTrace();
							}	
    						
    				}
    			}
    			System.out.println(Arrays.toString(val.toArray()));

    			int index = 0;
    			for (Status status : tweets.getTweets()) {
    				boolean dupli = true;
					try {
						dupli = dupli( status.getId() );
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
        			if (!dupli) {
						try {
							myWriter.write("\""+Long.toString(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+nettoyage(status.getText())+"\",\""+status.getCreatedAt()+"\",\""+research+"\","+val.get(index)+" \n");
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}}
        			index++;
    				}
    			try {
					myWriter.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	
            	}
            }) ;
      
        b3 = new JButton("Knn Evaluation");
        b3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		in.negative =0;
        		in.positive = 0;
        		in.neutral = 0;
        		File myObj = new File("knnClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("knnClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("knnClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			Classification c = new Classification("requests_csv");
        			int polarite = c.knn(tweetToEvaluate, "requests.csv", 10);
					myWriter.write("\""+Long.toString(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+polarite+" \n");
					if (polarite == 0) {
						in.negative+=1;
					}
					if (polarite == 2) {
						in.neutral+=1;
					}
					if (polarite == 4) {
						in.positive +=1;
					}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b4 = new JButton("Evaluation par mot-clés");
        b4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Classification c = new Classification("requests_csv");
        		in.negative =0;
        		in.positive = 0;
        		in.neutral = 0;
        		File myObj = new File("ClassificationkWords.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("ClassificationkWords.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("ClassificationkWords.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = c.kWords(tweetToEvaluate, "negative.txt", "positive.txt");
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b5 = new JButton("Evaluation Bayes uni-gramme");
        b5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		in.negative = 0;
        		in.positive = 0;
        		in.neutral = 0;
        		Bayes b = new Bayes("requests.csv");
    			
        		File myObj = new File("BayesClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("BayesClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("BayesClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = b.result(tweetToEvaluate);
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;
					}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b6 = new JButton("Evaluation Bayes bi-gramme");
        b6.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		in.negative = 0;
        		in.positive = 0;
        		in.neutral = 0;
        		Bayes b = new Bayes("requests.csv");
    			
        		File myObj = new File("BayesBiClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("BayesBiClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("BayesBiClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = b.resultBigramme(tweetToEvaluate);
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;
					}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b7 = new JButton("Evaluation Bayes uni-gramme et bi-gramme");
        b7.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		in.negative = 0;
        		in.positive = 0;
        		in.neutral = 0;
        		Bayes b = new Bayes("requests.csv");
    			
        		File myObj = new File("BayesUniBiClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("BayesUniBiClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("BayesUniBiClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = b.resultMotAndBigramme(tweetToEvaluate);
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;
					}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b8 = new JButton("Evaluation Bayes par fréquence uni-gramme");
        b8.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Bayes b = new Bayes("requests.csv");
        		in.negative = 0;
        		in.positive = 0;
        		in.neutral = 0;
        		File myObj = new File("BayesFrequenceClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("BayesFrequenceClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("BayesFrequenceClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = b.resultByFrequency(tweetToEvaluate);
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;}
					
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b9 = new JButton("Evaluation Bayes par fréquence bi-gramme");
        b9.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Bayes b = new Bayes("requests.csv");
        		in.negative =0;
        		in.positive = 0;
        		in.neutral = 0;
        		File myObj = new File("BayesFrequenceBigrammeClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("BayesFrequenceBigrammeClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("BayesFrequenceBigrammeClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = b.resultByFrequencyBigramme(tweetToEvaluate);
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b10 = new JButton("Evaluation Bayes par fréquence uni-gramme et bi-gramme");
        b10.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Bayes b = new Bayes("requests.csv");
        		in.negative =0;
        		in.positive = 0;
        		in.neutral = 0;
        		File myObj = new File("BayesFrequenceUniBigrammeClassification.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("BayesFrequenceUniBigrammeClassification.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("BayesFrequenceUniBigrammeClassification.csv", true);
        		for (Status status : tweets.getTweets()) {
        			String tweetToEvaluate = nettoyage(status.getText());
        			float polarite = b.resultByFrequencyMotAndBigramme(tweetToEvaluate);
        			int pol = Math.round(polarite);
					myWriter.write("\""+String.valueOf(status.getId()).replaceAll(",", ".")+"\","+"\""+status.getUser().getScreenName()+"\",\""+tweetToEvaluate+"\",\""+status.getCreatedAt()+"\",\""+t.getText()+"\","+pol+" \n");
					if (pol == 0) {
						in.negative+=1;
					}
					if (pol == 2) {
						in.neutral+=1;
					}
					if (pol == 4) {
						in.positive +=1;}
        	}
        		myWriter.close();}
        		catch(Exception a) {
        		a.printStackTrace();	
        		}
        		}
        });
        
        b11 = new JButton("Graphique");
        b11.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		PieChart pie = new PieChart("Comparaison", "Quel est le pourcentage de tweet de chaque polarité?", in.negative*5, in.neutral*5, in.positive*5);
    	        pie.pack();
    	        pie.setVisible(true);
        	}
        });
  
        // create a object of JTextField with 16 columns 
        t = new JTextField(16); 
  
 
        
        p.add(l);
        p.add(t); 
        p.add(b); 
        p.add(a);
      
        p3.add(p);
        p3.add(p2);
        p3.add(pb);
   
        JScrollPane scrollPane = new JScrollPane(p3,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setPreferredSize(new Dimension(950, 800));
        contentPane.add(scrollPane);
        

        
        f.add(contentPane);
        //f.add(p3);
        //f.add(s);
        
        //scrollPane.setPreferredSize(new Dimension(600, 600));
        //f.add(scrollPane);
        
         
  
        // set the size of frame 
        f.setSize(1200, 700); 
  
        f.show(); 
    }
    

	protected static boolean dupli(long id) throws FileNotFoundException {
		String i = Long.toString(id);
		i.replaceAll(",", ".");
		File myObj = new File("requests.csv");
	    @SuppressWarnings("resource")
		Scanner myReader = new Scanner(myObj);
	    while (myReader.hasNextLine()) {
	    	if (myReader.nextLine().contains(i)) {
	    		return true;
	    	}
	    }
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getValue(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putValue(String key, Object value) {
		// TODO Auto-generated method stub
		
	} 
}