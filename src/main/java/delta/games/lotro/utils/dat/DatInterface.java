package delta.games.lotro.utils.dat;

import delta.games.lotro.dat.data.DatConfiguration;
import delta.games.lotro.dat.data.DataFacade;
import delta.games.lotro.utils.cfg.ApplicationConfiguration;
import delta.games.lotro.utils.cfg.ConfigurationListener;

/**
 * Interface to DAT files management.
 * @author DAM
 */
public class DatInterface implements ConfigurationListener
{
  private static final DatInterface _instance=new DatInterface();

  private DatConfiguration _currentDatConfiguration;
  private DataFacade _currentFacade;

  private DatInterface()
  {
    ApplicationConfiguration applicationCfg=ApplicationConfiguration.getInstance();
    DatConfiguration datCfg=applicationCfg.getDatConfiguration();
    _currentDatConfiguration=new DatConfiguration(datCfg);
    applicationCfg.getListeners().addListener(this);
  }

  /**
   * Get the sole instance of this class.
   * @return the sole instance of this class.
   */
  public static final DatInterface getInstance()
  {
    return _instance;
  }

  @Override
  public void configurationUpdated(ApplicationConfiguration newConfiguration)
  {
    if (_currentFacade!=null)
    {
      DatConfiguration newDatCfg=newConfiguration.getDatConfiguration();
      if (!_currentDatConfiguration.same(newDatCfg))
      {
        _currentFacade.dispose();
        _currentFacade=buildFacade();
        DatConfiguration datCfg=newConfiguration.getDatConfiguration();
        _currentDatConfiguration=new DatConfiguration(datCfg);
      }
    }
  }

  /**
   * Get the facade for the DAT files.
   * @return the current facade.
   */
  public DataFacade getFacade()
  {
    if (_currentFacade==null)
    {
      _currentFacade=buildFacade();
    }
    return _currentFacade;
  }

  /**
   * Build a facade for the DAT files.
   * @return a new facade.
   */
  private DataFacade buildFacade()
  {
    ApplicationConfiguration cfg=ApplicationConfiguration.getInstance();
    DatConfiguration datCfg=cfg.getDatConfiguration();
    DataFacade facade=new DataFacade(datCfg);
    return facade;
  }
}
