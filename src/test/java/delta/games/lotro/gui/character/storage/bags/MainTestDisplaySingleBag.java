package delta.games.lotro.gui.character.storage.bags;

import javax.swing.JPanel;

import delta.common.ui.swing.windows.DefaultDisplayDialogController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.bags.BagsSetup;
import delta.games.lotro.character.storage.bags.SingleBagSetup;
import delta.games.lotro.character.storage.bags.io.BagsIo;

/**
 * Simple test class for the bag display panel controller.
 * @author DAM
 */
public class MainTestDisplaySingleBag
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    CharacterFile toon=mgr.getToonById("Landroval","Meva");
    //for(CharacterFile toon : mgr.getAllToons())
    //{
    BagsManager bagsMgr=BagsIo.load(toon);
    BagsSetup setup=bagsMgr.getBagsSetup();
    for(Integer index : setup.getBagIndexes())
    {
      SingleBagSetup bagSetup=setup.getBagSetup(index.intValue());
      int size=bagSetup.getSize();
      if (size==0)
      {
        continue;
      }
      BagWindow window=new BagWindow(bagsMgr,bagSetup);
      window.pack();
      window.show();
    }
    //}
  }

  private static class BagWindow extends DefaultDisplayDialogController<Void>
  {
    private BagsManager _bagsMgr;
    private SingleBagSetup _bagSetup;

    /**
     * Constructor.
     * @param bagsMgr Bags manager.
     * @param bagSetup Bag setup.
     */
    public BagWindow(BagsManager bagsMgr, SingleBagSetup bagSetup)
    {
      super(null,null);
      _bagsMgr=bagsMgr;
      _bagSetup=bagSetup;
    }

  @Override
    protected JPanel buildFormPanel()
    {
      BagDisplayPanelController ctrl=new BagDisplayPanelController(this,_bagsMgr,_bagSetup);
      return ctrl.getPanel();
    }
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestDisplaySingleBag().doIt();
  }
}
