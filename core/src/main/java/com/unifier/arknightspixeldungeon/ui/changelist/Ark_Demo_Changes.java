package com.unifier.arknightspixeldungeon.ui.changelist;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.Image;

import java.util.ArrayList;

public class Ark_Demo_Changes {

    public static void addAllChanges( ArrayList<ChangeInfo> changeInfos ){

        ChangeInfo changes = new ChangeInfo( "ArkPD Demo", true, "");
        changes.hardlight( Window.TITLE_COLOR);
        changeInfos.add(changes);

        add_Ark_Demo_Changes(changeInfos);
    }

    public static void add_Ark_Demo_Changes(ArrayList<ChangeInfo> changeInfos)
    {
        ChangeInfo changes = new ChangeInfo("Ark Demo", false, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes.addButton( new ChangeButton(new Image(Assets.TELLER, 0, 0, 16, 16), "写在前列",
                "_-_ 本地牢基于SPD _0.6.5c_ 版本开发\n" +
                        "_-_ 然而其已经，并将会融合SPD新版的部分内容\n" +
                        "\n" +"_-_ 本地牢在2020年1月已立项，创立之初作为材质版本"+
                        "\n" +"_-_ 本地牢的贴图，音频与背景IP源自明日方舟(Arknights)，但所用素材均源于手工绘制或免费素材"+
                        "\n" +"_-_ 在材质版本发布前，版本号将维持为Demo"+
                        "\n" +"_-_ 尽管当前不涉及过多的游戏机制上的改动，但不代表我们没有这方面的计划"
        ));

        changes.addButton(  new ChangeButton(Icons.get(Icons.LIBGDX), "Lib GDX",
                "_-_ 底层已用SPD _0.7.5_ 版本的libgdx框架重构\n" +
                        "_-_ 这意味着其可支持pc平台，具有更稳定且独立的代码(尽管可能还存在一些bug)\n" +
                        "\n" +"由Teller于_2021.7.29 - 2021.8.7_完成" +
                        "\n\n_-_ 更多的支持，如迭代的桌面版UI与手柄控制支持已基本实现，并附带其他诸如更多选项以及体验优化等小内容\n" +
                        "\n" +"由Teller于_2022-11-11_初步完成"));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_CHALICE3, null), "贴图更新",
                "_-_ 本地牢基于IP已更新大量相关贴图\n" +
                        "_-_ 截至2021/8/9，目前已完成：\n" +
                        "\n" +"人物贴图 100%"+
                        "\n" +"物品贴图与地图 100%"+
                        "\n" +"怪物贴图 75%"+
                        "\n" +"主界面与UI 30%"+
                        "\n" +"如有协助我们的意向，欢迎加入QQ群_839913550_来与我们取得联系"
                ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.BOOMERANG, null), "能天使的火铳",
                "_-_ 原回旋镖改动而来\n" +
                        "_-_ 火铳不再采用SPD旧版回旋镖显示，而是参考SPD新版灵能弓的演示效果\n" +
                        "_-_ 火铳仍然可以使用强化合成玉，而非像SPD随人物等级升级\n" +
                        "_-_ 火铳仍然触发旧版狙击精通效果\n" +
                        "\n" +"如果您对其有更好的设计想法，欢迎发邮件到邮箱_1015561267@qq.com_"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_SANDALS, null), "末药的草药包(破碎草鞋)迭代到新版本",
                "_-_ 截至2022/10/24，末药的草药包(破碎草鞋)已经迭代到破碎_1.4.0_版本\n" +
                        "_-_ 包括主动效果改动与被动效果修正，与破碎大体类似但有细微区别，可自行发掘\n" +
                        "\n" +"由Teller于_2022-11-10_初步完成"
        ));

        changes.addButton( new ChangeButton(new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER, null), "第三饰品栏",
                "_-_ 挺_好_的，不是吗，原来的两个槽真的有些拥挤\n" +
                        "_-_ 截至_2022/11/8_，相关的底层窗口选取迭代成功，这样重复的种子直接不可选取，而非旧版的弹出提示，其他类似情况（如魔典）也相应改动\n"
        ));

        changes.addButton( new ChangeButton(Icons.get(Icons.WARNING), "剧情对话",
                "_-_ 实现了剧情相关的对话，并尝试将npc相关内容整合\n" +
                        "\n" +"_-_目前已实现基本底层，包括点击前进与对话进度保存(于_2021/8/20_动工_2021/8/22_完成)" +
                        "\n" +"_-_已实现章节剧情对话，并取代了原有的故事窗口(第一章剧本统合于_2021/8/24_完成)"+
                        "\n" +"_-_已实现选项框，并已将幽灵任务整合，其余npc相关还在制作中(幽灵剧本统合于_2021/8/28_完成)"+
                        "\n" +"_-_特殊事件对话，以及boss战前后对话还在规划中"+
                        "\n" +"_-_实现类似于视觉小说的文字逐渐显现效果(初步内容于2021/9/10完成)"+
                        "\n" +"_-_如您对头像，对话框UI，字体与剧本内容有想法，建议或参与建设的意愿，请加QQ群_996755375_讨论"
        ));

        changes.addButton( new ChangeButton(Icons.get(Icons.SETTINGS), "杂项",
                "_-_ 杂项相关\n" +
                        "\n" +"配合火铳改动，现在投掷武器和法杖的弹道将不再受草丛阻挡(于_2021/7/25_完成)"+
                        "\n" +"新的关于界面(于_2021/7/28_完成)"+
                        "\n" +"成就系统更新到SPD_0.9.0_的版本(于_2021/8/6_完成)"+
                        "\n" +"已实现镜头平滑滚动(于_2021/8/7_完成)"+
                        "\n" +"语言设置移入设置窗口(于_2021/8/8_完成)"+
                        "\n" +"更新日志改进到新版本(于_2021/8/9_完成)"+
                        "\n" +"主界面改进到新版本(于_2021/8/14_完成)"
        ));
    }
}
