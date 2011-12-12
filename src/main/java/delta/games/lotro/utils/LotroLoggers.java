package delta.games.lotro.utils;

import org.apache.log4j.Logger;

import delta.common.utils.traces.LoggersRegistry;
import delta.common.utils.traces.LoggingConstants;

/**
 * Management class for all LOTRO loggers.
 * @author DAM
 */
public abstract class LotroLoggers
{
  /**
   * Name of the "LOTRO" logger.
   */
  public static final String LOTRO="APPS.LOTRO";
  /**
   * Name of the 'character' related LOTRO logger.
   */
  public static final String CHARACTER=LOTRO+LoggingConstants.SEPARATOR+"CHARACTER";
  /**
   * Name of the 'character log' related LOTRO logger.
   */
  public static final String CHARACTER_LOG=LOTRO+LoggingConstants.SEPARATOR+"CHARACTER_LOG";

  /**
   * Name of the 'web input' related LOTRO logger.
   */
  public static final String WEB_INPUT=LOTRO+LoggingConstants.SEPARATOR+"WEB_INPUT";

  private static final Logger _lotroLogger=LoggersRegistry.getLogger(LOTRO);
  private static final Logger _lotroCharacterLogger=LoggersRegistry.getLogger(CHARACTER);
  private static final Logger _lotroCharacterLogLogger=LoggersRegistry.getLogger(CHARACTER_LOG);
  private static final Logger _lotroWebInputLogger=LoggersRegistry.getLogger(WEB_INPUT);

  /**
   * Get the logger used for LOTRO (LOTRO).
   * @return the logger used for LOTRO.
   */
  public static Logger getLotroLogger()
  {
    return _lotroLogger;
  }

  /**
   * Get the logger used for the 'character log'.
   * @return the logger used for the 'character log'.
   */
  public static Logger getCharacterLogLogger()
  {
    return _lotroCharacterLogLogger;
  }

  /**
   * Get the logger used for the 'character'.
   * @return the logger used for the 'character'.
   */
  public static Logger getCharacterLogger()
  {
    return _lotroCharacterLogger;
  }

  /**
   * Get the logger used for the 'web input'.
   * @return the logger used for the 'web input'.
   */
  public static Logger getWebInputLogger()
  {
    return _lotroWebInputLogger;
  }
}
