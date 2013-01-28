package delta.games.lotro;

import java.io.File;
import java.util.HashMap;

import delta.games.lotro.utils.TypedProperties;

/**
 * Preferences manager.
 * @author DAM
 */
public class Preferences
{
  private static final String NAME_PROPERTY="__name__";
  private File _rootDir;
  private HashMap<String,TypedProperties> _props;

  /**
   * Constructor.
   * @param rootDir Root directorty for preferences files.
   */
  public Preferences(File rootDir)
  {
    _rootDir=rootDir;
    _props=new HashMap<String,TypedProperties>();
  }

  private File getPreferencesFile(String name)
  {
    String fileName=name+".properties";
    File ret=new File(_rootDir,fileName);
    return ret;
  }

  /**
   * Get a preferences set.
   * @param name Name of preferences set.
   * @return A preferences set.
   */
  public TypedProperties getPreferences(String name)
  {
    TypedProperties props=_props.get(name);
    if (props==null)
    {
      props=new TypedProperties();
      File propsFile=getPreferencesFile(name);
      if (propsFile.canRead())
      {
        props.loadFromFile(propsFile);
      }
      props.setStringProperty(NAME_PROPERTY,name);
      _props.put(name,props);
    }
    return props;
  }

  /**
   * Save all preferences.
   * @return <code>true</code> if if was successfull, <code>false</code> otherwise.
   */
  public boolean saveAllPreferences()
  {
    boolean ok=true;
    for(TypedProperties props : _props.values())
    {
      boolean localOK=savePreferences(props);
      if (!localOK)
      {
        ok=false;
      }
    }
    return ok;
  }

  /**
   * Save a preferences set.
   * @param properties Preferences to save.
   * @return <code>true</code> if if was successfull, <code>false</code> otherwise.
   */
  public boolean savePreferences(TypedProperties properties)
  {
    boolean ok=false;
    String name=properties.getStringProperty(NAME_PROPERTY,null);
    if (name!=null)
    {
      File propsFile=getPreferencesFile(name);
      ok=properties.saveToFile(propsFile);
    }
    return ok;
  }
}
