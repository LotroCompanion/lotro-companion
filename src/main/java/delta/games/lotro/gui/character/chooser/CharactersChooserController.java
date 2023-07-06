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
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.comparators.StandardSummaryComparatorsBuilder;

/**
 * Controller for a characters chooser.
 * @author DAM
 */
public final class CharactersChooserController extends DefaultFormDialogController<List<CharacterFile>>
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

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param toons Managed toons.
   * @param selectedToons Selected toons.
   */
  private CharactersChooserController(WindowController parent, List<CharacterFile> toons, List<CharacterFile> selectedToons)
  {
    super(parent,null);
    Comparator<CharacterSummary> summaryComparator=StandardSummaryComparatorsBuilder.buildAccountNameServerComparator();
    Comparator<CharacterFile> comparator=StandardSummaryComparatorsBuilder.buildCharacterFileComparator(summaryComparator);
    LabelProvider<CharacterFile> labelProvider=new LabelProvider<CharacterFile>()
    {
      @Override
      public String getLabel(CharacterFile item)
      {
        return getLabelForCharacterFile(item);
      }
    };
    _selectionController=new OrderedItemsSelectionController<CharacterFile>(comparator,labelProvider);
    _selectionController.setItems(toons);
    _selectionController.selectItems(selectedToons);
  }

  private static String getLabelForCharacterFile(CharacterFile character)
  {
    String name=character.getName();
    String ret=name;
    String server=character.getServerName();
    if (server.length()>0)
    {
      ret=name+"@"+server;
    }
    String accountName=character.getAccountName();
    if (accountName.length()>0)
    {
      ret=ret+" ("+accountName+")";
    }
    return ret;
  }

  @Override
  protected JDialog build()
  {
    JDialog dialog=super.build();
    dialog.setTitle("Choose characters..."); // I18n
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
