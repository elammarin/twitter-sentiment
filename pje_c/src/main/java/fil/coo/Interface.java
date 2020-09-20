package fil.coo;

import javax.swing.*;

import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;




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
    
    static JTextArea tw;
    
    public static Request r;

    public Interface()
    { 		
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
        
        tw = new JTextArea("Le résultat de votre recherche : \n \n ", 40, 80);
        tw.setEditable(false);
  
        try {
			a = new JLabel("Requêtes restantes : "+r.getRemainingRequest());
        } catch (TwitterException e1) {
			e1.printStackTrace();
		}
        
  
        // addActionListener to button 
        b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	String recherche;
        	recherche = t.getText();
        	try {QueryResult tweets;
        		tw.setText("Le résultat de votre recherche : \n \n "); 
        		tweets = r.run(recherche);
        		int re = r.getRemainingRequest();
        		a.setText(("Requêtes restantes : "+re));
        		for (Status status : tweets.getTweets()) {
        		tw.setText(tw.getText()+"@" + status.getUser().getScreenName() + ":" + status.getText()+" \n \n" );}
        		}
        	catch(Exception i) {
        		i.printStackTrace();
        		
        		}
        	
        	}
        }) ;
        
  
        // create a object of JTextField with 16 columns 
        t = new JTextField(16); 
  
        // create a panel to add buttons and textfield 
        JPanel p = new JPanel(); 
  
        // add buttons and textfield to panel 
        
        p.add(l);
        p.add(t); 
        p.add(b); 
        p.add(a);
        p.add(tw, BorderLayout.WEST);
        JScrollPane scrollPane = new JScrollPane(tw,  JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        p.add(scrollPane, BorderLayout.CENTER);
        

        // add panel to frame 
        f.add(p); 
         
  
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
