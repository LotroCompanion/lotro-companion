package delta.games.lotro.gui.lore.agents.mobs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.enums.AgentClass;
import delta.games.lotro.common.enums.Alignment;
import delta.games.lotro.common.enums.ClassificationFilter;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.Species;
import delta.games.lotro.common.enums.SubSpecies;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.lore.agents.mobs.MobDescription;

/**
 * Utility methods for mobs-related UIs.
 * @author DAM
 */
public class MobsUiUtils
{
  /**
   * Get the alignments used by the given mobs.
   * @param mobs List of mobs to use.
   * @return A list of alignments.
   */
  public static List<Alignment> getAlignments(List<MobDescription> mobs)
  {
    Set<Alignment> values=new HashSet<Alignment>();
    for(MobDescription mob : mobs)
    {
      values.add(mob.getClassification().getAlignment());
    }
    List<Alignment> ret=new ArrayList<Alignment>(values);
    Collections.sort(ret,new LotroEnumEntryNameComparator<Alignment>());
    return ret;
  }

  /**
   * Get the agent classes used by the given mobs.
   * @param mobs List of mobs to use.
   * @return A list of agent classes.
   */
  public static List<AgentClass> getAgentClasses(List<MobDescription> mobs)
  {
    Set<AgentClass> values=new HashSet<AgentClass>();
    for(MobDescription mob : mobs)
    {
      values.add(mob.getClassification().getAgentClass());
    }
    List<AgentClass> ret=new ArrayList<AgentClass>(values);
    Collections.sort(ret,new LotroEnumEntryNameComparator<AgentClass>());
    return ret;
  }

  /**
   * Get the classifications used by the given mobs.
   * @param mobs List of mobs to use.
   * @return A list of classifications.
   */
  public static List<ClassificationFilter> getClassifications(List<MobDescription> mobs)
  {
    Set<ClassificationFilter> values=new HashSet<ClassificationFilter>();
    for(MobDescription mob : mobs)
    {
      values.add(mob.getClassification().getClassificationFilter());
    }
    List<ClassificationFilter> ret=new ArrayList<ClassificationFilter>(values);
    Collections.sort(ret,new LotroEnumEntryNameComparator<ClassificationFilter>());
    return ret;
  }

  /**
   * Get the genuses used by the given mobs.
   * @param mobs List of mobs to use.
   * @return A list of genuses.
   */
  public static List<Genus> getGenuses(List<MobDescription> mobs)
  {
    Set<Genus> values=new HashSet<Genus>();
    for(MobDescription mob : mobs)
    {
      values.add(mob.getClassification().getEntityClassification().getGenuses().get(0));
    }
    List<Genus> ret=new ArrayList<Genus>(values);
    Collections.sort(ret,new LotroEnumEntryNameComparator<Genus>());
    return ret;
  }

  /**
   * Get the species used by the given mobs.
   * @param mobs List of mobs to use.
   * @return A list of species.
   */
  public static List<Species> getSpecies(List<MobDescription> mobs)
  {
    Set<Species> values=new HashSet<Species>();
    for(MobDescription mob : mobs)
    {
      values.add(mob.getClassification().getEntityClassification().getSpecies());
    }
    List<Species> ret=new ArrayList<Species>(values);
    Collections.sort(ret,new LotroEnumEntryNameComparator<Species>());
    return ret;
  }

  /**
   * Get the subspecies used by the given mobs.
   * @param mobs List of mobs to use.
   * @return A list of subspecies.
   */
  public static List<SubSpecies> getSubspecies(List<MobDescription> mobs)
  {
    Set<SubSpecies> values=new HashSet<SubSpecies>();
    for(MobDescription mob : mobs)
    {
      values.add(mob.getClassification().getEntityClassification().getSubSpecies());
    }
    List<SubSpecies> ret=new ArrayList<SubSpecies>(values);
    Collections.sort(ret,new LotroEnumEntryNameComparator<SubSpecies>());
    return ret;
  }


  /**
   * Build a combo-box controller to choose a mob.
   * @param mobs Mobs to show.
   * @param includeEmptyItem Include empty item.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<MobDescription> builMobsCombo(List<MobDescription> mobs, boolean includeEmptyItem)
  {
    ComboBoxController<MobDescription> ctrl=new ComboBoxController<MobDescription>();
    if (includeEmptyItem)
    {
      ctrl.addEmptyItem("");
    }
    Collections.sort(mobs,new NamedComparator());
    for(MobDescription mob : mobs)
    {
      String label=mob.getName();
      ctrl.addItem(mob,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }
}
