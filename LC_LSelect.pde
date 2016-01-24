//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 12, 2015         \\
// ------------------------------------- \\
//                                       \\
//             LC_LSelect.pde            \\
//                                       \\
// ------------------------------------- \\
//=======================================\\


// Level Select Class
class LSelect {
  // Number of Levels
  int numLevels;
  // Button List for Selecting Levels
  Button[] btnList;
  // Default Constructor  
  public LSelect()
  {
    // No Levels
    numLevels = 0;
    // No Buttons
    btnList = null;
  }
  // Normal Constructor
  public LSelect(int num)
  {
    // Initialize Button List
    btnList = new Button[0];
    // Set Number of Levels
    numLevels = num;
    // Button Propertie Variables
    String bLabel;
    int bHt, bWd, x, y;
    color bclr, tclr;
    // Make Buttons for Each Level
    for (int i = 0; i < numLevels; i++)
    {
      // Initialize Button Properties
      bLabel = "Level " + str(i+1);
      bHt = (hSize/((num/2)+4));
      bWd = wSize/5;
      x = ((wSize/3)*(1+(i%2)));
      y = (((bHt*3)/4)*(i-(i%2))+((bHt*3)/2));
      bclr = color(00,22,44);
      tclr = color(255);
      // Append Level Select Buttons to List
      btnList = (Button[])append(this.btnList, new Button(bHt, bWd, x, y, bclr, tclr, bLabel));
    }
  }
  // Display Level Select Screen
  public void Display()
  {
    // Check for Correct BtnList and Levels
    if (btnList == null || numLevels == 0)
    {
      // Make Warning Screen and Proceed no Further
      background(100, 00, 00);
      textFont(WarningFont, 20);
      fill(255);
      textAlign(CENTER);
      text("Error: Levels Failed to Load Properly!\n\nPlease Restart.", (width/2), (height/2));
    } else
    {
      // Make Background and Text
      background(00, 44, 88);
      textFont(GameFont, 30);
      fill(255);
      textAlign(CENTER,CENTER);
      text("Please Select a Level:", (wSize/2), hSize/(this.numLevels+2)-gSize/2);
      // Display All Buttons
      for (int i = 0; i < this.numLevels; i++)
      {
        btnList[i].Display();
      }
    }
  } 
  // Return # of Levels
  public int getLevels()
  {
    return this.numLevels;
  }
}