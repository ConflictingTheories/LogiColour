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
  color btnColour;
  color txtColour;
  // Text
  String btnLabel;
  // Activation Flag
  boolean isActive;

  // Default Constructor
  public Button()
  {
    this.btnHeight = hSize/10;
    this.btnWidth = wSize/10;
    this.btnPos = new PVector(wSize/2, hSize - hSize/3);
    this.btnColour = color(100);
    this.txtColour = color(30, 00, 00);
    this.btnLabel = "Button";
    this.isActive = true;
  }

  // Regular Contructor
  public Button(int ht, int wd, int x, int y, color bclr, color tclr, String lbl)
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
  public void setBtnColour(color clr)
  {
    this.btnColour = clr;
  }

  // Text Colour Change Method (For Rollover)
  public void setTxtColour(color clr)
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
  public color getColour()
  {
    return this.btnColour;
  }
}