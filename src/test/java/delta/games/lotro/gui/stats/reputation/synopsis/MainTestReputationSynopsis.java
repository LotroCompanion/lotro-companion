package delta.games.lotro.gui.stats.reputation.synopsis;

import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.DefaultWindowController;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.CharactersManager;

/**
 * Test for the reputation synopsis.
 * @author DAM
 */
public class MainTestReputationSynopsis
{
  private void doIt()
  {
    CharactersManager mgr=CharactersManager.getInstance();
    List<CharacterFile> allToons=mgr.getAllToons();
    ReputationSynopsisTableController table=new ReputationSynopsisTableController(null);
    table.refresh(allToons);
    JTable jtable=table.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(jtable);
    DefaultWindowController w=new DefaultWindowController();
    w.getFrame().add(scroll);
    w.getFrame().pack();
    w.show();
  }

  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainTestReputationSynopsis().doIt();
  }
}
