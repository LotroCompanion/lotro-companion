package delta.games.lotro.gui.stats.crafting;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.stats.crafting.CraftingStats;
import delta.games.lotro.stats.crafting.ProfessionStat;
import delta.games.lotro.utils.gui.DefaultWindowController;

/**
 * Controller for a "crafting stats" window.
 * @author DAM
 */
public class CraftingWindowController extends DefaultWindowController
{
  private CharacterFile _toon;
  private CraftingStats _stats;
  private HashMap<String,CraftingPanelController> _panels;

  /**
   * Constructor.
   * @param toon Managed toon.
   */
  public CraftingWindowController(CharacterFile toon)
  {
    _toon=toon;
    CharacterLog log=toon.getLastCharacterLog();
    String toonName=toon.getName();
    _stats=new CraftingStats(toonName,log);
    _panels=new HashMap<String,CraftingPanelController>();
  }

  /**
   * Get the window identifier for a given toon.
   * @param serverName Server name.
   * @param toonName Toon name.
   * @return A window identifier.
   */
  public static String getIdentifier(String serverName, String toonName)
  {
    String id="CRAFTING#"+serverName+"#"+toonName;
    id=id.toUpperCase();
    return id;
  }

  @Override
  protected JComponent buildContents()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Vocation label
    String vocation=_stats.getVocation();
    String vocationStr="Vocation: "+((vocation!=null)?vocation:"-");
    JLabel vocationLabel=GuiFactory.buildLabel(vocationStr);

    JComponent centerComponent=null;
    String[] professions=_stats.getProfessions();
    if ((professions!=null) && (professions.length>0))
    {
      JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
      for(String profession : professions)
      {
        ProfessionStat stats=_stats.getProfessionStat(profession);
        CraftingPanelController craftingPanelController=new CraftingPanelController(stats);
        JPanel craftingPanel=craftingPanelController.getPanel();
        tabbedPane.add(profession,craftingPanel);
        _panels.put(profession,craftingPanelController);
      }
      centerComponent=tabbedPane;
    }
    else
    {
      JLabel centerLabel=new JLabel("No vocation!");
      centerComponent=centerLabel;
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,10,0,10),0,0);
    panel.add(vocationLabel,c);
    GridBagConstraints c2=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,10,5,10),0,0);
    panel.add(centerComponent,c2);
    return panel;
  }

  @Override
  protected JFrame build()
  {
    JFrame frame=super.build();
    // Title
    String name=_toon.getName();
    String serverName=_toon.getServerName();
    String title="Crafting for "+name+" @ "+serverName;
    frame.setTitle(title);
    frame.pack();
    frame.setMinimumSize(new Dimension(500,380));
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
    if (_panels!=null)
    {
      for(CraftingPanelController controller : _panels.values())
      {
        controller.dispose();
      }
      _panels.clear();
      _panels=null;
    }
    _stats=null;
    _toon=null;
  }
}
