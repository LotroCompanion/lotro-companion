package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.utils.gui.DefaultWindowController;
import delta.games.lotro.utils.gui.WindowsManager;

/**
 * Controller for a "character data" window.
 * @author DAM
 */
public class CharacterDataWindowController extends DefaultWindowController implements ActionListener
{
  private ChararacterStatsPanelController _statsController;
  private EquipmentPanelController _equipmentController;
  private CharacterData _toon;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param data Managed toon.
   */
  public CharacterDataWindowController(CharacterData data)
  {
    _toon=data;
    _windowsManager=new WindowsManager();
    _statsController=new ChararacterStatsPanelController(data);
    _equipmentController=new EquipmentPanelController(this,data);
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="DATA#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Stats panel
    JPanel statsPanel=_statsController.getPanel();
    // Equipment panel
    JPanel equipmentPanel=_equipmentController.getPanel();

    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(equipmentPanel,c);
    c=new GridBagConstraints(1,0,1,1,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statsPanel,c);

    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServer();
    String title="Character: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(false);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String serverName=_toon.getServer();
    String toonName=_toon.getName();
    String id=getIdentifier(serverName,toonName);
    return id;
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    System.out.println("command:"+command);
  }

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    super.dispose();
    if (_statsController!=null)
    {
      _statsController.dispose();
      _statsController=null;
    }
    if (_equipmentController!=null)
    {
      _equipmentController.dispose();
      _equipmentController=null;
    }
    _toon=null;
  }
}
