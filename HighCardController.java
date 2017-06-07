import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class HighCardController 
{
	private HighCardView theView;
	private HighCardModel theModel;

	public HighCardController(HighCardView theView,HighCardModel theModel)
	{
		this.theView=theView;
		this.theModel=theModel;

		this.theView.setNumCardsPerHand(theModel.getNumCardsPerHand());
		this.theView.setNumPlayers(theModel.getNumPlayers());
		this.theView.setHighCardGame(theModel.getHighCardGame());
		this.theView.setWinningsArray(theModel.getWinningsArray());
		this.theView.addPlayCardListener(new CardPlayListener()); 
		this.theView.addSideButtonListener();
		this.theView.addPassButton();
	}

	class CardPlayListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			theView.setCurrentButton((JButton) e.getSource());

			loop:
				for (int x = 0; x < theView.NUM_CARDS_PER_HAND; x++)
				{
					theView.index = x;
					theView.selectedCard = theView.humanHand.inspectCard(x);
					
					if (theView.getCurrentButton() == theView.humanCardButtons[x] && theView.leftCard.build(theView.selectedCard) ||
							theView.getCurrentButton() == theView.humanCardButtons[x] && theView.rightCard.build(theView.selectedCard))
					{	
						if (theView.highCardGame.getNumCardsRemainingInDeck() == 0)
						{
							theView.myCardTable.humanHandPanel.remove(theView.getCurrentButton());
						}
						
						theView.passBool = false;
						theView.playCards(theView.humanHand.inspectCard(x), theView.computerHand);
						theView.addPlayCardListener(new CardPlayListener()); 
						break loop;
					}
				}
			theView.refreshPlayerPanel();
		}   
	}
}
