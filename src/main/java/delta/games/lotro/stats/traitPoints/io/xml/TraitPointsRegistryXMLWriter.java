package delta.games.lotro.stats.traitPoints.io.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.xml.XmlFileWriterHelper;
import delta.common.utils.io.xml.XmlWriter;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.CharacterClassNameComparator;
import delta.games.lotro.stats.traitPoints.TraitPoint;
import delta.games.lotro.stats.traitPoints.TraitPointsRegistry;

/**
 * Writes a trait points registry to an XML file.
 * @author DAM
 */
public class TraitPointsRegistryXMLWriter
{
  /**
   * Write a trait points registry to an XML file.
   * @param outFile Output file.
   * @param registry Registry to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, final TraitPointsRegistry registry, String encoding)
  {
    XmlWriter writer=new XmlWriter()
    {
      @Override
      public void writeXml(TransformerHandler hd) throws Exception
      {
        write(hd,registry);
      }
    };
    XmlFileWriterHelper helper=new XmlFileWriterHelper();
    boolean ret=helper.write(outFile,encoding,writer);
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
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_ID_ATTR,XmlWriter.CDATA,id);
    }
    // Category
    String category=point.getCategory();
    if (category!=null)
    {
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_CATEGORY_ATTR,XmlWriter.CDATA,category);
    }
    // Label
    String label=point.getLabel();
    if (label!=null)
    {
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_LABEL_ATTR,XmlWriter.CDATA,label);
    }
    // Required class
    Set<CharacterClass> requiredClasses=point.getRequiredClasses();
    if (!requiredClasses.isEmpty())
    {
      String classes=buildClassRequirement(requiredClasses);
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_REQUIRED_CLASSES_ATTR,XmlWriter.CDATA,classes);
    }
    // Achievable ID
    int achievableId=point.getAchievableId();
    if (achievableId!=0)
    {
      attrs.addAttribute("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_ACHIEVABLE_ID_ATTR,XmlWriter.CDATA,String.valueOf(achievableId));
    }
    hd.startElement("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG,attrs);
    hd.endElement("","",TraitPointsRegistryXMLConstants.TRAIT_POINT_TAG);
  }

  private String buildClassRequirement(Set<CharacterClass> classes)
  {
    StringBuilder sb=new StringBuilder();
    int index=0;
    List<CharacterClass> sortedClasses=new ArrayList<CharacterClass>(classes);
    Collections.sort(sortedClasses,new CharacterClassNameComparator());
    for(CharacterClass characterClass : sortedClasses)
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
