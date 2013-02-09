package delta.games.lotro.gui.character;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharacterInfosManager;
import delta.games.lotro.character.log.CharacterLogsManager;
import delta.games.lotro.gui.log.CharacterLogWindowController;
import delta.games.lotro.gui.stats.crafting.CraftingWindowController;
import delta.games.lotro.gui.stats.reputation.CharacterReputationWindowController;
import delta.games.lotro.gui.utils.GuiFactory;
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
  private static final String REPUTATION_COMMAND="reputation";
  private static final String CRAFTING_COMMAND="crafting";
  private static final String UPDATE_COMMAND="update";

  private CharacterSummaryPanelController _summaryController;
  private ChararacterStatsPanelController _statsController;
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
    _summaryController=new CharacterSummaryPanelController(_toon);
    _statsController=new ChararacterStatsPanelController(_toon);
    _equipmentController=new EquipmentPanelController(_toon);
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
    JPanel summaryPanel=_summaryController.getPanel();
    // Stats panel
    JPanel statsPanel=_statsController.getPanel();
    // Equipment panel
    JPanel equipmentPanel=_equipmentController.getPanel();

    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
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
    frame.setResizable(false);
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.CENTER,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Log
    JButton logButton=buildCommandButton("Log",LOG_COMMAND);
    panel.add(logButton,c);
    c.gridx++;
    // Reputation
    JButton reputationButton=buildCommandButton("Reputation",REPUTATION_COMMAND);
    panel.add(reputationButton,c);
    c.gridx++;
    // Crafting
    JButton craftingButton=buildCommandButton("Crafting",CRAFTING_COMMAND);
    panel.add(craftingButton,c);
    c.gridx++;
    // Update
    JButton updateButton=buildCommandButton("Update",UPDATE_COMMAND);
    panel.add(updateButton,c);
    return panel;
  }

  private JButton buildCommandButton(String label, String command)
  {
    JButton b=GuiFactory.buildButton(label);
    b.setActionCommand(command);
    b.addActionListener(this);
    return b;
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
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (REPUTATION_COMMAND.equals(command))
    {
      // Reputation
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterReputationWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CharacterReputationWindowController(_toon);
        _windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (CRAFTING_COMMAND.equals(command))
    {
      // Crafting
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CraftingWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller==null)
      {
        controller=new CraftingWindowController(_toon);
        _windowsManager.registerWindow(controller);
        controller.getWindow().setLocationRelativeTo(getFrame());
      }
      controller.bringToFront();
    }
    else if (UPDATE_COMMAND.equals(command))
    {
      performUpdate();
    }
  }

  private void performUpdate()
  {
    CharacterInfosManager infosManager=new CharacterInfosManager(_toon);
    boolean infosUpDateOK=infosManager.updateCharacterDescription();
    if (infosUpDateOK)
    {
      _summaryController.update();
      _statsController.update();
      _equipmentController.update();
    }
    CharacterLogsManager logManager=_toon.getLogsManager();
    Integer nbNewItems=logManager.updateLog();
    boolean logUpdateOK=(nbNewItems!=null);
    if (logUpdateOK)
    {
      String serverName=_toon.getServerName();
      String toonName=_toon.getName();
      String id=CharacterLogWindowController.getIdentifier(serverName,toonName);
      WindowController controller=_windowsManager.getWindow(id);
      if (controller!=null)
      {
        CharacterLogWindowController logController=(CharacterLogWindowController)controller;
        logController.update();
      }
    }
    CharacterLogWindowController.showLogUpdateMessage(nbNewItems,getFrame());
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
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
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
