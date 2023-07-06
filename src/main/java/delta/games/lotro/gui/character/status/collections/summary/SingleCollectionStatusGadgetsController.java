package delta.games.lotro.gui.character.status.collections.summary;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.collections.CollectionStatus;
import delta.games.lotro.lore.collections.CollectionDescription;

/**
 * Controller for the UI items to display a single collection status summary.
 * @author DAM
 */
public class SingleCollectionStatusGadgetsController
{
  // Data
  private CollectionStatus _status;
  // UI
  // - name
  private JLabel _name;
  // - progress
  private JProgressBar _state;
  // - details button
  private JButton _button;

  /**
   * Constructor.
   * @param status Collection status to use.
   */
  public SingleCollectionStatusGadgetsController(CollectionStatus status)
  {
    _status=status;
    CollectionDescription collection=status.getCollection();
    // Name
    _name=GuiFactory.buildLabel(collection.getName());
    // Progress
    _state=buildState(status);
    // Button
    _button=GuiFactory.buildButton("Details..."); // I18n
  }

  private JProgressBar buildState(CollectionStatus status)
  {
    int nbCompleted=status.getCompletedCount();
    int nbTotal=status.getTotalCount();
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,nbTotal);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setForeground(Color.BLUE);
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    bar.setValue(nbCompleted);
    String label=nbCompleted+"/"+nbTotal;
    bar.setString(label);
    return bar;
  }

  /**
   * Get the managed rewards track status.
   * @return a rewards track status.
   */
  public CollectionStatus getStatus()
  {
    return _status;
  }

  /**
   * Get the gadget for the name.
   * @return a label.
   */
  public JLabel getNameGadget()
  {
    return _name;
  }

  /**
   * Get the gadget for the progress.
   * @return a progress bar.
   */
  public JProgressBar getProgressGadget()
  {
    return _state;
  }

  /**
   * Get the details button.
   * @return a button.
   */
  public JButton getDetailsButton()
  {
    return _button;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _name=null;
    _state=null;
    _button=null;
  }
}
