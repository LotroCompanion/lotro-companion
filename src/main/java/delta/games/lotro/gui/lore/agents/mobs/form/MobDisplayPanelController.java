package delta.games.lotro.gui.lore.agents.mobs.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.character.skills.SkillDescription;
import delta.games.lotro.common.action.ActionEntry;
import delta.games.lotro.common.action.ActionTable;
import delta.games.lotro.common.action.ActionTableEntry;
import delta.games.lotro.common.action.ActionTables;
import delta.games.lotro.common.action.ActionTablesEntry;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.EffectGenerator;
import delta.games.lotro.common.enums.AgentClass;
import delta.games.lotro.common.enums.Alignment;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.Species;
import delta.games.lotro.common.enums.SubSpecies;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
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
  private List<NavigationHyperLink> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param mob Mob to show.
   */
  public MobDisplayPanelController(NavigatorWindowController parent, MobDescription mob)
  {
    super(parent);
    _mob=mob;
    _links=new ArrayList<NavigationHyperLink>();
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
    int y=0;
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    y++;
    // Startup effects
    JPanel startupEffectsPanel=buildStartupEffectsPanel();
    if (startupEffectsPanel!=null)
    {
      startupEffectsPanel.setBorder(GuiFactory.buildTitledBorder("Effects"));
      c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(startupEffectsPanel,c);
      y++;
    }
    // Skills
    JPanel skillsPanel=buildSkillsPanel();
    JScrollPane sp=GuiFactory.buildScrollPane(skillsPanel);
    sp.setBorder(GuiFactory.buildTitledBorder("Skills"));
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(sp,c);

    panel.setPreferredSize(new Dimension(500,500));
    return panel;
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
    if (!genusLabel.isEmpty())
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
    // Geo division: no! Not displayable AS IS.
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
    List<NavigationHyperLink> links=new ArrayList<NavigationHyperLink>();
    for(SkillDescription skill : getSkills())
    {
      PageIdentifier pageId=ReferenceConstants.getSkillReference(skill.getIdentifier());
      String text=skill.getName();
      NavigationHyperLink link=NavigationUtils.buildNavigationLink(getParent(),text,pageId);
      links.add(link);
    }
    _links.addAll(links);
    return NavigationUtils.buildPanel(links);
  }

  private JPanel buildStartupEffectsPanel()
  {
    List<EffectGenerator> effectGenerators=_mob.getStartupEffects();
    if (effectGenerators.isEmpty())
    {
      return null;
    }
    List<NavigationHyperLink> links=new ArrayList<NavigationHyperLink>();
    for(EffectGenerator effectGenerator : effectGenerators)
    {
      Effect effect=effectGenerator.getEffect();
      PageIdentifier pageId=ReferenceConstants.getEffectReference(effect);
      String text=effect.getName();
      NavigationHyperLink link=NavigationUtils.buildNavigationLink(getParent(),text,pageId);
      links.add(link);
    }
    _links.addAll(links);
    return NavigationUtils.buildPanel(links);
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
    Collections.sort(ret,new NamedComparator());
    return ret;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _mob=null;
    // Controllers
    if (_links!=null)
    {
      for(NavigationHyperLink links : _links)
      {
        links.dispose();
      }
      _links=null;
    }
  }
}
