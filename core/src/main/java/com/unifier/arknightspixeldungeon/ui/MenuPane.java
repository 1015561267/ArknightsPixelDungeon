package com.unifier.arknightspixeldungeon.ui;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Challenges;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.PDAction;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.items.Item;
import com.unifier.arknightspixeldungeon.journal.Document;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.windows.WndChallenges;
import com.unifier.arknightspixeldungeon.windows.WndGame;
import com.unifier.arknightspixeldungeon.windows.WndJournal;
import com.unifier.arknightspixeldungeon.windows.WndKeyBindings;
import com.watabou.input.GameAction;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

public class MenuPane extends Component {

    private Image bg;

    private Image depthIcon;
    private BitmapText depthText;
    private Button depthButton;

    private Image challengeIcon;
    private BitmapText challengeText;
    private Button challengeButton;

    private JournalButton btnJournal;
    private MenuButton btnMenu;

    private Toolbar.PickedUpItem pickedUp;

    private BitmapText version;

    private DangerIndicator danger;

    public static final int WIDTH = 32;

    @Override
    protected void createChildren() {
        super.createChildren();

        bg = new Image(Assets.MENU_PANE);
        add(bg);

        depthIcon = Icons.get(Dungeon.level.feeling);
        add(depthIcon);

        depthText = new BitmapText( Integer.toString( Dungeon.depth ), PixelScene.pixelFont);
        depthText.hardlight( 0xCACFC2 );
        depthText.measure();
        add( depthText );

        depthButton = new Button(){
            @Override
            protected String hoverText() {
                switch (Dungeon.level.feeling) {
                    case CHASM:     return Messages.get(GameScene.class, "chasm");
                    case WATER:     return Messages.get(GameScene.class, "water");
                    case GRASS:     return Messages.get(GameScene.class, "grass");
                    case DARK:      return Messages.get(GameScene.class, "dark");
                    //case LARGE:     return Messages.get(GameScene.class, "large");
                    //case TRAPS:     return Messages.get(GameScene.class, "traps");
                    //case SECRETS:   return Messages.get(GameScene.class, "secrets");
                }
                return null;
            }

            @Override
            protected void onClick() {
                super.onClick();
                //just open journal for now, maybe have it open landmarks after expanding that page?
                GameScene.show( new WndJournal() );
            }
        };
        add(depthButton);

        if (Challenges.activeChallenges() > 0){
            //challengeIcon = Icons.get(Icons.CHAL_COUNT);

            switch (Challenges.activeChallenges()){
                case 1:
                case 2:challengeIcon = Icons.get(Icons.SMALL_CHALLENGE_ON);break;
                case 7:challengeIcon = Icons.get(Icons.SMALL_CHALLENGE_FULL);break;
                default:challengeIcon = Icons.get(Icons.SMALL_CHALLENGE_MORE);break;
            }

            //challengeIcon = Icons.get(Icons.CHALLENGE_FULL);
            add(challengeIcon);

            challengeText = new BitmapText( Integer.toString( Challenges.activeChallenges() ), PixelScene.pixelFont);
            challengeText.hardlight( 0xCACFC2 );
            challengeText.measure();
            add( challengeText );

            challengeButton = new Button(){
                @Override
                protected void onClick() {
                    GameScene.show(new WndChallenges(Dungeon.challenges, false));
                }

                @Override
                protected String hoverText() {
                    return Messages.get(WndChallenges.class, "title");
                }
            };
            add(challengeButton);
        }

        btnJournal = new JournalButton();
        add( btnJournal );

        btnMenu = new MenuButton();
        add( btnMenu );

        version = new BitmapText( "v" + Game.version, PixelScene.pixelFont);
        version.alpha( 0.5f );
        add(version);

        danger = new DangerIndicator();
        add( danger );

        add( pickedUp = new Toolbar.PickedUpItem());
    }

    @Override
    protected void layout() {
        super.layout();

        bg.x = x;
        bg.y = y;

        btnMenu.setPos( x + WIDTH - btnMenu.width(), y );

        btnJournal.setPos( btnMenu.left() - btnJournal.width() + 2, y );

        depthIcon.x = btnJournal.left() - 7 + (7 - depthIcon.width())/2f - 0.1f;
        //depthIcon.y = y + 1;
        depthIcon.y = btnJournal.bottom() - depthIcon.height() - 0.4f*depthText.height();//FIXME quite messy,as our challenge icon are a little different
        if (PDSettings.interfaceSize() == 0) depthIcon.y++;
        PixelScene.align(depthIcon);

        depthText.scale.set(PixelScene.align(0.67f));
        depthText.x = depthIcon.x + (depthIcon.width() - depthText.width())/2f;
        depthText.y = depthIcon.y + depthIcon.height();
        PixelScene.align(depthText);

        depthButton.setRect(depthIcon.x, depthIcon.y, depthIcon.width(), depthIcon.height() + depthText.height());

        if (challengeIcon != null){
            challengeIcon.x = btnJournal.left() - 14 + (7 - challengeIcon.width())/2f - 0.1f - 4 ;
            //challengeIcon.y = y + 1;
            challengeIcon.y = btnJournal.bottom() - challengeIcon.height() - 0.4f*challengeText.height();

            if (PDSettings.interfaceSize() == 0) challengeIcon.y++;
            PixelScene.align(challengeIcon);

            challengeText.scale.set(PixelScene.align(0.67f));
            challengeText.x = challengeIcon.x + (challengeIcon.width() - challengeText.width())/2f;
            challengeText.y = challengeIcon.y + challengeIcon.height();
            PixelScene.align(challengeText);

            challengeButton.setRect(challengeIcon.x, challengeIcon.y, challengeIcon.width(), challengeIcon.height() + challengeText.height());
        }

        version.scale.set(PixelScene.align(0.5f));
        version.measure();
        version.x = x + WIDTH - version.width();
        //version.y = y + bg.height() + (3 - version.baseLine());
        version.y = y + bg.height() + (5 - version.baseLine());
        PixelScene.align(version);

        danger.setPos( x + WIDTH - danger.width(), y + bg.height + 5 );
    }

    public void pickup(Item item, int cell) {
        pickedUp.reset( item,
                cell,
                btnJournal.centerX(),
                btnJournal.centerY());
    }

    public void flashForPage(Document doc, String page ){
        btnJournal.flashingDoc = doc;
        btnJournal.flashingPage = page;
    }

    public void updateKeys(){
        btnJournal.updateKeyDisplay();
    }

    private static class JournalButton extends Button {

        private Image bg;
        private Image journalIcon;
        private KeyDisplay keyIcon;

        private Document flashingDoc = null;
        private String flashingPage = null;

        public JournalButton() {
            super();

            width = bg.width + 4;
            height = bg.height + 4;
        }

        @Override
        public GameAction keyAction() {
            return PDAction.JOURNAL;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            bg = new Image( Assets.MENU, 2, 2, 13, 11 );
            add( bg );

            journalIcon = new Image( Assets.MENU
                    , 31, 0, 11, 7);
            add( journalIcon );

            keyIcon = new KeyDisplay();
            add(keyIcon);
            updateKeyDisplay();
        }

        @Override
        protected void layout() {
            super.layout();

            bg.x = x + 2;
            bg.y = y + 2;

            journalIcon.x = bg.x + (bg.width() - journalIcon.width())/2f;
            journalIcon.y = bg.y + (bg.height() - journalIcon.height())/2f;
            PixelScene.align(journalIcon);

            keyIcon.x = bg.x + 1;
            keyIcon.y = bg.y + 1;
            keyIcon.width = bg.width - 2;
            keyIcon.height = bg.height - 2;
            PixelScene.align(keyIcon);
        }

        private float time;

        @Override
        public void update() {
            super.update();

            if (flashingPage != null){
                journalIcon.am = (float)Math.abs(Math.cos( StatusPane.FLASH_RATE * (time += Game.elapsed) ));
                keyIcon.am = journalIcon.am;
                bg.brightness(0.5f + journalIcon.am);
                if (time >= Math.PI/StatusPane.FLASH_RATE) {
                    time = 0;
                }
            }
        }

        public void updateKeyDisplay() {
            keyIcon.updateKeys();
            keyIcon.visible = keyIcon.keyCount() > 0;
            journalIcon.visible = !keyIcon.visible;
            if (keyIcon.keyCount() > 0) {
                bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
            } else {
                bg.resetColor();
            }
        }

        @Override
        protected void onPointerDown() {
            bg.brightness( 1.5f );
            Sample.INSTANCE.play( Assets.SND_CLICK );
        }

        @Override
        protected void onPointerUp() {
            if (keyIcon.keyCount() > 0) {
                bg.brightness(.8f - (Math.min(6, keyIcon.keyCount()) / 20f));
            } else {
                bg.resetColor();
            }
        }

        @Override
        protected void onClick() {
            time = 0;
            keyIcon.am = journalIcon.am = 1;
            if (flashingPage != null){
                /*if (flashingDoc == Document.ALCHEMY_GUIDE){
                    WndJournal.last_index = 1;
                    GameScene.show( new WndJournal() );
                }*/
                /*else if (flashingDoc.pageNames().contains(flashingPage)){
                    GameScene.show( new WndStory( flashingDoc.pageSprite(flashingPage),
                            flashingDoc.pageTitle(flashingPage),
                            flashingDoc.pageBody(flashingPage) ){
                        @Override
                        public void hide() {
                            super.hide();
                            if (SPDSettings.intro()){
                                GameScene.endIntro();
                            }
                        }
                    });
                    flashingDoc.readPage(flashingPage);
                }*/ //else {
                    GameScene.show( new WndJournal() );
                //}
                flashingPage = null;
            } else {
                GameScene.show( new WndJournal() );
            }
        }

        @Override
        protected String hoverText() {
            return Messages.titleCase(Messages.get(WndKeyBindings.class, "journal"));
        }
    }

    private static class MenuButton extends Button {

        private Image image;

        public MenuButton() {
            super();

            width = image.width + 4;
            height = image.height + 4;
        }

        @Override
        protected void createChildren() {
            super.createChildren();

            image = new Image( Assets.MENU, 17, 2, 12, 11 );
            add( image );
        }

        @Override
        protected void layout() {
            super.layout();

            image.x = x + 2;
            image.y = y + 2;
        }

        @Override
        protected void onPointerDown() {
            image.brightness( 1.5f );
            Sample.INSTANCE.play( Assets.SND_CLICK );
        }

        @Override
        protected void onPointerUp() {
            image.resetColor();
        }

        @Override
        protected void onClick() {
            GameScene.show( new WndGame() );
        }

        @Override
        public GameAction keyAction() {
            return GameAction.BACK;
        }

        @Override
        protected String hoverText() {
            return Messages.titleCase(Messages.get(WndKeyBindings.class, "menu"));
        }
    }
}
