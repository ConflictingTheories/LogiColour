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