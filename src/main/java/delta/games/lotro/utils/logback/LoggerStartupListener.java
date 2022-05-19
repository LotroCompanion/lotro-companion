package delta.games.lotro.utils.logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * Logger startup listener to setup a property for the user home.
 * @author DAM
 */
public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle
{
  private boolean _started=false;

  @Override
  public void start()
  {
    if (_started) return;
    String userHome=System.getProperty("user.home");
    Context loggerContext=getContext();
    loggerContext.putProperty("DATA_HOME",userHome);
    _started=true;
  }

  @Override
  public void stop()
  {
    // Nothing
  }

  @Override
  public boolean isStarted()
  {
    return _started;
  }

  @Override
  public boolean isResetResistant()
  {
    return true;
  }

  @Override
  public void onStart(LoggerContext loggerContext)
  {
    // Nothing
  }

  @Override
  public void onReset(LoggerContext loggerContext)
  {
    // Nothing
  }

  @Override
  public void onStop(LoggerContext loggerContext)
  {
    // Nothing
  }

  @Override
  public void onLevelChange(Logger logger, Level level)
  {
    // Nothing
  }
}
