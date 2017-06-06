import java.util.Arrays;
import java.util.Random;




public class HighCardModel {

    static int NUM_CARDS_PER_HAND = 7;
    static int NUM_PLAYERS = 2;
    
    // Phase 3 Declarations
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
    
    public HighCardModel(){

          if (!highCardGame.deal())
          {
             //System.out.print("Unable to deal");
             System.exit(1);
          }
    }
    
    
    public int getNumCardsPerHand(){
        return NUM_CARDS_PER_HAND;
    }

    public void setNumCardsPerHand(int numCardsPerHand){
        NUM_CARDS_PER_HAND=numCardsPerHand;
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
    
    public Card[] getWinningsArray(){
        return winnings;
    }
    
    public void setWinningsArray(Card[] winnings){
        this.winnings=winnings;
    }

}


/*
   Set valid suits and values for each card in a standard deck
*/

class Card
{
   // Valid suits
   public enum Suit
   {
      clubs, diamonds, hearts, spades
   }

   // Adding this array so we can know what values are valid
   public static char[] Value = {'A', '2', '3', '4', '5', '6', '7', '8',
         '9', 'T', 'J', 'Q', 'K', 'X'};

   public static char[] valueRanks = {'A', '2', '3', '4', '5', '6', '7', '8',
         '9', 'T', 'J', 'Q', 'K', 'X'};

   private char value;
   private Suit suit;
   private boolean errorFlag;

   // Default Constructor
   public Card()
   {
      value = 'A';
      suit = Suit.spades;
      errorFlag = false;
   }

   // Overloaded Constructor
   public Card(char value, Suit suit)
   {
      errorFlag = !set(value, suit);
   }

   static void arraySort(Card[] cards, int arraySize)
   {
      Card temp;   //holding variable

      for (int i = 0; i < arraySize; i++)
      {
         for (int j = 1; j < arraySize - i; j++)
         {
            if (getIndexValue(cards[j - 1].getchar())
                  > getIndexValue(cards[j].getchar()))
            {
               temp = cards[j - 1];
               cards[j - 1] = cards[j];
               cards[j] = temp;
            }
         }
      }
   }

   private static int getIndexValue(char value)
   {
      int index = -1;
      for (int i = 0; i < valueRanks.length; i++)
      {
         if (valueRanks[i] == value)
            return (index = i);
      }
      return index;
   }


   // Output message - invalid or display card
   public String toString()
   {
      if (errorFlag)
         return ("[ invalid ]");

      return (value + " of " + suit);
   }

   // Set and check the validity of the card
   public boolean set(char value, Suit suit)
   {
      if (isValid(value, suit))
      {
         this.value = value;
         this.suit = suit;
         return true;
      }
      return false;
   }

   // Accessors for suit, value, and error
   public Suit getSuit()
   {
      return suit;
   }

   public char getchar()
   {
      return value;
   }

   public boolean getErrorFlag()
   {
      return errorFlag;
   }

   // Both this value and suit objects are equal to passed in objects
   public boolean equals(Card card)
   {
      return card.value == value && card.suit == suit;
   }

   // Check if card has valid value and suit
   private boolean isValid(char value, Suit suit)
   {
      boolean valid = false;

      for (char v : Value)
      {
         if (value == v)
         {
            valid = true;
            break;
         }
      }

      if (valid)
      {
         for (Suit s : Suit.values())
         {
            if (suit == s)
               return true;
         }
      }
      return false;
   }
}

/*
   Hand class - Hand size, add cards to hand, and play cards from hands
 */
class Hand
{
   public static final int MAX_CARDS = 100; //Length of array

   private Card[] myCards;
   private int numCards;

   // Default constructor
   public Hand()
   {
      myCards = new Card[MAX_CARDS];   //Length
      numCards = 0;
   }

   // Remove all cards from the hand
   public void resetHand()
   {
      myCards = new Card[MAX_CARDS];
      numCards = 0;
   }

   // Adds a card to the next available position as an object copy
   public boolean takeCard(Card card)
   {
      if (numCards >= MAX_CARDS)
      {
         return false;
      } else
      {
         Card newCard = new Card(card.getchar(), card.getSuit());
         myCards[numCards] = newCard;
         numCards++;
         return true;
      }
   }

   // Returns and removes the card in the top occupied position
   public Card playCard()
   {
      Card card = myCards[numCards - 1];
      numCards--;
      return card;
   }

   // Overloaded playCard() to deal with an index
   public Card playCard(int index)
   {
      if (index > numCards || numCards == 0)
         return new Card();

      if (index == (numCards - 1))
         return playCard();

      Card temp = new Card(myCards[index].getchar(),
            myCards[index].getSuit());

      for (int i = index; i < numCards - 1 ; i++)
      {
         myCards[i].set(
               myCards[i + 1].getchar(), myCards[i + 1].getSuit());
      }

      myCards[numCards - 1] = null;
      numCards--;
      return temp;
   }

   // Output message
   public String toString()
   {
      String result = "Hand = ( ";
      if (numCards == 0)
      {
         result = result + ")";
      } else
      {
         for (int i = 0; i < numCards - 1; i++)
         {
            result = result + myCards[i] + ", ";
         }
         result = result + myCards[numCards - 1] + " )";
      }
      return result;
   }

   // Accessor for number of cards
   public int getNumCards()
   {
      return numCards;
   }

   // Accessor for each card
   public Card inspectCard(int k)
   {
      Card card;
      if (k > numCards || k < 0)
      {
         card = new Card('y', Card.Suit.spades);
      } else
      {
         card = myCards[k];
      }
      return card;
   }

   public void sort()
   {
      Card.arraySort(myCards, numCards);
   }

}

/*
   Deck class - Set size of deck, shuffle, and deal cards
*/

class Deck
{
   private static final int PACK_SIZE = 56;
   public final int MAX_DECKS = 6;
   public final int MAX_CARDS = MAX_DECKS * PACK_SIZE;
   private static Card[] masterPack = new Card[PACK_SIZE];
   public static boolean masterPackAllocated = false;

   private Card[] cards;

   private int topCard;
   private int numPacks;

   // Constructor for initial numPacks
   public Deck(int numPacks)
   {
      //if numPacks is passed an invalid value,
      //default it to 1
      if (numPacks < 0 || numPacks > MAX_DECKS) numPacks = 1;
      this.cards = new Card[numPacks * PACK_SIZE];
      this.numPacks = numPacks;
      this.topCard = numPacks * PACK_SIZE - 1;
      allocateMasterPack();
      init(numPacks);
   }

   // Overloaded Default Constructor
   public Deck()
   {
      this.numPacks = 1;
      this.cards = new Card[PACK_SIZE];
      this.topCard = PACK_SIZE - 1;
      allocateMasterPack();
      init(1);
   }

   // Private helper method called by Constructor to build masterPack only once
   private static void allocateMasterPack()
   {
      if (!masterPackAllocated)
      {
         int x = 0;
         for (Card.Suit suit : Card.Suit.values())
         {
            for (char value : Card.Value)
            {
               masterPack[x] = new Card(value, suit);
               x++;
            }
         }
         masterPackAllocated = true;
      }
   }

   //Initialize and Re-populate cards array
   public void init(int numPacks)
   {

      //if numPacks is passed an invalid value,
      //default it to 1
      if (numPacks < 0 || numPacks > MAX_DECKS) numPacks = 1;
      this.cards = new Card[numPacks * PACK_SIZE];
      this.numPacks = numPacks;
      this.topCard = numPacks * PACK_SIZE - 1;
      int cardNum = 0;
      while (cardNum < numPacks * PACK_SIZE)
      {
         for (int x = 0; x < numPacks; x++)
         {
            for (int y = 0; y < PACK_SIZE; y++)
            {
               cards[cardNum] = masterPack[y];
               cardNum++;
            }
         }
      }
   }

   // Mix up cards with random number generator
   public void shuffle()
   {
      Random rnd = new Random();
      for (int i = cards.length - 1; i > 0; i--)
      {
         int index = rnd.nextInt(i + 1);
         Card temp = cards[index];
         cards[index] = cards[i];
         cards[i] = temp;
      }
   }

   // Returns and removes the card  at the top position
   public Card dealCard()
   {
      int tCard = topCard;
      topCard--;
      return cards[tCard];
   }

   // Accessor for topCard
   public int getTopCard()
   {
      return topCard;
   }

   // Accessor for each card
   public Card inspectCard(int k)
   {
      Card testCard;
      if (k > topCard || k < 0)
      {
         testCard = new Card('y', Card.Suit.spades);
      } else
      {
         testCard = cards[k];
      }
      return testCard;
   }

   public boolean addCard(Card card)
   {
      if (Arrays.asList(cards).indexOf(card) > 0)
      {
         int openElement = 0;
         for (int x = 0; x < cards.length; x++)
         {
            if (cards[x] == null)
            {
               openElement = x;
               break;
            }
         }
         cards[openElement] = card;
         topCard = openElement;
         return true;
      }
      return false;
   }


   // you are looking to remove a specific card from the deck.
   // Put the current top card into its place.
   public boolean removeCard(Card card)
   {
      int index = Arrays.asList(cards).indexOf(card);
      if (index > 0)
      {
         Card topCardCopy = new Card(cards[topCard].getchar(),
               cards[topCard].getSuit());
         Card cardCopy = new Card(cards[index].getchar(),
               cards[index].getSuit());
         cards[index] = topCardCopy;
         cards[topCard] = cardCopy;
         topCard--;
         return true;
      }
      return false;
   }

   // put all of the cards in the deck back
   // into the right order according to their values.
   public void sort()
   {
      Card.arraySort(cards, cards.length);
   }

   //return the number of cards remaining in the deck
   public int getNumCards()
   {
      int numCards = 0;
      for (int x = 0; x < cards.length; x++)
      {
         if (cards[x] != null)
         {
            numCards++;
         }
      }
      return numCards;
   }
}

//class CardGameFramework  ----------------------------------------------------
class CardGameFramework
{
   private static final int MAX_PLAYERS = 50;

   private int numPlayers;
   private int numPacks;            // # standard 52-card packs per deck
   // ignoring jokers or unused cards
   private int numJokersPerPack;    // if 2 per pack & 3 packs per deck, get 6
   private int numUnusedCardsPerPack;  // # cards removed from each pack
   private int numCardsPerHand;        // # cards to deal each player
   private Deck deck;               // holds the initial full deck and gets
   // smaller (usually) during play
   private Hand[] hand;             // one Hand for each player
   private Card[] unusedCardsPerPack;   // an array holding the cards not used
   // in the game.  e.g. pinochle does not
   // use cards 2-8 of any suit

   public CardGameFramework(int numPacks, int numJokersPerPack,
                            int numUnusedCardsPerPack, Card[] unusedCardsPerPack,
                            int numPlayers, int numCardsPerHand)
   {
      int k;

      // filter bad values
      if (numPacks < 1 || numPacks > 6)
         numPacks = 1;
      if (numJokersPerPack < 0 || numJokersPerPack > 4)
         numJokersPerPack = 0;
      if (numUnusedCardsPerPack < 0 || numUnusedCardsPerPack > 50) //  > 1 card
         numUnusedCardsPerPack = 0;
      if (numPlayers < 1 || numPlayers > MAX_PLAYERS)
         numPlayers = 4;
      // one of many ways to assure at least one full deal to all players
      if (numCardsPerHand < 1 ||
            numCardsPerHand > numPacks * (52 - numUnusedCardsPerPack)
                  / numPlayers)
         numCardsPerHand = numPacks * (52 - numUnusedCardsPerPack) / numPlayers;

      // allocate
      this.unusedCardsPerPack = new Card[numUnusedCardsPerPack];
      this.hand = new Hand[numPlayers];
      for (k = 0; k < numPlayers; k++)
         this.hand[k] = new Hand();
      deck = new Deck(numPacks);

      // assign to members
      this.numPacks = numPacks;
      this.numJokersPerPack = numJokersPerPack;
      this.numUnusedCardsPerPack = numUnusedCardsPerPack;
      this.numPlayers = numPlayers;
      this.numCardsPerHand = numCardsPerHand;
      for (k = 0; k < numUnusedCardsPerPack; k++)
         this.unusedCardsPerPack[k] = unusedCardsPerPack[k];

      // prepare deck and shuffle
      newGame();
   }

   // constructor overload/default for game like bridge
   public CardGameFramework()
   {
      this(1, 0, 0, null, 4, 13);
   }

   public Hand getHand(int k)
   {
      // hands start from 0 like arrays

      // on error return automatic empty hand
      if (k < 0 || k >= numPlayers)
         return new Hand();

      return hand[k];
   }

   public Card getCardFromDeck()
   {
      return deck.dealCard();
   }

   public int getNumCardsRemainingInDeck()
   {
      return deck.getNumCards();
   }

   public void newGame()
   {
      int k, j;

      // clear the hands
      for (k = 0; k < numPlayers; k++)
         hand[k].resetHand();

      // restock the deck
      deck.init(numPacks);

      // remove unused cards
      for (k = 0; k < numUnusedCardsPerPack; k++)
         deck.removeCard(unusedCardsPerPack[k]);

      // add jokers
      for (k = 0; k < numPacks; k++)
         for (j = 0; j < numJokersPerPack; j++)
            deck.addCard(new Card('X', Card.Suit.values()[j]));

      // shuffle the cards
      deck.shuffle();
   }

   public boolean deal()
   {
      // returns false if not enough cards, but deals what it can
      int k, j;
      boolean enoughCards;

      // clear all hands
      for (j = 0; j < numPlayers; j++)
         hand[j].resetHand();

      enoughCards = true;
      for (k = 0; k < numCardsPerHand && enoughCards; k++)
      {
         for (j = 0; j < numPlayers; j++)
            if (deck.getNumCards() > 0)
               hand[j].takeCard(deck.dealCard());
            else
            {
               enoughCards = false;
               break;
            }
      }

      return enoughCards;
   }

   void sortHands()
   {
      int k;

      for (k = 0; k < numPlayers; k++)
         hand[k].sort();
   }
}
