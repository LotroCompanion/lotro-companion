package delta.games.lotro.lore.items.io.tulkas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemsSet;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.bonus.Bonus;
import delta.games.lotro.lore.items.bonus.Bonus.BONUS_OCCURRENCE;
import delta.games.lotro.lore.items.bonus.BonusManager;
import delta.games.lotro.lore.items.bonus.BonusType;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Items/sets loader for Tulkas DB (version 2).
 * @author DAM
 */
public class TulkasItemsLoader2 extends TulkasItemsLoader
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  /**
   * Inspect loaded data items to fetch possible values in fields.
   * @param items Loaded data items.
   */
  /*
  public void inspectItems(HashMap<Integer,HashMap<Object,Object>> items)
  {
    int[] keys={5,14};
    for(int key : keys)
    {
      Set<Integer> values=new HashSet<Integer>();
      Integer k=Integer.valueOf(key);
      for(HashMap<Object,Object> item:items.values())
      {
        Integer v=(Integer)item.get(k);
        if (v!=null)
        {
          values.add(v);
        }
      }
      System.out.println("values"+key+": "+values);
    }
    //values4: [2, 3, 4, 5]
    //values7: [1, 2, 3]
    //values8: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 2199, 15, 17, 16, 19, 2186, 18, 21, 20, 23, 22, 25, 24, 27, 26, 29, 28, 2183, 31, 30, 34, 35, 32, 33, 38, 39, 36, 37, 42, 43, 40, 41, 46, 47, 2228, 44, 45, 51, 50, 49, 48, 55, 54, 53, 52, 59, 2211, 58, 57, 56, 63, 62, 61, 2213, 60, 68, 69, 70, 71, 64, 65, 66, 2266, 67, 76, 77, 78, 2263, 79, 72, 2256, 73, 74, 75, 85, 84, 87, 86, 81, 80, 83, 82, 92, 95, 94, 89, 88, 91, 2242, 90, 102, 103, 100, 101, 98, 99, 96, 97, 110, 111, 108, 109, 106, 107, 104, 105, 119, 118, 117, 116, 115, 114, 113, 112, 127, 126, 125, 124, 123, 122, 121, 120, 137, 136, 139, 138, 141, 140, 143, 142, 129, 128, 2075, 131, 130, 133, 132, 2079, 135, 134, 152, 153, 4521, 154, 155, 156, 157, 158, 159, 144, 2056, 145, 146, 147, 2059, 148, 149, 150, 151, 171, 170, 169, 168, 175, 174, 173, 172, 163, 2106, 162, 161, 160, 167, 166, 165, 164, 186, 187, 184, 185, 190, 191, 188, 189, 178, 179, 176, 177, 182, 183, 180, 181, 205, 204, 207, 2134, 206, 201, 200, 203, 202, 197, 196, 199, 198, 193, 192, 195, 194, 220, 221, 222, 223, 216, 217, 218, 219, 212, 213, 214, 2127, 215, 208, 209, 210, 211, 239, 238, 237, 236, 235, 234, 233, 2160, 232, 231, 230, 229, 228, 227, 226, 2169, 225, 224, 254, 255, 252, 251, 248, 249, 246, 247, 244, 245, 242, 243, 240, 241, 275, 274, 273, 272, 279, 278, 277, 276, 283, 282, 281, 280, 287, 286, 285, 284, 258, 259, 256, 257, 262, 263, 260, 261, 266, 267, 264, 265, 2454, 270, 271, 2452, 268, 269, 305, 4096, 304, 307, 306, 309, 308, 311, 310, 2478, 313, 2464, 312, 315, 2467, 314, 317, 316, 319, 318, 288, 289, 290, 291, 292, 293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 343, 342, 341, 340, 339, 338, 337, 336, 351, 350, 349, 348, 4203, 347, 346, 345, 2496, 344, 326, 2527, 327, 324, 325, 322, 323, 320, 321, 334, 335, 332, 333, 330, 331, 328, 329, 373, 372, 374, 2537, 369, 368, 371, 370, 381, 380, 383, 382, 377, 376, 2531, 379, 378, 356, 357, 2558, 358, 359, 4176, 2552, 352, 353, 354, 355, 364, 365, 366, 367, 360, 361, 362, 363, 410, 411, 2304, 408, 409, 414, 415, 412, 413, 2314, 402, 403, 400, 401, 406, 407, 404, 405, 395, 393, 392, 399, 398, 2325, 397, 396, 387, 386, 2329, 385, 384, 391, 390, 389, 388, 440, 441, 442, 443, 444, 445, 446, 447, 432, 2345, 433, 434, 2346, 435, 436, 437, 438, 439, 425, 424, 427, 426, 429, 428, 431, 430, 417, 416, 419, 418, 421, 420, 422, 478, 2372, 476, 477, 474, 475, 472, 470, 471, 468, 469, 466, 467, 464, 465, 462, 461, 460, 459, 458, 457, 456, 455, 2399, 454, 453, 452, 451, 450, 449, 448, 508, 509, 510, 2400, 504, 507, 500, 501, 502, 503, 497, 498, 499, 493, 492, 495, 494, 489, 488, 491, 490, 485, 484, 487, 486, 481, 2425, 480, 2427, 483, 2426, 482, 550, 551, 548, 549, 546, 547, 2744, 544, 545, 558, 559, 556, 554, 555, 567, 565, 564, 563, 4866, 562, 561, 575, 574, 573, 571, 570, 569, 516, 517, 518, 519, 2719, 512, 513, 514, 515, 524, 526, 527, 520, 521, 522, 523, 533, 535, 534, 528, 531, 541, 540, 542, 537, 536, 539, 538, 610, 2810, 611, 608, 609, 614, 615, 612, 618, 619, 616, 617, 622, 623, 620, 621, 2795, 627, 626, 625, 624, 631, 630, 629, 2797, 628, 635, 633, 632, 639, 638, 637, 636, 576, 577, 578, 579, 581, 583, 584, 585, 586, 588, 589, 590, 591, 593, 592, 2760, 594, 597, 596, 599, 598, 601, 600, 603, 602, 605, 607, 606, 687, 686, 2612, 683, 680, 678, 677, 676, 675, 672, 702, 700, 698, 699, 697, 694, 695, 692, 2605, 693, 690, 688, 689, 653, 652, 655, 2582, 649, 648, 651, 650, 645, 644, 646, 641, 640, 643, 642, 668, 670, 671, 664, 665, 666, 667, 660, 661, 662, 663, 656, 657, 658, 659, 747, 746, 745, 751, 750, 749, 748, 739, 738, 743, 741, 762, 763, 760, 2657, 761, 766, 767, 764, 2667, 755, 752, 753, 758, 2670, 759, 756, 757, 713, 712, 716, 719, 718, 705, 704, 707, 706, 709, 708, 711, 710, 728, 730, 731, 732, 733, 2629, 734, 735, 720, 722, 723, 2637, 726, 727, 821, 820, 822, 816, 818, 829, 828, 830, 825, 824, 826, 804, 806, 807, 801, 802, 803, 812, 2999, 815, 810, 811, 790, 789, 787, 785, 784, 799, 798, 797, 2948, 796, 794, 793, 792, 774, 775, 771, 768, 782, 783, 780, 781, 779, 776, 777, 881, 883, 884, 889, 888, 891, 893, 895, 894, 864, 865, 866, 868, 872, 873, 874, 875, 877, 879, 4707, 850, 848, 854, 853, 858, 857, 3008, 863, 861, 834, 832, 838, 836, 837, 840, 846, 847, 844, 959, 955, 948, 950, 951, 944, 945, 946, 941, 940, 942, 937, 936, 939, 934, 929, 931, 930, 926, 920, 918, 916, 917, 914, 912, 911, 909, 907, 906, 903, 901, 2843, 899, 898, 897, 1016, 1017, 1018, 1020, 1023, 1012, 1013, 1001, 1003, 1002, 1005, 1007, 1006, 992, 999, 2942, 998, 986, 988, 989, 978, 982, 980, 981, 971, 970, 969, 968, 975, 974, 973, 972, 962, 961, 960, 967, 966, 965, 964, 1100, 1101, 1096, 1097, 1094, 1088, 1091, 1117, 1116, 1112, 1114, 1108, 1111, 1105, 1107, 1134, 1132, 1130, 1128, 1129, 1123, 1120, 1151, 1144, 1143, 1142, 1140, 1137, 1033, 1034, 1035, 1037, 1038, 1025, 1026, 1028, 1029, 1050, 3205, 1052, 1041, 1040, 1043, 1042, 1044, 1066, 1067, 1064, 1070, 1069, 1058, 1056, 3263, 1061, 1083, 1082, 1080, 1087, 1086, 3238, 1084, 1074, 1073, 1072, 1217, 1216, 1219, 1218, 1228, 1230, 3155, 1237, 1239, 1233, 1244, 1245, 1247, 1241, 1242, 1255, 1253, 1250, 1248, 1260, 1259, 1257, 1270, 1266, 1264, 1265, 1279, 3172, 1276, 1273, 1153, 1155, 1157, 1159, 1158, 3102, 1163, 1162, 1169, 1173, 1174, 1176, 1177, 1178, 1179, 1182, 3130, 1185, 1188, 1198, 1202, 1201, 1204, 1210, 1214, 1215, 1373, 1361, 1359, 1350, 1344, 1345, 1404, 1406, 1397, 1394, 1390, 1385, 1386, 1380, 1381, 1376, 1378, 1304, 1309, 1308, 1299, 1298, 1296, 1303, 1302, 1301, 1300, 3468, 1291, 1288, 1289, 1283, 1285, 1339, 1342, 1329, 1328, 1330, 1335, 1321, 1323, 1324, 1313, 1315, 1317, 1492, 1489, 1501, 1498, 1475, 1486, 1481, 1526, 1510, 1516, 1519, 1513, 1512, 1424, 3337, 1431, 1429, 1408, 1415, 1414, 1416, 1421, 1458, 1462, 1466, 1470, 1444, 1451, 1452, 1454, 1647, 1658, 1657, 1612, 1604, 1606, 1631, 1620, 1578, 1577, 1570, 1599, 1595, 1594, 1588, 1586, 1539, 1566, 1561, 1558, 1552, 1764, 1782, 1786, 1789, 3672, 1728, 1732, 1745, 1755, 1702, 6034, 1696, 1719, 1714, 1727, 1725, 1664, 1676, 1672, 1674, 1693, 1688, 1918, 1887, 1872, 1866, 1863, 1836, 1825, 1823, 1819, 1817, 1810, 1804, 1805, 2034, 5837, 2019, 1998, 1971, 1980, 1958, 1952, 1941, 1944]
    //values14: [185, 23, 172, 162, 24, 40, 193, 31, 194]
  }
  */

  /**
   * Build items from raw data items.
   * @param items Loaded data items.
   */
  public void buildItems(HashMap<Integer,HashMap<Object,Object>> items)
  {
    List<Integer> keys=new ArrayList<Integer>(items.keySet());
    Collections.sort(keys);
    int nbKeys=keys.size();
    System.out.println("Min: "+keys.get(0)+", max: "+keys.get(nbKeys-1));
    for(int i=0;i<nbKeys;i++)
    {
      Integer id=keys.get(i);
      HashMap<Object,Object> data=items.get(id);
      Item item=buildItem(id,data);
      item.setIdentifier(id.intValue());
      System.out.println(item.dump());
      /*
      ItemsManager mgr=ItemsManager.getInstance();
      mgr.writeItemFile(item);
      */
      //writeItemToDB(item);
    }
  }

  private Item buildItem(Integer id, HashMap<Object,Object> map)
  {
    /*
    [1879300219]={
      [1]={[1]="Thirteenth Exquisite Captain's Earring of Glittering Caves";[2]="";[3]="";[4]="";};
      [2]=85;[3]=136;[4]=5;[5]=1;
      [6]={[12]=435;[9]=161;[1]=140;[7]=184;[14]=435;};
      [14]=24;
      [15]={[1]=
          {[1]="Stalwart Captain's Jewelry of Helm's Deep";[2]="";[3]="";[4]="";};
           [2]={1879294461,1879295053,1879299996,1879300219};};};
    */
    @SuppressWarnings("unchecked")
    HashMap<Object,Object> map1=(HashMap<Object,Object>)map.get(Integer.valueOf(1));
    String name=(String)map1.get(Integer.valueOf(1)); // label US

    EquipmentLocation loc=null;
    // slot
    Integer locValue=(Integer)map.get(Integer.valueOf(5));
    // 5=armor piece: 12=boots, 11=leggings, 10=gauntlets, 8=cloak, 7=shoulders, 6=Head
    if (locValue!=null)
    {
      switch (locValue.intValue())
      {
        case 1: loc=EquipmentLocation.EAR; break;
        case 2: loc=EquipmentLocation.NECK; break;
        case 3: loc=EquipmentLocation.POCKET; break;
        case 4: loc=EquipmentLocation.WRIST; break;
        case 5: loc=EquipmentLocation.FINGER; break;
        case 6: loc=EquipmentLocation.HEAD; break;
        case 7: loc=EquipmentLocation.SHOULDER; break;
        case 8: loc=EquipmentLocation.BACK; break;
        case 9: loc=EquipmentLocation.CHEST; break;
        case 10: loc=EquipmentLocation.HAND; break;
        case 11: loc=EquipmentLocation.LEGS; break;
        case 12: loc=EquipmentLocation.FEET; break;
        case 13: loc=EquipmentLocation.MELEE_WEAPON; break;
        case 14: loc=EquipmentLocation.SHIELD; break;
        case 15: loc=EquipmentLocation.RANGED_WEAPON; break;
        case 16: loc=EquipmentLocation.RUNE_STONE; break;
        default:
        {
          _logger.warn("Unmanaged loc value: "+locValue);
        }
      }
    }

    Item ret=null;
    if (TulkasConstants.isArmor(loc))
    {
      Armour a=new Armour();
      // 8=armour value
      Integer armourValue=(Integer)map.get(Integer.valueOf(8));
      if (armourValue!=null)
      {
        a.setArmourValue(armourValue.intValue());
      }
      // 7=armor type: 1=light, 2=medium, 3=heavy
      Integer armourTypeInt=(Integer)map.get(Integer.valueOf(7));
      ArmourType armourType=null;
      if (armourTypeInt!=null)
      {
        switch (armourTypeInt.intValue())
        {
          case 1: armourType=ArmourType.LIGHT; break;
          case 2: armourType=ArmourType.MEDIUM; break;
          case 3: armourType=ArmourType.HEAVY; break;
          default:
          {
            _logger.warn("Unmanaged armour type : "+armourTypeInt);
          }
        }
      }
      armourTypeInt=(Integer)map.get(Integer.valueOf(9));
      if (armourTypeInt!=null)
      {
        switch (armourTypeInt.intValue())
        {
          case 14: armourType=ArmourType.SHIELD; break;
          case 15: armourType=ArmourType.HEAVY_SHIELD; break;
          case 16: armourType=ArmourType.WARDEN_SHIELD; break;
          default:
          {
            _logger.warn("Unmanaged armour type : "+armourTypeInt);
          }
        }
      }
      if (loc==EquipmentLocation.BACK)
      {
        armourType=ArmourType.LIGHT;
      }
      if (armourType==null)
      {
        _logger.warn("Unknown armour type: ["+armourTypeInt+"] (name="+name+")");
      }
      a.setArmourType(armourType);
      ret=a;
    }
    else
    {
      WeaponType weaponType=null;
      Integer weaponTypeInt=(Integer)map.get(Integer.valueOf(9));
      if (weaponTypeInt!=null)
      {
        switch (weaponTypeInt.intValue())
        {
          case 1: weaponType=WeaponType.ONE_HANDED_AXE; break;
          case 2: weaponType=WeaponType.TWO_HANDED_AXE; break;
          case 3: weaponType=WeaponType.ONE_HANDED_CLUB; break;
          case 4: weaponType=WeaponType.TWO_HANDED_CLUB; break;
          case 5: weaponType=WeaponType.DAGGER; break;
          case 6: weaponType=WeaponType.HALBERD; break;
          case 7: weaponType=WeaponType.ONE_HANDED_HAMMER; break;
          case 8: weaponType=WeaponType.TWO_HANDED_HAMMER; break;
          case 9: weaponType=WeaponType.ONE_HANDED_MACE; break;
          case 10: weaponType=WeaponType.SPEAR; break;
          case 11: weaponType=WeaponType.STAFF; break;
          case 12: weaponType=WeaponType.ONE_HANDED_SWORD; break;
          case 13: weaponType=WeaponType.TWO_HANDED_SWORD; break;
          case 17: weaponType=WeaponType.BOW; break;
          case 18: weaponType=WeaponType.CROSSBOW; break;
          case 19: weaponType=WeaponType.JAVELIN; break;
          default:
            _logger.warn("Unmanaged weapon type: "+weaponTypeInt.intValue());
        }
      }
      if (weaponType!=null)
      {
        // weapon:
        Weapon w=new Weapon();
        w.setWeaponType(weaponType);
        // [10]=Min damage;
        Integer minDMG=(Integer)map.get(Integer.valueOf(10));
        if (minDMG!=null)
        {
          w.setMinDamage(minDMG.intValue());
        }
        // [11]=Max damage;
        Integer maxDMG=(Integer)map.get(Integer.valueOf(11));
        if (maxDMG!=null)
        {
          w.setMaxDamage(maxDMG.intValue());
        }
        // [12]=DPS;
        Object dpsValue=map.get(Integer.valueOf(12));
        if (dpsValue instanceof Float)
        {
          w.setDPS(((Float)dpsValue).floatValue());
        }
        else if (dpsValue instanceof Integer)
        {
          w.setDPS(((Integer)dpsValue).floatValue());
        }
        // Damage type
        DamageType type=null;
        Integer damageTypeInt=(Integer)map.get(Integer.valueOf(13));
        if (damageTypeInt!=null)
        {
          switch (damageTypeInt.intValue())
          {
            case 1: type=DamageType.COMMON; break;
            case 2: type=DamageType.BELERIAND; break;
            case 3: type=DamageType.WESTERNESSE; break;
            case 4: type=DamageType.ANCIENT_DWARF; break;
            case 5: type=DamageType.FIRE; break;
            case 6: type=DamageType.LIGHT; break;
            case 7: type=DamageType.LIGHTNING; break;
            case 8: type=DamageType.FROST; break;
          }
        }
        if (type==null)
        {
          type=DamageType.COMMON;
          _logger.warn("Unmanaged damage type ["+damageTypeInt+"]");
        }
        w.setDamageType(type);
        ret=w;
      }
      if (ret==null)
      {
        ret=new Item();
        /*
        _TABLES["WEAPONTYPE"] =
          {
            [20] = {
              [1] = "Instrument";
            [21] = {
              [1] = "Chisel";
            [22] = {
              [1] = "Riffler";
            */
      }
    }
    // Name
    ret.setName(name);
    // Slot
    ret.setEquipmentLocation(loc);
    // Required level
    Integer requiredLevel=(Integer)map.get(Integer.valueOf(2));
    ret.setMinLevel(requiredLevel);
    // Item level
    Integer itemLevel=(Integer)map.get(Integer.valueOf(3));
    ret.setItemLevel(itemLevel);
    // Class
    CharacterClass cClass=null;
    Integer classInt=(Integer)map.get(Integer.valueOf(14));
    if (classInt!=null)
    {
      switch (classInt.intValue())
      {
        case 40: cClass=CharacterClass.BURGLAR; break;
        case 24: cClass=CharacterClass.CAPTAIN; break;
        case 172: cClass=CharacterClass.CHAMPION; break;
        case 23: cClass=CharacterClass.GUARDIAN; break;
        case 162: cClass=CharacterClass.HUNTER; break;
        case 185: cClass=CharacterClass.LORE_MASTER; break;
        case 31: cClass=CharacterClass.MINSTREL; break;
        case 193: cClass=CharacterClass.RUNE_KEEPER; break;
        case 194: cClass=CharacterClass.WARDEN; break;
        default:
        {
          _logger.warn("Unmanaged class ["+classInt+"]");
        }
      }
    }
    if (cClass!=null)
    {
      ret.setRequiredClass(cClass);
    }
    // Quality
    ItemQuality quality=ItemQuality.COMMON;
    Integer qualityInt=(Integer)map.get(Integer.valueOf(4));
    if (qualityInt!=null)
    {
      switch (qualityInt.intValue())
      {
        case 2: quality=ItemQuality.UNCOMMON; break;
        case 3: quality=ItemQuality.RARE; break;
        case 4: quality=ItemQuality.INCOMPARABLE; break;
        case 5: quality=ItemQuality.LEGENDARY; break;
        default:
        {
          _logger.warn("Unmanaged quality ["+qualityInt+"]");
        }
      }
    }
    ret.setQuality(quality);
    
    // Bonus
    @SuppressWarnings("unchecked")
    HashMap<Integer,Object> bonuses=(HashMap<Integer,Object>)map.get(Integer.valueOf(6));
    if (bonuses!=null)
    {
      BonusManager bonusMgr=ret.getBonusManager();
      List<Integer> keys=new ArrayList<Integer>(bonuses.keySet());
      Collections.sort(keys);
      for(Integer key : keys)
      {
        int index=key.intValue();
        if ((index>=0) && (index<TulkasConstants.BONUS_NAMES.length))
        {
          String bonusName=TulkasConstants.BONUS_NAMES[index];
          Object bonusValue=bonuses.get(key);
          BonusType type=BonusType.getByName(bonusName);
          Bonus bonus=new Bonus(type,BONUS_OCCURRENCE.ALWAYS);
          Object value=type.buildValue(bonusValue);
          bonus.setValue(value);
          bonusMgr.add(bonus);
          //ret.getBonus().add(bonusName+" : "+bonuses.get(key));
          bonuses.remove(key);
        }
        else
        {
          _logger.warn("Unmanaged index: "+index);
        }
      }
      if (bonuses.size()>0)
      {
        _logger.warn("Unmanaged bonuses: "+bonuses);
      }
    }
    return ret;
  }    

  /**
   * Build items sets from raw data items.
   * @param items Loaded data items.
   */
  public void buildSets(HashMap<Integer,HashMap<Object,Object>> items)
  {
    HashMap<String,ItemsSet> sets=new HashMap<String,ItemsSet>();
    List<Integer> keys=new ArrayList<Integer>(items.keySet());
    Collections.sort(keys);
    int nbKeys=keys.size();
    for(int i=0;i<nbKeys;i++)
    {
      Integer id=keys.get(i);
      HashMap<Object,Object> data=items.get(id);
      @SuppressWarnings("unchecked")
      HashMap<Object,Object> setDef=(HashMap<Object,Object>)data.get(Integer.valueOf(15));
      if (setDef!=null)
      {
        @SuppressWarnings("unchecked")
        HashMap<Object,Object> nameMap=(HashMap<Object,Object>)setDef.get(Integer.valueOf(1));
        String setName=(String)nameMap.get(Integer.valueOf(1));
        ItemsSet set=sets.get(setName);
        if (set!=null) continue;
        set=new ItemsSet();
        // Use name as set key
        set.setKey(setName);
        set.setName(setName);
        sets.put(setName,set);
        @SuppressWarnings("unchecked")
        List<Integer> ids=(List<Integer>)setDef.get(Integer.valueOf(2));
        //System.out.println(ids);
        for(Integer setItemId : ids)
        {
          set.addItem(setItemId.intValue(),"");
        }
        // No bonus in this file!
      }
    }
    for(ItemsSet set : sets.values())
    {
      System.out.println(set.dump());
    }
    /*
    [15]={[1]=
        {[1]="Stalwart Captain's Jewelry of Helm's Deep";[2]="";[3]="";[4]="";};
         [2]={1879294461,1879295053,1879299996,1879300219};};};
     */
  }
}
