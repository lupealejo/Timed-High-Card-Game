import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HighCardView 
{
	static int NUM_CARDS_PER_HAND = 7;
	static int NUM_PLAYERS = 2;
	static JLabel[] computerLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] humanLabels = new JLabel[NUM_CARDS_PER_HAND];
	static JLabel[] playedCardLabels = new JLabel[NUM_PLAYERS];
	static JLabel[] playLabelText = new JLabel[NUM_PLAYERS];
	static JLabel currentLeftCard, currentRightCard;
	static JLabel text = new JLabel("Select a Pile", JLabel.CENTER);
	static JButton pass = new JButton("Pass");
	static int turn = 0, index, compIndex;
	static boolean inCenter = false;
	static Card newCard = new Card();
	static Card selectedCard = new Card();
	static Card computerSelected = new Card();
	static boolean found = false;
	static boolean passBool = false;

	static JButton[] humanCardButtons = new JButton[NUM_CARDS_PER_HAND];
	static JButton[] sideButtons = new JButton[NUM_CARDS_PER_HAND];

	static final int NUM_CARD_IMAGES = 56; // 52 + 4 jokers

	static GUICard cardGUI = new GUICard();
	private static JButton currentButton = new JButton();
	// establish main frame in which program will run
	static CardTable myCardTable;

	static int numPacksPerDeck = 1;
	static int numJokersPerPack = 0;
	static int numUnusedCardsPerPack = 0;
	static Card[] unusedCardsPerPack = null;
	static Card[] winnings = new Card[NUM_CARDS_PER_HAND * 2];
	static int winningTotal = 0;

	static CardGameFramework highCardGame = new CardGameFramework(
			numPacksPerDeck, numJokersPerPack,
			numUnusedCardsPerPack, unusedCardsPerPack,
			NUM_PLAYERS, NUM_CARDS_PER_HAND);

	static Clock insertClock = new Clock();

	public static Hand humanHand;
	public static Hand computerHand;

	static Card leftCard = highCardGame.getCardFromDeck();
	static Card rightCard = highCardGame.getCardFromDeck();


	public HighCardView()
	{
		if (!highCardGame.deal())
		{
			System.exit(1);
		}else{
			init();
		}
	}

	static void init()
	{
		// Loop counter
		int k;


		myCardTable
		= new CardTable("CardTable", NUM_CARDS_PER_HAND, NUM_PLAYERS);

		myCardTable.setSize(900, 550);
		myCardTable.setLocationRelativeTo(null);
		myCardTable.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		myCardTable.compHandPanel.setBorder(
				BorderFactory.createTitledBorder("Computer Hand"));
		myCardTable.humanHandPanel.setBorder(
				BorderFactory.createTitledBorder("Your Hand"));
		myCardTable.leftPlay.setBorder(
				BorderFactory.createTitledBorder("Left Pile"));
		myCardTable.rightPlay.setBorder(
				BorderFactory.createTitledBorder("Right Pile"));
		myCardTable.timerPanel.setBorder(
				BorderFactory.createTitledBorder("Timer"));
		myCardTable.playAreaPanel.setLayout(new GridLayout(1, 3));

		// CREATE LABELS ----------------------------------------------------

		// prepare the image label arrays
		computerHand = highCardGame.getHand(0);
		for (k = 0; k < NUM_CARDS_PER_HAND; k++)
			computerLabels[k] = new JLabel(GUICard.getBackCardIcon());

		humanHand = highCardGame.getHand(1);

		highCardGame.sortHands();
		for (k = 0; k < NUM_CARDS_PER_HAND; k++)
			humanLabels[k] = new JLabel(GUICard.getIcon(humanHand.inspectCard(k)));

		// ADD LABELS TO PANELS -----------------------------------------
		for (k = 0; k < NUM_CARDS_PER_HAND; k++)
			myCardTable.compHandPanel.add(computerLabels[k]);

		setupPlayerHand(humanHand, computerHand);
		setupPlayArea();

		// ADD TIMER -----------------------------------------------------
		myCardTable.timerPanel.add(insertClock.timeText);
		myCardTable.timerPanel.add(insertClock.startStopButton);
		myCardTable.timerPanel.add(pass);

		// Increase timer display font size
		insertClock.timeText.setFont(new Font("Aerial", Font.BOLD, 20));

		// show everything to the user
		//myCardTable.setVisible(true);
	}

	public int getNumCardsPerHand(){
		return NUM_CARDS_PER_HAND;
	}

	public void setNumCardsPerHand(int numCardsPerHand){
		NUM_CARDS_PER_HAND=numCardsPerHand;
	}

	public int getWinningTotal(){
		return winningTotal;
	}

	public void setWinningTotal(int winningTotal){
		this.winningTotal=winningTotal;
	}

	public Card[] getWinningsArray(){
		return winnings;
	}

	public void setWinningsArray(Card[] winnings){
		this.winnings=winnings;
	}

	public int getNumPlayers(){
		return NUM_PLAYERS;
	}

	public void setNumPlayers(int numPlayers){
		NUM_PLAYERS=numPlayers;
	}

	public CardGameFramework getHighCardGame(){
		return highCardGame;
	}

	public void setHighCardGame(CardGameFramework highCardGame){
		highCardGame=highCardGame;
	}

	public static void setupPlayArea()
	{
		for (int k = 0; k < NUM_PLAYERS; k++)
		{
			if (0 == k)
			{
				sideButtons[k] = new JButton("Select");
				playedCardLabels[k] = new JLabel(
						cardGUI.getIcon(leftCard));
			} else
			{
				sideButtons[k] = new JButton("Select");
				playedCardLabels[k] = new JLabel(
						cardGUI.getIcon(rightCard));
			}
		}

		currentLeftCard = playedCardLabels[0];
		currentRightCard = playedCardLabels[1];

		myCardTable.leftPlay.add(sideButtons[0], BorderLayout.WEST);
		myCardTable.rightPlay.add(sideButtons[1], BorderLayout.EAST);
		myCardTable.leftPlay.add(currentLeftCard, BorderLayout.SOUTH);
		myCardTable.rightPlay.add(currentRightCard, BorderLayout.SOUTH);
	}

	public static void setupPlayerHand(
			final Hand humanHand, final Hand computerHand)
	{
		for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
			humanCardButtons[k] = new JButton(
					"", cardGUI.getIcon(humanHand.inspectCard(k)));

		for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
		{
			myCardTable.humanHandPanel.add(humanCardButtons[k]);
		}
	}

	void addPlayCardListener(ActionListener listenForPlayCard)
	{
		for (int k = 0; k < NUM_CARDS_PER_HAND; k++)
		{
			currentButton=humanCardButtons[k];
			humanCardButtons[k].addActionListener(listenForPlayCard);
		}
	}

	void addSideButtonListener()
	{
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			currentButton = sideButtons[i];
			sideButtons[i].addActionListener(selectSides);
		}
	}

	void addPassButton()
	{
		currentButton = pass;
		pass.addActionListener(addPass);
	}

	private ActionListener addPass = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			setCurrentButton((JButton) e.getSource());
			passBool = true;
			computerPlays();
		}
	};

	private ActionListener cardPlayListener = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			setCurrentButton((JButton) e.getSource());
			myCardTable.humanHandPanel.remove(getCurrentButton());

			loop:
				for (int x = 0; x < NUM_CARDS_PER_HAND; x++)
				{
					if (getCurrentButton() == humanCardButtons[x] && leftCard.build(selectedCard) ||
							getCurrentButton() == humanCardButtons[x] && rightCard.build(selectedCard))
					{
						index = x;
						myCardTable.humanHandPanel.remove(getCurrentButton());
						playCards(humanHand.inspectCard(x), computerHand);
						break loop;
					}
				}
			refreshPlayerPanel();
		}   
	};

	private ActionListener selectSides = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			setCurrentButton((JButton) e.getSource());

			loop:
				for (int x = 0; x < NUM_PLAYERS; x++)
				{
					if ((getCurrentButton() == sideButtons[0] && inCenter == true && leftCard.build(selectedCard)))
					{
						leftCard = selectedCard;
						myCardTable.centerPlay.remove(playedCardLabels[1]);
						myCardTable.leftPlay.remove(currentLeftCard);
						myCardTable.centerPlay.remove(text);
						currentLeftCard = playedCardLabels[1];
						myCardTable.leftPlay.add(currentLeftCard);
						inCenter = false;
						computerPlays();
					}
					else if (inCenter == true && rightCard.build(selectedCard))
					{
						rightCard = selectedCard;
						myCardTable.centerPlay.remove(playedCardLabels[1]);
						myCardTable.rightPlay.remove(currentRightCard);
						myCardTable.centerPlay.remove(text);
						currentRightCard = playedCardLabels[1];
						myCardTable.rightPlay.add(currentRightCard);
						inCenter = false;
						computerPlays();
					}
				}
			refreshPlayerPanel();
		}
	};

	public JButton getCurrentButton(){
		return currentButton;
	}

	public void setCurrentButton(JButton currentButton){
		this.currentButton=currentButton;
	}

	public static void refreshScreen()
	{
		myCardTable.mainPanel.setVisible(false);
		myCardTable.mainPanel.setVisible(true);
	}

	public static void refreshPlayerPanel()
	{
		myCardTable.humanHandPanel.setVisible(false);
		myCardTable.humanHandPanel.setVisible(true);
	}

	public static void refreshComputerPanel()
	{
		myCardTable.compHandPanel.setVisible(false);
		myCardTable.compHandPanel.setVisible(true);
	}

	public static void refreshPlayArea()
	{
		myCardTable.playAreaPanel.setVisible(false);
		myCardTable.playAreaPanel.setVisible(true);
	}

	public static void clearPlayArea()
	{	
		myCardTable.leftPlay.remove(playedCardLabels[0]);
		myCardTable.rightPlay.remove(playedCardLabels[1]);

		refreshPlayArea();
	}

	public static void addCardsToPlayArea()
	{
		myCardTable.centerPlay.add(playedCardLabels[1]);
		
		if (highCardGame.getNumCardsRemainingInDeck() != 0)
		{
			newCard = highCardGame.getCardFromDeck();
			humanHand.setCard(index, newCard);

			myCardTable.humanHandPanel.removeAll();
			myCardTable.humanHandPanel.revalidate();
			myCardTable.humanHandPanel.repaint();

			if ()
			
			setupPlayerHand(humanHand, computerHand);
			//humanCardButtons[index] = new JButton("", cardGUI.getIcon(humanHand.inspectCard(index)));
			humanLabels[index] = new JLabel(GUICard.getIcon(humanHand.inspectCard(index)));
			myCardTable.humanHandPanel.add(humanCardButtons[index]);
		}
		myCardTable.centerPlay.add(text);
		inCenter = true;
		refreshPlayArea();
	}

	public static void computerPlays()
	{	
		computerSelected = computerSelect();

		if(found == false && passBool == true && highCardGame.getNumCardsRemainingInDeck() == 0)
		{
			int compNullCards = 0, playerNullCards = 0;
			
			for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
			{
				if (humanHand.inspectCard(i) == null)
				{
					playerNullCards += 1;
				}
				
				if (computerHand.inspectCard(i) == null)
				{
					compNullCards += 1;
				}
			}
			
			if (playerNullCards > compNullCards)
			{
				WinnerWindow EndGame = new WinnerWindow("You won!");
			}
			else if (playerNullCards < compNullCards)
			{
				WinnerWindow EndGame = new WinnerWindow("You lost!");
			}
			else
			{
				WinnerWindow EndGame = new WinnerWindow("Draw!");
			}
		}
		else if (found == false && passBool == true) 
		{
			leftCard = highCardGame.getCardFromDeck();
			myCardTable.leftPlay.remove(currentLeftCard);
			currentLeftCard = new JLabel(cardGUI.getIcon(leftCard), JLabel.CENTER);
			myCardTable.leftPlay.add(currentLeftCard);
			
			rightCard = highCardGame.getCardFromDeck();
			myCardTable.rightPlay.remove(currentRightCard);
			currentRightCard = new JLabel(cardGUI.getIcon(rightCard), JLabel.CENTER);
			myCardTable.rightPlay.add(currentRightCard);;
			return;
		}
		else if (found == false)
		{
			return;
		}

		newCard = highCardGame.getCardFromDeck();
		computerHand.setCard(compIndex, newCard);

		playedCardLabels[0] = new JLabel(
				cardGUI.getIcon(computerSelected), JLabel.CENTER);

		if (leftCard.build(computerSelected))
		{
			leftCard = computerSelected;
			myCardTable.leftPlay.remove(currentLeftCard);
			currentLeftCard = playedCardLabels[0];
			myCardTable.leftPlay.add(currentLeftCard);
		}
		else if (rightCard.build(computerSelected))
		{
			rightCard = computerSelected;
			myCardTable.rightPlay.remove(currentRightCard);
			currentRightCard = playedCardLabels[0];
			myCardTable.rightPlay.add(currentRightCard);;
		}

		refreshPlayArea();
	}

	private static Card computerSelect()
	{
		Card selected = new Card();
		found = false;

		for (int i = 0; i < NUM_CARDS_PER_HAND; i++)
		{
			if (leftCard.build(computerHand.inspectCard(i)) || rightCard.build(computerHand.inspectCard(i)))
			{
				selected = computerHand.inspectCard(i);
				compIndex = i;
				found = true;
			}
		}

		return selected;
	}

	public static void playCards(Card playerCard, Hand computerHand)
	{
		playerPlayCard(playerCard);
		addCardsToPlayArea();
	}

	public static void endGame(){
		int x=0;
		int k=0;
		for (k=0;k<computerLabels.length;k++){
			if (computerLabels[k].getParent()==null){
				x++;
			}
		}

		if (k==x){
			clearPlayArea();
			if (getWinnings()>=8){
				myCardTable.playAreaPanel.add(new JLabel("You Win!", JLabel.CENTER));
			} else {

				myCardTable.playAreaPanel.add(new JLabel("You Lose!", JLabel.CENTER));
			}
		}
	}

	public static void playerPlayCard(Card card)
	{
		playedCardLabels[1] = new JLabel(
				cardGUI.getIcon(card), JLabel.CENTER);

		playLabelText[1] = new JLabel("You", JLabel.CENTER);
		refreshScreen();
	}

	public static Card computerPlayCard(Card playerCard, Hand computerHand)
	{
		//TODO: get card from Computer's hand
		Card computerCard = new Card();
		boolean higherCard = false;

		for (int i = 0; i < computerHand.getNumCards(); i++)
		{
			if (getIndexValue(playerCard.getchar()) <
					getIndexValue(computerHand.inspectCard(i).getchar()))
			{
				computerCard = computerHand.playCard(i);
				higherCard = true;
				break;
			}
		}

		if (!higherCard)
			computerCard = computerHand.playCard(0);

		playedCardLabels[0] = new JLabel(
				cardGUI.getIcon(computerCard), JLabel.CENTER);

		playLabelText[0] = new JLabel("Computer", JLabel.CENTER);

		loop:
			for (int k=0;k<computerLabels.length;k++){
				if (computerLabels[k].getParent()!=null){
					myCardTable.compHandPanel.remove(computerLabels[k]);
					break loop;
				}
			}
		refreshScreen();
		return computerCard;
	}

	public static void addToWinnings(Card playerCard, Card computerCard)
	{
		winnings[winningTotal] = playerCard;
		winnings[winningTotal+1] = computerCard;
		winningTotal = winningTotal + 2;
	}

	public static int getWinnings()
	{
		return winningTotal;
	}

	public static boolean playerWins(Card playerCard, Card computerCard)
	{
		int playerValue = getIndexValue(playerCard.getchar());
		int computerValue = getIndexValue(computerCard.getchar());

		if (playerValue >= computerValue)
		{
			addToWinnings(playerCard, computerCard);
			return true;
		}
		return false;
	}

	public static int getValue(char value)
	{
		return getIndexValue(value);

	}

	private static int getIndexValue(char value)
	{
		int index = -1;
		for (int i = 0; i < Card.valueRanks.length; i++)
		{
			if (Card.valueRanks[i] == value)
				return (index = i);
		}
		return index;
	}

	public static void setVisible(boolean bool){
		//init();
		myCardTable.setVisible(bool);
	}

	static class WinnerWindow extends JFrame
	{
		public JPanel mainPanel;
		static JLabel newtext;
		
		public WinnerWindow(String text)
		{
			setSize(800, 600);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new BorderLayout());
			
			newtext = new JLabel(text, JLabel.CENTER);
			mainPanel = new JPanel();
			
			add(mainPanel);
			mainPanel.add(newtext);
		}
	}
	
	static class CardTable extends JFrame
	{
		private int MAX_CARDS_PER_HAND = 56;
		private int MAX_PLAYERS = 2;  // for now, we only allow 2 person games

		private int numCardsPerHand;
		private int numPlayers;

		public JPanel compHandPanel, humanHandPanel, playAreaPanel, leftPlay, rightPlay, centerPlay;
		public JPanel mainPanel, timerPanel;

		public CardTable(String title, int numCardsPerHand, int numPlayers)
		{
			super(title);
			if (!isValid(title, numCardsPerHand, numPlayers)) return;
			setSize(800, 600);
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new BorderLayout());
			mainPanel = new JPanel();
			compHandPanel = new JPanel();
			humanHandPanel = new JPanel();
			playAreaPanel = new JPanel();
			timerPanel = new JPanel();
			leftPlay = new JPanel();
			rightPlay = new JPanel();
			centerPlay = new JPanel();

			add(compHandPanel, BorderLayout.NORTH);
			add(playAreaPanel, BorderLayout.CENTER);
			add(humanHandPanel, BorderLayout.SOUTH);
			add(timerPanel, BorderLayout.EAST);

			playAreaPanel.add(leftPlay, BorderLayout.WEST);
			playAreaPanel.add(centerPlay, BorderLayout.CENTER);
			playAreaPanel.add(rightPlay, BorderLayout.EAST);
		}

		private boolean isValid(String title, int numCardsPerHand, int numPlayers)
		{
			if (title.length() <= 0 ||
					numCardsPerHand <= 0 ||
					numCardsPerHand > MAX_CARDS_PER_HAND ||
					numPlayers <= 0 ||
					numPlayers > MAX_PLAYERS) return false;
			return true;
		}

		public int getNumCardsPerHand()
		{
			return numCardsPerHand;
		}

		public int getNumPlayers()
		{
			return numPlayers;
		}
	}
}


class GUICard
{
	private static Icon[][] iconCards = new ImageIcon[14][4];
	private static Icon iconBack;
	static boolean iconsLoaded = false;

	// Adding this array so we can know what values are valid
	//public static char[] cardValues = {'A', '2', '3', '4', '5', '6', '7', '8',
	//      '9', 'T', 'J', 'Q', 'K', 'X'};

	private static String[] cardSuites = new String[]{"C", "D", "H", "S"};

	public GUICard()
	{
		loadCardIcons();
	}

	static void loadCardIcons()
	{
		if (!iconsLoaded)
		{
			// build the file names ("AC.gif", "2C.gif", "3C.gif", "TC.gif", etc.)
			// in a SHORT loop.  For each file name, read it in and use it to
			// instantiate each of the 57 Icons in the icon[] array.
			//int x = 0;
			//int y = 0;
			for (int x = 0; x < cardSuites.length; x++)
			{
				for (int y = 0; y < Card.Value.length; y++)
				{
					iconCards[y][x] = new ImageIcon(
							"images/" + Card.Value[y] + cardSuites[x] + ".gif");
				}
			}
			iconBack = new ImageIcon("images/BK.gif");
			iconsLoaded = true;
		}
	}

	// turns 0 - 13 into "A", "2", "3", ... "Q", "K", "X"
	static String turnIntIntoCardValue(int k)
	{
		// an idea for a helper method (do it differently if you wish)
		return String.valueOf(Card.Value[k]);
	}

	// turns 0 - 3 into "C", "D", "H", "S"
	static String turnIntIntoCardSuit(int j)
	{
		// an idea for another helper method (do it differently if you wish)
		return cardSuites[j];
	}

	private static int valueAsInt(Card card)
	{
		String values = new String(Card.Value);
		//return Arrays.asList(Card.Value).indexOf(card.getchar());
		return values.indexOf(card.getchar());
	}

	private static int suitAsInt(Card card)
	{
		//String suite;
		return card.getSuit().ordinal();

	}

	static public Icon getIcon(Card card)
	{
		return (Icon) iconCards[valueAsInt(card)][suitAsInt(card)];
	}

	static public Icon getBackCardIcon()
	{
		return (Icon) iconBack;
	}
}

// Set up Timer and button actions to run on separate thread
class Clock extends JFrame
{
	private int counter = 0;
	private boolean runTimer = false;
	private final int PAUSE = 100; // Milliseconds
	private String start = "START";
	private String stop = "STOP";

	public Timer clockTimer;
	public JButton startStopButton;
	public JLabel timeText;
	public JPanel timerPanel;

	// Default constructor creates GUI
	public Clock()
	{
		// Timer action set to 1000 milliseconds
		clockTimer = new Timer(1000, timerEvent);

		timeText = new JLabel("" + formatToTime(counter));

		startStopButton = new JButton(start);
		startStopButton.addActionListener(buttonEvent);

		/***Display clock in separate window for testing***/
		//timerPanel = new JPanel();
		//timerPanel.add(timeText);
		//timerPanel.setLayout(new BorderLayout());
		//add(timerPanel, BorderLayout.CENTER);
		//timerPanel.add(timeText, BorderLayout.NORTH);
		//timerPanel.add(startStopButton, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(200, 200);
	}

	// Format timer output to string as minutes:seconds
	public String formatToTime(long seconds)
	{
		long s = seconds % 60;
		long m = (seconds / 60) % 60;
		return String.format("%01d:%02d", m, s);
	}

	// Increment Timer
	private ActionListener timerEvent = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			counter++;
			timeText.setText("" + formatToTime(counter));
		}
	};


	// Create Timer object and call run method
	private ActionListener buttonEvent = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			TimerClass timerThread = new TimerClass();
			timerThread.start();
		}
	};


	// Called by ActionListener to start, stop, and display time and buttons
	private class TimerClass extends Thread
	{
		public void run()
		{
			if (runTimer)
			{
				startStopButton.setText(start);
				clockTimer.stop();
				runTimer = false;
				timeText.setText("" + formatToTime(counter));
			}
			else if (!runTimer)
			{
				startStopButton.setText(stop);
				clockTimer.start();
				runTimer = true;
				counter = 0;
				timeText.setText("" + formatToTime(counter));
			}
			doNothing(PAUSE);
		}

		// Pause thread helper method
		public void doNothing(int milliseconds)
		{
			try
			{
				Thread.sleep(milliseconds);
			} catch (InterruptedException e)
			{
				System.out.println("Unexpected interrupt");
				System.exit(0);
			}
		}
	} //End TimerClass inner class
}
