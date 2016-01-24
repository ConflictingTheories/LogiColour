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
  color nColour;
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
      ellipse(this.nPos.x, this.nPos.y, gSize/2, gSize/2);
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
  public void setColour(color clr)
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
  public color getColour()
  {
    return this.nColour;
  }
}