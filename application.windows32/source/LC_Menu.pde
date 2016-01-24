//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 30, 2015         \\
// ------------------------------------- \\
//                                       \\
//             LC_Menu.pde               \\
//                                       \\
// ------------------------------------- \\
//=======================================\\


// Menu Class
class Menu 
{
  // Logo Container
  PShape Logo;
  // Menu Buttons
  Button playBtn;
  Button exitBtn;
  Button tutBtn;
  Button menuBtn;
  
  int type = MENU;
  
  // Default Constructor
  public Menu(int type)
  {
	// Main Menu Screen
    if (type == MENU)
    {
      this.type = type;
      // Load Logo
      Logo = loadShape("LogiColour_Logo.svg");
      // Create Buttons
      playBtn = new Button(75, 200, wSize/2, (hSize*5)/9, color(00, 22, 44), color(255), "Play Game");
      exitBtn = new Button(75, 200, wSize/2, (hSize*5)/9 + 2*gSize + 2*75, color(00, 22, 44), color(255), "Exit Game");
      tutBtn = new Button(75, 200, wSize/2, (hSize*5)/9 +gSize + 75, color(00, 22, 44), color(255), "Tutorial");
    } 
	// Tutorial Menu Screen	
	else if (type == TUTORIAL)
    {
      this.type = type;
      Logo = loadShape("ColourWheel.svg");
      menuBtn = new Button(75, 200, wSize/2, (hSize*9)/10, color(00, 22, 44), color(255), "Go Back");
    }
  }
  // Display Function for Menu
  public void Display()
  {
    if (type == MENU)
    {
      // Set Background Colour
      background(00, 44, 88);
      //fill(44, 88, 132, 100);
      fill(255);
      stroke(255);
      strokeWeight(2);
      rectMode(CENTER);
      rect(wSize/2, hSize/4, Logo.width, Logo.height);
      strokeWeight(1);
      // Draw Logo
      shapeMode(CENTER);
      shape(Logo, wSize/2, hSize/4);
      // Draw Menu Buttons
      playBtn.Display();
      tutBtn.Display();
      exitBtn.Display();
      // List Copyright at Bottom
      textFont(GameFont, 15);
      fill(255);
      textAlign(CENTER, CENTER);
      text("LogiColour - Kyle Derby MacInnis, 2015", (wSize/2), (hSize-gSize/2));
      // Instruction Text
      textAlign(LEFT, CENTER);
      text("INSTRUCTIONS:\n\n 1 - Click \'Play Game\'\n 2 - Select Level\n 3.a - Left Click to Select\n 3.b - Right Click to Remove\n 3.c - Press 'q' to Exit Puzzle\n 3.d - Press Solve Button when Ready\n", (wSize*5)/7, (hSize*7)/9);
      text("OBJECTIVE:\n\nConnect the Coloured Nodes to\neach other using the Provided \nLogiColour Logic Blocks.", (wSize*5)/7, (hSize*5)/9);
    } else if (type == TUTORIAL)
    {
      // Set Background Colour
      background(00, 44, 88);
      fill(44, 88, 132, 100);
      stroke(255);
      strokeWeight(1);
      // Draw Menu Button
      menuBtn.Display();
      // Instruction Text
      textFont(GameFont, 15);
      fill(255);
      textAlign(CENTER, CENTER);
      // PURPOSE STATEMENT
      text("The purpose of the game is to connect all connection points(connectors) on the\ngrid by forming a closed system(net) such that all connectors are linked\nto one another whilst trying to minimize the cost.", wSize/2, (hSize)/10);
      // PRIORITY CUE OF COLOURS
      text("Colours have a Priority Queue for Logical Operations. A colour that is Clockwise\nrelative to another colour(in the Colour Wheel) takes precedence in operations.", wSize/2, (hSize*4)/20);
      // EXPLANATION OF BLOCKS
      text("Mixer: Adds Colours together to form other colours based on priority. Cost = 3\n\nDemixer: Subtracts Colours based on priority. Cost = 3\n\nInverter: Inverts Colours to the opposing colour. Cost = 2\n\nDeconstructor: Breaks colours down into constituent colours based on Mixer. Cost = 4\n\nSplitter: Duplicates colour into two new connectors. Cost = 1", wSize/2, (hSize*2)/5);
      // TITLES
      textFont(GameFont, 20);
      fill(255,00,00);
      text("PURPOSE:", wSize/2, hSize/20);
      text("COLOUR PRIORITY:", wSize/2, (hSize)/5-gSize);
      text("LOGIC BLOCK TYPES:", wSize/2, (hSize*3)/10-gSize);
      // COLOUR WHEEL IMAGE
      if(Logo != null)
      {
      shapeMode(CENTER);
      shape(Logo, wSize/2, (hSize*3)/5+2*gSize-10, 110*2, 96*2);
      }
    }
  }
}