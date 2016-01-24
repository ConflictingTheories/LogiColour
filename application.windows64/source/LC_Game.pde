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
        fill(#aaaaaa);
        textAlign(CENTER);
        mainMusic.loop();
        text("Error: Game Failed to Load Properly!\n\nPlease Restart.", (wSize/2), (hSize/2));
      }
      // Error Checking for Time Out
      else if ( (cnt >= 300) && !gameLoaded)
      {
        // Make Warning Screen and Proceed no Further
        background(100, 00, 00);
        textFont(WarningFont, 20);
        fill(#aaaaaa);
        textAlign(CENTER);
        mainMusic.loop();
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
      this.score.checkScore();
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