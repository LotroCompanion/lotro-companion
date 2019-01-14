package delta.games.lotro.gui.character.stats.curves;

import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.CharacterData;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.WellKnownStat;

/**
 * Manager for all stat curves for a single character data.
 * @author DAM
 */
public class StatCurvesWindowsManager
{
  private WindowController _parent;
  private WindowsManager _childControllers;
  private CharacterData _toon;
  private StatCurvesConfigurationFactory _factory;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param toon Manager character data.
   */
  public StatCurvesWindowsManager(WindowController parent, CharacterData toon)
  {
    _parent=parent;
    _factory=new StatCurvesConfigurationFactory(toon.getCharacterClass());
    _toon=toon;
    _childControllers=new WindowsManager();
  }

  /**
   * Show a stat curves window.
   * @param config Configuration of the targeted window.
   */
  public void showStatCurvesWindow(StatCurvesChartConfiguration config)
  {
    StatCurvesWindowController controller=getStatCurvesWindowController(config.getTitle());
    if (controller==null)
    {
      config.setLevel(_toon.getLevel());
      config.setMaxRating(config.getAutoMaxRating());
      controller=new StatCurvesWindowController(_parent,config);
      _childControllers.registerWindow(controller);
      controller.update(_toon);
      controller.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    controller.bringToFront();
  }

  private StatCurvesWindowController getStatCurvesWindowController(String key)
  {
    WindowController controller=_childControllers.getWindow(key);
    return (StatCurvesWindowController)controller;
  }

  /**
   * Get the configuration associated to a given stat.
   * @param stat Targeted stat.
   * @return a configuration or <code>null</code> if not supported.
   */
  public StatCurvesChartConfiguration getConfigForStat(StatDescription stat)
  {
    if (stat==WellKnownStat.PHYSICAL_MASTERY) return _factory.buildPhysicalDamageChart();
    if (stat==WellKnownStat.TACTICAL_MASTERY) return _factory.buildTacticalDamageChart();
    if (stat==WellKnownStat.CRITICAL_RATING) return _factory.buildCriticalsChart();
    if (stat==WellKnownStat.FINESSE) return _factory.buildFinesseChart();
    if (stat==WellKnownStat.OUTGOING_HEALING) return _factory.buildHealingChart();
    if (stat==WellKnownStat.INCOMING_HEALING) return _factory.buildIncomingHealingChart();
    if (stat==WellKnownStat.BLOCK) return _factory.buildBlockChart();
    if (stat==WellKnownStat.PARRY) return _factory.buildParryChart();
    if (stat==WellKnownStat.EVADE) return _factory.buildEvadeChart();
    if (stat==WellKnownStat.RESISTANCE) return _factory.buildResistanceChart();
    if (stat==WellKnownStat.CRITICAL_DEFENCE) return _factory.buildCriticalDefenceChart();
    if (stat==WellKnownStat.PHYSICAL_MITIGATION) return _factory.buildPhysicalMitigationChart();
    if (stat==WellKnownStat.TACTICAL_MITIGATION) return _factory.buildTacticalMitigationChart();
    if (stat==WellKnownStat.OCFW_MITIGATION) return _factory.buildOrcCraftFellWroughtMitigationChart();
    return null;
  }

  /**
   * Update contents with current toon data.
   */
  public void update()
  {
    for(WindowController controller : _childControllers.getAll())
    {
      StatCurvesWindowController statCurvesWindow=(StatCurvesWindowController)controller;
      statCurvesWindow.update(_toon);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_childControllers!=null)
    {
      _childControllers.disposeAll();
      _childControllers=null;
    }
    _parent=null;
    _toon=null;
    _factory=null;
  }
}
