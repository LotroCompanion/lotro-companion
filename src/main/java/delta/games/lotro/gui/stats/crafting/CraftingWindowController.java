package delta.games.lotro.gui.stats.crafting;

import java.awt.Color;
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
    _stats=new CraftingStats(log);
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
    JPanel panel=new JPanel(new GridBagLayout());
    panel.setBackground(Color.BLACK);
    
    // Vocation label
    JLabel vocationLabel=new JLabel();
    String vocation=_stats.getVocation();
    vocationLabel.setText("Vocation: "+((vocation!=null)?vocation:"-"));
    vocationLabel.setForeground(Color.WHITE);
    
    JComponent centerComponent=null;
    String[] professions=_stats.getProfessions();
    if ((professions!=null) && (professions.length>0))
    {
      JTabbedPane tabbedPane=new JTabbedPane();
      tabbedPane.setBackground(Color.BLACK);
      tabbedPane.setForeground(Color.WHITE);
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
