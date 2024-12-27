package delta.games.lotro.gui.common.effects.chooser;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JDialog;

import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.gui.common.effects.table.EffectsTableBuilder;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller for an effect choice window.
 * @author DAM
 */
public class EffectChooser
{
  /**
   * Preference file for the columns of the effects chooser.
   */
  public static final String EFFECT_CHOOSER_PROPERTIES_ID="EffectChooserColumn";

  /**
   * Build an effect chooser window.
   * @param parent Parent controller.
   * @param prefs Preferences for this window.
   * @param effects Effects to use.
   * @param filter Filter to use.
   * @param filterController Filter UI to use.
   * @return the newly built chooser.
   */
  public static ObjectChoiceWindowController<Effect> buildChooser(WindowController parent, TypedProperties prefs, List<? extends Effect> effects, Filter<Effect> filter, EffectFilterController filterController)
  {
    // Table
    GenericTableController<Effect> effectsTable=EffectsTableBuilder.buildTable(effects);

    // Build and configure chooser
    final ObjectChoiceWindowController<Effect> chooser=new ObjectChoiceWindowController<Effect>(parent,prefs,effectsTable);
    // Filter
    chooser.setFilter(filter,filterController);
    JDialog dialog=chooser.getDialog();
    // Title
    dialog.setTitle("Choose item:");
    // Dimension
    dialog.setMinimumSize(new Dimension(400,300));
    dialog.setSize(1000,dialog.getHeight());
    // Add details column
    EffectsTableBuilder.addDetailsColumn(chooser,effectsTable);
    return chooser;
  }
}
