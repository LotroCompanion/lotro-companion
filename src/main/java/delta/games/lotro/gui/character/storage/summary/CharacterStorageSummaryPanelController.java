package delta.games.lotro.gui.character.storage.summary;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.storage.summary.CharacterStorageSummary;
import delta.games.lotro.character.storage.summary.SingleStorageSummary;
import delta.games.lotro.gui.character.storage.StorageUiUtils;

/**
 * Controller for a panel that displays storage summary for a single character.
 * @author DAM
 */
public class CharacterStorageSummaryPanelController extends AbstractPanelController
{
  // UI
  private JProgressBar _vault;
  private JProgressBar _bags;

  /**
   * Constructor.
   */
  public CharacterStorageSummaryPanelController()
  {
    setPanel(buildPanel());
  }

  /**
   * Update values.
   * @param summary Data to show.
   */
  public void update(CharacterStorageSummary summary)
  {
    // Bags
    {
      SingleStorageSummary bags=summary.getBags();
      int max=bags.getMax();
      if (max!=0)
      {
        Integer used=Integer.valueOf(bags.getUsed());
        StorageUiUtils.updateProgressBar(_bags,used,Integer.valueOf(max));
      }
      else
      {
        StorageUiUtils.updateProgressBar(_bags,null,null);
      }
    }
    // Vault
    {
      SingleStorageSummary ownVault=summary.getOwnVault();
      int max=ownVault.getMax();
      if (max!=0)
      {
        Integer used=Integer.valueOf(ownVault.getUsed());
        StorageUiUtils.updateProgressBar(_vault,used,Integer.valueOf(max));
      }
      else
      {
        StorageUiUtils.updateProgressBar(_vault,null,null);
      }
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    Dimension size=new Dimension(100,16);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Bags
    JLabel bagsLabel=GuiFactory.buildLabel("Bags:"); // I18n
    panel.add(bagsLabel,c);
    c.gridx++;
    _bags=StorageUiUtils.buildProgressBar(size);
    panel.add(_bags,c);
    c.gridy++;c.gridx=0;
    // Vault
    JLabel vaultLabel=GuiFactory.buildLabel("Vault:"); // I18n
    panel.add(vaultLabel,c);
    c.gridx++;
    _vault=StorageUiUtils.buildProgressBar(size);
    panel.add(_vault,c);
    c.gridy++;c.gridx=0;
    TitledBorder border=GuiFactory.buildTitledBorder("Capacity"); // I18n
    panel.setBorder(border);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // UI
    _bags=null;
    _vault=null;
  }
}
