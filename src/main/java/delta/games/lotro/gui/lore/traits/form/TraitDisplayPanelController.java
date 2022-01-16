package delta.games.lotro.gui.lore.traits.form;

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
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.enums.SkillCategory;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a trait display panel.
 * @author DAM
 */
public class TraitDisplayPanelController implements NavigablePanelController
{
  // Data
  private TraitDescription _trait;
  // GUI
  private JPanel _panel;
  // Controllers
  @SuppressWarnings("unused")
  private NavigatorWindowController _parent;
  private TraitReferencesDisplayController _references;
  private TraitStatsPanelController _stats;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param trait Trait to show.
   */
  public TraitDisplayPanelController(NavigatorWindowController parent, TraitDescription trait)
  {
    _parent=parent;
    _trait=trait;
    _references=new TraitReferencesDisplayController(parent,trait.getIdentifier());
    _stats=new TraitStatsPanelController(trait);
  }

  @Override
  public String getTitle()
  {
    return "Trait: "+_trait.getName();
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
    JEditorPane description=buildEditorPane(_trait.getDescription());
    if (description!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
      y++;
    }
    // Tooltip
    JEditorPane tooltip=buildEditorPane(_trait.getTooltip());
    if (tooltip!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(tooltip,c);
      y++;
    }
    // Build components for potential tabs
    JEditorPane references=_references.getComponent();
    JPanel statsPanel=_stats.getPanel();
    if ((references!=null) || (statsPanel!=null))
    {
      JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
      // - scaling
      if (statsPanel!=null)
      {
        tabbedPane.add("Stats",buildPanelForTab(statsPanel));
      }
      // - references
      if (references!=null)
      {
        tabbedPane.add("References",buildPanelForTab(references));
      }
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
      panel.add(tabbedPane,c);
      y++;
    }
    else
    {
      JPanel empty=GuiFactory.buildPanel(new BorderLayout());
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
      panel.add(empty,c);
      y++;
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
      ImageIcon icon=LotroIconsManager.getTraitIcon(_trait.getIconId());
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      // Name
      String name=_trait.getName();
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
    SkillCategory category=_trait.getCategory();
    if (category!=null)
    {
      String categoryLabel=category.getLabel().trim();
      if (categoryLabel.endsWith(":"))
      {
        categoryLabel=categoryLabel.substring(0,categoryLabel.length()-1);
      }
      ret.add("Category: "+categoryLabel);
    }
    // TODO:
    _trait.getMinLevel();
    _trait.getNature();
    _trait.getSkills();
    _trait.getStatsProvider();
    _trait.getTiersCount();
    _trait.getTooltip();
    return ret;
  }

  private JEditorPane buildEditorPane(String input)
  {
    JEditorPane editor=null;
    if ((input!=null) && (input.length()>0))
    {
      editor=GuiFactory.buildHtmlPanel();
      StringBuilder sb=new StringBuilder();
      sb.append("<html><body>");
      sb.append(HtmlUtils.toHtml(input));
      sb.append("</body></html>");
      editor.setText(sb.toString());
    }
    return editor;
  }

  @Override
  public void dispose()
  {
    // Data
    _trait=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    if (_stats!=null)
    {
      _stats.dispose();
      _stats=null;
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
