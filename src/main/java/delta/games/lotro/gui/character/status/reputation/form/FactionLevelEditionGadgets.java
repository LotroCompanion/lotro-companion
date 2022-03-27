package delta.games.lotro.gui.character.status.reputation.form;

import delta.common.ui.swing.text.dates.DateEditionController;
import delta.games.lotro.character.status.reputation.FactionLevelStatus;
import delta.games.lotro.gui.utils.l10n.DateFormat;

/**
 * Gathers the gadgets used to edit a faction level of a reputation.
 * @author DAM
 */
public class FactionLevelEditionGadgets
{
  private FactionLevelStatus _status;
  private DateEditionController _completionDate;

  /**
   * Constructor.
   * @param status Managed status.
   */
  public FactionLevelEditionGadgets(FactionLevelStatus status)
  {
    _status=status;
    _completionDate=new DateEditionController(DateFormat.getDateTimeCodec());
  }

  /**
   * Get the completion date edition controller.
   * @return a controller.
   */
  public DateEditionController getCompletionDate()
  {
    return _completionDate;
  }

  /**
   * Update UI.
   */
  public void updateUi()
  {
    // Completion date
    long completionDate=_status.getCompletionDate();
    Long date=(completionDate!=0)?Long.valueOf(completionDate):null;
    _completionDate.setDate(date);
    _completionDate.setState(true,true);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
  }
}
