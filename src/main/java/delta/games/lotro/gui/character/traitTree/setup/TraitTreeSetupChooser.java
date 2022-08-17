package delta.games.lotro.gui.character.traitTree.setup;

import java.awt.Dimension;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.classes.traitTree.setup.TraitTreeSetup;
import delta.games.lotro.gui.character.traitTree.setup.table.TraitTreeSetupTableBuilder;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Utility method to choose a trait tree setup.
 * @author DAM
 */
public class TraitTreeSetupChooser
{
  /**
   * Preference file for the columns of the trait tree setup chooser.
   */
  public static final String TRAIT_TREE_SETUP_CHOOSER_PROPERTIES_ID="TraitTreeSetupChooserColumn";
  /**
   * Preference file for the trait tree setup chooser.
   */
  public static final String TRAIT_TREE_SETUP_CHOOSER="TraitTreeSetupChooser";
  /**
   * Name of the property for column IDs.
   */
  public static final String COLUMNS_PROPERTY="columns";

  /**
   * Choose an essence.
   * @param parent Parent controller.
   * @param traitTreeKey Trait tree key to use.
   * @return A trait tree setup or <code>null</code>.
   */
  public static TraitTreeSetup chooseTraitTreeSetup(WindowController parent, String traitTreeKey)
  {
    TypedProperties prefs=null;
    if (parent!=null)
    {
      prefs=parent.getUserProperties(TRAIT_TREE_SETUP_CHOOSER);
    }
    ObjectChoiceWindowController<TraitTreeSetup> chooser=buildChooser(parent,prefs,traitTreeKey);
    TraitTreeSetup ret=chooser.editModal();
    return ret;
  }

  /**
   * Build a trait tree setup chooser window.
   * @param parent Parent controller.
   * @param prefs Preferences for this window.
   * @param traitTreeKey Trait tree key to use.
   * @return the newly built chooser.
   */
  public static ObjectChoiceWindowController<TraitTreeSetup> buildChooser(WindowController parent, TypedProperties prefs, String traitTreeKey)
  {
    // Table
    GenericTableController<TraitTreeSetup> table=TraitTreeSetupTableBuilder.buildTable(traitTreeKey);

    // Build and configure chooser
    final ObjectChoiceWindowController<TraitTreeSetup> chooser=new ObjectChoiceWindowController<TraitTreeSetup>(parent,prefs,table);
    JDialog dialog=chooser.getDialog();
    // Title
    dialog.setTitle("Choose trait tree setup:");
    // Dimension
    dialog.setMinimumSize(new Dimension(600,300));
    dialog.setSize(600,300);
    if (parent!=null)
    {
      dialog.setLocationRelativeTo(parent.getWindow());
    }
    return chooser;
  }
}
