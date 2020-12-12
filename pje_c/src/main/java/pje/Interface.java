package pje;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.Dimension;
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



public class Interface extends JFrame implements Action {
	/**
	 * 
	 */
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
  
    static JLabel l;
    
    static JLabel a; 
    
    public static JPanel p;
    public static JPanel p2;
    public static JPanel p3;
    
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
    	
    	Interface in = new Interface();
    	
    	in.enable();
   
    	
    	
    	
        // create a new frame to store text field and button 
        f = new JFrame("SearchTweetsApp"); 

        // create a label to display text 
        l = new JLabel("Recherche Twitter"); 
  
        // create a new button 
        b = new JButton("Rechercher"); 
        
        b3 = new JButton("KnnEvaluation");
        
        b4 = new JButton("BayesEvaluation");
        
        b5 = new JButton("BayesFrequencyEvaluation");
        
        b6 = new JButton("BayesFrequenceyAndBigrammeEvaluation");
        
        b8 = new JButton("ClassificationParMotsClés");
        
  
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
        		//QueryResult tweets;
        		
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
        			//tw = new JTextArea("", 40, 80);
        	        //tw.setEditable(false);
        			Line l = new Line(status, p2);
        			l.create();
        			//tw.setText(tw.getText()+"@" + status.getUser().getScreenName() + ":" + status.getText()+"	"+status.getCreatedAt()+" \n \n" );
	        		//tw.add(positiveButton);
        		}
        		myWriter.close();
        	}
        	catch(Exception i) {
        		i.printStackTrace();
        		
        		}
            p2.add(b2);
            p2.add(b3);
            p2.add(b4);
            p2.add(b5);
            p2.add(b6);
            p2.add(b8);
            p2.add(b7);
        	}
        }) ;
        
        b2 = new JButton("Valider"); 
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
      
        b3 = new JButton("knnEvaluation");
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
        			Classification c = new Classification();
        			int polarite = c.knn(tweetToEvaluate, "requests_2014.csv", 10);
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
        
        b4 = new JButton("BayesEvaluation");
        b4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		in.negative =0;
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
        
        b5 = new JButton("BayesEvaluationParFrequence");
        b5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Bayes b = new Bayes("requests.csv");
        		in.negative =0;
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
        
        b6 = new JButton("BayesEvaluationParFrequenceAvecBigramme");
        b6.addActionListener(new ActionListener() {
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
        			float polarite = b.resultByFrequencyAndBigramme(tweetToEvaluate);
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
        
        b8 = new JButton("EvaluationParMotClés");
        b8.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Classification c = new Classification();
        		in.negative =0;
        		in.positive = 0;
        		in.neutral = 0;
        		File myObj = new File("ClassifiactionkWords.csv");
        		try{if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("ClassifiactionkWords.csv");
        	        myWriter.write("Id,User,Text,Date,Request,Polarity \n");
        	        myWriter.close();
        	    } 
        		FileWriter myWriter = new FileWriter("ClassifiactionkWords.csv", true);
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
        
        b7 = new JButton("Graphique");
        b7.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		PieChart pie = new PieChart("Comparaison", "Quel est le pourcentage de tweet de chaque polarité?", in.negative*5, in.neutral*5, in.positive*5);
    	        pie.pack();
    	        pie.setVisible(true);
        	}
        });
  
        // create a object of JTextField with 16 columns 
        t = new JTextField(16); 
  
        // create a panel to add buttons and textfield 
        
  
        // add buttons and textfield to panel 
        
        p.add(l);
        p.add(t); 
        p.add(b); 
        p.add(a);
        //p.add(tw, BorderLayout.WEST);
        
        //JScrollBar s=new JScrollBar();  
        //s.setBounds(100,100, 50,100);  

        // add panel to frame 
        //p3.setLayout(new GridLayout(-1, 1));
        //p3.setBackground(Color.LIGHT_GRAY);
        //p3.setPreferredSize(new Dimension(1200, 700));
        p3.add(p);
        p3.add(p2);
        //p3.add(scrollPane);
        JScrollPane scrollPane = new JScrollPane(p3,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setPreferredSize(new Dimension(950, 800));
        contentPane.add(scrollPane);
        
        //contentPane.add(p2);
        
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