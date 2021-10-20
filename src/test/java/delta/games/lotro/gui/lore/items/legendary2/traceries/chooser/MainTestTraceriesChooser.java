package delta.games.lotro.gui.lore.items.legendary2.traceries.chooser;

import java.util.List;

import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.gui.lore.items.legendary2.traceries.TraceriesFilterController;
import delta.games.lotro.lore.items.legendary2.TraceriesManager;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.lore.items.legendary2.filters.TraceryFilter;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Test class for the tracery chooser UI.
 * @author DAM
 */
public class MainTestTraceriesChooser
{
  private Tracery chooseTracery()
  {
    LotroEnum<SocketType> typesMgr=LotroEnumsRegistry.getInstance().get(SocketType.class);
    SocketType type=typesMgr.getEntry(3);
    List<Tracery> traceries=TraceriesManager.getInstance().getTracery(type);
    TraceryFilter filter=new TraceryFilter();
    filter.getTierFilter().setTier(Integer.valueOf(14));
    TypedProperties props=new TypedProperties();
    TraceriesFilterController filterController=new TraceriesFilterController(filter,traceries,null);
    ObjectChoiceWindowController<Tracery> chooser=TraceryChooser.buildChooser(null,props,traceries,filter,filterController,traceries.get(0));
    Tracery ret=chooser.editModal();
    System.out.println(ret);
    return ret;
  }

  private void doIt()
  {
    chooseTracery();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestTraceriesChooser().doIt();
  }
}
