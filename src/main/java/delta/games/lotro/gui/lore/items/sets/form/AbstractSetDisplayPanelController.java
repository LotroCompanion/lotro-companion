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
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemUtils;
import delta.games.lotro.lore.items.sets.ItemsSet;
import delta.games.lotro.lore.items.sets.SetBonus;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Base class for a controller of a panel to display an items/traceries set.
 * @author DAM
 */
public abstract class AbstractSetDisplayPanelController implements NavigablePanelController
{
  // Data
  protected ItemsSet _set;
  // GUI
  private JPanel _panel;
  private JPanel _membersPanel;
  private JEditorPane _bonus;

  // Controllers
  private NavigatorWindowController _parent;
  private List<ItemDisplayGadgets> _itemIcons;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param set Items set to show.
   */
  public AbstractSetDisplayPanelController(NavigatorWindowController parent, ItemsSet set)
  {
    _parent=parent;
    _set=set;
    _itemIcons=new ArrayList<ItemDisplayGadgets>();
  }

  @Override
  public String getTitle()
  {
    String prefix=getTitlePrefix();
    return prefix+": "+_set.getName();
  }

  protected abstract String getTitlePrefix();

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

    // Build UI elements
    _membersPanel=GuiFactory.buildPanel(new GridBagLayout());
    _bonus=GuiFactory.buildHtmlPanel();
    _bonus.setBorder(GuiFactory.buildTitledBorder("Bonuses"));

    int y=0;

    // Attributes
    JPanel attributesPanel=buildAttributesPanel();
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,2),0,0);
    panel.add(attributesPanel,c);
    y++;

    // Description
    if (_set.getDescription().length()>0)
    {
      JEditorPane description=buildDescription();
      description.setBorder(GuiFactory.buildTitledBorder("Description"));
      c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(description,c);
      y++;
    }

    // Members configuration
    JPanel membersConfigurationPanel=buildMembersConfigurationPanel();
    if (membersConfigurationPanel!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(membersConfigurationPanel,c);
      y++;
    }

    // Members
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(2,2,2,2),0,0);
    updateMembersPanel();
    JScrollPane scrollPane=GuiFactory.buildScrollPane(_membersPanel);
    scrollPane.setBorder(GuiFactory.buildTitledBorder("Members"));
    panel.add(scrollPane,c);
    y++;

    // Bonus configuration
    JPanel bonusConfigurationPanel=buildBonusConfigurationPanel();
    if (bonusConfigurationPanel!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      panel.add(bonusConfigurationPanel,c);
      y++;
    }

    // Bonus
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(_bonus,c);
    y++;
    updateBonus();
    Dimension prefSize=panel.getPreferredSize();
    int height=Math.min(600,prefSize.height);
    prefSize.height=height;
    panel.setPreferredSize(prefSize);

    return panel;
  }

  protected JPanel buildMembersConfigurationPanel()
  {
    return null;
  }

  protected JPanel buildBonusConfigurationPanel()
  {
    return null;
  }

  private JPanel buildAttributesPanel()
  {
    // Name
    JLabel nameLabel=GuiFactory.buildLabel(_set.getName(), 28f);
    // Set level
    String setLevelStr;
    if (_set.useAverageItemLevelForSetLevel())
    {
      setLevelStr="use average item level";
    }
    else
    {
      setLevelStr=String.valueOf(_set.getSetLevel());
    }
    String setLevel="Set level: "+setLevelStr;
    JLabel setLevelLabel=GuiFactory.buildLabel(setLevel);
    // Required level
    String requiredLevel="Required level: "+_set.getRequiredMinLevel();
    JLabel requiredLevelLabel=GuiFactory.buildLabel(requiredLevel);
    // Max level
    JLabel requiredMaxLevelLabel=null;
    Integer maxLevel=_set.getRequiredMaxLevel();
    if (maxLevel!=null)
    {
      String requiredMaxLevel="Max level: "+maxLevel;
      requiredMaxLevelLabel=GuiFactory.buildLabel(requiredMaxLevel);
    }

    // Result panel
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
    panel.add(nameLabel,c);
    c=new GridBagConstraints(0,1,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(setLevelLabel,c);
    c=new GridBagConstraints(0,2,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
    panel.add(requiredLevelLabel,c);
    if (requiredMaxLevelLabel!=null)
    {
      c=new GridBagConstraints(0,3,2,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      panel.add(requiredMaxLevelLabel,c);
    }
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

  protected void updateMembersPanel()
  {
    _membersPanel.removeAll();
    disposeItemIcons();
    List<ItemDisplayGadgets> membersGadgets=initMembersGadgets();
    int y=0;
    for(ItemDisplayGadgets memberGadgets : membersGadgets)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,2,2,2),0,0);
      // Icon
      _membersPanel.add(memberGadgets.getIcon(),c);
      // Name
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,2,2,2),0,0);
      _membersPanel.add(memberGadgets.getName(),c);
      y++;
    }
  }

  private List<ItemDisplayGadgets> initMembersGadgets()
  {
    List<ItemDisplayGadgets> ret=new ArrayList<ItemDisplayGadgets>();
    List<Item> members=getMembers();
    for(Item member : members)
    {
      int itemId=member.getIdentifier();
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,itemId,1,"");
      ret.add(gadgets);
      _itemIcons.add(gadgets);
    }
    return ret;
  }

  protected abstract List<Item> getMembers();

  protected void updateBonus()
  {
    String html=getBonusHtml();
    if (!html.isEmpty())
    {
      _bonus.setVisible(true);
      _bonus.setText(html);
    }
    else
    {
      _bonus.setVisible(false);
    }
  }

  private String getBonusHtml()
  {
    List<String> allLines=new ArrayList<String>();
    Integer level=getSetLevel();
    if (level!=null)
    {
      int index=0;
      for(SetBonus bonus : _set.getBonuses())
      {
        List<String> lines=ItemUtils.buildLinesToShowItemsSetBonus(_set,bonus,level.intValue());
        if (lines.isEmpty())
        {
          continue;
        }
        if (index>0)
        {
          allLines.add("<p>");
        }
        int nbPieces=bonus.getPiecesCount();
        allLines.add("<b>"+nbPieces+" pieces</b>");
        for(String line : lines)
        {
          allLines.add("<br>"+HtmlUtils.toHtml(line));
        }
        index++;
      }
    }
    if (allLines.isEmpty())
    {
      return "";
    }
    // Full body
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    for(String line : allLines)
    {
      sb.append(line).append(EndOfLine.NATIVE_EOL);
    }
    sb.append("</body></html>");
    return sb.toString();
  }

  protected Integer getSetLevel()
  {
    return Integer.valueOf(_set.getSetLevel());
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
    _bonus=null;
    _membersPanel=null;
    // Controllers
    _parent=null;
    if (_itemIcons!=null)
    {
      disposeItemIcons();
      _itemIcons=null;
    }
  }

  private void disposeItemIcons()
  {
    for(ItemDisplayGadgets itemIcon : _itemIcons)
    {
      itemIcon.dispose();
    }
    _itemIcons.clear();
  }
}
