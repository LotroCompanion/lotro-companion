package delta.games.lotro.utils;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
   * Save properties files.
   * @param outFile Output file.
   * @return <code>true</code> if it was successfull, <code>false</code> otherwise.
   */
  public boolean saveToFile(File outFile)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      fos=new FileOutputStream(outFile);
      _props.store(fos,"");
      ret=true;
    }
    catch(IOException ioe)
    {
      ret=false;
      _logger.error("Cannot save properties to file ["+outFile+"]!",ioe);
    }
    finally
    {
      StreamTools.close(fos);
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

  /**
   * Set the value of an integer property.
   * @param name Property name.
   * @param value Value to set.
   */
  public void setIntProperty(String name, int value)
  {
    _props.setProperty(name,String.valueOf(value));
  }

  /**
   * Get the value of a strings list.
   * @param name Property name.
   * @return A possibly empty list of strings or <code>null</code> if not found.
   */
  public List<String> getStringList(String name)
  {
    List<String> ret=null;
    String countKey=name+".count";
    String countStr=_props.getProperty(countKey);
    if (countStr!=null)
    {
      int nb=NumericTools.parseInt(countStr,-1);
      if (nb>0)
      {
        ret=new ArrayList<String>();
        for(int i=0;i<nb;i++)
        {
          String key=name+"."+(i+1);
          String value=_props.getProperty(key);
          ret.add(value);
        }
      }
    }
    return ret;
  }

  /**
   * Set the value of a strings list property.
   * @param name Property name.
   * @param values Values to set.
   */
  public void setStringList(String name, List<String> values)
  {
    int nbValues=values.size();
    String countKey=name+".count";
    _props.setProperty(countKey,String.valueOf(nbValues));
    for(int i=0;i<nbValues;i++)
    {
      String key=name+"."+(i+1);
      _props.setProperty(key,values.get(i));
    }
  }

  /**
   * Get the value of a string property.
   * @param name Property name.
   * @param defaultValue Default value, used if the property does not exist.
   * @return A string value or <code>null</code> if not found.
   */
  public String getStringProperty(String name, String defaultValue)
  {
    String ret=_props.getProperty(name,defaultValue);
    return ret;
  }

  /**
   * Set the value of a string property.
   * @param name Property name.
   * @param value Value to set.
   */
  public void setStringProperty(String name, String value)
  {
    _props.put(name,value);
  }

  /**
   * Get the value of a bounds property.
   * @param name Property name.
   * @return A bounds value or <code>null</code> if not found.
   */
  public Rectangle getBoundsProperty(String name)
  {
    Rectangle r=null;
    String propValue=_props.getProperty(name);
    if (propValue!=null)
    {
      String[] items=propValue.split(",");
      if ((items!=null) && (items.length==4))
      {
        int x=NumericTools.parseInt(items[0],-1);
        int y=NumericTools.parseInt(items[1],-1);
        int width=NumericTools.parseInt(items[2],-1);
        int height=NumericTools.parseInt(items[3],-1);
        if ((x!=-1) && (y!=-1) && (width!=-1) && (height!=-1))
        {
          r=new Rectangle(x,y,width,height);
        }
      }
      if (r==null)
      {
        _logger.error("Wrong rectangle format ["+propValue+"]");
      }
    }
    return r;
  }

  /**
   * Set the value of a bounds property.
   * @param name Property name.
   * @param r Value to set.
   */
  public void setBoundsProperty(String name, Rectangle r)
  {
    String value=r.x+","+r.y+","+r.width+","+r.height;
    _props.setProperty(name,value);
  }
}
