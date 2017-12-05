package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.StreamTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointsRegistry;
import delta.games.lotro.stats.traitPoints.comparators.TraitPointIdComparator;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes a trait points registry to an XML file.
 * @author DAM
 */
public class TraitPointsRegistryXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";

  /**
   * Write a registry to an XML file.
   * @param outFile Output file.
   * @param registry Registry to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, TraitPointsRegistry registry, String encoding)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      File parentFile=outFile.getParentFile();
      if (!parentFile.exists())
      {
        parentFile.mkdirs();
      }
      fos=new FileOutputStream(outFile);
      SAXTransformerFactory tf=(SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler hd=tf.newTransformerHandler();
      Transformer serializer=hd.getTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING,encoding);
      serializer.setOutputProperty(OutputKeys.INDENT,"yes");

      StreamResult streamResult=new StreamResult(fos);
      hd.setResult(streamResult);
      hd.startDocument();
      write(hd,registry);
      hd.endDocument();
      ret=true;
    }
    catch (Exception exception)
    {
      _logger.error("",exception);
      ret=false;
    }
    finally
    {
      StreamTools.close(fos);
    }
    return ret;
  }

  /**
   * Write a trait points registry to the given XML stream.
   * @param hd XML output stream.
   * @param registry Registry to write.
   * @throws Exception If an error occurs.
   */
  public void write(TransformerHandler hd, TraitPointsRegistry registry) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();
    hd.startElement("","",TraitPointsRegistryXMLConstants.TRAIT_POINTS_REGISTRY_TAG,attrs);

    List<TraitPoint> points=registry.getAll();
    Collections.sort(points,new TraitPointIdComparator());

    for(TraitPoint point : points)
    {
      write(hd,point);
    }
    hd.endElement("","",TraitPointsRegistryXMLConstants.TRAIT_POINTS_REGISTRY_TAG);
  }

  /**
   * Write a trait points registry to the given XML stream.
   * @param hd XML output stream.
   * @param point Trait point to write.
   * @throws Exception
   */
  private void write(TransformerHandler hd, TraitPoint point) throws Exception
  {
    AttributesImpl attrs=new AttributesImpl();

    // Identifier
    String id=point.getId();
    if (id!=null)
    {
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_ID_ATTR,CDATA,id);
    }
    // Category
    String category=point.getCategory();
    if (category!=null)
    {
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_CATEGORY_ATTR,CDATA,category);
    }
    // Label
    String label=point.getLabel();
    if (label!=null)
    {
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_LABEL_ATTR,CDATA,label);
    }
    // Required class
    Set<CharacterClass> requiredClasses=point.getRequiredClasses();
    if (!requiredClasses.isEmpty())
    {
      String classes=buildClassRequirement(requiredClasses);
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_REQUIRED_CLASSES_ATTR,CDATA,classes);
    }
    hd.startElement("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG,attrs);
    hd.endElement("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG);
  }

  private String buildClassRequirement(Set<CharacterClass> classes)
  {
    StringBuilder sb=new StringBuilder();
    int index=0;
    for(CharacterClass characterClass : classes)
    {
      if (index>0)
      {
        sb.append(',');
      }
      String key=characterClass.getKey();
      sb.append(key);
      index++;
    }
    return sb.toString();
  }
}
