package delta.games.lotro.gui.lore.agents.mobs.form;

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

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.common.action.ActionEntry;
import delta.games.lotro.common.action.ActionTable;
import delta.games.lotro.common.action.ActionTableEntry;
import delta.games.lotro.common.action.ActionTables;
import delta.games.lotro.common.action.ActionTablesEntry;
import delta.games.lotro.common.enums.AgentClass;
import delta.games.lotro.common.enums.Alignment;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.Species;
import delta.games.lotro.common.enums.SubSpecies;
import delta.games.lotro.gui.utils.GadgetsControllersFactory;
import delta.games.lotro.gui.utils.IconLinkLabelGadgetsController;
import delta.games.lotro.lore.agents.AgentClassification;
import delta.games.lotro.lore.agents.EntityClassification;
import delta.games.lotro.lore.agents.mobs.MobDescription;

/**
 * Controller for a mob display panel.
 * @author DAM
 */
public class MobDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private MobDescription _mob;
  // Controllers
  private List<IconLinkLabelGadgetsController> _skills;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param mob Mob to show.
   */
  public MobDisplayPanelController(NavigatorWindowController parent, MobDescription mob)
  {
    super(parent);
    _mob=mob;
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    return "Mob: "+_mob.getName();
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
    return buildSkillsPanel();
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      // Name
      String name=_mob.getName();
      JLabel nameLabel=GuiFactory.buildLabel(name);
      nameLabel.setFont(nameLabel.getFont().deriveFont(28f).deriveFont(Font.BOLD));
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
    AgentClassification classification=_mob.getClassification();
    // Alignment
    Alignment alignment=classification.getAlignment();
    if (alignment!=null)
    {
      ret.add("Alignment: "+alignment);
    }
    // Class
    AgentClass agentClass=classification.getAgentClass();
    if (agentClass!=null)
    {
      ret.add("Class: "+agentClass);
    }
    // Classification filter??
    // Genus
    EntityClassification entityClassification=classification.getEntityClassification();
    List<Genus> genuses=entityClassification.getGenuses();
    String genusLabel=buildGenusLabel(genuses);
    if (genusLabel.length()>0)
    {
      ret.add("Genus: "+genusLabel);
    }
    // Species
    Species species=entityClassification.getSpecies();
    if (species!=null)
    {
      ret.add("Species: "+species.getLabel());
    }
    // Sub-species
    SubSpecies subSpecies=entityClassification.getSubSpecies();
    if (subSpecies!=null)
    {
      ret.add("Subspecies: "+subSpecies.getLabel());
    }
    // Geo division??
    return ret;
  }

  private String buildGenusLabel(List<Genus> genuses)
  {
    StringBuilder sb=new StringBuilder();
    for(Genus genus : genuses)
    {
      if (sb.length()>0)
      {
        sb.append(", ");
      }
      sb.append(genus.getLabel());
    }
    return sb.toString();
  }

  private JPanel buildSkillsPanel()
  {
    // Build traits gadgets controllers
    _skills=new ArrayList<IconLinkLabelGadgetsController>();

    for(SkillDescription skill : getSkills())
    {
      IconLinkLabelGadgetsController gadget=GadgetsControllersFactory.build(getParent(),skill);
      _skills.add(gadget);
    }
    return buildPanel(_skills);
  }

  private JPanel buildPanel(List<IconLinkLabelGadgetsController> gadgetsList)
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    for(IconLinkLabelGadgetsController gadgets : gadgetsList)
    {
      // Icon
      GridBagConstraints c=new GridBagConstraints(0,y,1,2,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getIcon().getIcon(),c);
      // Text
      boolean hasComplements=(gadgets.getComplement().getText().length()>0);
      int height=(hasComplements?1:2);
      c=new GridBagConstraints(1,y,1,height,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
      ret.add(gadgets.getLink().getLabel(),c);
      if (hasComplements)
      {
        c=new GridBagConstraints(1,y+1,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL, new Insets(0,0,0,0),5,5);
        ret.add(gadgets.getComplement(),c);
      }
      y+=2;
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.VERTICAL,new Insets(0,0,0,0),0,0);
    ret.add(Box.createVerticalGlue(),c);
    return ret;
  }

  private List<SkillDescription> getSkills()
  {
    List<SkillDescription> ret=new ArrayList<SkillDescription>();
    ActionTables actionTables=_mob.getActionTables();
    if (actionTables!=null)
    {
      for(ActionTablesEntry entry : actionTables.getEntries())
      {
        ActionTable table=entry.getTable();
        for(ActionTableEntry tableEntry : table.getEntries())
        {
          for(ActionEntry actionEntry : tableEntry.getActionsChain())
          {
            SkillDescription skill=actionEntry.getSkill();
            if (!ret.contains(skill))
            {
              ret.add(skill);
            }
          }
        }
      }
    }
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _mob=null;
    if (_skills!=null)
    {
      for(IconLinkLabelGadgetsController gadget : _skills)
      {
        gadget.dispose();
      }
      _skills=null;
    }
  }
}
