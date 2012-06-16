package delta.games.lotro.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.htmlparser.jericho.CharacterReference;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.StartTag;

/**
 * Set of tool methods related to using the Jericho HTML library.
 * @author DAM
 */
public class JerichoHtmlUtils
{
  /**
   * Find a tag and get its content.
   * @param root Parent tag.
   * @param tagName Tag to search.
   * @param attrName Attribute to look.
   * @param expectedValue Expected value for this attribute.
   * @return A string value or <code>null</code> if not found.
   */
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
  
  /**
   * Find a tag and get its content.
   * @param root Parent tag.
   * @param tagName Tag to search.
   * @return A string value or <code>null</code> if not found.
   */
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
  
  /**
   * Get all the child nodes of a given parent node.
   * This is a recursive search.
   * @param root Root node.
   * @return A list of nodes.
   */
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

  /**
   * Find tags.
   * @param root Parent tag.
   * @param tagName Tag to search.
   * @param attrName Attribute to look.
   * @param expectedValue Expected value for this attribute.
   * @return A possibly empty list of tag elements.
   */
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

  /**
   * Find a tag.
   * @param root Parent tag.
   * @param tagName Tag to search.
   * @param attrName Attribute to look.
   * @param expectedValue Expected value for this attribute.
   * @return A tag element or <code>null</code> if not found.
   */
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

  /**
   * Extract text from a tag.
   * @param tag Tag to use.
   * @return Extracted text.
   */
  public static String getTextFromTag(Element tag)
  {
    //TextExtractor extractor=tag.getTextExtractor();
    Renderer extractor=tag.getRenderer();
    extractor.setMaxLineLength(10000);
    extractor.setIncludeHyperlinkURLs(false);
    String text=extractor.toString();
    return text;
  }
}
