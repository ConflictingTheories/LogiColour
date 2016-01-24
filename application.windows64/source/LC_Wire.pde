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
  color wColour;
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
      deleteSound.play();
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
    this.nextNode = closestsNodes[int(random(closestsNodes.length))];

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