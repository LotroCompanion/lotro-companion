package delta.games.lotro.lore.items.io.datalotro;

import java.io.File;
import java.util.List;

import delta.common.utils.text.TextUtils;

/**
 * @author DAM
 */
public class MainLoadDataLotroItems
{
  private void doIt()
  {
    // 1879049233
    List<String> lines=TextUtils.readAsLines(new File("c:\\toto.txt"));
    File toDir=new File("d:\\tmp\\items");
    //Downloader d=new Downloader();
    for(String line : lines)
    {
      File out=new File(toDir,line+".xml");
      if (out.exists()) {
        List<String> lines2=TextUtils.readAsLines(out);
        if ((lines2.size()>=3) && (lines2.get(2).contains("ItemFetchError"))) {
          out.delete();
        }
        if ((lines2.size()>=3) && (lines2.get(2).contains("unknownError"))) {
          out.delete();
        }
      }
      /*
      try {
        System.out.println("ID: "+line);
        String url="http://data.lotro.com/valamar/a8ca0c5de7c466ecdd8e7f2df1d610ea/item/id/"+line+"/";
        d.downloadToFile(url,out);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
      */
    }
  }
  /**
   * @param args
   */
  public static void main(String[] args)
  {
    new MainLoadDataLotroItems().doIt();
  }

}
