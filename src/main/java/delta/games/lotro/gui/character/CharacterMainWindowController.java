package delta.games.lotro.gui.character;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.Character;
import delta.games.lotro.character.CharacterEquipment;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.gui.log.CharacterLogWindowController;
import delta.games.lotro.utils.gui.DefaultWindowController;
import delta.games.lotro.utils.gui.WindowController;
import delta.games.lotro.utils.gui.WindowsManager;

/**
 * Controller for a "character" window.
 * @author DAM
 */
public class CharacterMainWindowController extends DefaultWindowController implements ActionListener
{
  private static final String LOG_COMMAND="log";

  private CharacterSummaryPanelController _filterController;
  private ChararacterStatsPanelController _tableController;
  private EquipmentPanelController _equipmentController;
  private CharacterFile _toon;
  private WindowsManager _windowsManager;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CharacterMainWindowController(CharacterFile toon)
  {
    _toon=toon;
    _windowsManager=new WindowsManager();
    _filterController=new CharacterSummaryPanelController(_toon);
    _tableController=new ChararacterStatsPanelController(_toon);
    Character c=_toon.getLastCharacterInfo();
    CharacterEquipment equipment=c.getEquipment();
    _equipmentController=new EquipmentPanelController(equipment);
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="MAIN#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Summary panel
    JPanel summaryPanel=_filterController.getPanel();
    // Stats panel
    JPanel statsPanel=_tableController.getPanel();
    // Equipment panel
    JPanel equipmentPanel=_equipmentController.getPanel();

    // Whole panel
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    panel.add(summaryPanel,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(equipmentPanel,c);
    c=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHEAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(statsPanel,c);
    c=new GridBagConstraints(0,2,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel commandsPanel=buildCommandsPanel();
    panel.add(commandsPanel,c);
    
    // TODO crafting anvils?
    return panel;
  }
  
  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Character: "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setLocation(200,200);
    return frame;
  }

  @Override
  public String getWindowIdentifier()
  {
    String serverName=_toon.getServerName();
    String toonName=_toon.getName();
    String id=getIdentifier(serverName,toonName);
    return id;
  }

  private JPanel buildCommandsPanel()
  {
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    JButton b=new JButton("Log");
    b.setBackground(Color.BLACK);
    b.setForeground(Color.WHITE);
    b.setActionCommand(LOG_COMMAND);
    b.addActionListener(this);
    panel.add(b,c);
    return panel;
  }

  /**
   * Handle button actions.
   * @param e Source event.
   */
  public void actionPerformed(ActionEvent e)
  {
    String command=e.getActionCommand();
    if (LOG_COMMAND.equals(command))
    {
      // Show log 
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterLogWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterLogWindowController(_toon);
        _windowsManager.registerWindow(controller);
      }
      controller.bringToFront();
    }
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
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
    if (_equipmentController!=null)
    {
      _equipmentController.dispose();
      _equipmentController=null;
    }
    _toon=null;
  }
}
