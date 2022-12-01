package com.unifier.arknightspixeldungeon.items;

import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.Statistics;
import com.unifier.arknightspixeldungeon.actors.Actor;
import com.unifier.arknightspixeldungeon.actors.Char;
import com.unifier.arknightspixeldungeon.actors.buffs.Buff;
import com.unifier.arknightspixeldungeon.actors.buffs.LockedFloor;
import com.unifier.arknightspixeldungeon.actors.buffs.MindVision;
import com.unifier.arknightspixeldungeon.actors.hero.Hero;
import com.unifier.arknightspixeldungeon.effects.Speck;
import com.unifier.arknightspixeldungeon.items.artifacts.TimekeepersHourglass;
//import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfDebugging;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.unifier.arknightspixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.unifier.arknightspixeldungeon.messages.Messages;
//import com.unifier.arknightspixeldungeon.plants.Swiftthistle;
import com.unifier.arknightspixeldungeon.scenes.CellSelector;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.InterlevelScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.HeroSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.ScrollPane;
import com.unifier.arknightspixeldungeon.ui.StyledButton;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.unifier.arknightspixeldungeon.utils.BArray;
import com.unifier.arknightspixeldungeon.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class LevelTeleporter extends TestItem {

    {
        image = ItemSpriteSheet.AMULET;
        defaultAction = AC_DESCEND;
    }

    private static final String AC_ASCEND = "ascend";
    private static final String AC_DESCEND = "descend";
    private static final String AC_VIEW = "view";
    private static final String AC_TP = "teleport";
    private static final String AC_INTER_TP = "interlevel_tp";

    @Override
    public ArrayList<String> actions(Hero hero ) {
        ArrayList<String> actions = super.actions( hero );
        actions.add( AC_ASCEND );
        actions.add(AC_DESCEND);
        actions.add(AC_VIEW);
        actions.add(AC_TP);
        actions.add(AC_INTER_TP);
        return actions;
    }

    @Override
    protected boolean allowChange(String action){
        return !action.equals(AC_VIEW) && !action.equals(AC_INTER_TP) && super.allowChange(action);
    }

    @Override
    public void execute( Hero hero, String action ) {
        super.execute( hero, action );
        if(action.equals(AC_DESCEND)) {
            if(Dungeon.hero.buff(LockedFloor.class) != null || Dungeon.depth>= 40) {
                GLog.w("传送失败");
                return;
            }
            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
//            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
//            if (buff != null) buff.detach();

            InterlevelScene.mode = InterlevelScene.Mode.DESCEND;

            Game.switchScene( InterlevelScene.class );
        } else if(action.equals(AC_ASCEND)){
            if(Dungeon.hero.buff(LockedFloor.class) != null || Dungeon.depth<=1) {
                GLog.w("传送失败");
                return;
            }
            Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
            if (buff != null) buff.detach();
//            buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
//            if (buff != null) buff.detach();
            InterlevelScene.mode = InterlevelScene.Mode.ASCEND;
            Game.switchScene( InterlevelScene.class );
        } else if(action.equals(AC_VIEW)){
//            Item i = new ScrollOfDebugging();
//            i.collect();
            Buff.affect( hero, MindVision.class, MindVision.DURATION );
            Dungeon.observe();
            ScrollOfMagicMapping som = new ScrollOfMagicMapping();
            som.doRead();
        } else if(action.equals(AC_TP)){
            empoweredRead();
        }else if(action.equals(AC_INTER_TP)){
            if(Dungeon.hero.buff(LockedFloor.class) != null) {
                GLog.w("传送失败");
                return;
            }
            GameScene.show(new WndSelectLevel());
        }
    }

    public static class WndSelectLevel extends Window {
        private static final int WIDTH = 120;
        private static final int GAP = 2;
        private static final int BTN_SIZE = 16;
        private static final int PANE_MAX_HEIGHT = 96;

        private int selectedLevel = 0;
        private ArrayList<DepthButton> btns = new ArrayList<>();
        private StyledButton icb;

        public WndSelectLevel(){
            super();
            resize(WIDTH, 0);
            RenderedTextBlock ttl = PixelScene.renderTextBlock(8);
            ttl.text("传送");
            add(ttl);
            ttl.setPos(WIDTH/2f-ttl.width()/2f, GAP);
            PixelScene.align(ttl);
            ScrollPane sp = new ScrollPane(new Component()){
                @Override
                public void onClick(float x, float y) {
                    super.onClick(x, y);
                    for(DepthButton db: btns){
                        if(db.click(x, y)){
                            break;
                        }
                    }
                }
            };
            add(sp);
            //sp.setRect(0, ttl.bottom() + GAP * 2, WIDTH, PANE_MAX_HEIGHT);
            //GLog.i("%f", ttl.bottom() + GAP * 2);
            Component content = sp.content();
            float xpos = (WIDTH - 5*BTN_SIZE - GAP*8)/2f;
            float ypos = 0;
            float each = GAP*2 + BTN_SIZE;
            for(int i=0; i< 40; ++i){
                int column = i % 5;
                int row = i / 5;
                final int j = i+1;
                DepthButton db = new DepthButton(j){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        setSelectedLevel(j);
                    }
                };
                db.enable(!(j > Statistics.deepestFloor));
                db.setRect(xpos + column * each, ypos + row * each, BTN_SIZE, BTN_SIZE);
                PixelScene.align(db);
                content.add(db);
                btns.add(db);
            }

            content.setSize(WIDTH, btns.get(btns.size() - 1).bottom());
            sp.setRect(0, ttl.bottom() + GAP * 2, WIDTH, Math.min(btns.get(btns.size()-1).bottom(), PANE_MAX_HEIGHT));

            icb = new StyledButton(Chrome.Type.RED_BUTTON, "楼层选择", selectedLevel){
                @Override
                protected void onClick() {
                    super.onClick();
                    Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
                    if (buff != null) buff.detach();
//                    buff = Dungeon.hero.buff(Swiftthistle.TimeBubble.class);
//                    if (buff != null) buff.detach();
                    InterlevelScene.mode = InterlevelScene.Mode.RETURN;
                    InterlevelScene.returnDepth = selectedLevel;
                    InterlevelScene.returnPos = -1;
                    Game.switchScene( InterlevelScene.class );
                }
            };
            add(icb);
            icb.icon(Icons.get(Icons.STAIRS));
            icb.setRect(0, sp.bottom() + GAP * 2, WIDTH, BTN_SIZE);
            setSelectedLevel(0);

            sp.scrollTo(0, 0);

            resize(WIDTH, (int) (icb.bottom()));

            sp.setPos(0, ttl.bottom() + GAP * 2);
        }

        private void setSelectedLevel(int lvl){
            this.selectedLevel = lvl;
            icb.text("楼层选择");
            icb.enable(selectedLevel > 0 && selectedLevel <= 40);
        }
    }

    public static class DepthButton extends StyledButton{
        private int depth;
        public DepthButton(int depth){
            super(Chrome.Type.GEM, String.valueOf(depth), 8);
            this.depth = depth;
        }

        @Override
        protected void layout() {
            super.layout();
            hotArea.width = 0;
            hotArea.height = 0;
        }

        public int getDepth() {
            return depth;
        }

        public boolean click(float x, float y){
            if(active && x > left() && x < right() && y > top() && y < bottom()){
                onClick();
                return true;
            }
            return false;
        }

    }









    public void empoweredRead() {

        GameScene.selectCell(new CellSelector.Listener() {
            @Override
            public void onSelect(Integer target) {
                if (target != null) {
                    //time isn't spent
                    ((HeroSprite)curUser.sprite).read();
                    teleportToLocation(curUser, target);

                }
            }

            @Override
            public String prompt() {
                return Messages.get(ScrollOfTeleportation.class, "prompt");
            }
        });
    }

    public static void teleportToLocation(Hero hero, int pos){
        PathFinder.buildDistanceMap(pos, BArray.or(Dungeon.level.passable, Dungeon.level.avoid, null));
        if (Dungeon.level.avoid[pos] || !Dungeon.level.passable[pos]
                || Actor.findChar(pos) != null){
            GLog.w("到不了");
            return;
        }

        appear( hero, pos );
//        Dungeon.level.occupyCell(hero );
        Dungeon.observe();
        GameScene.updateFog();

    }

    public static void appear(Char ch, int pos ) {

        ch.sprite.interruptMotion();

        if (Dungeon.level.heroFOV[pos] || Dungeon.level.heroFOV[ch.pos]){
//            Sample.INSTANCE.play(Assets.Sounds.TELEPORT);
        }

        ch.move( pos );
        if (ch.pos == pos) ch.sprite.place( pos );

        if (ch.invisible == 0) {
            ch.sprite.alpha( 0 );
            ch.sprite.parent.add( new AlphaTweener( ch.sprite, 1, 0.4f ) );
        }

        if (Dungeon.level.heroFOV[pos] || ch == Dungeon.hero ) {
            ch.sprite.emitter().start(Speck.factory(Speck.LIGHT), 0.2f, 3);
        }
    }
}
