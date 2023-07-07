package delta.games.lotro.gui.common.requirements.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.requirements.UsageRequirement;

/**
 * Builder for columns that show requirements data.
 * @author DAM
 */
public class RequirementsColumnsBuilder
{
  /**
   * Build columns to display requirements.
   * @return A list of columns controllers for requirements.
   */
  public static List<DefaultTableColumnController<UsageRequirement,?>> buildRequirementsColumns()
  {
    List<DefaultTableColumnController<UsageRequirement,?>> ret=new ArrayList<DefaultTableColumnController<UsageRequirement,?>>();
    // Class column
    {
      CellDataProvider<UsageRequirement,AbstractClassDescription> classCell=new CellDataProvider<UsageRequirement,AbstractClassDescription>()
      {
        @Override
        public AbstractClassDescription getData(UsageRequirement requirement)
        {
          return requirement.getRequiredClass();
        }
      };
      DefaultTableColumnController<UsageRequirement,AbstractClassDescription> classColumn=new DefaultTableColumnController<UsageRequirement,AbstractClassDescription>(RequirementColumnIds.REQUIRED_CLASS.name(),"Class",AbstractClassDescription.class,classCell); // I18n
      classColumn.setWidthSpecs(80,100,80);
      ret.add(classColumn);
    }
    // Race column
    {
      CellDataProvider<UsageRequirement,RaceDescription> raceCell=new CellDataProvider<UsageRequirement,RaceDescription>()
      {
        @Override
        public RaceDescription getData(UsageRequirement requirement)
        {
          return requirement.getRequiredRace();
        }
      };
      DefaultTableColumnController<UsageRequirement,RaceDescription> raceColumn=new DefaultTableColumnController<UsageRequirement,RaceDescription>(RequirementColumnIds.REQUIRED_RACE.name(),"Race",RaceDescription.class,raceCell); // I18n
      raceColumn.setWidthSpecs(80,100,80);
      ret.add(raceColumn);
    }
    // Min level column
    {
      CellDataProvider<UsageRequirement,Integer> minLevelCell=new CellDataProvider<UsageRequirement,Integer>()
      {
        @Override
        public Integer getData(UsageRequirement requirement)
        {
          return requirement.getMinLevel();
        }
      };
      DefaultTableColumnController<UsageRequirement,Integer> minLevelColumn=new DefaultTableColumnController<UsageRequirement,Integer>(RequirementColumnIds.REQUIRED_LEVEL.name(),"Min Level",Integer.class,minLevelCell); // I18n
      minLevelColumn.setWidthSpecs(40,40,40);
      ret.add(minLevelColumn);
    }
    // Max level column
    {
      CellDataProvider<UsageRequirement,Integer> maxLevelCell=new CellDataProvider<UsageRequirement,Integer>()
      {
        @Override
        public Integer getData(UsageRequirement requirement)
        {
          return requirement.getMaxLevel();
        }
      };
      DefaultTableColumnController<UsageRequirement,Integer> maxLevelColumn=new DefaultTableColumnController<UsageRequirement,Integer>(RequirementColumnIds.MAX_LEVEL.name(),"Max Level",Integer.class,maxLevelCell); // I18n
      maxLevelColumn.setWidthSpecs(40,40,40);
      ret.add(maxLevelColumn);
    }
    return ret;
  }
}
