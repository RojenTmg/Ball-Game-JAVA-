import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JSlider;
import javax.swing.JTextField;

public class CBallMaze extends JFrame implements ActionListener,ChangeListener{
	
	private JMenuBar    bar;
	private JMenu 	    menu1,menu2,menu3,menu4;
			JMenuItem   m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12,m13,m14;
	private  JPanel	    panel1,panel2,panel3,panelx,panel2a,panel2b,panel2c,panel2d,panel2e,
					    panel2bi,panel2bii,panel3a,panel3b;
	private JButton     jBAct,jBRun,jBReset,jBUp,jBDown,jBRight,jBLeft,jBEmpty1,jBEmpty2,
	        		    jBEmpty3,jBEmpty4,jBEmpty5,jBOption1,jBOption2,jBOption3,jBExit;
	private JSlider     slider;
	private JLabel      jLOption,jLDirection,jLSquare,jLSpeed,jLabelEmpty1,jLabelEmpty2,
			            jLCompass,ball,sandstone,sand,white;
	private JTextField  jTOption,jTDirection,jTSquare,jTHour,jTMin,jTSec;
	private ImageIcon   iAct,iRun,iReset,iCompass,iBall,iSand,iSandstone,iWhite;
	private Timer       timer;	
	private Clip wav; 
			
	private int ticks = 0;
			
	private int pB =0, qB=15;	
	private int posB=15;	//initial position/indes of the ball
	private JButton jBTile[][] = new JButton[13][16];		//creates grid of 13rows * 17columns 
	private Timer time = new Timer (500, this);		//timer for slider
			
	
	public static void main(String[]args) {

		CBallMaze frame = new CBallMaze();	
		frame.setSize(775,650);	//create window of size 775 width * 650 height
		frame.setTitle("                                                                                    "
				+ "CBallMaze - Ball Maze Application");		//set's title of the maze
		frame.createGUI();		//calling method created for GUI
		frame.setVisible(true);		//makes the frame visible
	}
	
	private void createGUI() {
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);	// cannot minimize or maximize the window
		setLocationRelativeTo(null);	//centers the window 
		
		Container window = getContentPane();	//get the content's of Container window
		window.setLayout(null);		//independently setting the layout of the frame through coordinate's
		window.setBackground(Color.LIGHT_GRAY);
		
		
		
		//creating Menu Bars with Scenario, Edit, Controls, Help
		
		bar = new JMenuBar();
		setJMenuBar(bar);
		
		menu1 = new JMenu("Scenario");
		bar.add(menu1);
		menu2 = new JMenu("Edit");
		bar.add(menu2);		
		menu3 = new JMenu("Controls");
		bar.add(menu3);		
		menu4 = new JMenu("Help");
		bar.add(menu4);
		
		// menu items of 'Scenario'
		
		m1 = new JMenuItem("New");
		m2 = new JMenuItem("Open");
		m3 = new JMenuItem("Save");
		m4 = new JMenuItem("Quit");
		menu1.add(m1);
		menu1.add(m2);
		menu1.add(m3);
		menu1.add(m4);
		
		// menu items of 'Edit'
		
		m5 = new JMenuItem("Copy");
		m6 = new JMenuItem("Paste");
		m7 = new JMenuItem("Delete");
		menu2.add(m5);
		menu2.add(m6);
		menu2.add(m7);
		
		// menu items of 'Controls'
		
		m8 = new JMenuItem("Act");
		m9 = new JMenuItem("Run");
		m10 = new JMenuItem("Pause");
		m11 = new JMenuItem("Reset");
		menu3.add(m8);
		menu3.add(m9);
		menu3.add(m10);
		menu3.add(m11);
		
		// menu items of 'Help'
		
		m12 = new JMenuItem("About");
		m13 = new JMenuItem("Topic");
		m14 = new JMenuItem("Help");
		menu4.add(m12);
		menu4.add(m13);
		menu4.add(m14);
		
		
		// creating three panels for maze, navigations and slider's(act, run and reset included)
		
		GridBagLayout gbl = new GridBagLayout();
		GridLayout gl = new GridLayout(13,16);
		
		panel1 = new JPanel(gl);	//panel for maze
		panel1.setBackground(Color.white);
		panel1.setBounds(1, 3, 592, 545);
		window.add(panel1);
		
		panelx = new JPanel();		//separation panel between 'panel1' and 'panel2'
		panelx.setBounds(595, 3, 15, 545);
		window.add(panelx);
		
		panel2 = new JPanel();		//panel for navigation content's
		panel2.setBounds(611, 3, 155, 545);
	    panel2.setLayout(new GridLayout(5, 1));
		window.add(panel2);
		
		panel3 = new JPanel(null);		//panel for slider(act, run and reset included)
		panel3.setBounds(1, 550, 765, 49);
		window.add(panel3);
		
		

		// building maze (panel1)
		
		buildMaze();
		
		
		
		// Act,Run and Reset Buttons (panel3)
		
	    jBAct = new JButton("Act");
		jBAct.setBackground(Color.white);		
		iAct = new ImageIcon(getClass().getResource("step.png"));	
		// YouTube. (2018). Java Eclipse GUI Tutorial 7 # Add image, pictures and icons in JFrame. [online] Available at: https://www.youtube.com/watch?v=tFwp589MAFk [Accessed 15 Jun. 2018].
		jBAct.setIcon(iAct);        //setting image icon to button 'Act'
		jBAct.setBounds(120, 13, 80, 20);
		panel3.add(jBAct);
		jBAct.addActionListener(this);
		
		jBRun = new JButton("Run");
		jBRun.setBackground(Color.white);
		iRun = new ImageIcon(getClass().getResource("run.png"));
		jBRun.setIcon(iRun);        //setting image icon to button 'Run'
		jBRun.setBounds(210, 13, 80, 20);
		panel3.add(jBRun);
		jBRun.addActionListener(this);
		
		jBReset = new JButton("Reset");
		jBReset.setBackground(Color.white);
		iReset = new ImageIcon(getClass().getResource("reset.png"));
		jBReset.setIcon(iReset);        //setting image icon to button 'Reset'
		jBReset.setBounds(302, 13, 90, 20);
		panel3.add(jBReset);
	    jBReset.addActionListener(this);
	    
		jLSpeed = new JLabel("Speed : ");
		jLSpeed.setBounds(510, 13, 50, 20);
		panel3.add(jLSpeed);		

		slider = new JSlider(JSlider.HORIZONTAL, 10, 1500, 1000);	//slider with 10 as minimum and 1500 as maximum value and 1000 as its default position of the knob
		slider.setMajorTickSpacing(350);	//sets 350 gap for each ticks
		slider.setPaintTicks(true);		//sets tick visible
		slider.setBounds(570, 15, 180, 20);
		panel3.add(slider);         
		slider.addChangeListener(this);
		
		
		
		// adding panel2a(Option, Square and Direction) on panel2
		
		GridBagConstraints c = new GridBagConstraints();
		panel2a = new JPanel(gbl);
		c.insets = new Insets(5,5,5,5);   //sets margin to the gridbag items
        
        c.anchor = GridBagConstraints.LINE_START;
    	jLOption = new JLabel("Option :");
    	c.weightx = 0.5;
    	c.gridx = 0;
		c.gridy = 0;
    	panel2a.add(jLOption,c);
    	
    	jLSquare = new JLabel("Sqaure :");
		c.gridy = 1;
		panel2a.add(jLSquare,c);	
		
		jLDirection = new JLabel("Direction :");
		c.gridy = 2;
		panel2a.add(jLDirection,c);	
		
		c.anchor = GridBagConstraints.LINE_END;
		jTOption = new JTextField("1",5);
		c.gridx = 1;
		c.gridy = 0;
		panel2a.add(jTOption,c);
		
		jTSquare = new JTextField("15",5);
		c.gridy = 1;
		panel2a.add(jTSquare,c);		
		
		jTDirection = new JTextField("West",5);
		c.gridy = 2;
		panel2a.add(jTDirection,c);       
        
		panel2.add(panel2a);
		
		
		
		// adding panel2b(Digital Timer) on panel2
		
		GridBagConstraints c1 = new GridBagConstraints();
		c1.insets = new Insets(10,20,10,0);		//sets margin to the gridbag items
		
		panel2b = new JPanel(new GridLayout(3, 1));
	    
		panel2bi = new JPanel();
		JLabel JLTDigitalTimer = new JLabel(" DIGITAL TIMER" );
		panel2bi.add(JLTDigitalTimer);	
		panel2b.add(panel2bi);
		
		panel2bii = new JPanel();
		jTHour = new JTextField(3);
		jTHour.setBackground(Color.black);		//sets color of the background
		jTHour.setForeground(Color.white);		//sets color of the font
		panel2bii.add(jTHour);
		
		JLabel jLColon1 = new JLabel(" : ");
		panel2bii.add(jLColon1);
		
		jTMin = new JTextField(3);
		panel2bii.add(jTMin);
		jTMin.setBackground(Color.black);
		jTMin.setForeground(Color.white);
		
		JLabel jLColon2 = new JLabel(" : ");
		panel2bii.add(jLColon2);
		
		jTSec = new JTextField(3);
		jTSec.setBackground(Color.black);
		jTSec.setForeground(Color.white);
		panel2bii.add(jTSec); 
		
	    timer = new Timer (1000, this);		//start ticking(Digital Timer) as the frame gets displayed
	    timer.start();
	    timer.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				jTHour.setText(Integer.toString(ticks / 3600));	//hour field
				jTMin.setText(Integer.toString(ticks / 60));	//minute field
			    jTSec.setText(Integer.toString(ticks % 60));	//second field
			    ticks = ticks + 1;
			}
		});
				
	    panel2b.add(panel2bii);
		panel2.add(panel2b);
		
		
		
		// adding panel2c(navigation button's) on panel2
				
		panel2c = new JPanel(new GridLayout(3,3,2,2));
				
		jBEmpty1 = new JButton();
		jBEmpty1.setEnabled(false);		//disable the button
		panel2c.add(jBEmpty1);
		
		jBUp = new JButton("^");
		jBUp.setBackground(Color.white);
		panel2c.add(jBUp);
		jBUp.addActionListener(this);
		
		jBEmpty2 = new JButton();	
		jBEmpty2.setEnabled(false);
		panel2c.add(jBEmpty2);
		
		jBLeft = new JButton("<");
	    jBLeft.setBackground(Color.white);
		panel2c.add(jBLeft);
		jBLeft.addActionListener(this);
		
		jBEmpty3 = new JButton();	
		jBEmpty3.setEnabled(false);
		panel2c.add(jBEmpty3);
		
		jBRight = new JButton(">");
	    jBRight.setBackground(Color.white);
		panel2c.add(jBRight);
		jBRight.addActionListener(this);
		
		jBEmpty4 = new JButton();	
		jBEmpty4.setEnabled(false);
		panel2c.add(jBEmpty4);
		
	    jBDown = new JButton("v");
		jBDown.setBackground(Color.white);
		panel2c.add(jBDown);
		jBDown.addActionListener(this);
		
		jBEmpty5 = new JButton();
		jBEmpty5.setEnabled(false);
		panel2c.add(jBEmpty5);		
		
		panel2.add(panel2c);
		
		
		
		// adding panel2d(Option1, Option2, Option3 and Exit) on panel2
	
		panel2d = new JPanel(new GridLayout(3,2,4,4));
		
		Font f = new Font("Arial",Font.PLAIN,11);
		
		jLabelEmpty1 = new JLabel();
		panel2d.add(jLabelEmpty1);
		jLabelEmpty2 = new JLabel();
		panel2d.add(jLabelEmpty2);
		
		jBOption1 = new JButton("Option 1");
		jBOption1.setBackground(Color.white);
		jBOption1.setFont(f);
		panel2d.add(jBOption1);
		jBOption1.addActionListener(this);
		
		jBOption2 = new JButton("Option 2");
		jBOption2.setBackground(Color.white);
		jBOption2.setFont(f);
		panel2d.add(jBOption2);
		jBOption2.addActionListener(this);
		
		jBOption3 = new JButton("Option 3");
		jBOption3.setBackground(Color.white);
		jBOption3.setFont(f);
		panel2d.add(jBOption3);
		jBOption3.addActionListener(this);
		
		jBExit = new JButton("Exit");
		jBExit.setBackground(Color.white);
		jBExit.setFont(f);
		panel2d.add(jBExit);
		jBExit.addActionListener(this);
		
		panel2.add(panel2d);
		
		
		
		// adding panel2e(Compass) on panel2
		
		panel2e = new JPanel(new BorderLayout());		
		iCompass = new ImageIcon(getClass().getResource("west.jpg"));
		jLCompass = new JLabel(iCompass);
		panel2e.add(jLCompass,BorderLayout.CENTER);
		panel2.add(panel2e);		
		
	}
	
	
	
	private void buildMaze(){

		for(int p=0; p<13; p++){	//rows of 13 for maze
			for(int q=0; q<16; q++){	//column of 16 for maze
				jBTile[p][q] = new JButton();	//creating a new button on array for each loop executed
				panel1.add(jBTile[p][q]);
				//buttons?, H. (2018). How to remove border around buttons?. [online] Stack Overflow. Available at: https://stackoverflow.com/questions/2713190/how-to-remove-border-around-buttons [Accessed 20 Jun. 2018].
				jBTile[p][q].setBorderPainted(false); //removes border of button 
				jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("white.jpg")));

				if(p==0&& q==1||p==3&& q==1||p==6&& q==1||p==9&& q==1||p==12&& q==1||p==1&& q==1||p==2&& q==1||p==7&& q==1||p==8&& q==1||p==1&& q==5||p==2&& q==5||p==7&& q==5||p==8&& q==5||p==1&& q==9||p==2&& q==9||p==4&& q==11||p==5&& q==11||p==7&& q==12||p==8&& q==12
					||p==0&& q<=15||p==3&& q<=15||p==6&& q<=15||p==9&& q<=15||p==12&& q<=15||p==4&& q==2||p==5&& q==2||p==10&& q==2||p==11&& q==2||p==4&& q==6||p==5&& q==6||p==10&& q==6||p==11&& q==6||p==4&& q==11||p==5&& q==11)
				{	// indexes for sand.png
				jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("sand.jpg")));
				// DaniWeb. (2018). Comparing components using setName.. [online] Available at: https://www.daniweb.com/programming/software-development/threads/446021/comparing-components-using-setname [Accessed 3 Jul. 2018].
				jBTile[p][q].setName("path");	//(DaniWeb, 2018) -> setting name of sand for collision detection
				}
				
				else {
					jBTile[p][q].setName("block");	//(DaniWeb, 2018) -> setting name of white as block for collision detection
				}
				
				if (p ==12 && q==0){	//set's sandstone(end)
					jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("sandstone.jpg")));
					jBTile[p][q].setName("end");	//(DaniWeb, 2018) -> setting name of sandstone for collision detection 
				}
			}//end's loop after creation of a row 
		}//end loop for single row 
		jBTile[0][15].setIcon(new ImageIcon(getClass().getResource("ball.png")));	//adding ball image on 2D array index(0,15)
	}
	
	private void buildMaze2(){
		
		panel1.removeAll(); //clears the pannel	     
		panel1.updateUI();// Refreshes Panel

	
		pB = 0;		//re-initialization of position for ball
		qB = 15;	//re-initialization of position for ball
		posB = 15;		//re-initialization of position/index of the ball
		jLCompass.setIcon(new ImageIcon(getClass().getResource("west.jpg")));
		jTDirection.setText("West");
		jTSquare.setText(Integer.toString(posB));		//sets '15'(posB) on jTSquare
		time.stop();

		for(int p=0; p<13; p++){
			for(int q=0; q<16; q++){
				jBTile[p][q] = new JButton();
				panel1.add(jBTile[p][q]);
				jBTile[p][q].setBorderPainted(false); 
				jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("white.jpg")));

				if(p==0&& q==1||p==3&& q==1||p==6&& q==1||p==9&& q==1||p==12&& q==1||p==1&& q==1||p==2&& q==1||p==7&& q==1||p==8&& q==1||p==1&& q==5||p==2&& q==5||p==7&& q==5||p==8&& q==5||p==1&& q==9||p==2&& q==9||p==4&& q==11||p==5&& q==11||p==7&& q==12||p==8&& q==12
					||p==0&& q<=15||p==3&& q<=15||p==6&& q<=15||p==9&& q<=15||p==12&& q<=15||p==4&& q==2||p==5&& q==2||p==10&& q==2||p==11&& q==2||p==4&& q==6||p==5&& q==6||p==10&& q==6||p==11&& q==6||p==4&& q==11||p==5&& q==11)
				{jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("sand.jpg")));
				jBTile[p][q].setName("path");
				}
				
				else {
					jBTile[p][q].setName("block");
				}
				
			    if (p ==12 && q==0){ 		
					jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("sandstone.jpg")));
					jBTile[p][q].setName("end");
				}
				
				if (p ==3 && q==3 || p ==5 && q==11 || p ==8 && q==1 || p ==9 && q==9 ){ // set's bombs
					jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("bomb.png")));
					jBTile[p][q].setName("bomb");	//(DaniWeb, 2018) -> setting name of bomb for collision detection 
				}
				
				if (p ==2 && q==9 || p ==6 && q==4 || p ==9 && q==13 || p ==12 && q==8 ){ // set's foods
					jBTile[p][q].setIcon(new ImageIcon(getClass().getResource("grapes.png")));
				}
			}
		}
		jBTile[0][15].setIcon(new ImageIcon(getClass().getResource("ball.png")));
	}
	
	private void moveUp(){
		
		if(jTOption.getText().equals("3"))
			playSound("move");	//calling 'playSound()' in order to play sound
		jBTile[pB-1][qB].setIcon(new ImageIcon(getClass().getResource("ball.png")));	//set's ball image on next index on each 'up' button pressed
		jBTile[pB][qB].setIcon(new ImageIcon(getClass().getResource("sand.jpg")));		//set's sand image on current index on the ball
		pB = pB - 1;	//decrease column's value by '1' and set's it on instance variable
		jLCompass.setIcon(new ImageIcon(getClass().getResource("north.jpg")));
		jTDirection.setText("North");
		posB = posB -16;	//decreases the position/index of the ball by '16'
	}
	
	private void moveDown(){
		
		if(jTOption.getText().equals("3"))
			playSound("move");	//calling 'playSound()' in order to play sound
		jBTile[pB+1][qB].setIcon(new ImageIcon(getClass().getResource("ball.png")));
		jBTile[pB][qB].setIcon(new ImageIcon(getClass().getResource("sand.jpg")));
		pB = pB + 1;
		jLCompass.setIcon(new ImageIcon(getClass().getResource("south.jpg")));
		jTDirection.setText("South");
		posB = posB + 16;
	}
	
	private void moveRight(){
		
		if(jTOption.getText().equals("3"))
			playSound("move");	//calling 'playSound()' in order to play sound		
		jBTile[pB][qB+1].setIcon(new ImageIcon(getClass().getResource("ball.png")));
		jBTile[pB][qB].setIcon(new ImageIcon(getClass().getResource("sand.jpg")));
		qB = qB +1;
		jLCompass.setIcon(new ImageIcon(getClass().getResource("east.jpg")));
		jTDirection.setText("East");
		posB = posB + 1;
	}
	
	private void moveLeft(){
		
		if(jTOption.getText().equals("3"))
			playSound("move");	//calling 'playSound()' in order to play sound
		jBTile[pB][qB-1].setIcon(new ImageIcon(getClass().getResource("ball.png")));
		jBTile[pB][qB].setIcon(new ImageIcon(getClass().getResource("sand.jpg")));
		qB = qB -1;
		jTDirection.setText("West");
		jLCompass.setIcon(new ImageIcon(getClass().getResource("west.jpg")));
		posB = posB - 1;
	}

 
	private void reset() {	//resetting all the content's of the frame 
		
		//Coderanch.com. (2018). How to: clear the JPanel before repainting? (Swing / AWT / SWT forum at Coderanch). [online] Available at: https://coderanch.com/t/345317/java/clear-JPanel-repainting [Accessed 20 Jun. 2018].
		panel1.removeAll();     //clears the pannel	 
		panel1.updateUI();		// Refreshes Panel
		
		pB = 0;		//re-initialization of position for ball
		qB = 15;	//re-initialization of position for ball
		posB = 15;		//re-initialization of position/index of the ball
		buildMaze();	
		jLCompass.setIcon(new ImageIcon(getClass().getResource("west.jpg")));
		jTDirection.setText("West");
		jTOption.setText("1");
		jTSquare.setText(Integer.toString(posB));
		time.stop();	
	}
	
	private void reset3() {		//resetting contents as Option3 when ball touches bomb('gameover')
		
		reset();
		jTOption.setText("3");
		buildMaze2();
	}
	
	public void fall() {	//fall's automatically when there's block below the ball(Option2)
		
		//YouTube. (2018). How to Build Scheduled Tasks in Java : Java & Other Tech Tips. [online] Available at: https://www.youtube.com/watch?v=bzsSqM4eXWk [Accessed 20 Jun. 2018].
		java.util.Timer fall = new java.util.Timer ();		//creating object(fall) java.util.Timer
		if (pB<12){
			for(int i = 0; i < 3;i++) {
				fall.schedule(new java.util.TimerTask() {		
					public void run() {
						playSound("fall");
						moveDown();
					}
				}, 100);
				posB = posB + 16;			
			}
		}
	}
	
	private void playSound(String sound) {
		
		// YouTube. (2018). How to play music in Java (Simple). [online] Available at: https://www.youtube.com/watch?v=3q4f6I5zi2w [Accessed 11 Jul. 2018].
		try {
			AudioInputStream music = AudioSystem.getAudioInputStream(new File("sounds/"+sound+".wav"));
			  wav = AudioSystem.getClip();	//gets the clip
			  wav.open(music);	//opens the source file
			  wav.start();		//starts the clip
		}
		
		catch(Exception e) {
			System.out.println("Error");	//if file is not found, display this message
		}
	}
	
	public void stateChanged(ChangeEvent e)	{
		
		slider.setInverted(true);		//invert's the values of the slider
		int timeGap = slider.getValue();	//get's current value of the slider 
		time.setDelay(timeGap);		//control's the speed for Run(JButton)
	}

	
	
	
	public void actionPerformed(ActionEvent event) {

		if( event.getSource() == jBLeft){		
			if (qB>0){
				if (jBTile[pB][qB-1].getName()=="end"){		//if ball gets in contact with sandstone'end' display the following
					if(jTOption.getText().equals("3"))
						playSound("finish");	// plays sound only in Option3
					moveLeft();
					JOptionPane.showMessageDialog(null, "Congratulations, You've completed this stage!"); 
				}
				else if (jBTile[pB][qB-1].getName()=="block"){		//if ball gets in contact with white image, dont move
					System.out.println("Cannot Move Left");
				}
				else if (jBTile[pB][qB-1].getName()=="bomb" &&  jTOption.getText().equals("3")){	//if it satisfy both condition(get in contact with bomb image and Option3)
					playSound("blast1");
					JOptionPane.showMessageDialog(null, "Game Over!"); 
					reset3();		//reset's Option3 scenario
				}
				else {
					moveLeft();
				}
				jTSquare.setText(Integer.toString(posB)); 	//set's current ball position
			}		
		}

		if( event.getSource() == jBRight){
			if (qB<15){
				if (jBTile[pB][qB+1].getName()=="block"){
					System.out.println("Cannot Move Right");
				}
				else if (jBTile[pB][qB+1].getName()=="bomb" &&  jTOption.getText().equals("3")){
					playSound("blast1");
					JOptionPane.showMessageDialog(null, "Game Over!"); 
					reset3();
				}
				else {
					moveRight();
				}
				jTSquare.setText(Integer.toString(posB));
			}
		}
		
		
		if( event.getSource() == jBLeft && jTOption.getText().equals("2")){		//if it satisfies (button pressed left and Option2) run the following code			
			if (pB<12){
				if(jBTile[pB+1][qB].getName()=="path") {  	//if there is sand block below, run this code
					fall();
				}
				jTSquare.setText(Integer.toString(posB));
			}
		}

		if( event.getSource() == jBRight && jTOption.getText().equals("2") ){	//if it satisfies (button pressed right and Option2) run the following code			
			if (pB<12){
				if(jBTile[pB+1][qB].getName()=="path") {	//if there is sand block below, run this code
					fall();
				}
				jTSquare.setText(Integer.toString(posB));
			}
		}
		
		if( event.getSource() == jBUp){
			if (pB>0){
				if (jBTile[pB-1][qB].getName()=="block"){
					System.out.println("Cannot Move Up");
				}
				else if (jBTile[pB-1][qB].getName()=="bomb" &&  jTOption.getText().equals("3")){
					playSound("blast1");
					JOptionPane.showMessageDialog(null, "Game Over!"); 
					reset3();
				}
				else {
					moveUp();			
				}
				jTSquare.setText(Integer.toString(posB));
			}
		}					

		if( event.getSource() == jBDown){  					
			if (pB<12){
				if (jBTile[pB+1][qB].getName()=="block"){
					System.out.println("Cannot Move Down");
				}
				else if (jBTile[pB+1][qB].getName()=="bomb" &&  jTOption.getText().equals("3")){
					playSound("blast1");
					JOptionPane.showMessageDialog(null, "Game Over!"); 
					reset3();
				}
				else {
					moveDown();
				}
				jTSquare.setText(Integer.toString(posB));
			} 
		}
		
		if( event.getSource() == jBAct){	//if player presses Act Button, run tis code
			if (qB==9||qB==6||qB==5||qB==2){
				if(qB==9&&pB==3||qB==6&&pB==6||qB==5&&pB==9||qB==2&&pB==12) {
					moveLeft();
				}
				else {
				moveDown();
				}
			}
			else if (posB == 193){
				moveLeft();
				JOptionPane.showMessageDialog(null, "Congratulations, You've completed this stage!"); 
				reset();
			}
			else {
				moveLeft();
			}
			jTSquare.setText(Integer.toString(posB));
		}	
		
		if(event.getSource()==time){	//setting the path for auto-run
			if (qB==9||qB==6||qB==5||qB==2){
				if(qB==9&&pB==3||qB==6&&pB==6||qB==5&&pB==9||qB==2&&pB==12) {
					moveLeft();
				}
				else {
				moveDown();
				}
			}
			else if (posB == 193){
				moveLeft();
				JOptionPane.showMessageDialog(null, "Congratulations, You've completed this stage!"); 
				reset();
			}
			else {
				moveLeft();
			}
			jTSquare.setText(Integer.toString(posB));
		}
		
		if( event.getSource() == jBRun){	//auto move the ball, when 'Run' button pressed
			time.start();
		}	
		
		if( event.getSource() == jBReset){		//reset's the scenario of the frame	
			reset();
		}	

	    if(event.getSource() == jBOption1){		//reset's the scenario of the frame
	    	reset();
	    }
	    
	    if(event.getSource() == jBOption2){		//reset's the scenario of the frame and set's Option label to '2'
			reset();
			jTOption.setText("2");
	    }
	   
	    if(event.getSource() == jBOption3){		//build's maze2 
	    	buildMaze2();
	    	jTOption.setText("3");
	    }
	    
	    if(event.getSource() == jBExit){	//exit's the frame
			System.exit(0);
		}
	    
	}

}