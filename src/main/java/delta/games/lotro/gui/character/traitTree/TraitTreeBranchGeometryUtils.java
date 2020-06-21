package delta.games.lotro.gui.character.traitTree;

import java.awt.Dimension;

import delta.common.utils.NumericTools;

/**
 * Utility methods for the geometry of trait tree branch panels.
 * @author DAM
 */
public class TraitTreeBranchGeometryUtils
{
  /**
   * Icon size.
   */
  public static final int ICON_SIZE=32;
  private static final int ICON_FRAME_SIZE=0;
  private static final int FRAMED_ICON_SIZE=2*ICON_FRAME_SIZE+ICON_SIZE;
  private static final int DELTA_X=24;
  private static final int DELTA_Y=24;
  private static final int X_MARGIN=15;
  private static final int Y_MARGIN=10;
  private static final int NB_ROWS=7;
  private static final int NB_COLUMNS=4;
  /**
   * Trait tree branch panel width.
   */
  public static final int WIDTH=X_MARGIN+NB_COLUMNS*FRAMED_ICON_SIZE+(NB_COLUMNS-1)*DELTA_X+X_MARGIN;
  /**
   * Trait tree branch panel height.
   */
  private static final int HEIGHT=Y_MARGIN+NB_ROWS*FRAMED_ICON_SIZE+(NB_ROWS-1)*DELTA_Y+Y_MARGIN;

  /**
   * Get the position of a cell of a trait tree branch.
   * @param cellId Cell identifier.
   * @return A position.
   */
  public static Dimension getPosition(String cellId)
  {
    int index=cellId.indexOf('_');
    int y=NumericTools.parseInt(cellId.substring(0,index),0);
    int x=NumericTools.parseInt(cellId.substring(index+1),0);
    int column=(x-1)%4;
    int row=y-1;
    return getPosition(column,row);
  }

  /**
   * Get the position of a cell of a trait tree branch.
   * @param column Column, starting at 0.
   * @param row Row, starting at 0.
   * @return A position.
   */
  private static Dimension getPosition(int column, int row)
  {
    int x=X_MARGIN+column*(FRAMED_ICON_SIZE+DELTA_X);
    int y=Y_MARGIN+row*(FRAMED_ICON_SIZE+DELTA_Y);
    return new Dimension(x,y);
  }

  /**
   * Get the position of the center of a cell of a trait tree branch.
   * @param cellId Cell identifier.
   * @return A position.
   */
  public static Dimension getCenterPosition(String cellId)
  {
    Dimension position=getPosition(cellId);
    int x=position.width+ICON_SIZE/2;
    int y=position.height+ICON_SIZE/2;
    return new Dimension(x,y);
  }

  /**
   * Get the size of the trait tree branch panel.
   * @return A size.
   */
  public static Dimension getPanelSize()
  {
    return new Dimension(WIDTH,HEIGHT);
  }
}
