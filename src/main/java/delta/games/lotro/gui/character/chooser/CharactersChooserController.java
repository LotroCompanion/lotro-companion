package delta.games.lotro.gui.character.chooser;

import java.util.Comparator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JPanel;

import delta.common.ui.swing.lists.LabelProvider;
import delta.common.ui.swing.lists.OrderedItemsSelectionController;
import delta.common.ui.swing.windows.DefaultFormDialogController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterFile;

/**
 * Controller for a characters chooser.
 * @author DAM
 */
public class CharactersChooserController extends DefaultFormDialogController<List<CharacterFile>>
{
  private OrderedItemsSelectionController<CharacterFile> _selectionController;

  /**
   * Show the toon selection dialog.
   * @param parent Parent controller.
   * @param toons Toons to show.
   * @param selectedToons Pre-selected toons.
   * @return A list of selected toons or <code>null</code> if the window was closed or canceled.
   */
  public static List<CharacterFile> selectToons(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons)
  {
    CharactersChooserController controller=new CharactersChooserController(parent,toons,selectedToons);
    List<CharacterFile> newSelectedToons=controller.editModal();
    return newSelectedToons;
  }

  private static class TableColumnsComparator implements Comparator<CharacterFile>
  {
    public int compare(CharacterFile o1, CharacterFile o2)
    {
      return o1.getName().compareTo(o2.getName());
    }
  }

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toons Managed toons.
   * @param selectedToons Selected toons.
   */
  private CharactersChooserController(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons)
  {
    super(parent,null);
    TableColumnsComparator comparator=new TableColumnsComparator();
    LabelProvider<CharacterFile> labelProvider=new LabelProvider<CharacterFile>()
    {
      public String getLabel(CharacterFile item)
      {
        return item.getName();
      }
    };
    _selectionController=new OrderedItemsSelectionController<CharacterFile>(comparator,labelProvider);
    _selectionController.setItems(toons);
    _selectionController.selectItems(selectedToons);
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose characters...");
    return dialog;
  }

  @Override
  protected JPanel buildFormPanel()
  {
    return _selectionController.getPanel();
  }

  @Override
  protected void okImpl()
  {
    _data=_selectionController.getSelectedItems();
  }
}
