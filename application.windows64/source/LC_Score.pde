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
      if (curScore < int(oldhScore[i]))
      {
        newhScore = (String[])append(newhScore, str(curScore));
        this.hiScore = true;

        for (int j = i; j < oldhScore.length; j++)
        {
          newhScore = (String[])append(newhScore, oldhScore[j]);
          curScore = int(oldhScore[j]);
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