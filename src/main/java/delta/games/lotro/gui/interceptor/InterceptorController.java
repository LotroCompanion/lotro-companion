package delta.games.lotro.gui.interceptor;

import org.apache.log4j.Logger;

import delta.games.lotro.dat.data.DatConfiguration;
import delta.games.lotro.interceptor.data.InterceptionSession;
import delta.games.lotro.interceptor.data.monitoring.InterceptionLog;
import delta.games.lotro.interceptor.input.InterceptorStateListener;
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

  private InterceptionSession _session;
  private NetworkPacketsInterceptor _interceptor;
  private InterceptorStateListener _listener;

  /**
   * Constructor.
   * @param listener State listener.
   * @param configuration Configuration.
   */
  public InterceptorController(InterceptorStateListener listener, DatConfiguration configuration)
  {
    _listener=listener;
    _session=new InterceptionSession(configuration);
  }

  /**
   * Get the interception session.
   * @return the interception session.
   */
  public InterceptionSession getSession()
  {
    return _session;
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
    LotroPacketsReceiver receiver=new LotroPacketsReceiver(input.getQueue(),_session);
    //BasicPacketsReceiver receiver=new BasicPacketsReceiver(input.getQueue());
    receiver.start();
    _interceptor=new NetworkPacketsInterceptor(input,_session.getLog());
    _interceptor.setStateListener(_listener);
    boolean startOK=_interceptor.start();
    if (!startOK)
    {
      input.publishEndOfStream();
    }
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
    return (_interceptor!=null) && (_interceptor.isRunning());
  }

  /**
   * Get the managed log.
   * @return the managed log.
   */
  public InterceptionLog getLog()
  {
    return _session.getLog();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    stop();
    _session=null;
    _listener=null;
  }
}
