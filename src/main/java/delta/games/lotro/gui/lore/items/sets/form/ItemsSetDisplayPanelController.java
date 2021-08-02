package delta.games.lotro.gui.lore.items.sets.form;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.ItemsSetBonus;
import delta.games.lotro.utils.Proxy;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a panel to display an items set.
 * @author DAM
 */
public class ItemsSetDisplayPanelController implements NavigablePanelController
{
  // Data
  private ItemsSet _set;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private List<ItemDisplayGadgets> _itemIcons;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param set Items set to show.
   */
  public ItemsSetDisplayPanelController(NavigatorWindowController parent, ItemsSet set)
  {
    _parent=parent;
    _set=set;
    _itemIcons=new ArrayList<ItemDisplayGadgets>();
  }

  @Override
  public String getTitle()
  {
    return "Items set: "+_set.getName();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Attributes
    JPanel attributesPanel=buildAttributesPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(attributesPanel,c);

    // Description
    if (_set.getDescription().length()>0)
    {
      JEditorPane description=buildDescription();
      description.setBorder(GuiFactory.buildTitledBorder("Description"));
      c=new GridBagConstraints(0,1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(description,c);
    }

    // Members
    c=new GridBagConstraints(0,2,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    JPanel membersPanel=buildMembersPanel();
    membersPanel.setBorder(GuiFactory.buildTitledBorder("Members"));
    panel.add(membersPanel,c);

    // Bonus
    if (hasBonus())
    {
      JEditorPane bonus=buildBonus();
      bonus.setBorder(GuiFactory.buildTitledBorder("Bonuses"));
      c=new GridBagConstraints(0,3,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(bonus,c);
    }

    return panel;
  }

  private JPanel buildAttributesPanel()
  {
    // Name
    JLabel nameLabel=GuiFactory.buildLabel(_set.getName(), 28f);
    // Set level
    String setLevel="Set level: "+_set.getLevel();
    JLabel setLevelLabel=GuiFactory.buildLabel(setLevel);
    // Required level
    String requiredLevel="Required level: "+_set.getRequiredLevel();
    JLabel requiredLevelLabel=GuiFactory.buildLabel(requiredLevel);

    // Result panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(nameLabel,c);
    c=new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(setLevelLabel,c);
    c=new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(requiredLevelLabel,c);
    return panel;
  }

  private JEditorPane buildDescription()
  {
    JEditorPane editor=GuiFactory.buildHtmlPanel();
    editor.setPreferredSize(new Dimension(500,100));
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append(HtmlUtils.toHtml(_set.getDescription()));
    sb.append("</body></html>");
    editor.setText(sb.toString());
    return editor;
  }

  private JPanel buildMembersPanel()
  {
    List<ItemDisplayGadgets> ingredientsGadgets=initMembersGadgets();
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(ItemDisplayGadgets ingredientsGadget : ingredientsGadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      // Icon
      panel.add(ingredientsGadget.getIcon(),c);
      // Name
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(ingredientsGadget.getName(),c);
      y++;
    }
    return panel;
  }

  private List<ItemDisplayGadgets> initMembersGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    for(Proxy<Item> member : _set.getMembers())
    {
      int itemId=member.getId();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,1,"");
      ret.add(gadgets);
      _itemIcons.add(gadgets);
    }
    return ret;
  }

  private boolean hasBonus()
  {
    for(ItemsSetBonus bonus : _set.getBonuses())
    {
      StatsProvider statsProvider=bonus.getStatsProvider();
      if (statsProvider.getNumberOfStatProviders()>0)
      {
        return true;
      }
    }
    return false;
  }

  private JEditorPane buildBonus()
  {
    JEditorPane editor=GuiFactory.buildHtmlPanel();
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    int level=_set.getLevel();
    int index=0;
    for(ItemsSetBonus bonus : _set.getBonuses())
    {
      StatsProvider statsProvider=bonus.getStatsProvider();
      BasicStatsSet stats=statsProvider.getStats(1,level);
      String[] lines=StatUtils.getFullStatsDisplay(stats,statsProvider);
      if (lines.length==0)
      {
        continue;
      }
      if (index>0)
      {
        sb.append("<p>");
      }
      int nbPieces=bonus.getPiecesCount();
      sb.append("<b>").append(nbPieces).append(" pieces</b>");
      for(String line : lines)
      {
        sb.append("<br>").append(HtmlUtils.toHtml(line));
      }
      index++;
    }
    sb.append("</body></html>");
    editor.setText(sb.toString());
    return editor;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _set=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    _parent=null;
    if (_itemIcons!=null)
    {
      for(ItemDisplayGadgets itemIcon : _itemIcons)
      {
        itemIcon.dispose();
      }
      _itemIcons.clear();
      _itemIcons=null;
    }
  }
}
