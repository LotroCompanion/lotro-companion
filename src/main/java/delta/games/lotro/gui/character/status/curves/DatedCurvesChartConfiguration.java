package delta.games.lotro.gui.character.status.curves;

/**
 * Configuration of a dated curves chart.
 * @author DAM
 */
public class DatedCurvesChartConfiguration
{
  private String _chartTitle;
  private String _timeAxisLabel;
  private String _valueAxisLabel;
  private double[] _valueAxisTicks;
  private boolean _useSquareMoves;

  /**
   * Constructor.
   */
  public DatedCurvesChartConfiguration()
  {
    _chartTitle="???";
    _timeAxisLabel="Time"; // I18n
    _valueAxisLabel="Values"; // I18n
    _valueAxisTicks=null;
    _useSquareMoves=true;
  }

  /**
   * Get the title of the chart.
   * @return a title.
   */
  public String getChartTitle()
  {
    return _chartTitle;
  }

  /**
   * Set the title of the chart.
   * @param title Title to set.
   */
  public void setChartTitle(String title)
  {
    _chartTitle=title;
  }

  /**
   * Get the label for the time axis.
   * @return a label.
   */
  public String getTimeAxisLabel()
  {
    return _timeAxisLabel;
  }

  /**
   * Get the label for the value axis.
   * @return a label.
   */
  public String getValueAxisLabel()
  {
    return _valueAxisLabel;
  }

  /**
   * Set the label for the value axis. 
   * @param label Label to set.
   */
  public void setValueAxisLabel(String label)
  {
    _valueAxisLabel=label;
  }

  /**
   * Get the ticks for the value axis.
   * @return some tick values, or <code>null</code> to use defaults.
   */
  public double[] getValueAxisTicks()
  {
    return _valueAxisTicks;
  }

  /**
   * Set the ticks for the value axis.
   * @param ticks Ticks to set.
   */
  public void setValueAxisTicks(double[] ticks)
  {
    _valueAxisTicks = ticks;
  }

  /**
   * Get the the 'use square moves' flag.
   * @return a boolean value.
   */
  public boolean useSquareMoves()
  {
    return _useSquareMoves;
  }

  /**
   * Set the the 'use square moves' flag.
   * @param useSquareMoves Value to set.
   */
  public void setUseSquareMoves(boolean useSquareMoves)
  {
    _useSquareMoves=useSquareMoves;
  }
}
