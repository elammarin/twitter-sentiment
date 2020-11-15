package pje;

import java.awt.BorderLayout;

import javax.swing.*;

import twitter4j.Status;

public class Line {
	private Status status;
	JTextArea jt;
	ButtonGroup bg;
	private JPanel p;
	public Line(Status s, JPanel p) {
		this.status=s;
		this.jt=new JTextArea("", 5, 15);
		this.bg=new ButtonGroup();
		this.p = p;
	}
	
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
        p.add(jt, BorderLayout.WEST);
        p.add(noneButton, BorderLayout.EAST);
        p.add(negativeButton, BorderLayout.EAST);
        p.add(neutralButton, BorderLayout.EAST);
        p.add(positiveButton, BorderLayout.EAST);
	}

}
