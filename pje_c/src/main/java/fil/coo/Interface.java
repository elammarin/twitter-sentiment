package fil.coo;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.io.File;
import java.io.FileWriter;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;




public class Interface extends JFrame implements Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int remaining;
	
    static JTextField t; 
  
    static JFrame f; 
  
    static JButton b; 
  
    static JLabel l;
    
    static JLabel a; 
    
    public static JPanel p;
    public static JPanel p2;
    
    static JTextArea tw;
    
    public static Request r;

    public Interface()
    { 		
    	this.p=new JPanel(); 
    	this.p2=new JPanel(); 
    	 r= new Request();
    	 try {
			remaining = r.getRemainingRequest();
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

        	recherche = t.getText();
        	try {
        		QueryResult tweets;
        		tweets = r.run(recherche);
        		int re = r.getRemainingRequest();
        		a.setText(("Requêtes restantes : "+re));
        		if (!myObj.exists()) {
        			FileWriter myWriter = new FileWriter("requests.csv");
        	        myWriter.write("Id,User,Text,Date,Request \n");
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
        			
	        		myWriter.write("\""+status.getId()+"\","+"\""+status.getUser().getScreenName()+"\",\""+status.getText()+"\",\""+status.getCreatedAt()+"\",\""+recherche+"\"\n");
	        		//tw.setText(tw.getText()+"@" + status.getUser().getScreenName() + ":" + status.getText()+"	"+status.getCreatedAt()+" \n \n" );
	        		//tw.add(positiveButton);
        		}
        		myWriter.close();
        	}
        	catch(Exception i) {
        		i.printStackTrace();
        		
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
        JScrollPane scrollPane = new JScrollPane(p2,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        //p2.add(scrollPane, BorderLayout.CENTER);
        

        // add panel to frame 
       
        f.add(p,BorderLayout.PAGE_START);
        f.add(p2);
        //scrollPane.setPreferredSize(new Dimension(600, 600));
        //f.add(scrollPane);
        
         
  
        // set the size of frame 
        f.setSize(1200, 700); 
  
        f.show(); 
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
