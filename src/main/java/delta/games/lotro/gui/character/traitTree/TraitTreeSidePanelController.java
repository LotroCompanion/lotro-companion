package delta.games.lotro.gui.character.traitTree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.classes.traitTree.TraitTree;
import delta.games.lotro.character.classes.traitTree.TraitTreeBranch;
import delta.games.lotro.character.classes.traitTree.TraitTreeProgression;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;
import delta.games.lotro.character.traits.TraitDescription;

/**
 * Controller for trait tree branch panel.
 * @author DAM
 */
public class TraitTreeSidePanelController
{
  private static final Integer ICONS_DEPTH=Integer.valueOf(1);

  private static final int ICON_SIZE=32;
  private static final int ICON_FRAME_SIZE=0;
  private static final int FRAMED_ICON_SIZE=2*ICON_FRAME_SIZE+ICON_SIZE;
  private static final int DELTA_Y=24;
  private static final int X_MARGIN=15;
  private static final int Y_MARGIN=10;
  private static final int NB_ROWS=7;
  private static final int WIDTH=X_MARGIN+FRAMED_ICON_SIZE+X_MARGIN;
  private static final int HEIGHT=Y_MARGIN+(NB_ROWS+1)*FRAMED_ICON_SIZE+NB_ROWS*DELTA_Y+Y_MARGIN;

  // UI
  private JPanel _panel;
  private JLayeredPane _layeredPane;
  // Controllers
  private List<TraitTreeCellController> _cells;
  // Data
  private CharacterData _toon;
  private TraitTreeBranch _selectedBranch;
  private TraitTreeStatus _status;

  /**
   * Constructor.
   * @param toon Character data.
   * @param tree Tree to use.
   * @param status Trait tree status to show.
   */
  public TraitTreeSidePanelController(CharacterData toon, TraitTree tree, TraitTreeStatus status)
  {
    _toon=toon;
    _selectedBranch=status.getSelectedBranch();
    _status=status;
    _cells=new ArrayList<TraitTreeCellController>();
    _panel=buildPanel();
    updateUi();
  }

  /**
   * Set the selected branch.
   * @param branch Branch to set.
   */
  public void setSelectedBranch(TraitTreeBranch branch)
  {
    _selectedBranch=branch;
    updateUi();
  }

  /**
   * Get the position for a cell of a branch progression.
   * @param row Row, starting at 0.
   * @return A position.
   */
  private Dimension getPosition(int row)
  {
    int x=X_MARGIN;
    int y=Y_MARGIN+(row+1)*(FRAMED_ICON_SIZE+DELTA_Y);
    return new Dimension(x,y);
  }

  /**
   * Update UI to reflect the current trait tree status.
   */
  public void updateUi()
  {
    disposeCells();
    _cells=new ArrayList<TraitTreeCellController>();
    if (_selectedBranch!=null)
    {
      initCells();
      updateUiWithSelectedBranch();
    }
    layoutCells();
  }

  private void initCells()
  {
    TraitTreeProgression progression=_selectedBranch.getProgression();
    List<TraitDescription> traits=progression.getTraits();
    int nbTraits=traits.size();
    for(int i=0;i<nbTraits;i++)
    {
      TraitDescription trait=traits.get(i);
      TraitTreeCellController cellController=new TraitTreeCellController(_toon,"",trait);
      _cells.add(cellController);
    }
  }

  private void updateUiWithSelectedBranch()
  {
    int nbRanks=_status.getTotalRanksInTree();
    TraitTreeProgression progression=_selectedBranch.getProgression();
    List<Integer> steps=progression.getSteps();
    List<TraitDescription> traits=progression.getTraits();
    int nbSteps=_cells.size();
    for(int i=0;i<nbSteps;i++)
    {
      TraitTreeCellController ctrl=_cells.get(i);
      ctrl.setTrait(traits.get(i));
      int requiredRanks=steps.get(i).intValue();
      boolean enabled=(nbRanks>=requiredRanks);
      int rank=enabled?1:0;
      ctrl.setRank(rank);
      ctrl.setEnabled(enabled);
    }
  }

  /**
   * Get the managed panel.
   * @return a panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(null);
    _layeredPane=new JLayeredPane();
    panel.add(_layeredPane,BorderLayout.CENTER);
    Dimension d=new Dimension(WIDTH,HEIGHT);
    panel.setPreferredSize(d);
    panel.setMinimumSize(d);
    _layeredPane.setSize(d);
    return panel;
  }

  private void layoutCells()
  {
    _layeredPane.removeAll();
    int nbCells=_cells.size();
    for(int i=0;i<nbCells;i++)
    {
      TraitTreeCellController cellController=_cells.get(i);
      JButton button=cellController.getButton();
      Dimension position=getPosition(i);
      button.setBounds(position.width,position.height,ICON_SIZE,ICON_SIZE);
      _layeredPane.add(button,ICONS_DEPTH);
    }
    _layeredPane.revalidate();
    _layeredPane.repaint();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_layeredPane!=null)
    {
      _layeredPane.removeAll();
      _layeredPane=null;
    }
    // Controllers
    disposeCells();
    // Data
    _toon=null;
    _selectedBranch=null;
    _status=null;
  }

  private void disposeCells()
  {
    if (_cells!=null)
    {
      for(TraitTreeCellController cellController : _cells)
      {
        cellController.dispose();
      }
      _cells.clear();
      _cells=null;
    }
  }
}
