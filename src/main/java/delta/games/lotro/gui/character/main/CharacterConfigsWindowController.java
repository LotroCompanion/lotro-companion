package delta.games.lotro.gui.character.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Controller for a "character configs" window.
 * @author DAM
 */
public class CharacterConfigsWindowController extends DefaultWindowController
{
  /**
   * Identifier for this window.
   */
  public static final String IDENTIFIER="CONFIGS_WINDOW";

  // Data
  private CharacterFile _toon;
  // Controllers
  private CharacterConfigurationsPanelController _configsController;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterConfigsWindowController(CharacterFile toon)
  {
    _toon=toon;
    _configsController=new CharacterConfigurationsPanelController(this,toon);
    setContext();
  }

  private void setContext()
  {
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,_toon.getSummary());
    setContextProperty(ContextPropertyNames.CHARACTER_FILE,_toon);
  }

  @Override
  protected JComponent buildContents()
  {
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Character configs
    JPanel configsPanel=_configsController.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(configsPanel,c);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Configurations: "+name+" @ "+serverName; // I18n
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
    super.dispose();
    if (_configsController!=null)
    {
      _configsController.dispose();
      _configsController=null;
    }
    if (_toon!=null)
    {
      _toon.gc();
      _toon=null;
    }
  }
}
