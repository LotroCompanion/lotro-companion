package delta.games.lotro.gui.character.traitTree;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.status.traitTree.TraitTreeStatus;

/**
 * Controller for the "trait tree edition" dialog.
 * @author DAM
 */
public class TraitTreeWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="TRAIT_TREE_WINDOW";

  private TraitTreePanelController _treePanel;

  /**
   * Constructor.
   * @param parentController Parent controller.
   * @param toon Character data.
   * @param status Status to edit.
   */
  public TraitTreeWindowController(WindowController parentController, CharacterData toon, TraitTreeStatus status)
  {
    super(parentController);
    _treePanel=new TraitTreePanelController(this,toon,status,false);
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    frame.setTitle("Trait Tree"); // I18n
    frame.pack();
    frame.setResizable(false);
    return frame;
  }

  @Override
  public void configureWindow()
  {
    automaticLocationSetup();
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=_treePanel.getPanel();
    return panel;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_treePanel!=null)
    {
      _treePanel.dispose();
      _treePanel=null;
    }
  }
}
