package delta.games.lotro.gui.lore.items.legendary.relics.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicsCategory;

/**
 * Controller for a relic display panel.
 * @author DAM
 */
public class RelicDisplayPanelController implements NavigablePanelController
{
  // Data
  private Relic _relic;
  // GUI
  private JPanel _panel;
  // Controllers
  private RelicReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param relic Relic to show.
   */
  public RelicDisplayPanelController(NavigatorWindowController parent, Relic relic)
  {
    _relic=relic;
    _references=new RelicReferencesDisplayController(parent,relic.getIdentifier());
  }

  @Override
  public String getTitle()
  {
    return "Relic: "+_relic.getName();
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

  private MultilineLabel2 buildStatsDisplay()
  {
    MultilineLabel2 statsLabel=null;
    BasicStatsSet stats=_relic.getStats();
    if (stats.getStatsCount()>0)
    {
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      statsLabel=new MultilineLabel2();
      statsLabel.setText(lines);
      statsLabel.setBorder(GuiFactory.buildTitledBorder("Stats"));
    }
    return statsLabel;
  }

  private JPanel buildCenter()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    // Stats
    MultilineLabel2 stats=buildStatsDisplay();
    if (stats!=null)
    {
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(stats,c);
      y++;
    }
    // References
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      JScrollPane scrollPane=GuiFactory.buildScrollPane(references);
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
      panel.add(scrollPane,c);
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

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Icon
      ImageIcon icon=LotroIconsManager.getRelicIcon(_relic.getIconFilename());
      JLabel iconLabel=GuiFactory.buildIconLabel(icon);
      panelLine.add(iconLabel);
      // Name
      String name=_relic.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(nameLabel);
      panel.add(panelLine,c);
      c.gridy++;
    }
    // Category
    RelicsCategory category=_relic.getCategory();
    if (category!=null)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Category: "+category.getName()));
    }
    // Type(s)
    String types=_relic.getTypesStr();
    if ((types!=null) && (types.length()>0))
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Type: "+types));
    }
    // Allowed slot(s)
    String slots=_relic.getAllowedSlotsForUI();
    if ((slots!=null) && (slots.length()>0))
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Slot: "+slots));
    }
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(_relic.getUsageRequirement());
    if (requirements.length()>0)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(GuiFactory.buildLabel("Requirements: "+requirements));
    }

    // Padding to push everything on left
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.HORIZONTAL;
    c.weightx=1.0;
    panel.add(paddingPanel,c);

    return panel;
  }

  @Override
  public void dispose()
  {
    // Data
    _relic=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
