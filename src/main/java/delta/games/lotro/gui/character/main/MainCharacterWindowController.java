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
import delta.games.lotro.character.status.crafting.CraftingStatus;
import delta.games.lotro.character.status.crafting.CraftingStatusSummaryBuilder;
import delta.games.lotro.character.status.crafting.summary.CraftingStatusSummary;
import delta.games.lotro.gui.character.gear.EquipmentDisplayPanelController;
import delta.games.lotro.gui.character.status.crafting.summary.CraftingStatusSummaryPanelController;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.character.CharacterData;

/**
 * Controller for the main window of a character.
 * @author DAM
 */
public class MainCharacterWindowController extends DefaultWindowController
{
  // Data
  private CharacterFile _toon;
  // Controllers
  private CharacterSummaryPanelController _summaryController;
  private CraftingStatusSummaryPanelController _crafting;
  private EquipmentDisplayPanelController _gear;
  private CharacterMainButtonsController _mainButtons;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public MainCharacterWindowController(CharacterFile toon)
  {
    _toon=toon;
    _summaryController=new CharacterSummaryPanelController(this,_toon);
    setContextProperty(ContextPropertyNames.BASE_CHARACTER_SUMMARY,toon.getSummary());
    _crafting=new CraftingStatusSummaryPanelController();
    CharacterData current=_toon.getInfosManager().getCurrentData();
    _gear=new EquipmentDisplayPanelController(this,current.getEquipment());
    _mainButtons=new CharacterMainButtonsController(this,toon);
    fill();
  }

  private void fill()
  {
    CraftingStatus status=_toon.getCraftingMgr().getCraftingStatus();
    CraftingStatusSummaryBuilder b=new CraftingStatusSummaryBuilder();
    CraftingStatusSummary summary=b.buildSummary(status);
    _crafting.setStatus(summary);
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="FILE#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    // Whole panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Summary panel
    JPanel summaryPanel=_summaryController.getPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,2,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(3,5,3,5),0,0);
    panel.add(summaryPanel,c);
    // Equipment
    JPanel gearPanel=_gear.getPanel();
    gearPanel.setBorder(GuiFactory.buildTitledBorder("Gear"));
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(gearPanel,c);
    // Crafting
    JPanel craftingStatusPanel=_crafting.getPanel();
    craftingStatusPanel.setBorder(GuiFactory.buildTitledBorder("Crafting"));
    c=new GridBagConstraints(1,1,1,1,0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(craftingStatusPanel,c);
    // Command buttons
    JPanel commandsPanel=_mainButtons.getPanel();
    c=new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(commandsPanel,c);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Character: "+name+" @ "+serverName; // I18n
    frame.setTitle(title);
    frame.pack();
    frame.setResizable(true);
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

  /**
   * Release all managed resources.
   */
  @Override
  public void dispose()
  {
    super.dispose();
    if (_summaryController!=null)
    {
      _summaryController.dispose();
      _summaryController=null;
    }
    if (_gear!=null)
    {
      _gear.dispose();
      _gear=null;
    }
    if (_crafting!=null)
    {
      _crafting.dispose();
      _crafting=null;
    }
    if (_mainButtons!=null)
    {
      _mainButtons.dispose();
      _mainButtons=null;
    }
    if (_toon!=null)
    {
      _toon.getPreferences().saveAllPreferences();
      _toon.gc();
      _toon=null;
    }
  }
}
