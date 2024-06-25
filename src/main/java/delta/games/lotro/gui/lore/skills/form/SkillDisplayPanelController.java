package delta.games.lotro.gui.lore.skills.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.common.enums.SkillCategory;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.effects.SkillEffectsDisplay;
import delta.games.lotro.lore.parameters.Game;
import delta.games.lotro.utils.ContextPropertyNames;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Controller for a skill display panel.
 * @author DAM
 */
public class SkillDisplayPanelController implements NavigablePanelController
{
  // Data
  private SkillDescription _skill;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private SkillReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param skill Skill to show.
   */
  public SkillDisplayPanelController(NavigatorWindowController parent, SkillDescription skill)
  {
    _parent=parent;
    _skill=skill;
    _references=new SkillReferencesDisplayController(parent,skill.getIdentifier());
  }

  @Override
  public String getTitle()
  {
    return "Skill: "+_skill.getName();
  }

  @Override
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

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenter();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(center,c);
    panel.setPreferredSize(new Dimension(500,500));
    return panel;
  }

  private JPanel buildCenter()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Description
    JEditorPane description=buildDescription();
    if (description!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
      y++;
    }
    // Build components for potential tabs
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
      // - references
      tabbedPane.add("References",buildPanelForTab(references));
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
      panel.add(tabbedPane,c);
    }
    else
    {
      JPanel empty=GuiFactory.buildPanel(new BorderLayout());
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
      panel.add(empty,c);
    }
    return panel;
  }

  private JPanel buildPanelForTab(Component contents)
  {
    JPanel wrapper=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JScrollPane scrollPane=GuiFactory.buildScrollPane(contents);
    wrapper.add(scrollPane,BorderLayout.CENTER);
    return wrapper;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      ImageIcon icon=LotroIconsManager.getSkillIcon(_skill.getIconId());
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      // Name
      String name=_skill.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    List<String> lines=getAttributesLines();
    for(String line : lines)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel(line));
    }
    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    panel.add(paddingPanel,c);

    return panel;
  }

  private List<String> getAttributesLines()
  {
    List<String> ret=new ArrayList<String>();
    // Category
    SkillCategory category=_skill.getCategory();
    if (category!=null)
    {
      String categoryLabel=category.getLabel().trim();
      if (categoryLabel.endsWith(":"))
      {
        categoryLabel=categoryLabel.substring(0,categoryLabel.length()-1);
      }
      ret.add("Category: "+categoryLabel);
    }
    // Effects
    Integer characterLevel=_parent.getContextProperty(ContextPropertyNames.CHARACTER_LEVEL,Integer.class);
    int skillLevel=(characterLevel!=null)?characterLevel.intValue():Game.getParameters().getMaxCharacterLevel();
    List<String> effects=SkillEffectsDisplay.showSkill(_skill,skillLevel);
    ret.addAll(effects);
    return ret;
  }

  private JEditorPane buildDescription()
  {
    JEditorPane editor=null;
    String description=_skill.getDescription();
    if (description.length()>0)
    {
      editor=GuiFactory.buildHtmlPanel();
      StringBuilder sb=new StringBuilder();
      sb.append("<html><body>");
      sb.append(HtmlUtils.toHtml(description));
      sb.append("</body></html>");
      editor.setText(sb.toString());
    }
    return editor;
  }

  @Override
  public void dispose()
  {
    // Data
    _skill=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    _parent=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
