package delta.games.lotro.gui.common.requirements;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.gui.common.requirements.table.RequirementColumnIds;

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
      CellDataProvider<UsageRequirement,CharacterClass> classCell=new CellDataProvider<UsageRequirement,CharacterClass>()
      {
        @Override
        public CharacterClass getData(UsageRequirement requirement)
        {
          return requirement.getRequiredClass();
        }
      };
      DefaultTableColumnController<UsageRequirement,CharacterClass> classColumn=new DefaultTableColumnController<UsageRequirement,CharacterClass>(RequirementColumnIds.REQUIRED_CLASS.name(),"Class",CharacterClass.class,classCell);
      classColumn.setWidthSpecs(80,100,80);
      ret.add(classColumn);
    }
    // Race column
    {
      CellDataProvider<UsageRequirement,Race> raceCell=new CellDataProvider<UsageRequirement,Race>()
      {
        @Override
        public Race getData(UsageRequirement requirement)
        {
          return requirement.getRequiredRace();
        }
      };
      DefaultTableColumnController<UsageRequirement,Race> raceColumn=new DefaultTableColumnController<UsageRequirement,Race>(RequirementColumnIds.REQUIRED_RACE.name(),"Race",Race.class,raceCell);
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
      DefaultTableColumnController<UsageRequirement,Integer> minLevelColumn=new DefaultTableColumnController<UsageRequirement,Integer>(RequirementColumnIds.REQUIRED_LEVEL.name(),"Min Level",Integer.class,minLevelCell);
      minLevelColumn.setWidthSpecs(40,40,40);
      ret.add(minLevelColumn);
    }
    return ret;
  }
}
