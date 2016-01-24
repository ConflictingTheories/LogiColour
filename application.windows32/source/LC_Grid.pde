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
      int nx = int((i%this.gW)*gSize + gSize/2 + this.gPos.x);
      int ny = int((i/this.gW)*gSize + gSize/2 + this.gPos.y);
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
    int i = int((y - this.gPos.y)/gSize)*this.gW + int( (x - this.gPos.x)/gSize );
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