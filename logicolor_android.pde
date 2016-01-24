//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 30, 2015         \\
// ------------------------------------- \\
//                                       \\
//         LogiColour_v2.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

/* ============================ */
/* IMPORTED EXTERNAL LIBRARIES  */
/* ============================ */

// For Sound Implementation
//import processing.sound.*;
import android.view.MotionEvent;

/* ============================ */
/* GLOBAL VARIABLES AND OBJECTS */
/* ============================ */

  // Window Size (in Pixels)
  //int wSize = 800;
  //int hSize = 480;
  // 720P***
  //int wSize = 1280;
  //int hSize = 720;
  // 1080P ***
  int wSize = 1920;
  int hSize = 1080;
  // 1440P ***
  //int hSize = 1440;
  //int wSize = 2560;
  
  float ratio = wSize/hSize;
  
  // Grid Size (in grid cells)
  int gWidth = 30;

  // Grid Node Size (in Pixels)
  int gSize = wSize/43;
  
  int gHeight = 20;

  // Block Bank Size (in Grid Nodes)
  int bbHeight = 0;
  int bbWidth = 11;

// Load Game Fonts
PFont WarningFont;
PFont GameFont;

// Game Sound Files
//SoundFile mainMusic;
//SoundFile blockSound;
//SoundFile wireSound;
//SoundFile connectorSound;
//SoundFile deleteSound;
//SoundFile errorSound;

// Global Objects
Menu        gameMenu;
Menu        tutMenu;
LSelect     levelMenu;
Puzzle      puzzleScreen;
Score       scoreScreen;
Game        game;

// Connector List for Puzzle
ArrayList<Node> connectorList = new ArrayList<Node>();

// Block Selected Flag
BLOCK blockSelected;
boolean isBlockSelected = false;

// Connector Flags
boolean connect_1 = false;
boolean connect_2 = false;

// Placement Flag
boolean reCalc = true;
boolean reCalcWires = true;

// Connector Nodes
Node nConnect_1;
Node nConnect_2;

// Which Puzzle is Selected
int puzzleSelected = -1;

// Global States
final int INIT = 0;
final int MENU = 1;
final int TUTORIAL = 2;
final int LEVEL_SELECT = 3;
final int PUZZLE = 4;
final int SCORE = 5;
final int CLEAR = 6;

/* ============================ */
/* GAME INITIALIZATION AND LOOP */
/* ============================ */

// Initialization Function
void setup()
{
  // Load Font
  WarningFont = loadFont("BitstreamVeraSansMono-Roman-10.vlw");
  GameFont = loadFont("SourceCodePro-Semibold-48.vlw");

  // Load Game Music
  //mainMusic = new SoundFile(this, "game.mp3");
  // Load Sound Effects
  //blockSound = new SoundFile(this, "block.mp3");
  //wireSound = new SoundFile(this, "wire.mp3");
  //connectorSound = new SoundFile(this, "connector.mp3");
  //deleteSound = new SoundFile(this, "delete.ogg");
  //errorSound = new SoundFile(this, "error.ogg");
  
  //     Proper Game Setup      \\
  //----------------------------\\
  // Create Menu Instance
  gameMenu = new Menu(MENU);
  // Creat Tutorial Instance
  tutMenu = new Menu(TUTORIAL);
  // Create Level Select Screen Instance
  levelMenu = new LSelect(10);
  // Create Blank Puzzle Instance
  puzzleScreen = new Puzzle();
  // Create Blank Score Instance
  scoreScreen = new Score();
  // Create New Game Instance
  game = new Game(gameMenu, tutMenu, levelMenu, puzzleScreen, scoreScreen);

  // Set Screen Size
  //size(1280,720);
  orientation(LANDSCAPE);

  // Set Frame Rate
  frameRate(60);
  
  //Loop Music
  //mainMusic.loop();
}

// Game Loop
void draw()
{
  // Run State Machine Controller
  game.StateMachine();
}

/* ============================ */
/* INPUT/OUTPUT EVENT LISTENERS */
/* ============================ */

// Mouse Press Event Listener
void mouseMoved()
{
  // Do Mouse Pressed Stuff
  // in relation to the current
  // state of the State Machine
  switch(game.getState())
  {
    // MENU SCREEN
  case MENU:
    // Play Button
    PVector playSize = gameMenu.playBtn.getBtnSize();
    PVector playPos = gameMenu.playBtn.getBtnPos();
    // Exit Button
    PVector exitSize = gameMenu.exitBtn.getBtnSize();
    PVector exitPos = gameMenu.exitBtn.getBtnPos();
    // Tutorial Button
    PVector tutSize = gameMenu.tutBtn.getBtnSize();
    PVector tutPos = gameMenu.tutBtn.getBtnPos();
    // Play Button Rollover Effect
    if ( (mouseX <= (playPos.x+playSize.x/2)) && (mouseX >= (playPos.x-playSize.x/2)) && (mouseY <= playPos.y+playSize.y/2) && (mouseY >= playPos.y-playSize.y/2) )
    {
      gameMenu.playBtn.setBtnColour(color(00, 88, 176));
    } else
    {
      gameMenu.playBtn.setBtnColour(color(00, 22, 44));
    }
    // Tutorial Button Rollover Effect
    if ( (mouseX <= (tutPos.x+tutSize.x/2)) && (mouseX >= (tutPos.x-tutSize.x/2)) && (mouseY <= tutPos.y+tutSize.y/2) && (mouseY >= tutPos.y-tutSize.y/2) )
    {
      gameMenu.tutBtn.setBtnColour(color(00, 88, 176));
    } else
    {
      gameMenu.tutBtn.setBtnColour(color(00, 22, 44));
    }
    // Exit Button Rollover Effect
    if ( (mouseX <= (exitPos.x+exitSize.x/2)) && (mouseX >= (exitPos.x-exitSize.x/2)) && (mouseY <= exitPos.y+exitSize.y/2) && (mouseY >= exitPos.y-exitSize.y/2) )
    {
      gameMenu.exitBtn.setBtnColour(color(00, 88, 176));
    } else
    {
      gameMenu.exitBtn.setBtnColour(color(00, 22, 44));
    }
    break;
    
      // TUTORIAL SCREEN
  case TUTORIAL:
    // Button Location Information
    PVector tbtnPos;
    PVector tbtnSize;
    // Check for Button Presses
    
      tbtnPos = tutMenu.menuBtn.getBtnPos();
      tbtnSize = tutMenu.menuBtn.getBtnSize();
      // If Button Clicked - Load Puzzle
      if ( (mouseX <= (tbtnPos.x+tbtnSize.x/2)) && (mouseX >= (tbtnPos.x-tbtnSize.x/2)) && (mouseY <= tbtnPos.y+tbtnSize.y/2) && (mouseY >= tbtnPos.y-tbtnSize.y/2) )
      {
      tutMenu.menuBtn.setBtnColour(color(00, 88, 176));
    } else
    {
      tutMenu.menuBtn.setBtnColour(color(00, 22, 44));
    }
    break;


    // LEVEL SELECT SCREEN
  case LEVEL_SELECT:
    // Button Position Information
    PVector btnPos;
    PVector btnSize;
    // Loop through all Buttons
    for (int i = 0; i < levelMenu.getLevels (); i++)
    {
      btnPos = levelMenu.btnList[i].getBtnPos();
      btnSize = levelMenu.btnList[i].getBtnSize();
      // Rollover Effect
      if ( (mouseX <= (btnPos.x+btnSize.x/2)) && (mouseX >= (btnPos.x-btnSize.x/2)) && (mouseY <= btnPos.y+btnSize.y/2) && (mouseY >= btnPos.y-btnSize.y/2) )
      {
        levelMenu.btnList[i].setBtnColour(color(00, 88, 176));
      } else
      {
        levelMenu.btnList[i].setBtnColour(color(00, 22, 44));
      }
    }
    break;

    // PUZZLE SCREEN  
  case PUZZLE:
    // Get Button Properties
    btnPos = game.puzzle.solveBtn.getBtnPos();
    btnSize = game.puzzle.solveBtn.getBtnSize();
    color btnColour = game.puzzle.solveBtn.getColour();

    // Puzzle Mouse Rollover Commands
    if ( (mouseX <= (btnPos.x+btnSize.x/2)) && (mouseX >= (btnPos.x-btnSize.x/2)) && (mouseY <= btnPos.y+btnSize.y/2) && (mouseY >= btnPos.y-btnSize.y/2) )
    {
      if (game.puzzle.solveBtn.isActive())
        game.puzzle.solveBtn.setBtnColour(color(00, 88, 176));
    } else
    {
      if (game.puzzle.solveBtn.isActive())
      {
        game.puzzle.solveBtn.setBtnColour(color(00, 22, 44));
      } else
      {
        game.puzzle.solveBtn.setBtnColour(color(66, 66, 66, 100));
      }
    }
    break;

    // NON-INTERACTIVE SCREENS
  case INIT:
  case CLEAR:
  default:
    // Do Nothing
    break;
  }
}


// Mouse Press Event Listener
void mousePressed()
{
  // Do Mouse Pressed Stuff
  // in relation to the current
  // state of the State Machine
  switch(game.getState())
  {
    // MENU SCREEN
  case MENU:
    // Play Button
    PVector playSize = gameMenu.playBtn.getBtnSize();
    PVector playPos = gameMenu.playBtn.getBtnPos();
    // Tutorial Button
    PVector tutSize = gameMenu.tutBtn.getBtnSize();
    PVector tutPos = gameMenu.tutBtn.getBtnPos();
    // Exit Button
    PVector exitSize = gameMenu.exitBtn.getBtnSize();
    PVector exitPos = gameMenu.exitBtn.getBtnPos();
    // Play Button Pressed
    if ( (mouseX <= (playPos.x+playSize.x/2)) && (mouseX >= (playPos.x-playSize.x/2)) && (mouseY <= playPos.y+playSize.y/2) && (mouseY >= playPos.y-playSize.y/2) )
    {
      //Play Sound
      //blockSound.play();
      // Load Level Select Here
      game.setState(LEVEL_SELECT);
      // Temp: Colour Change
      gameMenu.playBtn.setBtnColour(color(00, 22, 44));
    }
    // Play Button Pressed
    if ( (mouseX <= (tutPos.x+tutSize.x/2)) && (mouseX >= (tutPos.x-tutSize.x/2)) && (mouseY <= tutPos.y+tutSize.y/2) && (mouseY >= tutPos.y-tutSize.y/2) )
    {
      //Play Sound
      //blockSound.play();
      // Load Level Select Here
      game.setState(TUTORIAL);
      // Temp: Colour Change
      gameMenu.tutBtn.setBtnColour(color(00, 22, 44));
    }
    // Exit Button Pressed
    if ( (mouseX <= (exitPos.x+exitSize.x/2)) && (mouseX >= (exitPos.x-exitSize.x/2)) && (mouseY <= exitPos.y+exitSize.y/2) && (mouseY >= exitPos.y-exitSize.y/2) )
    {
      // Exit Game
      //blockSound.play();
      exit();
    }
    break;

    // TUTORIAL SCREEN
  case TUTORIAL:
    // Button Location Information
    PVector tbtnPos;
    PVector tbtnSize;
    // Check for Button Presses
    
      tbtnPos = tutMenu.menuBtn.getBtnPos();
      tbtnSize = tutMenu.menuBtn.getBtnSize();
      // If Button Clicked - Load Puzzle
      if ( (mouseX <= (tbtnPos.x+tbtnSize.x/2)) && (mouseX >= (tbtnPos.x-tbtnSize.x/2)) && (mouseY <= tbtnPos.y+tbtnSize.y/2) && (mouseY >= tbtnPos.y-tbtnSize.y/2) )
      {
        // Change to Puzzle Screen
        game.setState(MENU);
      }
    break;



    // LEVEL SELECT SCREEN
  case LEVEL_SELECT:
    // Button Location Information
    PVector btnPos;
    PVector btnSize;
    // Check for Button Presses
    for (int i = 0; i < levelMenu.getLevels (); i++)
    {
      btnPos = levelMenu.btnList[i].getBtnPos();
      btnSize = levelMenu.btnList[i].getBtnSize();
      // If Button Clicked - Load Puzzle
      if ( (mouseX <= (btnPos.x+btnSize.x/2)) && (mouseX >= (btnPos.x-btnSize.x/2)) && (mouseY <= btnPos.y+btnSize.y/2) && (mouseY >= btnPos.y-btnSize.y/2) )
      {
        // Load Puzzle
        puzzleSelected = i+1;
        game.loadPuzzle(puzzleSelected);
        //blockSound.play();
        // Change to Puzzle Screen
        game.setState(PUZZLE);
      }
    }
    break;

    // PUZZLE SCREEN  
  case PUZZLE:
    // Connector Location Information
    PVector connectPos;
    // Duplicate Flag
    boolean nDuplicate = false;
    // Check for Connector Clicks
    for (int i = 0; i < connectorList.size (); i++)
    {
      connectPos = connectorList.get(i).getPosition();
      // If Connector Clicked - Load Puzzle
      if ( (mouseX <= (connectPos.x + gSize/2)) && (mouseX >= (connectPos.x - gSize/2)) && (mouseY <= (connectPos.y + gSize/2)) && (mouseY >= (connectPos.y - gSize/2)))
      {
        if (!connect_1 && (connectorList.get(i).getColour() != color(255) || connectorList.get(i).isOutput()))
        {
          // Ensure only one wire per connector
          for (int j = 0; j < game.puzzle.wireList.size (); j++)
          {
            if (((game.puzzle.wireList.get(j).getStart() == connectorList.get(i)) || (game.puzzle.wireList.get(j).getEnd() == connectorList.get(i))) && game.puzzle.wireList.get(j).isActive())
            {
              //if (mouseButton == RIGHT)
              //{
                if(mousePressed == true){
                    game.puzzle.wireList.get(j).Deactivate();
                }
              // Set Duplicate Flag and Break;
              nDuplicate = true;
              break;
            }
          }
          if (!nDuplicate)
          {  
            nConnect_1 = connectorList.get(i);
            connect_1 = true;
            //wireSound.play();
          }
          else
          {
            //errorSound.play();
          }
        } else if ((!connect_2 && connect_1) && ((nConnect_1.nPos.x != connectorList.get(i).nPos.x) || (nConnect_1.nPos.y != connectorList.get(i).nPos.y)))
        {
          // If they are the same colour
          if (nConnect_1.getColour() == connectorList.get(i).getColour() || (connectorList.get(i).getColour() == color(255) && !connectorList.get(i).isOutput))
          {
            
            // Ensure only One Connection per Connector
            for (int j = 0; j < game.puzzle.wireList.size (); j++)
            {
              if (((game.puzzle.wireList.get(j).getStart() == connectorList.get(i)) || (game.puzzle.wireList.get(j).getEnd() == connectorList.get(i))) && game.puzzle.wireList.get(j).isActive())
              {
                // Right Click to Remove Wire
                //if (mouseButton == RIGHT)
                //{
                  if (mousePressed == true){
                  game.puzzle.wireList.get(j).Deactivate();
                  }
                // Set Duplicate Flag and Break;
                nDuplicate = true;
                break;
              }
            }
            // If Not a duplicate Place Wire
            if (!nDuplicate)
            {
              // If Blank Connector Change Colour
            if (connectorList.get(i).getColour() == color(255) && !connectorList.get(i).isOutput())
              connectorList.get(i).setColour(nConnect_1.getColour());

              // Associate Connector
              nConnect_2 = connectorList.get(i);
              connect_2 = true;
              // Add Wire to Map 
              Wire new_wire = new Wire(nConnect_1, nConnect_2, game.puzzle.pGrid);
              game.puzzle.addWire(new_wire);
              //wireSound.play();
              // Reset Selectors
              connect_1 = false;
              connect_2 = false;
            }
          }
        } else
        {
          // If Reclicked Unselect Connector
          if (connectorList.get(i) == nConnect_1)
          {
            nConnect_1 = null;
            connect_1 = false;
            //connectorSound.play();
          }
        }
      }
    }

    // Block Placement
    for (int i=0; i < game.puzzle.blockBank.size (); i++)
    {
      // If Clicked on Block Bank Block
      if ( (mouseX >= game.puzzle.blockBank.get(i).bPos.x - (gSize*3)/2) && (mouseX <= game.puzzle.blockBank.get(i).bPos.x + (gSize*3)/2) && (mouseY >= game.puzzle.blockBank.get(i).bPos.y - (gSize*3)/2) && (mouseY <= game.puzzle.blockBank.get(i).bPos.y + (gSize*3)/2) )
      {
        //if (mouseButton == LEFT)
        //{
          if (mousePressed == true) {
          // Figure out Type
          blockSelected = game.puzzle.blockBank.get(i).blockType();
          // Set Flag to True
          isBlockSelected = true;
          }
      }
    }

    // Delete Block with Right Click
    for (int i=0; i < game.puzzle.blockList.size (); i++)
    {
      if ( (mouseX >= game.puzzle.blockList.get(i).bPos.x - (gSize*3)/2) && (mouseX <= game.puzzle.blockList.get(i).bPos.x + (gSize*3)/2) && (mouseY >= game.puzzle.blockList.get(i).bPos.y - (gSize*3)/2) && (mouseY <= game.puzzle.blockList.get(i).bPos.y + (gSize*3)/2) )
      {
        //if (mouseButton == RIGHT)
        //{
        if (mousePressed == true) {
          game.puzzle.blockList.get(i).Deactivate();
        }
      }
    }

    // Place Selected Block ont Grid
    if (blockSelected != null && isBlockSelected)
    {
      // Find Center Node
      Node sNode = game.puzzle.pGrid.getNodeAt(mouseX, mouseY);
      boolean fits = true;

      if ( sNode != null)
      {
        // Check for Boundaries
        for (int i=0; i< sNode.nSiblings.length; i++)
        {
          if (sNode.nSiblings[i].isConnector())
          {
            fits = false;
            break;
          }
        }
        if (fits)
        {  // Add Block to blockList
          game.puzzle.addBlock(new Block(game.puzzle.pGrid, blockSelected, sNode.nPos.x, sNode.nPos.y));
          //blockSound.play();
          // Reset Flag and Block Selection
          isBlockSelected = false;
          blockSelected = null;
        }
        else
        {
          //errorSound.play();
        }
      }
    }

    // Check for Active Button
    if (game.puzzle.solveBtn.isActive)
    {
      if (mouseX <= game.puzzle.solveBtn.getBtnPos().x+game.puzzle.solveBtn.btnWidth/2 && mouseX >= game.puzzle.solveBtn.getBtnPos().x-game.puzzle.solveBtn.btnWidth/2 && mouseY >= game.puzzle.solveBtn.getBtnPos().y-game.puzzle.solveBtn.btnHeight/2 && mouseY <= game.puzzle.solveBtn.getBtnPos().y+game.puzzle.solveBtn.btnHeight/2)
      {
        // Make New Score Screen
        game.score = new Score(game.puzzle.score);
        //Play Sound
        //blockSound.play();
        // Change State to Score Screen
        game.setState(SCORE);
      }
    }
    break;

    // SCORE SCREEN
  case SCORE:
  if(mousePressed == true) {
      game.setState(CLEAR);
    }
    break;
    // NON-INTERACTIVE SCREENS
  case INIT:
  case CLEAR:
  default:
    // Do Nothing
    break;
  }
}

// Keyboard Press Event Listener
void keyPressed()
{
  // Do Keyboard Pressed Stuff
  // in relation to the current
  // state of the State Machine
  switch(game.getState())
  {
    // MENU SCREEN
  case MENU:
    // Menu Key Commands
    break;    
    // LEVEL SELECT SCREEN
  case LEVEL_SELECT:
    // Level Select Key Commands
    break;      
    // PUZZLE SCREEN  
  case PUZZLE:
    // Puzzle Key Commands
    //if (key == 'q')
    if(keyCode == BACK)
    {
      // Go to Clearing Routine
      game.setState(CLEAR);
    }
    break;    
    // NON-INTERACTIVE SCREENS

  case SCORE:
    if (key == 'q')
    {
      game.setState(CLEAR);
    }
    if (key == ENTER)
    {
      game.setState(CLEAR);
    }
    break;

    // Non-interactive
  case INIT:
  case CLEAR:
  default:
    // Do Nothing
    break;
  }
}