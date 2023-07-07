package delta.games.lotro.gui.kinship;

import java.util.HashMap;
import java.util.Map;

import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.Genders;
import delta.games.lotro.dat.data.strings.renderer.OptionItem;
import delta.games.lotro.dat.data.strings.renderer.StringParser;
import delta.games.lotro.dat.data.strings.renderer.StringRenderer;
import delta.games.lotro.kinship.KinshipRank;

/**
 * Renderer for kinship ranks.
 * @author DAM
 */
public class KinshipRankRenderer
{
  private static Map<String,String> _cache=new HashMap<String,String>();

  /**
   * Render a kinship rank.
   * @param rank Rank to render.
   * @param sex Sex to use.
   * @return the rendered rank name.
   */
  public static String render(KinshipRank rank, CharacterSex sex)
  {
    // TODO Use generic context system
    String key=buildKey(rank.getName(),sex);
    String ret=_cache.get(key);
    if (ret==null)
    {
      ret=privateRender(rank,sex);
      _cache.put(key,ret);
    }
    return ret;
  }

  private static String buildKey(String input, CharacterSex sex)
  {
    return input+"#"+sex.getKey();
  }

  private static String privateRender(KinshipRank rank, CharacterSex sex)
  {
    String input=rank.getName();
    String ret=input;
    int openBrace=input.indexOf(StringParser.OPEN_BRACE);
    if (openBrace!=-1)
    {
      int closeBrace=input.indexOf(StringParser.CLOSE_BRACE,openBrace+1);
      if (closeBrace!=-1)
      {
        String optionsStr=input.substring(openBrace+1,closeBrace);
        OptionItem[] options=StringParser.parseOptions(optionsStr);
        ret=renderOptions(options,sex);
      }
    }
    return ret;
  }

  private static String renderOptions(OptionItem[] options, CharacterSex sex)
  {
    StringRenderer r=new StringRenderer(null);
    StringBuilder sb=new StringBuilder();
    String text=((sex==Genders.MALE)?"[M]":"[f]");
    r.renderOptions(sb,options,text);
    return sb.toString();
  }
}
