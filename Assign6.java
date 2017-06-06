/**
 * CST 338 - Fall 2015 Session A
 * Assignment 6
 * Phase I
 *
 * @author Robert Contreras
 * @author Ryan Doherty
 * @author Hyo Lee
 */

public class Assign6
{
   public static void main(String[] args)
   {
       HighCardView theView = new HighCardView();
       HighCardModel theModel = new HighCardModel();
       HighCardController theController = new HighCardController(theView,theModel);
       
       theView.setVisible(true);
   }
}

