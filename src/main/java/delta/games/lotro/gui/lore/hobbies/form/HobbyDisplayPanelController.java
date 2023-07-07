package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.HobbyTitleEntry;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a hobby display panel.
 * @author DAM
 */
public class HobbyDisplayPanelController extends AbstractNavigablePanelController
{
  // Controllers
  private List<HyperLinkController> _links;
  private List<ItemDisplayGadgets> _items;
  private HobbyRewardsPanelController _rewards;
  // Data
  private HobbyDescription _hobby;
  // GUI
  private JPanel _panel;

  private JLabel _name;
  private JEditorPane _details;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param hobby Hobby to show.
   */
  public HobbyDisplayPanelController(NavigatorWindowController parent, HobbyDescription hobby)
  {
    super(parent);
    _links=new ArrayList<HyperLinkController>();
    _items=new ArrayList<ItemDisplayGadgets>();
    _hobby=hobby;
    _rewards=new HobbyRewardsPanelController(parent,hobby);
    _panel=build();
  }

  @Override
  public String getTitle()
  {
    return "Hobby: "+_hobby.getName(); // I18n
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Left panel
    JPanel leftPanel=buildLeftPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,1.0,GridBagConstraints.WEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    panel.add(leftPanel,c);
    // Right panel (rewards)
    JPanel rewardsPanel=_rewards.getPanel();
    rewardsPanel.setBorder(GuiFactory.buildTitledBorder("Rewards")); // I18n
    c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    return panel;
  }

  private JPanel buildLeftPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }
    y++;

    // Items
    JPanel itemsPanel=buildItemsTable();
    itemsPanel.setBorder(GuiFactory.buildTitledBorder("Items")); // I18n
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(itemsPanel,c);
    y++;

    // Titles
    JPanel titlesPanel=buildTitlesTable();
    titlesPanel.setBorder(GuiFactory.buildTitledBorder("Titles")); // I18n
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(titlesPanel,c);
    y++;

    // Details
    _details=buildDescriptionPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Description")); // I18n
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(detailsPane,c);

    _panel=panel;
    setHobby();
    return _panel;
  }

  private JEditorPane buildDescriptionPane()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(300,100));
    editor.setOpaque(false);
    return editor;
  }

  private String buildHtml()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append(toHtml(_hobby.getDescription()));
    sb.append("</body></html>");
    return sb.toString();
  }

  private String toHtml(String text)
  {
    text=text.trim();
    text=text.replace("\n","<br>");
    return text;
  }

  private JPanel buildItemsTable()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    for(Item item : _hobby.getItems())
    {
      c.gridx=0;
      c.gridy++;
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(getParent(),item.getIdentifier(),1,"");
      _items.add(gadgets);
      // Icon
      ret.add(gadgets.getIcon(),c);
      c.gridx++;
      // Link
      ret.add(gadgets.getName(),c);
      c.gridx++;
    }
    return ret;
  }

  private JPanel buildTitlesTable()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Headers
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Proficiency"),c);c.gridx++; // I18n
    ret.add(GuiFactory.buildLabel("Title"),c);c.gridx++; // I18n
    for(HobbyTitleEntry entry : _hobby.getTitles())
    {
      c.gridx=0;
      c.gridy++;
      // Proficiency
      int proficiency=entry.getProficiency();
      ret.add(GuiFactory.buildLabel(String.valueOf(proficiency)),c);
      c.gridx++;
      // Title
      TitleDescription title=entry.getTitle();
      String rawTitleName=title.getRawName();
      String titleName=ContextRendering.render(this,rawTitleName);
      JLabel titleLabel=GuiFactory.buildLabel(titleName);
      HyperLinkController titleCtrl=TitleUiUtils.buildTitleLink(getParent(),title,titleLabel);
      ret.add(titleCtrl.getLabel(),c);
      c.gridx++;
    }
    return ret;
  }

  /**
   * Set the hobby to display.
   */
  private void setHobby()
  {
    String name=_hobby.getName();
    // Name
    _name.setText(name);
    // Details
    _details.setText(buildHtml());
    int nbTitles=_hobby.getTitles().size();
    _details.setPreferredSize(new Dimension(300,50+(30*nbTitles)));
    _details.setCaretPosition(0);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _hobby=null;
    // Controllers
    if (_links!=null)
    {
      for(HyperLinkController link : _links)
      {
        link.dispose();
      }
      _links.clear();
      _links=null;
    }
    if (_items!=null)
    {
      for(ItemDisplayGadgets gadgets : _items)
      {
        gadgets.dispose();
      }
      _items.clear();
      _items=null;
    }
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    _details=null;
  }
}
