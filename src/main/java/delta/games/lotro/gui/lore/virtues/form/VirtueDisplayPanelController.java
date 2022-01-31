package delta.games.lotro.gui.lore.virtues.form;

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
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.enums.SkillCategory;
import delta.games.lotro.common.enums.TraitNature;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.utils.gui.HtmlUtils;

/**
 * Controller for a virtue display panel.
 * @author DAM
 */
public class VirtueDisplayPanelController implements NavigablePanelController
{
  // Data
  private VirtueDescription _virtue;
  // GUI
  private JPanel _panel;
  // Controllers
  private NavigatorWindowController _parent;
  private TraitReferencesDisplayController _references;
  private VirtueStatsPanelController _stats;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param virtue Virtue to show.
   */
  public VirtueDisplayPanelController(NavigatorWindowController parent, VirtueDescription virtue)
  {
    _parent=parent;
    _virtue=virtue;
    _references=new TraitReferencesDisplayController(parent,virtue.getIdentifier());
    _stats=new VirtueStatsPanelController(virtue);
  }

  @Override
  public String getTitle()
  {
    return "Trait: "+_virtue.getName();
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
    JEditorPane description=buildEditorPane(_virtue.getDescription());
    if (description!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
      y++;
    }
    // Tooltip
    JEditorPane tooltip=buildEditorPane(_virtue.getTooltip());
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
      ImageIcon icon=LotroIconsManager.getVirtueIcon(_virtue);
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      // Name
      String name=_virtue.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Attributes
    List<String> lines=getAttributesLines();
    for(String line : lines)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panelLine.add(GuiFactory.buildLabel(line));
      panel.add(panelLine,c);
      c.gridy++;
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
    SkillCategory category=_virtue.getCategory();
    if (category!=null)
    {
      String categoryLabel=category.getLabel().trim();
      if (categoryLabel.endsWith(":"))
      {
        categoryLabel=categoryLabel.substring(0,categoryLabel.length()-1);
      }
      ret.add("Category: "+categoryLabel);
    }
    // Minimum Level
    int minLevel=_virtue.getMinLevel();
    if (minLevel>1)
    {
      ret.add("Minimum Level: "+minLevel);
    }
    TraitNature nature=_virtue.getNature();
    if (nature!=null)
    {
      ret.add("Nature: "+nature.getLabel());
    }
    int tiers=_virtue.getTiersCount();
    if (tiers>1)
    {
      ret.add("Tiers: "+tiers);
    }
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
    _virtue=null;
    // Controllers
    _parent=null;
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
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
