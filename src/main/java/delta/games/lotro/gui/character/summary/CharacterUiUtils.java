package delta.games.lotro.gui.character.summary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.utils.misc.IntegerHolder;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.Config;
import delta.games.lotro.account.Account;
import delta.games.lotro.account.AccountComparator;
import delta.games.lotro.account.AccountsManager;
import delta.games.lotro.character.classes.AbstractClassDescription;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.classes.ClassesManager;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.CharacterSex;
import delta.games.lotro.common.enums.LotroEnum;
import delta.games.lotro.common.enums.LotroEnumsRegistry;
import delta.games.lotro.gui.utils.SharedUiUtils;

/**
 * Utility methods for character UI.
 * @author DAM
 */
public class CharacterUiUtils
{
  /**
   * Build a character class combobox.
   * @param includeEmptyChoice Include an empty choice or not.
   * @return a character class combobox.
   */
  public static ComboBoxController<ClassDescription> buildCharacterClassCombo(boolean includeEmptyChoice)
  {
    List<ClassDescription> classes=ClassesManager.getInstance().getAllCharacterClasses();
    return buildClassCombo(includeEmptyChoice,classes);
  }

  /**
   * Build a class combobox.
   * @param includeEmptyChoice Include an empty choice or not.
   * @param includeMonsterClasses Use monster classes or not.
   * @return a class combobox.
   */
  public static ComboBoxController<AbstractClassDescription> buildClassCombo(boolean includeEmptyChoice, boolean includeMonsterClasses)
  {
    List<AbstractClassDescription> classes=new ArrayList<AbstractClassDescription>();
    if (includeMonsterClasses)
    {
      List<AbstractClassDescription> allClasses=ClassesManager.getInstance().getAllClasses();
      classes.addAll(allClasses);
    }
    else
    {
      List<ClassDescription> characterClasses=ClassesManager.getInstance().getAllCharacterClasses();
      classes.addAll(characterClasses);
    }
    return buildClassCombo(includeEmptyChoice,classes);
  }

  /**
   * Build a class combobox.
   * @param includeEmptyChoice Include an empty choice or not.
   * @param classes Classes to use.
   * @return a class combobox.
   */
  private static <T extends AbstractClassDescription> ComboBoxController<T> buildClassCombo(boolean includeEmptyChoice, List<T> classes)
  {
    ComboBoxController<T> ctrl=new ComboBoxController<T>();
    if (includeEmptyChoice)
    {
      ctrl.addEmptyItem("");
    }
    for(T currentClass : classes)
    {
      ctrl.addItem(currentClass,currentClass.getName());
    }
    return ctrl;
  }

  /**
   * Build a character race combobox.
   * @param includeEmptyChoice Include an empty choice or not.
   * @return a character race combobox.
   */
  public static ComboBoxController<RaceDescription> buildRaceCombo(boolean includeEmptyChoice)
  {
    ComboBoxController<RaceDescription> ctrl=new ComboBoxController<RaceDescription>();
    if (includeEmptyChoice)
    {
      ctrl.addEmptyItem("");
    }
    for(RaceDescription race : RacesManager.getInstance().getAll())
    {
      ctrl.addItem(race,race.getName());
    }
    return ctrl;
  }

  /**
   * Build a character sex combobox.
   * @param includeEmptyChoice Include an empty choice or not.
   * @return a character sex combobox.
   */
  public static ComboBoxController<CharacterSex> buildSexCombo(boolean includeEmptyChoice)
  {
    ComboBoxController<CharacterSex> ctrl=new ComboBoxController<CharacterSex>();
    if (includeEmptyChoice)
    {
      ctrl.addEmptyItem("");
    }
    LotroEnum<CharacterSex> genderEnum=LotroEnumsRegistry.getInstance().get(CharacterSex.class);
    for(CharacterSex characterSex : genderEnum.getAll())
    {
      ctrl.addItem(characterSex,characterSex.getLabel());
    }
    return ctrl;
  }

  /**
   * Build a server combobox.
   * @return a server combobox.
   */
  public static ComboBoxController<String> buildServerCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    List<String> servers=Config.getInstance().getServerNames();
    for(String server : servers)
    {
      ctrl.addItem(server,server);
    }
    TypedProperties props=Config.getInstance().getParameters();
    String defaultServer=props.getStringProperty("default.server",null);
    ctrl.selectItem(defaultServer);
    return ctrl;
  }

  /**
   * Build an account combobox.
   * @return an account combobox.
   */
  public static ComboBoxController<Account> buildAccountCombo()
  {
    List<Account> accounts=AccountsManager.getInstance().getAllAccounts();
    Collections.sort(accounts,new AccountComparator());
    ComboBoxController<Account> ctrl=new ComboBoxController<Account>();
    ctrl.addEmptyItem("");
    Map<String,IntegerHolder> counts=countAccountsByName(accounts);
    for(Account account : accounts)
    {
      String label=account.getAccountName();
      int count=counts.get(label).getInt();
      if (count>1)
      {
        String gameAccountName=account.getSubscriptionKey();
        label=label+" / "+gameAccountName;
      }
      ctrl.addItem(account,label);
    }
    return ctrl;
  }

  private static Map<String,IntegerHolder> countAccountsByName(List<Account> accounts)
  {
    Map<String,IntegerHolder> counts=new HashMap<String,IntegerHolder>();
    for(Account account : accounts)
    {
      String name=account.getAccountName();
      IntegerHolder counter=counts.get(name);
      if (counter==null)
      {
        counter=new IntegerHolder();
        counts.put(name,counter);
      }
      counter.increment();
    }
    return counts;
  }

  /**
   * Build a level combobox.
   * @return a level combobox.
   */
  public static ComboBoxController<Integer> buildLevelCombo()
  {
    int maxLevel=Config.getInstance().getMaxCharacterLevel();
    List<Integer> levels=new ArrayList<Integer>();
    for(int i=1;i<=maxLevel;i++)
    {
      levels.add(Integer.valueOf(i));
    }
    ComboBoxController<Integer> ctrl=SharedUiUtils.buildIntegerCombo(levels,false);
    ctrl.selectItem(Integer.valueOf(maxLevel));
    return ctrl;
  }
}
