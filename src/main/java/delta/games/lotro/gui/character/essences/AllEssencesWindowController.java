package delta.games.lotro.gui.character.essences;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterData;

/**
 * Controller for a "all essences edition" window.
 * @author DAM
 */
public class AllEssencesWindowController extends DefaultWindowController
{
  /**
   * Window identifier.
   */
  public static final String IDENTIFIER="ESSENCES";

  private AllEssencesEditionPanelController _editionController;
  private CharacterData _toon;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public AllEssencesWindowController(CharacterData toon)
  {
    _editionController=new AllEssencesEditionPanelController(this,toon);
    _toon=toon;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel editionPanel=_editionController.getPanel();
    return editionPanel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Essences for: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(true);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    return IDENTIFIER;
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_editionController!=null)
    {
      _editionController.dispose();
      _editionController=null;
    }
    _toon=null;
    super.dispose();
  }
}