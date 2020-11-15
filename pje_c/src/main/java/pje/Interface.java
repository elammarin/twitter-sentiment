package fil.coo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
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





public class Interface extends JFrame implements Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static QueryResult tweets;
	//private static String recherche;

	public int remaining;
	
    static JTextField t; 
  
    static JFrame f; 
  
    static JButton b; 
    static JButton b2;
  
    static JLabel l;
    
    static JLabel a; 
    
    public static JPanel p;
    public static JPanel p2;
    public static JPanel p3;
    
    static JTextArea tw;
    
    public static Request r;

    public Interface()
    { 		
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
    	
    return s.replaceAll("(@[^\\s]+)|(#([^\\s]+))|((www\\.[^\\s]+)|(https?://[^\\s]+))|(\")|(RT)", "");
    	
    }
  
    @SuppressWarnings("deprecation")
	public static void main(String[] args) 
    { 
    	
    	Interface in = new Interface();
    	
    	in.enable();
   
    	
    	
    	
        // create a new frame to store text field and button 
        f = new JFrame("SearchTweetsApp"); 

        // create a label to display text 
        l = new JLabel("Recherche Twitter"); 
  
        // create a new button 
        b = new JButton("Rechercher"); 
        
        
  
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

        	recherche = t.getText()+" lang:fr";
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
							myWriter.write("\""+status.getId()+"\","+"\""+status.getUser().getScreenName()+"\",\""+nettoyage(status.getText())+"\",\""+status.getCreatedAt()+"\",\""+research+"\","+val.get(index)+" \n");
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