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
    this.solveBtn = new Button(gSize-5, gSize*5, wSize-((gSize*11)/2), hSize-((gSize*3)/2), color(100, 50, 25), color(200), "Solve");
  }

  // Generate Puzzle Function
  public void Generate()
  {
    // Connector Information
    int nodeRow;
    int nodeCol;
    int nodeIndex;
    String ndColour;
    color nodeClr;

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
    blockBank.add(new Block(BLOCK.INVERTER, wSize-(11*gSize)/2 - 2*gSize, (8*gSize)/2));

    // MIXER
    blockBank.add(new Block(BLOCK.MIXER, wSize-(11*gSize)/2 - 2*gSize, (18*gSize)/2));

    //DEMIXER
    blockBank.add(new Block(BLOCK.DEMIXER, wSize-(11*gSize)/2 + 2*gSize, (18*gSize)/2));

    // DECONSTRUCTOR
    blockBank.add(new Block(BLOCK.DECONSTRUCTOR, wSize-(11*gSize)/2 + 2*gSize, (8*gSize)/2));
    
    // SPLITTER
    blockBank.add(new Block(BLOCK.SPLITTER, wSize-(11*gSize)/2 - 2*gSize, (28*gSize)/2));
    
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
    text("Inverter", wSize-(11*gSize)/2 - 2*gSize, (4*gSize)/2);
    text("Mixer", wSize-(11*gSize)/2 - 2*gSize, (14*gSize)/2);
    text("Demixer", wSize-(11*gSize)/2 + 2*gSize, (14*gSize)/2);
    text("Deconstructor", wSize-(11*gSize)/2 + 2*gSize, (4*gSize)/2);
    text("Splitter", wSize-(11*gSize)/2 - 2*gSize, (24*gSize)/2);
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
      ellipse(wSize-((9*gSize)/2), (hSize)-gSize/2, 20, 20);
    } else
    {
      ellipseMode(CENTER);
      stroke(255);
      fill(00, 44, 88);
      ellipse(wSize-((9*gSize)/2), (hSize)-gSize/2, 20, 20);
    }

    if (isBlockSelected)
    {
      ellipseMode(CENTER);
      fill(00, 00, 100);
      stroke(255);
      ellipse(wSize-((13*gSize)/2), (hSize)-gSize/2, 20, 20);
    } else
    {
      ellipseMode(CENTER);
      fill(00, 44, 88);
      stroke(255);
      ellipse(wSize-((13*gSize)/2), (hSize)-gSize/2, 20, 20);
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