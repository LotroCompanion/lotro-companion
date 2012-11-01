package delta.games.lotro.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import delta.common.utils.NumericTools;
import delta.common.utils.io.StreamTools;

/**
 * Typed properties access.
 * @author DAM
 */
public class TypedProperties
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private Properties _props;

  /**
   * Constructor.
   */
  public TypedProperties()
  {
    _props=new Properties();
  }

  /**
   * Load properties files.
   * @param inFile Input file.
   * @return <code>true</code> if it was successfull, <code>false</code> otherwise.
   */
  public boolean loadFromFile(File inFile)
  {
    boolean ret;
    Properties props=new Properties();
    FileInputStream fis=null;
    try
    {
      fis=new FileInputStream(inFile);
      props.load(fis);
      for(Map.Entry<Object,Object> entry : props.entrySet())
      {
        _props.put(entry.getKey(),entry.getValue());
      }
      ret=true;
    }
    catch(IOException ioe)
    {
      ret=false;
      _logger.error("Cannot load properties from file ["+inFile+"]!",ioe);
    }
    finally
    {
      StreamTools.close(fis);
    }
    return ret;
  }

  /**
   * Get the value of an integer property.
   * @param name Property name.
   * @param defaultValue Default value, used if the property does not exist.
   * @return An integer value.
   */
  public int getIntProperty(String name, int defaultValue)
  {
    int ret=defaultValue;
    String value=_props.getProperty(name,null);
    if (value!=null)
    {
      ret=NumericTools.parseInt(value,ret);
    }
    return ret;
  }
}
