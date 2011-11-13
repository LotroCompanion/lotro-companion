package delta.games.lotro.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;

/**
 * @author DAM
 */
public class JerichoHtmlUtils
{
  public static String getTagContents(Segment root, String tagName, String attrName, String expectedValue)
  {
    Element item=JerichoHtmlUtils.findElementByTagNameAndAttributeValue(root,tagName,attrName,expectedValue);
    String ret=null;
    if (item!=null)
    {
      ret=CharacterReference.decodeCollapseWhiteSpace(item.getContent());
    }
    return ret;
  }
  
  public static String getTagContents(Segment root, String tagName)
  {
    Element item=root.getFirstElement(tagName);
    String ret=null;
    if (item!=null)
    {
      ret=CharacterReference.decodeCollapseWhiteSpace(item.getContent());
    }
    return ret;
  }
  
  public static List<Segment> getChildNodes(Element root)
  {
    List<Segment> nodes=new ArrayList<Segment>();
    Segment seg=root.getContent();
    for(Iterator<Segment> it=seg.getNodeIterator();it.hasNext();)
    {
      Segment s=it.next();
      nodes.add(s);
    }
    return nodes;
  }

  public static List<Element> findElementsByTagNameAndAttributeValue(Segment root, String tagName, String attrName, String expectedValue)
  {
    List<Element> ret=new ArrayList<Element>();
    List<Element> elements=root.getAllElements(tagName);
    for(Element element : elements)
    {
      StartTag tag=element.getStartTag();
      String value=tag.getAttributeValue(attrName);
      if (expectedValue.equals(value))
      {
        ret.add(element);
      }
    }
    return ret;
  }

  public static Element findElementByTagNameAndAttributeValue(Segment root, String tagName, String attrName, String expectedValue)
  {
    Element ret=null;
    List<Element> elements=root.getAllElements(tagName);
    for(Element element : elements)
    {
      StartTag tag=element.getStartTag();
      String value=tag.getAttributeValue(attrName);
      if (expectedValue.equals(value))
      {
        ret=element;
        break;
      }
    }
    return ret;
  }
}
