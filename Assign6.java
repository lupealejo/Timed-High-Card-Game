

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

