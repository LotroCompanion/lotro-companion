package delta.games.lotro.gui.stats.reputation.synopsis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.reputation.FactionData;
import delta.games.lotro.character.reputation.ReputationData;
import delta.games.lotro.lore.reputation.Faction;
import delta.games.lotro.lore.reputation.FactionLevel;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Controller for a reputation synopsis panel.
 * @author DAM
 */
public class ReputationSynopsisPanelController
{
  private String _category;
  private MultipleToonsReputationStats _data;
  private List<Faction> _factions;
  private Map<String,Map<Faction,JLabel>> _labels;
  private JPanel _panel;
  private JPanel _displayPanel;

  /**
   * Constructor.
   * @param category Category filter.
   * @param data Data to display.
   */
  public ReputationSynopsisPanelController(String category,MultipleToonsReputationStats data)
  {
    _category=category;
    _data=data;
    _factions=new ArrayList<Faction>();
    _labels=new HashMap<String,Map<Faction,JLabel>>();
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    _displayPanel=buildDisplayPanel();
    JScrollPane scroll=GuiFactory.buildScrollPane(_displayPanel);
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    panel.add(scroll,BorderLayout.CENTER);
    return panel;
  }

  private JPanel buildDisplayPanel()
  {
    FactionsRegistry registry=FactionsRegistry.getInstance();
    List<Faction> factions;
    if (_category == null)
    {
      factions=registry.getAll();
    }
    else
    {
      factions=registry.getFactionsForCategory(_category);
    }
    _factions.clear();
    _factions.addAll(factions);
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    for(Faction faction : factions)
    {
      JLabel label=GuiFactory.buildLabel(faction.getName());
      panel.add(label,c);
      c.gridy++;
    }
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.CENTER,GridBagConstraints.BOTH,new Insets(1,1,1,1),0,0);
    List<CharacterFile> characters=_data.getToonsList();
    for(CharacterFile character : characters)
    {
      c.gridy=0;
      String name=character.getName();
      JLabel headerLabel=GuiFactory.buildLabel(name);
      headerLabel.setHorizontalAlignment(SwingConstants.HORIZONTAL);
      panel.add(headerLabel,c);
      String id=character.getIdentifier();
      ReputationData data=_data.getStatsForToon(id);
      Map<Faction,JLabel> labelsMaps=new HashMap<Faction,JLabel>();
      _labels.put(id,labelsMaps);
      for(Faction faction : _factions)
      {
        c.gridy++;
        JLabel label=new JLabel();
        label.setOpaque(false);
        labelsMaps.put(faction,label);
        FactionData factionData=data.getFactionStat(faction);
        configureFactionLabel(label,factionData);
        panel.add(label,c);
      }
      c.gridx++;
    }
    return panel;
  }

  private void configureFactionLabel(JLabel label, FactionData factionData)
  {
    Color backgroundColor=null;
    String text="";
    if (factionData!=null)
    {
      //Faction faction=factionData.getFaction();
      //FactionLevel[] levels=faction.getLevels();
      FactionLevel level=factionData.getFactionLevel();
      //int index=Arrays.binarySearch(levels,level);
      String key=level.getKey();
      if (FactionLevel.NEUTRAL.getKey().equals(key)) backgroundColor=Color.GRAY;
      else if (FactionLevel.ENEMY.getKey().equals(key)) backgroundColor=Color.RED;
      else if (FactionLevel.OUTSIDER.getKey().equals(key)) backgroundColor=Color.RED;
      else if (FactionLevel.ACQUAINTANCE.getKey().equals(key)) backgroundColor=Color.ORANGE;
      else if (FactionLevel.FRIEND.getKey().equals(key)) backgroundColor=Color.YELLOW;
      else if (FactionLevel.ALLY.getKey().equals(key)) backgroundColor=new Color(0,128,0);
      else if (FactionLevel.KINDRED.getKey().equals(key)) backgroundColor=Color.GREEN;
      else if (FactionLevel.RESPECTED.getKey().equals(key)) backgroundColor=Color.GREEN;
      else if (FactionLevel.HONOURED.getKey().equals(key)) backgroundColor=Color.GREEN;
      else if (FactionLevel.CELEBRATED.getKey().equals(key)) backgroundColor=Color.GREEN;
      else if ("CELEBRATED_GORGOROTH".equals(key)) backgroundColor=Color.GREEN;
      // Host of the West
      else if ("NONE".equals(key)) backgroundColor=Color.GRAY;
      else if ("INITIAL".equals(key)) backgroundColor=Color.ORANGE;
      else if ("INTERMEDIATE".equals(key)) backgroundColor=Color.YELLOW;
      else if ("ADVANCED".equals(key)) backgroundColor=new Color(0,128,0);
      else if ("FINAL".equals(key)) backgroundColor=Color.GREEN;
      // Guild
      else if ("INITIATE".equals(key)) backgroundColor=Color.GRAY;
      else if ("APPRENTICE".equals(key)) backgroundColor=Color.GREEN;
      else if ("JOURNEYMAN".equals(key)) backgroundColor=Color.GREEN;
      else if ("EXPERT".equals(key)) backgroundColor=Color.GREEN;
      else if ("ARTISAN".equals(key)) backgroundColor=Color.GREEN;
      else if ("MASTER".equals(key)) backgroundColor=Color.GREEN;
      else if ("EASTEMNET MASTER".equals(key)) backgroundColor=Color.GREEN;
      else if ("WESTEMNET MASTER".equals(key)) backgroundColor=Color.GREEN;
      // Hobnanigans
      else if ("ROOKIE".equals(key)) backgroundColor=Color.GRAY;
      else if ("LEAGUER".equals(key)) backgroundColor=Color.ORANGE;
      else if ("MAJOR_LEAGUER".equals(key)) backgroundColor=Color.YELLOW;
      else if ("ALL_STAR".equals(key)) backgroundColor=new Color(0,128,0);
      else if ("HALL_OF_FAMER".equals(key)) backgroundColor=Color.GREEN;
      else System.out.println(key);
      text=level.getName();
    }
    label.setForeground(Color.BLACK);
    if (backgroundColor!=null)
    {
      label.setOpaque(true);
      backgroundColor=new Color(backgroundColor.getRed(),backgroundColor.getGreen(),backgroundColor.getBlue(),128);
      label.setBackground(backgroundColor);
    }
    else
    {
      label.setOpaque(false);
    }
    label.setText(text);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    Dimension preferredSize=label.getPreferredSize();
    label.setMaximumSize(new Dimension(50,preferredSize.height));
  }
}
