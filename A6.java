/**
Part 2 : A & B

- Display the timer box and numbers
- Create start and stop buttons to control the timer
*/

// Abstact Window Toolkit: 
import java.awt.*; 
import java.awt.event.*; 

// Swing GUI: 
import javax.swing.Timer;
import javax.swing.*;  

// Time: 
import java.time.Duration;

public class A6 {
  	
	public static void main(String[] args) {
	
		new A6();
    	}

	public A6() {
  
	  	// Execute Runnable on the AWT thread.
	  	EventQueue.invokeLater (new Runnable() {
	    
			// Override run():         
			@Override
			public void run() {
			   
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} 
				
				catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
					ex.printStackTrace();
				}

				// Creating JFrame Object: 
				JFrame frame = new JFrame("Timer");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new Pane());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
		
			} // Close run
				
	    	} );
    
	} // End A6 Class. 
	
	public class Pane extends JPanel {
		
		private JLabel label;
		private long lastTickTime;
		private Timer timer;

		public Pane() {
		
			// Arrange components using GBL: 
			setLayout(new GridBagLayout());
			label = new JLabel(String.format("%04d:%02d:%02d.%03d", 0, 0, 0, 0));

			timer = new Timer(100, new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					
						long runningTime = System.currentTimeMillis() - lastTickTime;
					     	 Duration duration = Duration.ofMillis(runningTime);
					      	long hours = duration.toHours();
					      	duration = duration.minusHours(hours);
					      	long minutes = duration.toMinutes();
					      	duration = duration.minusMinutes(minutes);
					      	long millis = duration.toMillis();
					      	long seconds = millis / 1000;
					      	millis -= (seconds * 1000);
					      	label.setText(String.format("%04d:%02d:%02d.%03d", hours, minutes, seconds, millis));
			
				} // Close actionPerformed 
			
			});
			
			// Specifiy constraints:
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
      			gbc.weightx = 1;
      			gbc.gridwidth = GridBagConstraints.REMAINDER;
      			gbc.insets = new Insets(4, 4, 4, 4);
      			add(label, gbc);

			JButton start = new JButton("Start");
			start.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
		    
		    			if (!timer.isRunning()) {
		      
		      				lastTickTime = System.currentTimeMillis();
		       				timer.start();
		      			}
				}
			});

			JButton stop = new JButton("Stop");
			stop.addActionListener(new ActionListener() {
				
				@Override
        			public void actionPerformed(ActionEvent e) {
        
					timer.stop();
				}
			});

			gbc.gridx = 0;
		      	gbc.gridy++;
		     	 gbc.weightx = 0;
		      	gbc.gridwidth = 1;
		      	add(start, gbc);
		      	gbc.gridx++;
		      	add(stop, gbc);
	
		}

	} // Close Pane

} // Close A6 
