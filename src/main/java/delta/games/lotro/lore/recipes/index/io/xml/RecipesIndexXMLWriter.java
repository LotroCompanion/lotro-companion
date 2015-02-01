package delta.games.lotro.lore.recipes.index.io.xml;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.StreamTools;
import delta.games.lotro.lore.recipes.index.RecipeSummary;
import delta.games.lotro.lore.recipes.index.RecipesIndex;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO recipe indexes to XML files.
 * @author DAM
 */
public class RecipesIndexXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write a recipes index to a XML file.
   * @param outFile Output file.
   * @param index Index to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, RecipesIndex index, String encoding)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      fos=new FileOutputStream(outFile);
      SAXTransformerFactory tf=(SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler hd=tf.newTransformerHandler();
      Transformer serializer=hd.getTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING,encoding);
      serializer.setOutputProperty(OutputKeys.INDENT,"yes");

      StreamResult streamResult=new StreamResult(fos);
      hd.setResult(streamResult);
      hd.startDocument();
      write(hd,index);
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
  
  private void write(TransformerHandler hd, RecipesIndex index) throws Exception
  {
    AttributesImpl indexAttrs=new AttributesImpl();
    hd.startElement("","",RecipesIndexXMLConstants.INDEX_TAG,indexAttrs);

    String[] professions=index.getProfessions();
    for(String profession : professions)
    {
      AttributesImpl attrs=new AttributesImpl();
      attrs.addAttribute("","",RecipesIndexXMLConstants.PROFESSION_NAME_ATTR,CDATA,profession);
      hd.startElement("","",RecipesIndexXMLConstants.PROFESSION_TAG,attrs);
      Integer[] tiers=index.getTiersForProfession(profession);
      for(Integer tier : tiers)
      {
        AttributesImpl tierAttrs=new AttributesImpl();
        tierAttrs.addAttribute("","",RecipesIndexXMLConstants.TIER_VALUE_ATTR,CDATA,tier.toString());
        hd.startElement("","",RecipesIndexXMLConstants.TIER_TAG,tierAttrs);

        String[] keys=index.getKeysForProfessionAndTier(profession,tier.intValue());
        for(String key : keys)
        {
          RecipeSummary summary=index.getRecipe(key);
          AttributesImpl recipeAttrs=new AttributesImpl();
          recipeAttrs.addAttribute("","",RecipesIndexXMLConstants.RECIPE_KEY_ATTR,CDATA,summary.getKey());
          recipeAttrs.addAttribute("","",RecipesIndexXMLConstants.RECIPE_NAME_ATTR,CDATA,summary.getName());
          hd.startElement("","",RecipesIndexXMLConstants.RECIPE_TAG,recipeAttrs);
          hd.endElement("","",RecipesIndexXMLConstants.RECIPE_TAG);
        }
        hd.endElement("","",RecipesIndexXMLConstants.TIER_TAG);
      }
      hd.endElement("","",RecipesIndexXMLConstants.PROFESSION_TAG);
    }
    hd.endElement("","",RecipesIndexXMLConstants.INDEX_TAG);
  }
}
