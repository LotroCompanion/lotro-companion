package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.lore.titles.TitleUiUtils.TitleRenderingFormat;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.HobbyTitleEntry;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.titles.TitleDescription;

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

  @Override
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Left panel
    JPanel leftPanel=buildLeftPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(leftPanel,c);
    // Right panel
    JPanel rightPanel=buildRightPanel();
    c=new GridBagConstraints(1,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(rightPanel,c);
    // Size
    panel.setPreferredSize(new Dimension(900,600));
    return panel;
  }

  private JPanel buildRightPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Description
    JEditorPane description=buildDescriptionPane();
    JScrollPane descriptionPane=GuiFactory.buildScrollPane(description);
    descriptionPane.setBorder(GuiFactory.buildTitledBorder("Description")); // I18n
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(descriptionPane,c);
    y++;
    // Rewards
    JPanel rewardsPanel=_rewards.getPanel();
    rewardsPanel.setBorder(GuiFactory.buildTitledBorder("Rewards")); // I18n
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(rewardsPanel,c);
    return panel;
  }

  private JPanel buildLeftPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    JPanel firstLine=GuiFactory.buildPanel(new FlowLayout());
    // Icon
    ImageIcon icon=LotroIconsManager.getHobbyIcon(_hobby.getIconId());
    firstLine.add(GuiFactory.buildIconLabel(icon));
    // Name
    String name=_hobby.getName();
    JLabel nameLabel=GuiFactory.buildLabel(name);
    nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    firstLine.add(nameLabel);
    panel.add(firstLine,c);
    y++;
    // Minimum level
    int minLevel=_hobby.getMinLevel();
    String minLevelText="Min level: "+L10n.getString(minLevel);
    JLabel minLevelLabel=GuiFactory.buildLabel(minLevelText);
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    panel.add(minLevelLabel,c);
    y++;
    // Max daily proficiency gain
    int maxDailyGain=_hobby.getDailyProficiencyGainLimit();
    String maxDailyGainText="Maximum daily proficiency gain: "+L10n.getString(maxDailyGain);
    JLabel gainLabel=GuiFactory.buildLabel(maxDailyGainText);
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    panel.add(gainLabel,c);
    y++;

    // Items
    JPanel itemsPanel=buildItemsTable();
    itemsPanel.setBorder(GuiFactory.buildTitledBorder("Items")); // I18n
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(itemsPanel,c);
    y++;

    // Titles
    JPanel titlesPanel=buildTitlesTable();
    titlesPanel.setBorder(GuiFactory.buildTitledBorder("Titles")); // I18n
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(titlesPanel,c);
    y++;

    // Trainer Info
    JEditorPane trainerInfo=buildTrainerInfoPane();
    JScrollPane trainerInfoPane=GuiFactory.buildScrollPane(trainerInfo);
    trainerInfoPane.setBorder(GuiFactory.buildTitledBorder("Trainer Info")); // I18n
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(trainerInfoPane,c);

    return panel;
  }

  private JEditorPane buildDescriptionPane()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(300,50));
    editor.setOpaque(false);
    editor.setText(buildDescriptionHtml());
    editor.setCaretPosition(0);
    return editor;
  }

  private JEditorPane buildTrainerInfoPane()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(300,100));
    editor.setOpaque(false);
    editor.setText(buildTrainerInfoHtml());
    editor.setCaretPosition(0);
    return editor;
  }

  private String buildDescriptionHtml()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append(toHtml(_hobby.getDescription()));
    sb.append("</body></html>");
    return sb.toString();
  }

  private String buildTrainerInfoHtml()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append(toHtml(_hobby.getTrainerDisplayInfo()));
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
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(getParent(),item.getIdentifier(),1,"");
      _items.add(gadgets);
      // Icon
      ret.add(gadgets.getIcon(),c);
      c.gridx++;
      // Link
      ret.add(gadgets.getName(),c);
      c.gridx++;
      c.gridy++;
    }
    c=new GridBagConstraints(c.gridx,c.gridy,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(Box.createGlue(),c);
    return ret;
  }

  private JPanel buildTitlesTable()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Headers
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
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
      JLabel titleLabel=null;
      TitleDescription title=entry.getTitle();
      if (title!=null)
      {
        PageIdentifier titlePageId=ReferenceConstants.getTitleReference(title.getIdentifier());
        String titleName=TitleUiUtils.renderTitle(getParent(),title,TitleRenderingFormat.MINIMAL);
        NavigationHyperLink titleLink=new NavigationHyperLink(getParent(),titleName,titlePageId);
        _links.add(titleLink);
        titleLabel=titleLink.getLabel();
      }
      else
      {
        titleLabel=GuiFactory.buildLabel("-");
      }
      ret.add(titleLabel,c);
      c.gridx++;
    }
    c=new GridBagConstraints(3,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(Box.createGlue(),c);
    return ret;
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
  }
}
