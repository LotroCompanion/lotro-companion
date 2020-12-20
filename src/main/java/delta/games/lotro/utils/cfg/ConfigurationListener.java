package delta.games.lotro.utils.cfg;

/**
 * Listener for configuration changes.
 * @author DAM
 */
public interface ConfigurationListener
{
  /**
   * Called when the application configuration was updated.
   * @param newConfiguration the new configuration.
   */
  void configurationUpdated(ApplicationConfiguration newConfiguration);
}
