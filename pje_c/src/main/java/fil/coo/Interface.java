package fil.coo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class Interface extends JFrame implements Action {
    static JTextField t; 
  
    static JFrame f; 
  
    static JButton b; 
  
    static JLabel l; 
    
    public static Request r;

    public Interface()
    { 		
    	 r= new Request();
    } 
  
    @SuppressWarnings("deprecation")
	public static void main(String[] args) 
    { 
    	Interface in = new Interface();
    	
        // create a new frame to store text field and button 
        f = new JFrame("textfield"); 

        // create a label to display text 
        l = new JLabel("Recherche Twitter"); 
  
        // create a new button 
        b = new JButton("Rechercher"); 
  

  
        // addActionListener to button 
        b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	String recherche;
        	recherche = t.getText();
        	try {
        	r.run(recherche);
        	}
        	catch(Exception i) {
        		i.printStackTrace();
        		
        	}
        	
        }}) ; 
    
  
        // create a object of JTextField with 16 columns 
        t = new JTextField(16); 
  
        // create a panel to add buttons and textfield 
        JPanel p = new JPanel(); 
  
        // add buttons and textfield to panel 
        p.add(l);
        p.add(t); 
        p.add(b); 
        String recherche;
        

		


        // add panel to frame 
        f.add(p); 
  
        // set the size of frame 
        f.setSize(500, 500); 
  
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
