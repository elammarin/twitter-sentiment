package pje;

import java.awt.BorderLayout;

import javax.swing.*;

import twitter4j.Status;

/**
 * @author Arthur Assima & Nordine El Ammari
 * Graphical interface's line
 */
public class Line {
	private Status status;
	JTextArea jt;
	ButtonGroup bg;
	private JPanel p;
	
	/**
	 * @param s the status of the request
	 * @param p the panel where we add the line
	 */
	public Line(Status s, JPanel p) {
		this.status=s;
		this.jt=new JTextArea("", 5,5);
		this.bg=new ButtonGroup();
		this.p = p;
	}
	
	/**
	 * creates a line in the classification window
	 */
	public void create() {
        jt.setEditable(false);
		jt.setText(jt.getText()+"@" + status.getUser().getScreenName() + ":" + status.getText()+"	"+status.getCreatedAt()+" \n \n" );

    	JRadioButton noneButton   = new JRadioButton("Aucun", true);
        JRadioButton negativeButton    = new JRadioButton("Négatif");
        JRadioButton neutralButton    = new JRadioButton("Neutre");
        JRadioButton positiveButton = new JRadioButton("Positif");
        bg.add(noneButton);
        bg.add(negativeButton);
        bg.add(neutralButton);
        bg.add(positiveButton);
        p.add(new JScrollPane(jt), BorderLayout.WEST);
        p.add(noneButton, BorderLayout.WEST);
        p.add(negativeButton, BorderLayout.WEST);
        p.add(neutralButton, BorderLayout.WEST);
        p.add(positiveButton, BorderLayout.WEST);
	}

}
