package delta.games.lotro.gui.interceptor;

import org.apache.log4j.Logger;

import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLogListener;
import delta.games.lotro.interceptor.input.NetworkPacketsInterceptor;
import delta.games.lotro.interceptor.input.PacketEventsQueueManager;
import delta.games.lotro.interceptor.protocol.LotroPacketsReceiver;

/**
 * Controller for the network interceptor.
 * @author DAM
 */
public class InterceptorController
{
  private static final Logger LOGGER=Logger.getLogger(InterceptorController.class);

  private InterceptionLog _log;
  private NetworkPacketsInterceptor _interceptor;

  /**
   * Constructor.
   * @param listener Listener.
   */
  public InterceptorController(InterceptionLogListener listener)
  {
    _log=new InterceptionLog(listener);
  }

  /**
   * Start interception.
   */
  public void start()
  {
    if (isStarted())
    {
      return;
    }
    LOGGER.info("Starting interception");
    PacketEventsQueueManager input=new PacketEventsQueueManager();
    LotroPacketsReceiver receiver=new LotroPacketsReceiver(input.getQueue(),_log);
    //BasicPacketsReceiver receiver=new BasicPacketsReceiver(input.getQueue());
    receiver.start();
    _interceptor=new NetworkPacketsInterceptor(input);
    _interceptor.start();
  }

  /**
   * Stop interception.
   */
  public void stop()
  {
    if (!isStarted())
    {
      return;
    }
    LOGGER.info("Stop interception");
    _interceptor.stop();
    _interceptor=null;
  }

  /**
   * Indicates if the managed interceptor is started or not.
   * @return <code>true</code> if it is, <code>false</code> otherwise.
   */
  public boolean isStarted()
  {
    return _interceptor!=null;
  }

  /**
   * Get the managed log.
   * @return the managed log.
   */
  public InterceptionLog getLog()
  {
    return _log;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    stop();
    _log=null;
  }
}
