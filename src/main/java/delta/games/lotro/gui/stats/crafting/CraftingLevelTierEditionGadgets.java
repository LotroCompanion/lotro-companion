package delta.games.lotro.gui.stats.crafting;

import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.common.ui.swing.text.IntegerEditionController;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.games.lotro.character.crafting.CraftingLevelTierStatus;
import delta.games.lotro.utils.DateFormat;

/**
 * Gathers the gadgets used to edit a crafting level tier of a profession.
 * @author DAM
 */
public class CraftingLevelTierEditionGadgets
{
  private CraftingLevelTierStatus _status;
  private IntegerEditionController _xp;
  private DateEditionController _completionDate;
  private CheckboxController _completed;

  /**
   * Constructor.
   * @param status Managed status.
   */
  public CraftingLevelTierEditionGadgets(CraftingLevelTierStatus status)
  {
    _status=status;
    JTextField xpTextField=GuiFactory.buildTextField("");
    _xp=new IntegerEditionController(xpTextField);
    _completionDate=new DateEditionController(DateFormat.getDateTimeCodec());
    _completed=new CheckboxController("");
  }

  /**
   * Get the XP edition controller.
   * @return a controller.
   */
  public IntegerEditionController getXp()
  {
    return _xp;
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
   * Get the completion status edition controller.
   * @return a controller.
   */
  public CheckboxController getCompleted()
  {
    return _completed;
  }

  /**
   * Update data from UI contents.
   */
  public void updateDatafromUi()
  {
    // XP
    Integer acquiredXp=_xp.getValue();
    if (acquiredXp!=null)
    {
      _status.setAcquiredXP(acquiredXp.intValue());
    }
  }

  /**
   * Update UI.
   */
  public void updateUi()
  {
    // XP
    int xp=_status.getAcquiredXP();
    _xp.setValue(Integer.valueOf(xp));
    // Completion date
    long completionDate=_status.getCompletionDate();
    Long date=(completionDate!=0)?Long.valueOf(completionDate):null;
    _completionDate.setDate(date);
    // Completed
    boolean completed=_status.isCompleted();
    _completed.setSelected(completed);
    // Update UI states (enabled,editable)
    if (completed)
    {
      _xp.setState(false,false);
      _completionDate.setState(true,true);
    }
    else
    {
      _xp.setState(true,true);
      _completionDate.setState(false,false);
    }
  }

  /**
   * Get the currently displayed state into the given status.
   * @param status Storage status.
   */
  public void getState(CraftingLevelTierStatus status)
  {
    Integer xp=_xp.getValue();
    status.setAcquiredXP((xp!=null)?xp.intValue():0);
    boolean completed=_completed.isSelected();
    status.setCompleted(completed);
    Long date=_completionDate.getDate();
    status.setCompletionDate((date!=null)?date.longValue():0);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_xp!=null)
    {
      _xp.dispose();
      _xp=null;
    }
    if (_completionDate!=null)
    {
      _completionDate.dispose();
      _completionDate=null;
    }
    if (_completed!=null)
    {
      _completed.dispose();
      _completed=null;
    }
  }
}
