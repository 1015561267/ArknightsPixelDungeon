package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.actors.hero.HeroClass;
import com.unifier.arknightspixeldungeon.actors.hero.Talent;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.IconButton;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.TalentIcon;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;

public class WndHeroInfo extends WndTabbed{

    private HeroInfoTab heroInfo;
    private SkillInfoTab skillInfo;
    private SpecificInfoTab specificInfo;

    private static int WIDTH = 120;
    private static int MIN_HEIGHT = 125;
    private static int MARGIN = 2;

    public WndHeroInfo( HeroClass cl ){

        Image tabIcon;
        Image skillIcon;
        Image specificIcon;

        switch (cl){
            case WARRIOR: default:
                tabIcon = new ItemSprite(ItemSpriteSheet.SEAL, null);
                skillIcon = new TalentIcon(Talent.FLASH);
                specificIcon = new TalentIcon(Talent.PREEMPTIVE_STRIKE);
                break;
            case MAGE:
                tabIcon = new ItemSprite(ItemSpriteSheet.MAGES_STAFF, null);
                skillIcon = new TalentIcon(Talent.SHEATH_THROW);
                specificIcon = new TalentIcon(Talent.SHEATH_BOUNCE);
                break;
            case ROGUE:
                tabIcon = new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK, null);
                skillIcon = new TalentIcon(Talent.REPRIMAND);
                specificIcon = new TalentIcon(Talent.WELL_PREPARED);
                break;
            case HUNTRESS:
                tabIcon = new ItemSprite(ItemSpriteSheet.BOOMERANG, null);
                skillIcon = new TalentIcon(Talent.PARRY);
                specificIcon = new TalentIcon(Talent.COUNTER_STRIKE);
                break;
        }

        int finalHeight = MIN_HEIGHT;

        heroInfo = new HeroInfoTab(cl);
        add(heroInfo);
        heroInfo.setSize(WIDTH, MIN_HEIGHT);
        finalHeight = (int)Math.max(finalHeight, heroInfo.height());

        add( new IconTab( tabIcon ){
            @Override
            protected void select(boolean value) {
                super.select(value);
                heroInfo.visible = heroInfo.active = value;
            }
        });

        skillInfo = new SkillInfoTab(cl);
        add(skillInfo);
        skillInfo.setSize(WIDTH, MIN_HEIGHT);
        finalHeight = (int)Math.max(finalHeight, skillInfo.height());

        add( new IconTab( skillIcon ){
            @Override
            protected void select(boolean value) {
                super.select(value);
                skillInfo.visible = skillInfo.active = value;
            }
        });

        specificInfo = new SpecificInfoTab(cl);
        add(specificInfo);
        specificInfo.setSize(WIDTH, MIN_HEIGHT);
        finalHeight = (int)Math.max(finalHeight, specificInfo.height());

        add( new IconTab( specificIcon ){
            @Override
            protected void select(boolean value) {
                super.select(value);
                specificInfo.visible = specificInfo.active = value;
            }
        });

        resize(WIDTH, finalHeight);

        layoutTabs();
        heroInfo.layout();

        select(0);

    }

    private static class HeroInfoTab extends Component {

        private RenderedTextBlock title;
        private RenderedTextBlock[] info;
        private Image[] icons;

        private HeroClass heroClass;//have to remeber it temp

        public HeroInfoTab(HeroClass cls){
            super();

            heroClass = cls;

            title = PixelScene.renderTextBlock(Messages.titleCase(cls.title()), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            String[] desc_entries = cls.desc().split("\n\n");

            info = new RenderedTextBlock[desc_entries.length];

            for (int i = 0; i < desc_entries.length; i++){
                info[i] = PixelScene.renderTextBlock(desc_entries[i], 6);
                add(info[i]);
            }

            switch (cls){
                case WARRIOR: default:
                    icons = new Image[]{ new ItemSprite(ItemSpriteSheet.SEAL),
                            new ItemSprite(ItemSpriteSheet.WORN_SHORTSWORD),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case MAGE:
                    icons = new Image[]{ new ItemSprite(ItemSpriteSheet.MAGES_STAFF),
                            new ItemSprite(ItemSpriteSheet.WAND_MAGIC_MISSILE),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case ROGUE:
                    icons = new Image[]{ new ItemSprite(ItemSpriteSheet.ARTIFACT_CLOAK),
                            Icons.get(Icons.DEPTH),
                            new ItemSprite(ItemSpriteSheet.DAGGER),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
                case HUNTRESS:
                    icons = new Image[]{ new ItemSprite(ItemSpriteSheet.BOOMERANG),
                            new Image(Assets.TILES_SEWERS, 112, 96, 16, 16),
                            new ItemSprite(ItemSpriteSheet.KNUCKLEDUSTER),
                            new ItemSprite(ItemSpriteSheet.SCROLL_ISAZ)};
                    break;
            }
            for (Image im : icons) {
                add(im);
            }

        }

        @Override
        protected void layout() {
            super.layout();

            switch (heroClass){
                case WARRIOR:
                case MAGE:
                case ROGUE:
                case HUNTRESS:

                    title.setPos((width-title.width())/2, MARGIN);

                    float pos = title.bottom()+4*MARGIN;

                    for (int i = 0; i < info.length; i++){
                        info[i].maxWidth((int)width - 20);
                        info[i].setPos(20, pos);

                        icons[i].x = (20-icons[i].width())/2;
                        icons[i].y = info[i].top() + (info[i].height() - icons[i].height())/2;

                        pos = info[i].bottom() + 4*MARGIN;
                    }

                    height = Math.max(height, pos - 4*MARGIN);
            }
        }
    }

    private static class SkillInfoTab extends Component {
        private RenderedTextBlock title;
        private RenderedTextBlock message;
        private Image[] skillImages;
        private RenderedTextBlock[] skillDescs;
        private IconButton[] skillInfoIcons;

        private HeroClass heroClass;

        public SkillInfoTab(HeroClass cls){

            super();
            heroClass = cls;

            switch (heroClass){
                case WARRIOR:
                case MAGE:
                case ROGUE:
                case HUNTRESS:

                    title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(HeroClass.class, "skill_title")), 9);
                    title.hardlight(TITLE_COLOR);
                    add(title);

                    message = PixelScene.renderTextBlock(Messages.get(HeroClass.class,heroClass.name()+"_skill_desc"), 6);
                    add(message);

                    skillImages = cls.getSkillIcons();

                    Integer size = cls.getSkillSize();

                    skillDescs = new RenderedTextBlock[size];
                    //skillInfoIcons = new IconButton[size];

                    String[] desc_entries = cls.skillDesc(heroClass).split("\n\n");

                    for(int i = 0;i < size;i++){

                        skillDescs[i] = PixelScene.renderTextBlock(desc_entries[i], 6);

                        int finalI = i;

                        /*skillInfoIcons[i] = new IconButton( Icons.get(Icons.INFO) ){
                            @Override
                            protected void onClick() {
                                Game.scene().addToFront(new WndInfoHeroAbility(heroClass, WndInfoHeroAbility.Type.Skill,finalI));
                            }
                        };*/

                        add(skillImages[i]);
                        add(skillDescs[i]);
                        //add(skillInfoIcons[i]);
                    }
            }
        }

        @Override
        protected void layout() {
            super.layout();

            switch (heroClass){
                case WARRIOR:
                case MAGE:
                case ROGUE:
                case HUNTRESS:
                    title.setPos((width-title.width())/2, MARGIN);
                    message.maxWidth((int)width);
                    message.setPos(0, title.bottom()+4*MARGIN);

                    float pos = message.bottom()+4*MARGIN;

                    Integer size = heroClass.getSkillSize();

                    for (int i = 0; i < size; i++){

                        skillImages[i].x = 0;
                        skillImages[i].y = pos;

                        skillDescs[i].maxWidth((int)(width - skillImages[i].width()));
                        skillDescs[i].setPos(skillImages[i].x  + skillImages[i].width() + 2 * MARGIN, pos);

                        //skillInfoIcons[i].setRect(width-20, skillDescs[i].top() + (skillDescs[i].height()-20)/2, 20, 20);

                        pos = skillDescs[i].bottom() + 4*MARGIN;
                    }

                    height = Math.max(height, pos - 4*MARGIN);
            }
        }
    }

    private static class SpecificInfoTab extends Component {

        private RenderedTextBlock title;
        private RenderedTextBlock message;
        private Image[] specificImages;
        private RenderedTextBlock[] specificDescs;
        private IconButton[] specificInfoIcons;

        private HeroClass heroClass;
        public SpecificInfoTab(HeroClass cls){

            super();
            heroClass = cls;

            switch (heroClass){
                case WARRIOR:
                case MAGE:
                case ROGUE:
                case HUNTRESS:

                    title = PixelScene.renderTextBlock(Messages.titleCase(Messages.get(HeroClass.class, "specific_title")), 9);
                    title.hardlight(TITLE_COLOR);
                    add(title);

                    message = PixelScene.renderTextBlock(Messages.get(HeroClass.class,cls.name()+"_specific_title"), 6);
                    add(message);

                    specificImages = cls.getSpecificIcons();

                    Integer size = cls.getSpecificSize();

                    specificDescs = new RenderedTextBlock[size];
                    //specificInfoIcons = new IconButton[size];

                    String[] desc_entries = cls.specificDesc(heroClass).split("\n\n");

                    for(int i = 0;i < size;i++){
                        specificDescs[i] = PixelScene.renderTextBlock(desc_entries[i], 6);
                        int finalI = i;
                        /*specificInfoIcons[i] = new IconButton( Icons.get(Icons.INFO) ){
                            @Override
                            protected void onClick() {
                                Game.scene().addToFront(new WndInfoHeroAbility(heroClass, WndInfoHeroAbility.Type.Specific,finalI));
                            }
                        };*/
                        add(specificImages[i]);
                        add(specificDescs[i]);
                        //add(specificInfoIcons[i]);
                    }
            }
        }

        @Override
        protected void layout() {
            super.layout();

            switch (heroClass){
                case WARRIOR:
                case MAGE:
                case ROGUE:
                case HUNTRESS:
                    title.setPos((width-title.width())/2, MARGIN);
                    message.maxWidth((int)width);
                    message.setPos(0, title.bottom()+4*MARGIN);

                    float pos = message.bottom()+4*MARGIN;

                    Integer size = heroClass.getSpecificSize();

                    for (int i = 0; i < size; i++){

                        specificImages[i].x = 0;
                        specificImages[i].y = pos;

                        specificDescs[i].maxWidth((int)(width - specificImages[i].width()));
                        specificDescs[i].setPos(specificImages[i].x  + specificImages[i].width() + 2 * MARGIN, pos);

                        //specificInfoIcons[i].setRect(width-20, specificDescs[i].top() + (specificDescs[i].height()-20)/2, 20, 20);

                        pos = specificDescs[i].bottom() + 4*MARGIN;
                    }

                    height = Math.max(height, pos - 4*MARGIN);
            }
        }
    }
}

