package delta.games.lotro.gui.common.requirements.table;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.tables.CellDataProvider;
import delta.common.ui.swing.tables.DefaultTableColumnController;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.requirements.FactionRequirement;
import delta.games.lotro.common.requirements.ProfessionRequirement;
import delta.games.lotro.common.requirements.UsageRequirement;
import delta.games.lotro.gui.utils.l10n.Labels;

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
    ret.add(buildRequiredClassColumn(RequirementColumnIds.REQUIRED_CLASS.name()));
    // Race column
    ret.add(buildRequiredRaceColumn(RequirementColumnIds.REQUIRED_RACE.name()));
    // Min level column
    ret.add(buildMinLevelColumn(RequirementColumnIds.REQUIRED_LEVEL.name()));
    // Max level column
    ret.add(buildMaxLevelColumn(RequirementColumnIds.MAX_LEVEL.name()));
    // Crafting requirement column
    ret.add(buildCraftingRequirementColumn(RequirementColumnIds.CRAFTING.name()));
    // Reputation requirement column
    ret.add(buildReputationRequirementColumn(RequirementColumnIds.REPUTATION.name()));
    return ret;
  }

  /**
   * Build a column to show class requirement.
   * @param id Column identifier.
   * @return the new column.
   */
  public static DefaultTableColumnController<UsageRequirement,AbstractClassDescription> buildRequiredClassColumn(String id)
  {
    CellDataProvider<UsageRequirement,AbstractClassDescription> classCell=new CellDataProvider<UsageRequirement,AbstractClassDescription>()
    {
      @Override
      public AbstractClassDescription getData(UsageRequirement requirement)
      {
        return requirement.getRequiredClass();
      }
    };
    String columnName=Labels.getLabel("requirements.column.class");
    DefaultTableColumnController<UsageRequirement,AbstractClassDescription> classColumn=new DefaultTableColumnController<UsageRequirement,AbstractClassDescription>(id,columnName,AbstractClassDescription.class,classCell);
    classColumn.setWidthSpecs(100,100,100);
    return classColumn;
  }

  /**
   * Build a column to show race requirement.
   * @param id Column identifier.
   * @return the new column.
   */
  public static DefaultTableColumnController<UsageRequirement,RaceDescription> buildRequiredRaceColumn(String id)
  {
    CellDataProvider<UsageRequirement,RaceDescription> raceCell=new CellDataProvider<UsageRequirement,RaceDescription>()
    {
      @Override
      public RaceDescription getData(UsageRequirement requirement)
      {
        return requirement.getRequiredRace();
      }
    };
    String columnName=Labels.getLabel("requirements.column.race");
    DefaultTableColumnController<UsageRequirement,RaceDescription> raceColumn=new DefaultTableColumnController<UsageRequirement,RaceDescription>(id,columnName,RaceDescription.class,raceCell);
    raceColumn.setWidthSpecs(80,100,80);
    return raceColumn;
  }

  /**
   * Build a column to show minimum level requirement.
   * @param id Column identifier.
   * @return the new column.
   */
  public static DefaultTableColumnController<UsageRequirement,Integer> buildMinLevelColumn(String id)
  {
    CellDataProvider<UsageRequirement,Integer> minLevelCell=new CellDataProvider<UsageRequirement,Integer>()
    {
      @Override
      public Integer getData(UsageRequirement requirement)
      {
        return requirement.getMinLevel();
      }
    };
    String columnName=Labels.getLabel("requirements.column.minLevel");
    DefaultTableColumnController<UsageRequirement,Integer> minLevelColumn=new DefaultTableColumnController<UsageRequirement,Integer>(id,columnName,Integer.class,minLevelCell);
    minLevelColumn.setWidthSpecs(40,40,40);
    return minLevelColumn;
  }

  /**
   * Build a column to show maximum level requirement.
   * @param id Column identifier.
   * @return the new column.
   */
  public static DefaultTableColumnController<UsageRequirement,Integer> buildMaxLevelColumn(String id)
  {
    CellDataProvider<UsageRequirement,Integer> maxLevelCell=new CellDataProvider<UsageRequirement,Integer>()
    {
      @Override
      public Integer getData(UsageRequirement requirement)
      {
        return requirement.getMaxLevel();
      }
    };
    String columnName=Labels.getLabel("requirements.column.maxLevel");
    DefaultTableColumnController<UsageRequirement,Integer> maxLevelColumn=new DefaultTableColumnController<UsageRequirement,Integer>(id,columnName,Integer.class,maxLevelCell);
    maxLevelColumn.setWidthSpecs(40,40,40);
    return maxLevelColumn;
  }

  /**
   * Build a column to show crafting requirement.
   * @param id Column identifier.
   * @return the new column.
   */
  public static DefaultTableColumnController<UsageRequirement,ProfessionRequirement> buildCraftingRequirementColumn(String id)
  {
    CellDataProvider<UsageRequirement,ProfessionRequirement> cell=new CellDataProvider<UsageRequirement,ProfessionRequirement>()
    {
      @Override
      public ProfessionRequirement getData(UsageRequirement requirement)
      {
        return requirement.getProfessionRequirement();
      }
    };
    String columnName=Labels.getLabel("requirements.column.craftingRequirement");
    DefaultTableColumnController<UsageRequirement,ProfessionRequirement> column=new DefaultTableColumnController<UsageRequirement,ProfessionRequirement>(id,columnName,ProfessionRequirement.class,cell);
    column.setWidthSpecs(20,150,150);
    return column;
  }

  /**
   * Build a column to show reputation requirement.
   * @param id Column identifier.
   * @return the new column.
   */
  public static DefaultTableColumnController<UsageRequirement,FactionRequirement> buildReputationRequirementColumn(String id)
  {
    CellDataProvider<UsageRequirement,FactionRequirement> cell=new CellDataProvider<UsageRequirement,FactionRequirement>()
    {
      @Override
      public FactionRequirement getData(UsageRequirement requirement)
      {
        return requirement.getFactionRequirement();
      }
    };
    String columnName=Labels.getLabel("requirements.column.factionRequirement");
    DefaultTableColumnController<UsageRequirement,FactionRequirement> column=new DefaultTableColumnController<UsageRequirement,FactionRequirement>(id,columnName,FactionRequirement.class,cell);
    column.setWidthSpecs(20,200,200);
    return column;
  }
}
