package ATM;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class ATM_Server_GUI{
	JLabel label;
	public  void init(){
		JFrame window = new JFrame("Server");
        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE) ;
        window.setSize(300,100);
    //Declaring a main container panel to contain text fields and buttons
	Container panel = window.getContentPane();
	panel.setLayout(new FlowLayout());

	//Creating Text fields and buttons
		label = new JLabel(" Server is running");
        JButton cancel = new JButton("Disconnect");
       
   //Adding Text fields and buttons to the container
        panel.add(label);

        panel.add(cancel);
        cancel.addActionListener(new CloseListener());
	

       	window.setVisible(true);
   //Setting maximum and minimum sizes to maintain the current layout of the GUI
       	window.setMinimumSize(new Dimension(300,100));
       	window.setMaximumSize(new Dimension(300,100));

	}
	
	public void setLabel(String text){
		label.setText(text);
	}
}
//Defined listener for closing window
class CloseListener implements ActionListener{

	public void actionPerformed(ActionEvent e) {
		System.exit(0);
		
	}
	
}
