package processing.test.logicolor_android;

import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import android.view.MotionEvent; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class logicolor_android extends PApplet {

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


/* ============================ */
/* GLOBAL VARIABLES AND OBJECTS */
/* ============================ */

  // Window Size (in Pixels)
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
public void setup()
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
public void draw()
{
  // Run State Machine Controller
  game.StateMachine();
}

/* ============================ */
/* INPUT/OUTPUT EVENT LISTENERS */
/* ============================ */

// Mouse Press Event Listener
public void mouseMoved()
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
    int btnColour = game.puzzle.solveBtn.getColour();

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
public void mousePressed()
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
public void keyPressed()
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
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 30, 2015         \\
// ------------------------------------- \\
//                                       \\
//             LC_Block.pde              \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

// Block Class Declaration
class Block 
{
  // Identifier Object
  BLOCK blockType;
  // Position
  PVector bPos;
  // List of Connectors
  ArrayList<Node> conList;
  // Parent Grid
  Grid pGrid;
  // Block Bank Flag
  boolean isBlockBank;
  // Activation Flag
  boolean isActive = false;
  boolean isGenerated = false;

  // Normal Constructor
  public Block( Grid grid, BLOCK blkType, float x, float y )
  {
    // Set Block Type
    this.blockType = blkType;
    // Set Parent Grid
    this.pGrid = grid;
    // Set Position
    this.bPos = new PVector(x, y);
    // Generate Empty Connection List
    this.conList = new ArrayList<Node>();
    // Initialize Connectors and Walls
    this.InitBlock();
    // Not Block Bank
    this.isBlockBank = false;
  }

  // Block Bank Constructor
  public Block(BLOCK blkType, float x, float y)
  {
    // Set Block Type
    this.blockType = blkType;
    // Set Position
    this.bPos = new PVector(x, y);
    // Creat Null Parent Grid
    this.pGrid = null;
    // Set as Block Bank
    this.isBlockBank = true;
    // Activate Block
    this.isActive = true;
  }

  // Deactivate Block
  public void Deactivate()
  {
    if (this.isActive && this.isGenerated)
    {
      this.isActive = false;
      this.isGenerated = false;
      //deleteSound.play();
      for (int i= 0; i<25; i++)
      {
        Node bNode = this.pGrid.getNodeAt((int)((this.bPos.x-(gSize*2)) + gSize*(i%5)), (int)((this.bPos.y-(gSize*2))+(int)(i/5)*gSize));
        if (bNode != null)
        {
          bNode.setWall(false);
          bNode.setConnector(false);
          bNode.resetColour();
          bNode.resetDistance();
        }
        for ( int j = 0; j<connectorList.size (); j++)
        {
          if (bNode == connectorList.get(j))
          {
            if (game.puzzle.wireList.size() > 0)
            {
              for (int k = 0; k<game.puzzle.wireList.size (); k++)
              {
                if ((bNode == game.puzzle.wireList.get(k).startNode ) || ( bNode == game.puzzle.wireList.get(k).endNode ) )
                  game.puzzle.wireList.get(k).Deactivate();
              }
            }
            connectorList.remove(j);
          }
        }
        reCalc = true;
        reCalcWires = true;
      }
    }
  }

  // Set up Connectors
  public void InitBlock()
  {
    // Create Map for Blocks
    int[] blockMap = new int[0];

    // Check for BlockBank
    if (!this.isBlockBank())
    {
      // Initialize Connector Nodes in Grid
      switch(blockType)
      {
        // INVERTER
      case INVERTER:
        // Inverter Block Map for Path Finding Purposes
        int[] invBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 0, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, invBlockMap[i]);
        }

        // Using Center Node as Placed (Array Node 13)
        // Locate Nodes for Connectors
        // and then Adjust them to be Connectors
        // Additionally, Convert all Block Nodes to
        // Wall Nodes for Path Routing Purposes
        break;

        // MIXER
      case MIXER:
        // Mixer Block Map
        int[] mxBlockMap = {
          0, 0, 0, 0, 0, 
          2, 1, 1, 1, 0, 
          0, 1, 1, 1, 2, 
          2, 1, 1, 1, 0, 
          0, 0, 0, 0, 0
        };

        for (int i = 0; i<25; i++)
        {
          blockMap = (int[])append(blockMap, mxBlockMap[i]);
        }
        break;

        // DEMIXER
      case DEMIXER:
        // Demixer Block Map
        int[] dmxBlockMap = {
          0, 0, 0, 0, 0, 
          2, 1, 1, 1, 0, 
          0, 1, 1, 1, 2, 
          2, 1, 1, 1, 0, 
          0, 0, 0, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, dmxBlockMap[i]);
        }
        break;

        // DECONSTRUCTOR
      case DECONSTRUCTOR:
        // Deconstructor Block Map
        int[] dcBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 2, 
          2, 1, 1, 1, 0, 
          0, 1, 1, 1, 2, 
          0, 0, 0, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, dcBlockMap[i]);
        }
        break;

      // SPLITTER
      case SPLITTER:
        int[] spltBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 2, 
          2, 1, 1, 1, 0, 
          0, 1, 1, 1, 2, 
          0, 0, 0, 0, 0
        };
        for ( int i = 0; i< 25; i++)
        {
          blockMap = (int[])append(blockMap, spltBlockMap[i]);
        }
        break;

        // CCW_GATE
      case CCW_GATE:
        // CCW_Gate Block Map
        int[] ccwgBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 2, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, ccwgBlockMap[i]);
        }
        break;

        // CW_GATE  
      case CW_GATE:
        // CW_Gate Block Map
        int[] cwgBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 2, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, cwgBlockMap[i]);
        }
        break;

        // INV_GATE  
      case INV_GATE:
        // Inverted Gate Block Map
        int[] invgBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 2, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, invgBlockMap[i]);
        }
        break;

        // CCW_SIPHON  
      case CCW_SIPHON:
        // CCW_Siphon Block Map
        int[] ccwsBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 2, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, ccwsBlockMap[i]);
        }
        break;

        // CW_SIPHON  
      case CW_SIPHON:
        // CW_Siphon Block Map
        int[] cwsBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 2, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, cwsBlockMap[i]);
        }
        break;

        // TELEPORTER TX  
      case TELEPORTER_TX:
        // Teleporter Transmitter Block Map
        int[] txBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          0, 1, 1, 1, 2, 
          0, 1, 1, 1, 0, 
          0, 0, 0, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, txBlockMap[i]);
        }
        break;

        // TELEPORTER RX  
      case TELEPORTER_RX:
        // Teleporter Receiver Block Map
        int[] rxBlockMap = {
          0, 0, 0, 0, 0, 
          0, 1, 1, 1, 0, 
          2, 1, 1, 1, 0, 
          0, 1, 1, 1, 0, 
          0, 0, 0, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap =(int[])append(blockMap, rxBlockMap[i]);
        }
        break;

        // ERROR  
      default:
        // Empty Block Map
        int[] errBlockMap = {
          0, 0, 0, 0, 0, 
          0, 0, 0, 0, 0, 
          0, 0, 0, 0, 0, 
          0, 0, 0, 0, 0, 
          0, 0, 0, 0, 0
        };
        for (int i = 0; i<25; i++)
        {
          blockMap = (int[])append(blockMap, errBlockMap[i]);
        }
        break;
      }

      // Problem Checker
      boolean problem = false;

      // Edit Grid Node List for Proper Path Finding
      for (int i=0; i<25; i++)
      {
        // Find Node at Position
        Node bNode = this.pGrid.getNodeAt((int)((this.bPos.x-(gSize*2)) + gSize*(i%5)), (int)((this.bPos.y-(gSize*2))+(int)(i/5)*gSize));
        if (bNode == null)
        {
          problem = true;
        } else if (bNode.isWall())
        {
          problem = true;
        } else if (bNode.isConnector())
        {
          problem = true;
        }
      }

      // If no problems were found
      if (!problem)
      {
        // Loop Through and Calculate Node Types
        for (int i= 0; i<25; i++)
        {
          Node bNode = this.pGrid.getNodeAt((int)((this.bPos.x-(gSize*2)) + gSize*(i%5)), (int)((this.bPos.y-(gSize*2))+(int)(i/5)*gSize));

          // If Empty do Nothing
          if (blockMap[i] == 0)
          {
            // Do Nothing
          }
          // if Wall Set to Wall
          else if (blockMap[i] == 1)
          {
            bNode.setWall(true);
            // Recalculate Wire Placement
            reCalc = true;
            reCalcWires = true;
          }
          // If connector 
          else if (blockMap[i] == 2)
          {
            bNode.setConnector(true);
            //bNode.setColour(color(00, 00, 00));
            bNode.setColour(color(255));
            connectorList.add(bNode);
            this.conList.add(bNode);
            // Recalculate Wire Placement
            reCalc = true;
            reCalcWires = true;
          }
          this.isActive = true;
          this.isGenerated = true;
        }
      } else
      {
        this.isActive = false;
        this.isGenerated = false;
        this.Deactivate();
      }
    }
  }

  // Calculate Logic Operations
  public void Calculate()
  {

    if (this.isActive())
    {
      // Inputs/Outputs
      Node A;
      Node B;
      Node C;

      // Perform Logic Calculations
      switch(this.blockType)
      {
        // Inverter
      case INVERTER:
        if (this.conList != null && this.conList.size() == 2)
        {
          // Setup Node A
          A = this.conList.get(0);
          // Setup Node B
          B = this.conList.get(1);
          // Setup Node C (Not Used)
          C = null;

          // Setup Inputs
          A.setOutput(false);
          // Setup Outputs
          B.setOutput(true);

          int cnt = 0;
          // Check for Disconnections
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == A || game.puzzle.wireList.get(i).getEnd() == A)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cnt++;
            }
          }
          if (cnt != 1)
            //A.setColour(color(00));
            A.setColour(color(255));

          // INVERTER Truth Table
           if ( A.getColour() == color(00) )
           B.setColour(color(00));
           else if ( A.getColour() == color(255, 00, 00))
           B.setColour(color(00, 255, 255));
           else if ( A.getColour() == color(00, 255, 00))
           B.setColour(color(255, 00, 255));
           else if ( A.getColour() == color(00, 00, 255))
           B.setColour(color(255, 255, 00));
           else if ( A.getColour() == color(255, 255, 255) )
           B.setColour(color(255, 255, 255));
           else if ( A.getColour() == color(255, 255, 00))
           B.setColour(color(00, 00, 255));
           else if ( A.getColour() == color(00, 255, 255))
           B.setColour(color(255, 00, 00));
           else if ( A.getColour() == color(255, 00, 255))
           B.setColour(color(00, 255, 00));
        }     
        break;

        // MIXER BLOCK
      case MIXER:
        if (this.conList != null && this.conList.size() == 3)
        {
          // Setup Node A
          A = this.conList.get(0);
          // Setup Node B
          B = this.conList.get(2);
          // Setup Node C 
          C = this.conList.get(1);

          // Setup Inputs
          A.setOutput(false);
          B.setOutput(false);
          // Setup Output
          C.setOutput(true);

          // Connection Counter
          int cntA = 0;
          int cntB = 0;

          // Look for Connections to A
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == A || game.puzzle.wireList.get(i).getEnd() == A)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cntA++;
            }
          }
          if (cntA != 1)
          {
            A.setColour(color(255));
            //A.setColour(color(00));
          }
          // Look for Connection to B
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == B || game.puzzle.wireList.get(i).getEnd() == B)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cntB++;
            }
          }
          if (cntB != 1)
          {
            //B.setColour(color(00));
            B.setColour(color(255));
          }

          // MIXER Truth Table
          // ERROR COLOUR
          if ( A.getColour() == color(00) || B.getColour() == color(00) )
            C.setColour(color(00));

          // RED + X
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 00, 00))
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(00, 255, 00))
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(00, 00, 255))
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(00, 255, 255))
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 255, 00))
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 00, 255))
            C.setColour(color(255, 00, 00));   
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 255, 255))
            C.setColour(color(255, 00, 00));

          // GREEN + X
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(00, 255, 00))
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 00, 00))
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(00, 00, 255))
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 255, 255))
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 255, 00))
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(00, 255, 255))
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 00, 255))
            C.setColour(color(255, 255, 255));

          // BLUE + X
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(00, 00, 255))
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 00, 00))
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(00, 255, 00))
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 255, 00))
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 00, 255))
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(00, 255, 255))
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 255, 255))
            C.setColour(color(00, 00, 255));

          // WHITE + X
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(00, 00, 255));

          // YELLOW + X
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(255, 255, 00));

          // CYAN + X
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(00, 255, 255));

          // MAGENTA + X
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(255, 00, 255));
        }
        break;
	
		// DEMIXER
      case DEMIXER:
        if (this.conList != null && this.conList.size() == 3)
        {
          // Setup Node A
          A = this.conList.get(0);
          // Setup Node B
          B = this.conList.get(2);
          // Setup Node C 
          C = this.conList.get(1);

          // Setup Inputs
          A.setOutput(false);
          B.setOutput(false);
          // Setup Output
          C.setOutput(true);

          // Connection Counter
          int cntA = 0;
          int cntB = 0;

          // Look for Connections to A
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == A || game.puzzle.wireList.get(i).getEnd() == A)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cntA++;
            }
          }
          if (cntA != 1)
          {
            //A.setColour(color(00));
            A.setColour(color(255));
          }
          // Look for Connection to B
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == B || game.puzzle.wireList.get(i).getEnd() == B)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cntB++;
            }
          }
          if (cntB != 1)
          {
            //B.setColour(color(00));
            B.setColour(color(255));
          }

          // DEMIXER Truth Table
          // ERROR STUFF
          if ( A.getColour() == color(00) || B.getColour() == color(00) )
            C.setColour(color(00));

          // RED - X
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 00, 00))
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(00, 255, 00))
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(00, 00, 255))
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(00, 255, 255))
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 255, 00))
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 00, 255))
            C.setColour(color(255, 255, 00));   
          else if ( A.getColour() == color(255, 00, 00) && B.getColour() == color(255, 255, 255))
            C.setColour(color(255, 00, 00));

          // GREEN - X
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(00, 255, 00))
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 00, 00))
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(00, 00, 255))
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 255, 255))
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 255, 00))
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(00, 255, 255))
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(00, 255, 00) && B.getColour() == color(255, 00, 255))
            C.setColour(color(00, 255, 00));

          // BLUE - X
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(00, 00, 255))
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 00, 00))
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(00, 255, 00))
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 255, 00))
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 00, 255))
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(00, 255, 255))
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(00, 00, 255) && B.getColour() == color(255, 255, 255))
            C.setColour(color(00, 00, 255));

          // WHITE - X
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 255, 255) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(255, 255, 00));

          // YELLOW - X
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(255, 255, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 255, 00) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(00, 255, 00));

          // CYAN - X
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(00, 255, 00));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(00, 255, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(255, 255, 255));
          else if ( A.getColour() == color(00, 255, 255) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(00, 255, 255));

          // MAGENTA - X
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 255, 255) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 00, 00) )
            C.setColour(color(00, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(00, 255, 00) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(00, 00, 255) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 255, 00) )
            C.setColour(color(255, 00, 00));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(00, 255, 255) )
            C.setColour(color(255, 00, 255));
          else if ( A.getColour() == color(255, 00, 255) && B.getColour() == color(255, 00, 255) )
            C.setColour(color(255, 255, 255));
        }
        break;

      case DECONSTRUCTOR:
        if (this.conList != null && this.conList.size() == 3)
        {
          // Setup Node A (Input)
          A = this.conList.get(1);
          // Setup Node B
          B = this.conList.get(0);
          // Setup Node C`
          C = this.conList.get(2);

          // Setup Inputs
          A.setOutput(false);
          // Setup Outputs
          B.setOutput(true);
          C.setOutput(true);

          int cnt = 0;
          // Check for Disconnections
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == A || game.puzzle.wireList.get(i).getEnd() == A)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cnt++;
            }
          }
          if (cnt != 1)
          {
            //A.setColour(color(00));
            A.setColour(color(255));
          } 

          // DECONSTRUCTOR Truth Table
          if ( A.getColour() == color(00) )
          {
            B.setColour(color(00));
            C.setColour(color(00));
          } else if ( A.getColour() == color(255, 00, 00))
          {
            B.setColour(color(255, 00, 255));
            C.setColour(color(255, 255, 00));
          } else if ( A.getColour() == color(00, 255, 00))
          {
            B.setColour(color(255, 255, 00));
            C.setColour(color(00, 255, 255));
          } else if ( A.getColour() == color(00, 00, 255))
          {
            B.setColour(color(00, 255, 255));
            C.setColour(color(255, 00, 255));
          } else if ( A.getColour() == color(255, 255, 255) )
          {
            B.setColour(color(255, 255, 255));
            C.setColour(color(255, 255, 255));
          } else if ( A.getColour() == color(255, 255, 00))
          {
            B.setColour(color(255, 00, 00));
            C.setColour(color(00, 255, 00));
          } else if ( A.getColour() == color(00, 255, 255))
          {
            B.setColour(color(00, 255, 00));
            C.setColour(color(00, 00, 255));
          } else if ( A.getColour() == color(255, 00, 255))
          {
            B.setColour(color(00, 00, 255));
            C.setColour(color(255, 00, 00));
          }
        }
        break;

      case SPLITTER:
        if (this.conList != null && this.conList.size() == 3)
        {
          // Setup Node A (Input)
          A = this.conList.get(1);
          // Setup Node B
          B = this.conList.get(0);
          // Setup Node C`
          C = this.conList.get(2);

          // Setup Inputs
          A.setOutput(false);
          // Setup Outputs
          B.setOutput(true);
          C.setOutput(true);

          int cnt = 0;
          // Check for Disconnections
          for (int i=0; i<game.puzzle.wireList.size (); i++)
          {
            if (game.puzzle.wireList.get(i).getStart() == A || game.puzzle.wireList.get(i).getEnd() == A)
            {
              if (game.puzzle.wireList.get(i).isActive())
                cnt++;
            }
          }
          if (cnt != 1)
          {
            //A.setColour(color(00));
            A.setColour(color(255));
          } 

          // SPLITTER Truth Table
          if ( A.getColour() == color(00) )
          {
            B.setColour(color(00));
            C.setColour(color(00));
          } else if ( A.getColour() == color(255, 00, 00))
          {
            B.setColour(color(255, 00, 00));
            C.setColour(color(255, 00, 00));
          } else if ( A.getColour() == color(00, 255, 00))
          {
            B.setColour(color(00, 255, 00));
            C.setColour(color(00, 255, 00));
          } else if ( A.getColour() == color(00, 00, 255))
          {
            B.setColour(color(00, 00, 255));
            C.setColour(color(00, 00, 255));
          } else if ( A.getColour() == color(255, 255, 255) )
          {
            B.setColour(color(255, 255, 255));
            C.setColour(color(255, 255, 255));
          } else if ( A.getColour() == color(255, 255, 00))
          {
            B.setColour(color(255, 255, 00));
            C.setColour(color(255, 255, 00));
          } else if ( A.getColour() == color(00, 255, 255))
          {
            B.setColour(color(00, 255, 255));
            C.setColour(color(00, 255, 255));
          } else if ( A.getColour() == color(255, 00, 255))
          {
            B.setColour(color(255, 00, 255));
            C.setColour(color(255, 00, 255));
          }
        }
        break;

      // TO BE IMPLEMENTED IN FUTURE
      case CCW_GATE:
      case CW_GATE:
      case INV_GATE:
      case CCW_SIPHON:
      case CW_SIPHON:
      case TELEPORTER_TX:
      case TELEPORTER_RX:
      default:

        break;
      }
    }
    for (int i =0; i< this.conList.size (); i++)
    {
      boolean blank = false;

      // if Blank Connection
      if (this.conList.get(i).getColour() == color(00))
      {
        for (int j =0; j< game.puzzle.wireList.size (); j++)
        {
          // If Wire Still Connected - Remove
          if (game.puzzle.wireList.get(j).startNode == this.conList.get(i) || game.puzzle.wireList.get(j).endNode == this.conList.get(i))
          {
            game.puzzle.wireList.get(j).Deactivate();
          }
        }
      }
    }
  }

  // Return Status of Block Bank
  public boolean isBlockBank()
  {
    return this.isBlockBank;
  }

  // Return BLOCK type Specification
  public BLOCK blockType()
  {
    return this.blockType;
  }

  // Return Active Status
  public boolean isActive()
  {
    return this.isActive;
  }

  // Display Block to Scren
  public void Display()
  {
    if (this.isActive())
    {
      // Display Appropriate Block on Screen
      switch(blockType)
      {
        // INVERTER
      case INVERTER:
        if (this.isBlockBank() || this.conList.size() == 2)
        {
          rectMode(CENTER);
          fill(88, 88, 88);
          strokeWeight(2);
          stroke(255);
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          line(this.bPos.x-(gSize*3)/2, this.bPos.y, this.bPos.x+(gSize*3)/2, this.bPos.y);
          line(this.bPos.x-(gSize/2)+(gSize/3), this.bPos.y-(gSize), this.bPos.x+(gSize/2)+(gSize/3), this.bPos.y+(gSize));
          line(this.bPos.x-(gSize/2)-(gSize/3), this.bPos.y-(gSize), this.bPos.x+(gSize/2)-(gSize/3), this.bPos.y+(gSize));
          stroke(255);
          noFill();
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(1);
          // Draw Blocks using 9 Squares
          // These do not include the connectors
          // This is based on where it is placed
          // Using the nodeList
        }
        break;

        // MIXER
      case MIXER:
        if (this.isBlockBank() || this.conList.size() == 3)
        {
          rectMode(CENTER);
          stroke(255);
          fill(88, 88, 88);
          strokeWeight(2);
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          line(this.bPos.x+(gSize*3)/2, this.bPos.y, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x, this.bPos.y+gSize);
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x-(gSize*3)/2, this.bPos.y-gSize);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x-(gSize*3)/2, this.bPos.y+gSize);
          fill(200);
          ellipseMode(CENTER);
          ellipse(this.bPos.x, this.bPos.y, gSize/2, gSize/2);
          stroke(255);
          noFill();
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(1);
        } 
        break;

        // DEMIXER
      case DEMIXER:
        if (this.isBlockBank() || this.conList.size() == 3)
        {
          rectMode(CENTER);
          fill(88, 88, 88);
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(2);
          line(this.bPos.x+(gSize*3)/2, this.bPos.y, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x-(gSize*3)/2, this.bPos.y-gSize);
          stroke(55);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x-(gSize*3)/2, this.bPos.y+gSize);
          fill(200);
          ellipseMode(CENTER);
          ellipse(this.bPos.x, this.bPos.y, gSize/2, gSize/2);
          stroke(255);
          noFill();
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(1);
        }
        break;

        // DECONSTRUCTOR
      case DECONSTRUCTOR:
        if (this.isBlockBank() || this.conList.size() == 3)
        {
          rectMode(CENTER);
          fill(88, 88, 88);
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(2);
          // Main Line
          line(this.bPos.x-(gSize*3)/2, this.bPos.y, this.bPos.x, this.bPos.y);
          // Up Line
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x+(gSize*3)/2, this.bPos.y-gSize);
          // Down Line
          stroke(55);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x+(gSize*3)/2, this.bPos.y+gSize);
          // Center Circle
          fill(200);
          ellipseMode(CENTER);
          ellipse(this.bPos.x, this.bPos.y, gSize/2, gSize/2);
          // Restroke Boundary
          stroke(255);
          noFill();
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(1);
        }
        break;
        
        // SPLITTER
      case SPLITTER:
      if (this.isBlockBank() || this.conList.size() == 3)
        {
          rectMode(CENTER);
          fill(88, 88, 88);
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(2);
          // Main Line
          line(this.bPos.x-(gSize*3)/2, this.bPos.y, this.bPos.x, this.bPos.y);
          // Up Line
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y-gSize, this.bPos.x+(gSize*3)/2, this.bPos.y-gSize);
          // Down Line
          stroke(255);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x, this.bPos.y);
          line(this.bPos.x, this.bPos.y+gSize, this.bPos.x+(gSize*3)/2, this.bPos.y+gSize);
          // Restroke Boundary
          stroke(255);
          noFill();
          rect(this.bPos.x, this.bPos.y, 3*gSize, 3*gSize);
          strokeWeight(1);
        }

        // CCW_GATE
      case CCW_GATE:
        break;

        // CW_GATE  
      case CW_GATE:
        break;

        // INV_GATE  
      case INV_GATE:
        break;

        // CCW_SIPHON  
      case CCW_SIPHON:
        break;

        // CW_SIPHON  
      case CW_SIPHON:
        break;

        // TELEPORTER TX  
      case TELEPORTER_TX:
        break;

        // TELEPORTER RX  
      case TELEPORTER_RX:
        break;

        // ERROR  
      default:
        break;
      }
    }
  }
}
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 12, 2015         \\
// ------------------------------------- \\
//                                       \\
//             LC_Button.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

// Menu Button Class
class Button 
{
  // Dimensions
  int btnHeight;
  int btnWidth;
  // Position
  PVector btnPos;
  // Colour
  int btnColour;
  int txtColour;
  // Text
  String btnLabel;
  // Activation Flag
  boolean isActive;

  // Default Constructor
  public Button()
  {
    this.btnHeight = 50;
    this.btnWidth = 100;
    this.btnPos = new PVector(wSize/2, hSize - hSize/3);
    this.btnColour = color(100);
    this.txtColour = color(30, 00, 00);
    this.btnLabel = "Button";
    this.isActive = true;
  }

  // Regular Contructor
  public Button(int ht, int wd, int x, int y, int bclr, int tclr, String lbl)
  {
    this.btnHeight = ht;
    this.btnWidth = wd;
    this.btnPos = new PVector(x, y);
    this.btnColour = bclr;
    this.txtColour = tclr;
    this.btnLabel = lbl;
    this.isActive = true;
  }

  // Deactivate Button
  public void Deactivate()
  {
    this.isActive = false;
  }

  // Activate Button
  public void Activate()
  {
    this.isActive = true;
  }

  // Main Button Display Function
  public void Display()
  {
    if (this.isActive())
    {
      // Draw Rectangle
      fill(this.btnColour);
      stroke(0);
      rectMode(CENTER);
      rect(this.btnPos.x, this.btnPos.y, this.btnWidth, this.btnHeight, 10);
      // Draw Text;
      textFont(GameFont, 20);
      fill(this.txtColour);
      textAlign(CENTER, CENTER);
      text(this.btnLabel, this.btnPos.x, this.btnPos.y);
    } else
    {
      // Draw Rectangle
      fill(66, 66, 66, 100);
      stroke(0);
      rectMode(CENTER);
      rect(this.btnPos.x, this.btnPos.y, this.btnWidth, this.btnHeight, 10);
      // Draw Text;
      textFont(GameFont, 20);
      fill(this.txtColour);
      textAlign(CENTER, CENTER);
      text(this.btnLabel, this.btnPos.x, this.btnPos.y);
    }
  }

  // Check Status of Button
  public boolean isActive()
  {
    return this.isActive;
  }

  // Button Colour Change Method (For Rollover)
  public void setBtnColour(int clr)
  {
    this.btnColour = clr;
  }

  // Text Colour Change Method (For Rollover)
  public void setTxtColour(int clr)
  {
    this.txtColour = clr;
  }

  // Label Change Method (For Rollover)
  public void setBtnLabel(String text)
  {
    this.btnLabel = text;
  }

  // Return Button Position Vector
  public PVector getBtnPos()
  {
    return this.btnPos;
  }

  // Return Button Size Vector
  public PVector getBtnSize()
  {
    PVector size = new PVector(this.btnWidth, this.btnHeight);
    return size;
  }

  // Return Button Colour
  public int getColour()
  {
    return this.btnColour;
  }
}
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 30, 2015         \\
// ------------------------------------- \\
//                                       \\
//               LC_Game.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\


// GAME CLASS DECLARATION
class Game 
{
  //Frame Counter
  int cnt;  
  // Internal Pointers to Global Objects
  Menu menu;
  Menu tutorial;
  LSelect levels;
  Puzzle puzzle;
  Score score;
  // State Machine - State Declaration
  int cur_State;
  int next_State;
  // Game Loader Flag
  boolean gameLoaded = false;
  // Timer for Score Screen
  int count = 0;
  // Default Constructor  
  public Game()
  {
    // Null Objects
    menu = null;
    tutorial = null;
    levels = null;
    puzzle = null;
    score = null;
    // Initialize State Machine
    cur_State = INIT;
    next_State = INIT;
    // Frame counter
    cnt = 0;
  }
  // Main Constructor
  public Game(Menu gmenu, Menu gtut, LSelect glevels, Puzzle gpuzzle, Score gscore)
  {
    // Associate Objects
    this.menu = gmenu;
    this.tutorial = gtut;
    this.levels = glevels;
    this.puzzle = gpuzzle;
    this.score = gscore; 
    // Initialize State Machine
    cur_State = INIT;
    next_State = INIT;
    // Frame Counter
    cnt = 0;
  }
  // Load Puzzle
  public void loadPuzzle(int diff)
  {
    this.puzzle = new Puzzle(diff);
  }
  // Return Current State of State Machine
  public int getState()
  {
    return this.cur_State;
  }
  // Set State of State Machine
  public void setState(int gState)
  {
    this.next_State = gState;
  } 
  // State Machine Controller
  public void StateMachine()
  {
    switch(cur_State)
    {
      // INITIALIZATION STATE
    case INIT:
      // Make a loading screen for 3 seconds
      if ( (cnt >= 60) && gameLoaded) 
      {
        game.setState(MENU);
      }
      // Error Checking for Proper Game Object Creation
      else if ( (menu == null) || (levels == null) || (puzzle == null) || (score == null) )
      {
        // Make Warning Screen and Proceed no Further
        background(100, 00, 00);
        textFont(WarningFont, 20);
        fill(0xffaaaaaa);
        textAlign(CENTER);
        //mainMusic.loop();
        text("Error: Game Failed to Load Properly!\n\nPlease Restart.", (wSize/2), (hSize/2));
      }
      // Error Checking for Time Out
      else if ( (cnt >= 300) && !gameLoaded)
      {
        // Make Warning Screen and Proceed no Further
        background(100, 00, 00);
        textFont(WarningFont, 20);
        fill(0xffaaaaaa);
        textAlign(CENTER);
        //mainMusic.loop();
        text("Error: Game Loading Timed Out!\n\nPlease Restart.", (wSize/2), (hSize/2));
      }   
      // Load Game Files 
      else
      {
        // Increment Count
        cnt++;
        // Check for Loading Complete
        if (!this.gameLoaded)
        {
          // Load Files Here --------->

          // Once Loaded - Set Flag
          this.gameLoaded = true;
        }
        // Loading Screen Animation
        background(00,44,88);
        if (cnt%3==0)
        {
          noStroke();
          fill(random(255), random(255), random(255));
          ellipse(random(wSize), random(hSize), 50, 50);
          fill(random(255), random(255), random(255));
          ellipse(random(wSize/2), random(hSize), 100, 100);
        }
        textFont(GameFont, 30);
        fill(255);
        textAlign(CENTER);
        text("Initializing!", (wSize/2), (hSize/2));
      }
      break;

      // MENU STATE
    case MENU:
      // Menu Display Loop
      this.menu.Display();
      break;
  
      // TUTORIAL SCREEN
    case TUTORIAL:
      // Display Tutorial Screen
      this.tutorial.Display();
      break;

      // LEVEL SELECT  
    case LEVEL_SELECT:
      // Level Select Screen Display Loop
      this.levels.Display();
      break;

      // PUZZLE  
    case PUZZLE:
      // Puzzle Functions go here
      if (!this.puzzle.isGenerated())
      {
        this.puzzle.Generate();
      } else
      {
        // Simulate Puzzle
        this.puzzle.Simulate();
        // Display Puzzle
        this.puzzle.Display();
      }
      break;

      // SCORE  
    case SCORE:
      // Score Functions go here
      // Check for High Score
      
      //Play Music
      if(count <= 65)
      {
        //deleteSound.play();
        count++;
      }
      //this.score.checkScore();
      // Display Screen
      this.score.Display();
      break;

      // CLEAR  
    case CLEAR:
      // Clearing Functions go here
      this.puzzle = new Puzzle();
      this.score = new Score();
      connectorList = new ArrayList<Node>();
      this.setState(MENU);
      break;
      // ERROR  
    default:
      // Error - Reinitialize
      next_State = INIT;
    }
    // Set Next State
    cur_State = next_State;
  }
}
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 24, 2015         \\
// ------------------------------------- \\
//                                       \\
//               LC_Grid.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

// Grid Class Declaration
class Grid 
{
  // Grid Dimensions
  final int gW, gH;
  // Grid Position
  PVector gPos;
  // Grid Node List
  Node[] nodeList;
  // Default Constructor
  public Grid()
  {
    // Default Size (30x20)
    this(30, 20, gSize, gSize);
  }
  // Regular Constructor
  public Grid(int w, int h, int x, int y)
  {
    // Set Dimensions
    this.gW = w;
    this.gH = h;
    // Set Position
    this.gPos = new PVector(x, y);
    // Create Node List
    this.nodeList = new Node[this.gW*this.gH];

    // Populate Node List
    for (int i = 0; i < nodeList.length; i++) {
      // Calculate Position of Nodes
      int nx = PApplet.parseInt((i%this.gW)*gSize + gSize/2 + this.gPos.x);
      int ny = PApplet.parseInt((i/this.gW)*gSize + gSize/2 + this.gPos.y);
      // Make Node with Position Values
      Node b = new Node(new PVector(nx, ny), this);
      // Add node to list
      this.nodeList[i] = b;
    } 
    // Find Adjacent Nodes
    for (int i = 0; i < nodeList.length; i++) {
      // all except left column
      if (i%this.gW != 0) {
        // Associate Sibling Nodes with Each other
        nodeList[i].addSibling(nodeList[i-1]);
      }
      // all except right column
      if (i%this.gW != this.gW - 1) {
        // Associate Sibling Nodes with Each Other
        nodeList[i].addSibling(nodeList[i+1]);
      }
      // all except top row
      if (i >= this.gW) {
        // Associate Sibling Nodes with Each Other
        nodeList[i].addSibling(nodeList[i-this.gW]);
      }
      // all except bottom row
      if (i < (this.gH - 1) * this.gW) {
        // Associate Sibling Nodes with Each Other
        nodeList[i].addSibling(nodeList[i+gW]);
      }
    }
  }
  // Display Function
  public void Display()
  {
    // Loop Through all Nodes and Display
    for (int i = 0; i < this.nodeList.length; i++)
    {
      Node node = this.nodeList[i];
      node.Display();
    }
  }
  // Return Node at Position
  public Node getNodeAt(int x, int y)
  {
    // Check for Out of Bounds
    if ( ( x >= (this.gW*gSize + this.gPos.x) ) || ( x < this.gPos.x ) ) 
      return null;  
    if ( ( y >= (this.gH*gSize + this.gPos.y) ) || ( y < this.gPos.y) )
      return null;
    // Calculate Node Index based on Position
    int i = PApplet.parseInt((y - this.gPos.y)/gSize)*this.gW + PApplet.parseInt( (x - this.gPos.x)/gSize );
    // Return Node
    return this.nodeList[i];
  }
  // Return Node List
  public Node[] getNodeList()
  {
    return this.nodeList;
  }
  // Return Grid Width
  public int getWidth()
  {
    return this.gW*gSize;
  }
  // Return Grid Height
  public int getHeight()
  {
    return this.gH*gSize;
  }
}
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
    int bclr, tclr;
    // Make Buttons for Each Level
    for (int i = 0; i < numLevels; i++)
    {
      // Initialize Button Properties
      bLabel = "Level " + str(i+1);
      bHt = (hSize/((num/2)+4));
      bWd = 200;
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
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 21, 2015         \\
// ------------------------------------- \\
//                                       \\
//               LC_Node.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

// Node Class Declaration
class Node 
{
  // Node Position Vector
  PVector nPos;
  // Parent Grid
  Grid pGrid;
  // Node Siblings
  Node[] nSiblings;
  // Node Colour
  int nColour;
  // Distance to End
  int distanceToEnd = -1;
  // Distance Flags
  boolean ignoreDistance = false;
  boolean distanceSet = false;
  // Type Flags
  boolean isConnector = false;
  boolean isWall = false;
  // Wire Flags (Optional?)
  boolean isHWire = false;
  boolean isVWire = false;
  // Solution Checker Flag
  boolean isChecked = false;
  boolean isOutput = false;
  
  // Regular Contructor
  public Node(PVector pos, Grid grid)
  {
    // Set Position of Node
    this.nPos = pos;
    // Associate Node to Grid
    this.pGrid = grid;
    // Create Empty Sibling List
    this.nSiblings = new Node[0];
    // Set Basic Colour
    this.nColour = color(00, 44, 88);
  }

  // Add Sibling to Node
  public void addSibling(Node sib)
  {
    this.nSiblings = (Node[])append(nSiblings, sib);
  }

  // Display Function for Node
  public void Display()
  {
    // If Wall or Empty
    if ( !this.isConnector )
      // Select Node Colour
      fill(this.nColour);
    else
      // Empty Node Colour
    fill(00, 44, 88);

    // White Stroke Line
    stroke(255);
    // Center Rectangles
    rectMode(CENTER);
    // Draw Node onto Screen
    rect(this.nPos.x, this.nPos.y, gSize, gSize);

    // If Connector Draw Circle
    if ( this.isConnector )
    {
      // Connector Colour
      fill(this.nColour);
      // Center Connector
      ellipseMode(CENTER);
      // Black Stroke
      stroke(255);
      strokeWeight(2);
      // Draw Circle
      ellipse(this.nPos.x, this.nPos.y, gSize*3/4, gSize*3/4);
      strokeWeight(1);
    }
  }

  // Set Distance Vallue
  public void setDistance(int i)
  {
    this.distanceToEnd = i;
    this.distanceSet = true;
  }

  // Clear Distance Value
  public void resetDistance()
  {
    this.distanceSet = false;
  }

  // Reset Distance Value to -1
  public void clearDistanceValue()
  {
    this.distanceToEnd = -1;
  }

  // Reset Color for Node
  public void resetColour()
  {
    this.nColour = color(00, 44, 88);
  }

  // Set Colour for Node
  public void setColour(int clr)
  {
    this.nColour = clr;
  }

  // Set Wall Value for Node
  public void setWall(boolean w)
  {
    this.isWall = w;
    // If wall, Set appropriate Flags
    if (w) {
      this.ignoreDistance = true;
      this.distanceToEnd = -1;
      this.setColour(color(150));
      this.isConnector = false;
    }
    // Otherwise Clear Wall flags
    else {
      this.ignoreDistance = false;
      this.resetColour();
    }
  }

  // Set Connector Value for Node
  public void setConnector(boolean c)
  {
    this.isConnector = c;
    // If Connector, Set Appropriate Flags
    if (c) {
      this.ignoreDistance = true;
      this.distanceToEnd = -1;
      this.isWall = false;
    } else {
      this.ignoreDistance = false;
      this.resetColour();
    }
  }

  // For Maintaining Overlap Issues
  public void setWire(boolean hWire, boolean vWire)
  {
    // Set Wire Values
    isHWire = hWire;
    isVWire = vWire;
  }

  // Check for V Wire Placement
  public boolean getVWire()
  {
    return isVWire;
  }

  // Check for H Wire placement
  public boolean getHWire()
  {
    return isHWire;
  }

  // Set Ignore Flag
  public void setIgnore(boolean b)
  {
    this.ignoreDistance = b;
  }

  // Return Distance Flag
  public boolean isDistanceSet()
  {
    return this.distanceSet;
  }

  // Return Ignore Distance Flag
  public boolean ignoreDistanceSet()
  {
    return this.ignoreDistance;
  }
  
  public void setOutput(boolean c)
  {
    this.isOutput = c;
  }
  
  // Check I/O Status
  public boolean isOutput()
  {
    return this.isOutput;
  }

  // Return if Connector
  public boolean isConnector()
  {
    return this.isConnector;
  }

  // Return if Wall
  public boolean isWall()
  {
    return this.isWall;
  }

  // Return Distance Value
  public int getDistance()
  {
    return this.distanceToEnd;
  }

  // Return Position of Node
  public PVector getPosition()
  {
    return this.nPos;
  }

  // Return Node Colour
  public int getColour()
  {
    return this.nColour;
  }
}
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 30, 2015         \\
// ------------------------------------- \\
//                                       \\
//             LC_Puzzle.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\


// Puzzle Class Declaration
class Puzzle {

  // Puzzle Grid
  Grid pGrid;
  // Wire List
  ArrayList<Wire> wireList;
  // Block List
  ArrayList<Block> blockList;
  // Block Bank List
  ArrayList<Block> blockBank;
  // Initial Connector List
  ArrayList<Node> initialCon;
  // Node List
  Node[] nodeList;

  // Puzzle Data
  Table puzzleData;

  // Difficulty Level
  int difficultyLevel;

  // Score
  int score = 0;

  // Generation Flag
  boolean isGenerated = false;
  boolean isSolved = false;

  // Solution Button
  Button solveBtn;

  // Default Constructor
  public Puzzle()
  {
    // Set to Null;
    this.pGrid = null;
    this.wireList = null;
    this.blockList = null;
    this.blockBank = null;
    this.nodeList = null;
    this.initialCon = null;
    this.difficultyLevel = -1;
    this.solveBtn = null;
  }

  // Regular Constructor
  public Puzzle(int diff)
  {
    // Generate Grid
    this.pGrid = new Grid(gWidth, gHeight, gSize, gSize);
    // Generate Empty Wirelist
    this.wireList = new ArrayList<Wire>();
    // Generate Empty Blocklist
    this.blockList = new ArrayList<Block>();
    // Generate Empty Block Bank list
    this.blockBank = new ArrayList<Block>();
    // Generate Empty Initial Connector List
    this.initialCon = new ArrayList<Node>();
    // Copy over nodeList from Grid
    this.nodeList = pGrid.getNodeList();
    // Set Difficulty Level
    this.difficultyLevel = diff;
    // Set Button
    this.solveBtn = new Button(gSize-5, gSize*5, wSize-((gSize*bbWidth)/2 + 3*gSize/2), hSize-((gSize*3)/2), color(100, 50, 25), color(200), "Solve");
  }

  // Generate Puzzle Function
  public void Generate()
  {
    // Connector Information
    int nodeRow;
    int nodeCol;
    int nodeIndex;
    String ndColour;
    int nodeClr;

    // Read in Data From Puzzle File
    puzzleData = loadTable("Level_"+str(puzzleSelected)+".csv", "header");

    // Populate Connectors
    for ( TableRow row : puzzleData.rows () )
    {
      // Isolate Information
      nodeRow = row.getInt("R");
      nodeCol = row.getInt("C");
      ndColour = row.getString("K");
      // Find Node
      nodeIndex = (nodeRow-1)*((pGrid.getWidth())/gSize) + (nodeCol-1);

      // Determine Colour
      if (ndColour.equals("R"))
      {
        nodeClr = color(255, 0, 0);
      } else if ( ndColour.equals("r"))
      {
        nodeClr = color(0, 255, 255);
      } else if ( ndColour.equals("B"))
      {
        nodeClr = color(0, 0, 255);
      } else if ( ndColour.equals("b"))
      {
        nodeClr = color(255, 255, 0);
      } else if ( ndColour.equals("G"))
      {
        nodeClr = color(0, 255, 0);
      } else if ( ndColour.equals("g"))
      {
        nodeClr = color(255, 0, 255);
      } else if ( ndColour.equals("W"))
      {
        nodeClr = color(255);
      } else
      {
        nodeClr = color(10, 220, 100);
      }  
      // Set Node to Connector
      this.pGrid.nodeList[nodeIndex].setConnector(true);
      this.pGrid.nodeList[nodeIndex].setColour(nodeClr);

      // Append Node to Connector List
      connectorList.add(this.pGrid.nodeList[nodeIndex]);
      this.initialCon.add(this.pGrid.nodeList[nodeIndex]);
    }

    // Fill Up Block Bank With Blocks

    // INVERTER
    blockBank.add(new Block(BLOCK.INVERTER, wSize-(bbWidth*gSize)/2 - 3.25f*gSize - 1*gSize, (8*gSize)/2));

    // MIXER
    blockBank.add(new Block(BLOCK.MIXER, wSize-(bbWidth*gSize)/2 - 3.25f*gSize - 1*gSize, (18*gSize)/2));

    //DEMIXER
    blockBank.add(new Block(BLOCK.DEMIXER, wSize-(bbWidth*gSize)/2 + 0.75f*gSize - 1*gSize, (18*gSize)/2));

    // DECONSTRUCTOR
    blockBank.add(new Block(BLOCK.DECONSTRUCTOR, wSize-(bbWidth*gSize)/2 + 0.75f*gSize - 1*gSize, (8*gSize)/2));
    
    // SPLITTER
    blockBank.add(new Block(BLOCK.SPLITTER, wSize-(bbWidth*gSize)/2 - 3.25f*gSize - 1*gSize, (28*gSize)/2));
    
	//OTHERS GO HERE
    //--------------

    this.isGenerated = true;
  }

  // Simulate Puzzle Function
  public void Simulate()
  {

    for (int i = 0; i< this.wireList.size (); i++)
    {
      if (!wireList.get(i).startNode.isConnector() || !wireList.get(i).endNode.isConnector() || (wireList.get(i).startNode.getColour() != wireList.get(i).endNode.getColour()))
        this.wireList.get(i).Deactivate();
      if (!this.wireList.get(i).isActive())
      {
        this.wireList.remove(i);
      } else
      {
        if (reCalcWires)
        {
          Node s = this.wireList.get(i).startNode;
          Node e = this.wireList.get(i).endNode;
          this.wireList.remove(i);
          this.addWire(new Wire(s, e, this.pGrid));
        }
      }
    }
    // Reset Calculator Bool
    reCalcWires = false;
    // Reset Score
    this.score = 0;

    // Calculate all Logic Operations for Blocks
    for (int i = 0; i< this.blockList.size (); i++)
    {
      // Adjust Block and Perform Logic
      if (!this.blockList.get(i).isActive())
        this.blockList.remove(i);
      else
      {
        if (this.blockList.get(i).isActive() && this.blockList.get(i).isGenerated)
        {
          this.blockList.get(i).Calculate();
          // Calculate Score  
          switch(this.blockList.get(i).blockType())
          {
          case INVERTER:
            score += 2;
            break;
          case MIXER:
            score += 3;
            break;
          case DEMIXER:
            score += 3;
            break;
          case DECONSTRUCTOR:
            score += 4;
            break;
          case SPLITTER:
            score += 1;
            break;
          case CCW_GATE:
            score += 5;
            break;
          case CW_GATE:
            score += 5;
            break;
          case INV_GATE:
            score += 5;
            break;
          case CCW_SIPHON:
            score += 6;
            break;
          case CW_SIPHON:
            score += 6;
            break;
          case TELEPORTER_TX:
          case TELEPORTER_RX:
            score += 1;
            break;
          }
        }
      }
      reCalc = false;
      //}
    }

    // Calculate all Logic Operations for Closed System
    // ie: Check through Wirelist
    // ie: Check through Connection List
    // ie: Check Through Block List
    this.isSolved = this.checkForSolve();

    // Activate Button when Solved
    if (this.isSolved())
    {
      this.solveBtn.Activate();
    } else
    {
      this.solveBtn.Deactivate();
    }
  }

  // Check to see if Solved
  public boolean isSolved()
  {
    return this.isSolved;
  }

  // Solution Checker
  public boolean checkForSolve()
  {
    boolean found = false;
    boolean init = false;
    int cnt = 0;

    //TEMPORARY:
    for (int i = 0; i<connectorList.size (); i++)
    {
      found = false;

      for (int j = 0; j < this.wireList.size (); j++)
      {
        if (connectorList.get(i) == this.wireList.get(j).startNode || connectorList.get(i) == this.wireList.get(j).endNode)
        {
          found = true;
          init = false;
          for (int k = 0; k < this.initialCon.size (); k++)
          {
            if (this.wireList.get(j).startNode == this.initialCon.get(k) || this.wireList.get(j).endNode == this.initialCon.get(k))
            {
              if (init)
                found = false;
              else
              {
                init = true;
              }
            }
          }
          break;
        }
      }
      if (found)
      {
        cnt++;
      }
    }

    if (cnt == connectorList.size())
    {
      return true;
    } else
    {
      return false;
    }
  }

  // Display Puzzle Function
  public void Display()
  {
    // Clear Background
    background(00, 44, 88);
    // Draw Text
    fill(255);
    textFont(GameFont, 20);
    textAlign(CENTER, CENTER);
    text("LogiColour: A Colour Puzzle Game - Level: " + str(puzzleSelected), gSize+(gWidth/2)*gSize, gSize/2+5);
    text("Block Bank", wSize-(gSize*11)/2, gSize/2+5);
    text("Cost: "+str(this.score)+" ( Wires: " + str(this.wireList.size()) + " Connectors: " + str(connectorList.size()) + " )", gSize+(gWidth/2)*gSize, hSize-gSize/2-5);
    // Display Grid
    this.pGrid.Display();
    // Display Blocks
    for (int i=0; i<this.blockList.size (); i++)
    {
      blockList.get(i).Display();
    }
    // Display Wires
    for (int i=0; i<this.wireList.size (); i++)
    {
      wireList.get(i).Display();
    }
    // Draw Block Bank Area
    noFill();
    stroke(255);
    rectMode(CORNER);
    rect((gSize*(gWidth + 2)), gSize, (bbWidth-3)*gSize, hSize-2*(gSize));
    line((gSize*(gWidth+2)), hSize-(gSize*2), gSize*(gWidth+bbWidth-1), hSize-(gSize*2));

    // Block Bank Text Labels
    textAlign(CENTER, CENTER);
    fill(255);
    textFont(GameFont, 15);
    text("Inverter", wSize-(bbWidth*gSize)/2 - 3.25f*gSize - 1*gSize, (4*gSize)/2);
    text("Mixer", wSize-(bbWidth*gSize)/2 - 3.25f*gSize - 1*gSize, (14*gSize)/2);
    text("Demixer",wSize-(bbWidth*gSize)/2 + 0.75f*gSize - 1*gSize, (14*gSize)/2);
    text("Deconstructor", wSize-(bbWidth*gSize)/2 + 0.75f*gSize - 1*gSize, (4*gSize)/2);
    text("Splitter", wSize-(bbWidth*gSize)/2 - 3.25f*gSize - 1*gSize, (24*gSize)/2);
    // Draw Block Bank Blocks
    for (int i=0; i<this.blockBank.size (); i++)
    {
      this.blockBank.get(i).Display();
    }
    // Display Solve Button
    solveBtn.Display();
    // Show Wire Placement Selected Colour
    if (connect_1)
    {
      ellipseMode(CENTER);
      fill(nConnect_1.getColour());
      ellipse(gSize+(gWidth/2)*gSize + gSize, hSize-gSize/2-5 - 2*gSize, 35, 35);
    } else
    {
      ellipseMode(CENTER);
      stroke(255);
      fill(00, 00, 00);
      ellipse(gSize+(gWidth/2)*gSize + gSize, hSize-gSize/2-5 - 2*gSize, 35, 35);
    }

    if (isBlockSelected)
    {
      ellipseMode(CENTER);
      fill(255, 180, 200);
      stroke(255);
      ellipse(gSize+(gWidth/2)*gSize - gSize, hSize-gSize/2-5 - 2*gSize, 35, 35);
    } else
    {
      ellipseMode(CENTER);
      fill(00, 00, 00);
      stroke(255);
      ellipse(gSize+(gWidth/2)*gSize - gSize, hSize-gSize/2-5 - 2*gSize, 35, 35);
    }
  }

  // Add Wire to Puzzle
  public void addWire(Wire new_wire)
  {
    this.wireList.add(new_wire);
  }

  // Add Block to Puzzle
  public void addBlock(Block new_block)
  {
    this.blockList.add(new_block);
    //reCalc = true;
    reCalcWires = true;
  }

  // Return if Puzzle is Generated
  public boolean isGenerated()
  {
    return this.isGenerated;
  }

  // Clear Puzzle Instance
  public void Clear()
  {
    this.wireList = null;
    this.blockList = null;
    this.blockBank = null;
    this.initialCon = null;
    this.nodeList = null;
    this.pGrid = null;
  }
}
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 21, 2015         \\
// ------------------------------------- \\
//                                       \\
//             LC_Score.pde              \\
//                                       \\
// ------------------------------------- \\
//=======================================\\


// Score Screen Class
class Score {

  // Score
  int score;
  // Hi Score Flag
  boolean hiScore;

  // Default Constructor
  public Score()
  {
    this.score = 0;
  }

  // Regular Constructor
  public Score(int score)
  {
    this.score = score;
  }

  // Display Function
  public void Display()
  {
    background(00, 44, 88);
    noFill();
    stroke(255);
    rectMode(CENTER);
    rect(wSize/2, hSize/2, (wSize*2)/3, (hSize*2)/3);
    fill(255);
    textAlign(CENTER, CENTER);
    textFont(GameFont, 30);
    text("YOU SOLVED IT!\nScore: " +str(score) +"\n\n\n\nPress Enter to Continue.", wSize/2, hSize/2);

    if (this.hiScore)
    {
      fill(255, 255, 00);
      text("\n\n\nCongratulations! Its a New Record!", wSize/2, hSize/2);
    }
  }

  // Perform Calculations with High Score and Save it
  public void checkScore()
  {
    // Load High Score File
    String[] oldhScore = loadStrings("HScore_"+str(puzzleSelected)+".txt");
    String[] newhScore = new String[0];

    //file = createOutput("HScore_"+str(puzzleSelected)+".txt");

    // Load score
    int curScore = score;
    int nxtScore = score;
    // Reset Flag
    this.hiScore = false;

    // Check High Scores
    for (int i = 0; i < oldhScore.length; i++)
    {
      // If better score
      if (curScore < PApplet.parseInt(oldhScore[i]))
      {
        newhScore = (String[])append(newhScore, str(curScore));
        this.hiScore = true;

        for (int j = i; j < oldhScore.length; j++)
        {
          newhScore = (String[])append(newhScore, oldhScore[j]);
          curScore = PApplet.parseInt(oldhScore[j]);
          if ( oldhScore.length == newhScore.length )
            break;
        }
      }
    }
    // Save New High Scores
    for (int i = 0; i< newhScore.length; i++)
    {
      saveStrings("data/HScore_"+str(puzzleSelected)+".txt", newhScore);
    }
  }
}
//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 30, 2015         \\
// ------------------------------------- \\
//                                       \\
//               LC_Wire.pde             \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

// Wire Class Declaration
class Wire
{
  // Position Vector for Pathfinding
  PVector wPos;
  // Node List
  Node[] nodeList;
  // Start and Endpoints
  Node startNode;
  Node endNode;
  // Next Node (For Path Finding)
  Node nextNode;
  // Parent Grid
  Grid pGrid;
  // Jump Point List
  Node[] jmpList;
  // Wire Colour
  int wColour;
  // Flag for isConnected
  boolean isConnected = false;
  boolean isActive = false;

  // Regular Constructor
  public Wire(Node sNode, Node eNode, Grid grid)
  {
    // Associate Parent Grid
    this.pGrid = grid;
    // Setup Start and End Nodes
    this.startNode = sNode;
    this.endNode = eNode;
    // Set Next Node for Pathfinding
    this.nextNode = sNode;
    // Set Initial Position
    this.wPos = new PVector(sNode.nPos.x, sNode.nPos.y);
    // Load Node List
    this.nodeList = grid.getNodeList();
    // Load Empty Jump Point List
    this.jmpList = new Node[0];
    // Load Wire Colour from Start Node
    this.wColour = sNode.getColour();
    // Add Start Node as First Jmp Node
    this.jmpList = (Node[])append(this.jmpList, sNode);
    // Activate Wire for Use
    this.isActive = true;
    // Place Wire
    this.placeWire();
  }

  //disconnect Wires
  public void Disconnect()
  {
    this.isConnected = false;
  }
  // Wire Display Function
  public void Display()
  {
    if ( !isConnected || !isActive)
    {
      // do Nothing
    } else {
      // Position Vectors
      PVector p0, p1;
      // Loop Through Jump List
      for ( int i = 1; i < jmpList.length; i++ )
      {
        // Find End Point
        p1 = jmpList[i].nPos;
        // Find Start Point
        p0 = jmpList[i-1].nPos;
        // Set Wire Colour
        stroke(this.wColour);
        // Set Wire Size
        strokeWeight(2);
        // Draw Connecting Line Segments
        line(p0.x, p0.y, p1.x, p1.y);
        // Restore StrokeWeight
        strokeWeight(1);
      }
    }
  }

  // Deactivate Wires
  public void Deactivate()
  {
    this.isActive = false;
    if(!reCalcWires)
    {
      //deleteSound.play();
    }
    reCalc = true;
    reCalcWires = true;
  }

  // Calculate the Wire Placement
  public void placeWire()
  {
    while (!isConnected && isActive)
    {
      // Calculate Distances
      boolean dist = this.calculateDistance();
      // If the Wire is at End Node
      if (this.isAtNode(endNode) && dist) {
        this.isConnected = true;
        break;
      }
      // If Made it to Next Node
      if (this.isAtNode(nextNode) && dist) {
        // Adjust Position of Wire
        this.wPos.set(nextNode.nPos.x, nextNode.nPos.y, 0);
        // Find Next Node
        this.findNextNode();
      }
      // Otherwise Move until at next Node 
      else 
      {
        if (nextNode.nPos.x < this.wPos.x && dist) 
        {
          this.wPos.x--;
        } else if (nextNode.nPos.x > this.wPos.x && dist) 
        {
          this.wPos.x++;
        }
        if (nextNode.nPos.y < this.wPos.y && dist) 
        {
          this.wPos.y--;
        } else if (nextNode.nPos.y > this.wPos.y && dist) 
        {
          this.wPos.y++;
        }
      }
    }
  }

  //  Check Activation Status of Wire
  public boolean isActive()
  {
    return this.isActive;
  }

  // Find Next Node to Travel To
  public void findNextNode()
  {
    // Allow Use Internally
    this.startNode.setIgnore(false);
    this.endNode.setIgnore(false);

    // Set Initial Min Distance
    int minDistance = -1;
    // Loop through Node Siblings
    for (int i = 0; i < nextNode.nSiblings.length; i++) 
    {
      // Find Sibling Distances
      Node s = nextNode.nSiblings[i];
      int d = s.getDistance();
      // If Shorter Set Min Distance
      if ((minDistance == -1) || (d > -1 && d < minDistance)) 
      {
        minDistance = d;
      }
    }
    // If No Path Exists, return
    if (minDistance == -1) {
      return;
    }
    // Find Closest Nodes Amongst Siblings
    Node[] closestsNodes = new Node[0];
    // Loop Through Siblings
    for (int i = 0; i < nextNode.nSiblings.length; i++) 
    {
      Node s = nextNode.nSiblings[i];
      // If Node is in a Path - Add it to List
      if (s.getDistance() == minDistance) {
        closestsNodes = (Node[]) append(closestsNodes, s);
      }
    }
    // Choose one of the Closest Paths and Set as Next Node
    this.nextNode = closestsNodes[PApplet.parseInt(random(closestsNodes.length))];

    boolean problem = false;

    // Skip Connector Nodes
    if (!nextNode.isConnector())
    {
      /*problem = true;
       // Look out for overlapping Vertical Wires
       if((this.jmpList[this.jmpList.length-1].nPos.x == nextNode.nPos.x) && !nextNode.getVWire())
       {
       nextNode.setWire(false,true);
       jmpList[this.jmpList.length-1].setWire(jmpList[this.jmpList.length-1].getHWire(), true);
       problem = false;
       }
       // Look out for Overlapping Horizontal Wires
       else if((this.jmpList[this.jmpList.length-1].nPos.y == nextNode.nPos.y) && !nextNode.getHWire())
       {
       nextNode.setWire(true,false);
       jmpList[this.jmpList.length-1].setWire(true,jmpList[this.jmpList.length-1].getVWire());
       problem = false;        
       }
       */
    }

    if (!problem)
    {
      // Append Node to Jump List
      this.addJmpNode(nextNode);
    }

    this.startNode.setIgnore(true);
    this.endNode.setIgnore(true);
  }

  // Add Node to Jump List
  public void addJmpNode(Node jmpNode)
  {
    this.jmpList = (Node[])append(jmpList, jmpNode);
  }

  // Check to see wire is at node
  public boolean isAtNode(Node node)
  {
    return (dist(this.wPos.x, this.wPos.y, node.nPos.x, node.nPos.y) < 2);
  }

  // Return Start Node
  public Node getStart()
  {
    return this.startNode;
  }

  // Return End Node
  public Node getEnd()
  {
    return this.endNode;
  }

  // Calculate the Distance from Nodes to End Node
  public boolean calculateDistance() 
  {
    // Allow Use for Calculate Distance
    this.startNode.setIgnore(false);
    this.endNode.setIgnore(false);

    // Loop through Node List and Clear Distances
    for (int i = 0; i < this.nodeList.length; i++) 
    {
      this.nodeList[i].clearDistanceValue();
    }
    // Set End Node Distance to 0
    this.endNode.setDistance(0);
    // Length Counter
    int k = 1;
    // Loop until Distances are Found
    while (k > 0) {
      // Reset Counter to 0
      k = 0;
      // Loop through Node List
      for (int i = 0; i < this.nodeList.length; i++) 
      {
        // Ignore Previously Calculated Values
        if (nodeList[i].isDistanceSet() || nodeList[i].ignoreDistanceSet()) 
        {
          continue;
        }
        // Loop Through Siblings
        for (int j = 0; j < nodeList[i].nSiblings.length; j++) 
        {
          if (nodeList[i].nSiblings[j].isDistanceSet() && !nodeList[i].nSiblings[j].ignoreDistanceSet()) 
          {
            nodeList[i].setDistance(nodeList[i].nSiblings[j].getDistance() + 1);
            k++;
            continue;
          }
        }
      }
    }
    // Reset Distance Flags
    for (int i = 0; i < nodeList.length; i++) 
    {
      nodeList[i].resetDistance();
    }
    // If Path is Blocked Return False
    if (startNode.getDistance() == -1) 
    {
      return false;
    }
    this.startNode.setIgnore(true);
    this.endNode.setIgnore(true);
    // Else Return True 
    return true;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "logicolor_android" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
