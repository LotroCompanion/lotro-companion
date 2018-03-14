package delta.games.lotro.gui.deed.form;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.rewards.RewardsPanelController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedType;

/**
 * Controller for an deed display panel.
 * @author DAM
 */
public class DeedDisplayPanelController
{
  // Data
  private DeedDescription _deed;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _type;
  private JLabel _category;
  private JLabel _name;
  private JLabel _requirements;
  private JTextArea _description;
  private JTextArea _objectives;
  // Controllers
  private DeedDisplayWindowController _parent;
  private RewardsPanelController _rewards;
  private DeedLinksDisplayPanelController _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param deed Deed to edit.
   */
  public DeedDisplayPanelController(DeedDisplayWindowController parent, DeedDescription deed)
  {
    _parent=parent;
    _deed=deed;
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

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Icon
      _icon=GuiFactory.buildIconLabel(null);
      panelLine.add(_icon);
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }

    // Line 2
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Category
      panelLine.add(GuiFactory.buildLabel("Category: "));
      _category=GuiFactory.buildLabel("");
      panelLine.add(_category);
      // Type
      panelLine.add(GuiFactory.buildLabel("Type: "));
      _type=GuiFactory.buildLabel("");
      panelLine.add(_type);
    }
    // Line 3
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Requirements
      panelLine.add(GuiFactory.buildLabel("Requirements: "));
      _requirements=GuiFactory.buildLabel("");
      panelLine.add(_requirements);
    }

    // Rewards
    _rewards=new RewardsPanelController(_deed.getRewards());
    JPanel rewardsPanel=_rewards.getPanel();
    panel.add(rewardsPanel,c);
    c.gridy++;
    // Links
    _links=new DeedLinksDisplayPanelController(_parent,_deed);
    JPanel linksPanel=_links.getPanel();
    c=new GridBagConstraints(0,c.gridy,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(linksPanel,c);
    c.gridy++;

    // Description
    _description=GuiFactory.buildTextArea("",false);
    JScrollPane descriptionPane=GuiFactory.buildScrollPane(_description);
    descriptionPane.setBorder(GuiFactory.buildTitledBorder("Description"));
    _description.setColumns(40);
    _description.setLineWrap(true);
    _description.setWrapStyleWord(true);
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(descriptionPane,c);
    c.gridy++;

    // Objectives
    _objectives=GuiFactory.buildTextArea("",false);
    JScrollPane objectivesPane=GuiFactory.buildScrollPane(_objectives);
    objectivesPane.setBorder(GuiFactory.buildTitledBorder("Objectives"));
    _objectives.setColumns(40);
    _objectives.setLineWrap(true);
    _objectives.setWrapStyleWord(true);
    panel.add(objectivesPane,c);
    c.gridy++;

    _panel=panel;
    setItem();
    return _panel;
  }

  /**
   * Set the deed to display.
   */
  private void setItem()
  {
    String name=_deed.getName();
    // Name
    _name.setText(name);
    // Type
    DeedType type=_deed.getType();
    _type.setText((type!=null)?type.name():"-");
    // Icon
    ImageIcon icon=LotroIconsManager.getDeedTypeIcon(type);
    _icon.setIcon(icon);
    // Category
    String category=_deed.getCategory();
    _category.setText((category!=null)?category:"");
    // Requirements
    String requirements=buildRequirementString();
    _requirements.setText(requirements);
    // Description
    _description.setText(_deed.getDescription());
    _description.setEditable(false);
    _description.setCaretPosition(0);
    // Objectives
    _objectives.setText(_deed.getObjectives());
    _objectives.setEditable(false);
    _objectives.setCaretPosition(0);
  }

  /**
   * Build a requirement string.
   * @return A string, empty if no requirement.
   */
  private String buildRequirementString()
  {
    CharacterClass requiredClass=_deed.getRequiredClass();
    Race requiredRace=_deed.getRequiredRace();
    Integer minLevel=_deed.getMinLevel();
    StringBuilder sb=new StringBuilder();
    if (requiredClass!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(requiredClass.getLabel());
    }
    if (requiredRace!=null)
    {
      if (sb.length()>0) sb.append(", ");
      sb.append(requiredRace.getLabel());
    }
    if (minLevel!=null)
    {
      if (sb.length()>0) sb.append(", ");
      if (minLevel.intValue()==1000)
      {
        sb.append("level cap");
      }
      else
      {
        sb.append("level>=").append(minLevel);
      }
    }
    String ret=sb.toString();
    if (ret.isEmpty())
    {
      ret="-";
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _deed=null;
    // Controllers
    if (_rewards!=null)
    {
      _rewards.dispose();
      _rewards=null;
    }
    if (_links!=null)
    {
      _links.dispose();
      _links=null;
    }
    _parent=null;
    // UI
    _type=null;
    _category=null;
    _requirements=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _description=null;
    _objectives=null;
  }
}
