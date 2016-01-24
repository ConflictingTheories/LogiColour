package processing.test.logicolor_android;//=======================================\\
// LOGICOLOUR : A COLOURFUL PUZZLE GAME  \\
// ------------------------------------- \\
// AUTHOR: KYLE DERBY MACINNIS           \\
//   DATE: MARCH 12, 2015                \\
// ------------------------------------- \\
// LAST UPDATED:  MARCH 12, 2015         \\
// ------------------------------------- \\
//                                       \\
//             BLOCK.java                \\
//                                       \\
// ------------------------------------- \\
//=======================================\\

// Block Types
enum BLOCK 
{
  INVERTER, // Inverter Block Type
  MIXER, // Mixer Block Type
  DEMIXER, // Demixer Block Type
  DECONSTRUCTOR, // Deconstructor Block Type
  SPLITTER,
  CCW_GATE, // Counter-Clockwise Gate
  CW_GATE, // Clockwise Gate
  INV_GATE, // Inverted Gate
  CCW_SIPHON, // Counter-Clockwise Siphon
  CW_SIPHON, // Clockwise Siphon
  TELEPORTER_TX, // Teleporter Transmitter
  TELEPORTER_RX    // Teleporter Receiver
};