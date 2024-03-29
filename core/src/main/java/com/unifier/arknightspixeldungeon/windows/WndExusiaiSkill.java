package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.Dungeon;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Attachments.Attachment;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.AssaultRifle;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.ExusiaiSkill;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.GrenadeLauncher;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.Revolver;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.Shotgun;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.SniperRifle;
import com.unifier.arknightspixeldungeon.actors.hero.skills.Exusiai.Guns.Vector;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.ItemSprite;
import com.unifier.arknightspixeldungeon.sprites.ItemSpriteSheet;
import com.unifier.arknightspixeldungeon.ui.Button;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.NinePatch;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;

import java.util.ArrayList;

public class WndExusiaiSkill extends Window {

    private ArrayList<AttachmentSlot> attachmentSlot;

    public ExusiaiSkill.GunType gunType;

    private static final int WIDTH    = 135;
    private static final int HEIGHT   = 150;

    protected static final int GAP	= 2;

    private static final float BUTTON_HEIGHT	= 16;

    public WndExusiaiSkill(ExusiaiSkill skill) {

        this.gunType = skill.getType();
        RenderedTextBlock title = PixelScene.renderTextBlock(Messages.get(skill, "name"), 12 );
        title.hardlight(Window.TITLE_COLOR);
        title.setPos((WIDTH - title.width())/2f, 2);
        add(title);

        Image bluePrint = skill.bluePrintPicture();
        bluePrint.x = WIDTH / 2f - bluePrint.width() / 2f;
        bluePrint.y = title.height() + 2 * GAP;
        PixelScene.align(bluePrint);
        add(bluePrint);

        attachmentSlot = new ArrayList<>();
        AttachmentSlot button;

        //FIXME it looks like there would be some problem or issues if I make this button arranged in seperated function
        if(skill instanceof Revolver){

            button = new AttachmentSlot(Attachment.AttachType.GUN_SIGHT , skill.getGUN_SIGHT(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 27 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 62 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BULLET , skill.getBULLET(), skill,this);
            button.setPos( bluePrint.x +(bluePrint.width * 102 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 62 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);
        }else if(skill instanceof Vector){

            button = new AttachmentSlot(Attachment.AttachType.FRONT_HANG , skill.getFRONT_HANG(), skill,this);
            button.setPos( bluePrint.x +(bluePrint.width * 17 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BELOW_HANG , skill.getBELOW_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 48 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);


            button = new AttachmentSlot(Attachment.AttachType.AMMO_BOX , skill.getAMMO_BOX(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 79 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.GUN_SIGHT , skill.getGUN_SIGHT(), skill ,this);
            button.setPos( bluePrint.x + (bluePrint.width * 111 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);
        }else if(skill instanceof AssaultRifle){

            button = new AttachmentSlot(Attachment.AttachType.FRONT_HANG , skill.getFRONT_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 8 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BELOW_HANG , skill.getBELOW_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 31 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);


            button = new AttachmentSlot(Attachment.AttachType.AMMO_BOX , skill.getAMMO_BOX(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 53 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.GUN_SIGHT , skill.getGUN_SIGHT(), skill ,this);
            button.setPos( bluePrint.x + (bluePrint.width * 77 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BULLET , skill.getAMMO_BOX(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 97 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BACK_HANG , skill.getBELOW_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 120 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);
        }
        else if(skill instanceof SniperRifle){

            button = new AttachmentSlot(Attachment.AttachType.FRONT_HANG , skill.getFRONT_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 17 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.GUN_SIGHT , skill.getGUN_SIGHT(), skill ,this);
            button.setPos( bluePrint.x + (bluePrint.width * 49 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.AMMO_BOX , skill.getAMMO_BOX(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 80 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BACK_HANG , skill.getBELOW_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 111 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);
        }else if(skill instanceof Shotgun){

            button = new AttachmentSlot(Attachment.AttachType.FRONT_HANG , skill.getFRONT_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 21 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BELOW_HANG , skill.getGUN_SIGHT(), skill ,this);
            button.setPos( bluePrint.x + (bluePrint.width * 50 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BULLET , skill.getAMMO_BOX(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 79 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BACK_HANG , skill.getBELOW_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 109 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);
        }else if(skill instanceof GrenadeLauncher){

            button = new AttachmentSlot(Attachment.AttachType.BELOW_HANG , skill.getFRONT_HANG(), skill,this);
            button.setPos( bluePrint.x + (bluePrint.width * 27 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);

            button = new AttachmentSlot(Attachment.AttachType.BACK_HANG , skill.getGUN_SIGHT(), skill ,this);
            button.setPos( bluePrint.x + (bluePrint.width * 102 / 128) - (button.width() / 2) , bluePrint.y + (bluePrint.height * 61 / 64) );
            attachmentSlot.add(button);
            addToFront(button);
            add(button);
        }
        String message = skill.desc() + "\n\n" +skill.otherInfo();
        RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
        text.text( message, WIDTH - 2 * GAP );
        text.setPos( GAP, attachmentSlot.get(attachmentSlot.size() - 1).bottom() + GAP );
        add( text );

        int height = layOutNecessaryButton(skill,text.bottom());

        update();
        resize( WIDTH, HEIGHT);

        float y = HEIGHT;

        if (Dungeon.hero.isAlive() && !skill.windowActions().isEmpty()) {
            y += GAP;
            ArrayList<RedButton> buttons = new ArrayList<>();
            for (final String action : skill.windowActions()) {

                RedButton btn = new RedButton( skill.actionName(action), 8 ) {
                    @Override
                    protected void onClick() {
                        hide();
                        //if (owner != null && owner.parent != null) owner.hide();
                        skill.excuteActions(action);
                    }
                };

                btn.setSize( btn.reqWidth(), BUTTON_HEIGHT );
                buttons.add(btn);
                add( btn );
            }
            y = layoutButtons(buttons, width, y);
        }
        resize( WIDTH, (int)(y) );
    }

    private static float layoutButtons(ArrayList<RedButton> buttons, float width, float y){
        ArrayList<RedButton> curRow = new ArrayList<>();
        float widthLeftThisRow = width;
        while( !buttons.isEmpty() ){
            RedButton btn = buttons.get(0);
            widthLeftThisRow -= btn.width();
            if (curRow.isEmpty()) {
                curRow.add(btn);
                buttons.remove(btn);
            } else {
                widthLeftThisRow -= 1;
                if (widthLeftThisRow >= 0) {
                    curRow.add(btn);
                    buttons.remove(btn);
                }
            }
            //layout current row. Currently forces a max of 3 buttons but can work with more
            if (buttons.isEmpty() || widthLeftThisRow <= 0 || curRow.size() >= 3){

                //re-use this variable for laying out the buttons
                widthLeftThisRow = width - (curRow.size()-1);
                for (RedButton b : curRow){
                    widthLeftThisRow -= b.width();
                }
                //while we still have space in this row, find the shortest button(s) and extend them
                while (widthLeftThisRow > 0){
                    ArrayList<RedButton> shortest = new ArrayList<>();
                    RedButton secondShortest = null;
                    for (RedButton b : curRow) {
                        if (shortest.isEmpty()) {
                            shortest.add(b);
                        } else {
                            if (b.width() < shortest.get(0).width()) {
                                secondShortest = shortest.get(0);
                                shortest.clear();
                                shortest.add(b);
                            } else if (b.width() == shortest.get(0).width()) {
                                shortest.add(b);
                            } else if (secondShortest == null || secondShortest.width() > b.width()){
                                secondShortest = b;
                            }
                        }
                    }
                    float widthToGrow;

                    if (secondShortest == null){
                        widthToGrow = widthLeftThisRow / shortest.size();
                        widthLeftThisRow = 0;
                    } else {
                        widthToGrow = secondShortest.width() - shortest.get(0).width();
                        if ((widthToGrow * shortest.size()) >= widthLeftThisRow){
                            widthToGrow = widthLeftThisRow / shortest.size();
                            widthLeftThisRow = 0;
                        } else {
                            widthLeftThisRow -= widthToGrow * shortest.size();
                        }
                    }
                    for (RedButton toGrow : shortest){
                        toGrow.setRect(0, 0, toGrow.width()+widthToGrow, toGrow.height());
                    }
                }
                //finally set positions
                float x = 0;
                for (RedButton b : curRow){
                    b.setRect(x, y, b.width(), b.height());
                    x += b.width() + 1;
                }
                //move to next line and reset variables
                y += BUTTON_HEIGHT+1;
                widthLeftThisRow = width;
                curRow.clear();
            }
        }
        return y - 1;
    }

    private int layOutNecessaryButton(ExusiaiSkill skill, float bottom) {
        if(skill.available()){

        }
        return (int) bottom;
    }

    private void arrangeRevolverButton(Image bluePrint) {
        //attachmentSlot.get(0).setPos( bluePrint.x * (27/128) - attachmentSlot.get(0).width() / 2 , bluePrint.y);
        //attachmentSlot.get(1).setPos( bluePrint.x * (102/128) - attachmentSlot.get(0).width() / 2 , bluePrint.y);
    }

    protected class AttachmentSlot extends Button {
        protected NinePatch bg;
        public Attachment.AttachType attachType;
        public Attachment attachment;
        public ExusiaiSkill skill;
        private Image icon;

        private WndExusiaiSkill basement;

        private ItemSprite.Glowing glowing;
        private boolean glowUp;

        private float phase;

        AttachmentSlot(Attachment.AttachType attachType, Attachment attachment, ExusiaiSkill skill, WndExusiaiSkill wndExusiaiSkill) {
            super();

            basement = wndExusiaiSkill;

            width = 16;
            height = 16;
            this.attachType = attachType;
            this.attachment = attachment;
            this.skill = skill;

            bg = Chrome.get(Chrome.Type.RED_BUTTON);
            addToBack(bg);
            bg.size(width, height);

            if (attachment != Attachment.NULL_ATTACHMENT) {
                icon = attachment.icon();

            } else {
                switch (attachType) {
                    case GUN_SIGHT:
                        icon = new ItemSprite(ItemSpriteSheet.NULLWARN, null);
                        break;
                    case FRONT_HANG:
                        icon = new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER, null);
                        break;
                    case BELOW_HANG:
                        icon = new ItemSprite(ItemSpriteSheet.ARMOR_HOLDER, null);
                        break;
                    case BACK_HANG:
                        icon = new ItemSprite(ItemSpriteSheet.WAND_HOLDER, null);
                        break;
                    case AMMO_BOX:
                        icon = new ItemSprite(ItemSpriteSheet.RING_HOLDER, null);
                        break;
                    case BULLET:
                        icon = new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER, null);
                        break;
                }
            }
            add(icon);

            this.glowing = new ItemSprite.Glowing( 0xFFFFFF );
            this.glowUp  = true;
        }

        @Override
        protected void layout() {
            super.layout();
            bg.x = x;
            bg.y = y;
            icon.x = x;
            icon.y = y;
            PixelScene.align(icon);
        }

        @Override
        protected void onClick() {
            super.onClick();
            GameScene.show(new AttachmentListWindow(attachType, attachment, skill, basement,this));
        }

        @Override
        protected void onPointerDown() {
            bg.brightness(1.2f);
            Sample.INSTANCE.play(Assets.SND_CLICK);
        }

        @Override
        protected void onPointerUp() {
            bg.resetColor();
        }

        public void updateIcon() {

            icon.destroy();

            switch (attachType) {
                case GUN_SIGHT:
                    attachment = skill.getGUN_SIGHT();
                    break;
                case FRONT_HANG:
                    attachment = skill.getFRONT_HANG();
                    break;
                case BELOW_HANG:
                    attachment = skill.getBELOW_HANG();
                    break;
                case BACK_HANG:
                    attachment = skill.getBACK_HANG();
                    break;
                case AMMO_BOX:
                    attachment = skill.getAMMO_BOX();
                    break;
                case BULLET:
                    attachment = skill.getBULLET();
                    break;
            }

            if (attachment != Attachment.NULL_ATTACHMENT) {
                icon = attachment.icon();
                glowUp = false;
                phase = glowing.period;
            } else {
                switch (attachType) {
                    case GUN_SIGHT:
                        icon = new ItemSprite(ItemSpriteSheet.NULLWARN, null);
                        break;
                    case FRONT_HANG:
                        icon = new ItemSprite(ItemSpriteSheet.WEAPON_HOLDER, null);
                        break;
                    case BELOW_HANG:
                        icon = new ItemSprite(ItemSpriteSheet.ARMOR_HOLDER, null);
                        break;
                    case BACK_HANG:
                        icon = new ItemSprite(ItemSpriteSheet.WAND_HOLDER, null);
                        break;
                    case AMMO_BOX:
                        icon = new ItemSprite(ItemSpriteSheet.RING_HOLDER, null);
                        break;
                    case BULLET:
                        icon = new ItemSprite(ItemSpriteSheet.ARTIFACT_HOLDER, null);
                        break;
                }
            }
            icon.x = x;
            icon.y = y;
            add(icon);
            PixelScene.align(icon);

            update();
            parent.update();
        }

        public void update(){
            super.update();
            if (visible && glowing != null) {

                //if (glowUp && (phase += Game.elapsed) > glowing.period) {
                //    glowUp = false;
                //    phase = glowing.period;
                // }

                ///else

                if (!glowUp && (phase -= 1.5f * Game.elapsed) < 0) {//a little bit faster at here
                    glowUp = true;//stop
                    phase = 0;
                }

                float value = phase / glowing.period * 0.6f;

                icon.rm =  icon.gm =  icon.bm = 1 - value;
                icon.ra = glowing.red * value;
                icon.ga = glowing.green * value;
                icon.ba = glowing.blue * value;
            }
        }

        public synchronized void glow( ItemSprite.Glowing glowing ){
            this.glowing = glowing;
            if (glowing == null) icon.resetColor();
        }
    }

    private class AttachmentListWindow extends Window {

        private static final int WIDTH = 120;
        private static final int HEIGHT = 130;
        private static final int GAP = 2;

        private int index = 0;

        public RedButton lhs;
        public RedButton rhs;

        public ArrayList<Attachment> attachmentArrayList;
        public RedButton listedUsing;

        public ArrayList<Attachment> listedAttachments;
        public ArrayList<RedButton> listedButton;

        private WndExusiaiSkill basement;
        private AttachmentListWindow pointer;
        private AttachmentSlot slot;

        public AttachmentListWindow(Attachment.AttachType attachType, Attachment attachment, ExusiaiSkill skill, WndExusiaiSkill wndExusiaiSkill, AttachmentSlot attachmentSlot) {

            super();

            this.basement = wndExusiaiSkill;
            slot = attachmentSlot;
            pointer = this;

            resize(WIDTH, HEIGHT);

            index = 0;

            attachmentArrayList = Attachment.getAttachmentList(attachType);

            layOutAttachMentList(attachment,index,skill);

            lhs = new RedButton("<<<", 8){//idea from red dragon(),thanks!
                @Override
                public void onClick(){
                    index = Math.max(index-1 , 0);
                    layOutAttachMentList(attachment,index,skill);
                }
            };
            lhs.setRect(GAP, HEIGHT - 18, 24, 18);
            add(lhs);

            rhs = new RedButton(">>>", 8){
                @Override
                public void onClick(){
                    index = Math.min(index+1 , attachmentArrayList.size() / 4);
                    layOutAttachMentList(attachment,index,skill);
                }
            };
            rhs.setRect(WIDTH - 24 - GAP, HEIGHT - 18 , 24, 18);
            add(rhs);

            update();
        }

        @Override
        public void update(){
            super.update();

            if(lhs!=null) {
                lhs.enable(index > 0);
            }

            if(rhs!=null) {
                rhs.enable(index < attachmentArrayList.size() / 4);
            }
            
        }

        private void layOutAttachMentList(Attachment attachment, int index , ExusiaiSkill skill) {

            if(listedUsing!=null) {listedUsing.killAndErase();listedUsing.destroy();}
            if(listedButton!=null){for(RedButton button:listedButton){ button.killAndErase(); button.destroy();} listedButton=null;}

                listedUsing = new RedButton(attachment != Attachment.NULL_ATTACHMENT ? attachment.desc() : "当前未装备",6){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        if(attachment!=null) {
                            GameScene.show(new AttachmentDetailWindow(attachment, attachment.condition(Dungeon.hero, skill),skill,basement,pointer,slot));
                        }
                    }
                };

                listedUsing.multiline = true;

                if(attachment!= Attachment.NULL_ATTACHMENT){
                    Image usingIcon = attachment.icon();
                    listedUsing.icon(usingIcon);
                    //listedUsing.enable( );
                    listedUsing.leftJustify = true;
                }else {
                    listedUsing.enable(false);
                    listedUsing.alpha(0.3f);
                }

                listedUsing.setSize(WIDTH,20);
                listedUsing.setRect(0, 0 , WIDTH, 20);

                add(listedUsing);

                ColorBlock separator = new ColorBlock(1, 1, 0xFF222222);
                separator.size(WIDTH, 1);
                separator.x = 0;
                separator.y = listedUsing.bottom() + GAP;
                add(separator);

                listedButton = new ArrayList<>();

                if(attachmentArrayList!=null && !attachmentArrayList.isEmpty() && attachment!=null){
                    attachmentArrayList.remove(attachment);
                }

                for(int i= index * 4,t = 0; i< ( index + 1 ) * 4; i++,t++){

                    if(i<0 || i >= attachmentArrayList.size()){
                        break;
                    }

                    Attachment temp = attachmentArrayList.get(i);
                    Attachment.Status condition = temp.condition(Dungeon.hero,skill);

                    RedButton button = new RedButton(temp.desc() ,6){
                        @Override
                        protected void onClick() {
                            super.onClick();
                            GameScene.show(new AttachmentDetailWindow(temp,condition,skill, basement, pointer,slot));
                        }

                        @Override
                        protected void onPointerDown() {
                            if(condition == Attachment.Status.available) {
                                bg.brightness(1.2f);
                            }
                            Sample.INSTANCE.play( Assets.SND_CLICK );
                        }

                        @Override
                        protected void onPointerUp() {
                            if(condition == Attachment.Status.available) {
                                bg.resetColor();
                            }
                        }
                    };

                    Image usingIcon = temp.icon();
                    button.icon(usingIcon);

                    button.alpha((condition == Attachment.Status.available ? 1f :(condition == Attachment.Status.locked_by_using ? 0.6f:0.3f)));
                    //button.enable(temp.condition(Dungeon.hero,skill)== Attachment.Status.available || temp.condition(Dungeon.hero,skill)== Attachment.Status.using);
                    //button.enable(false);

                    button.leftJustify = true;
                    button.multiline = true;

                    button.setSize(width,20);
                    button.setRect(0, separator.y + separator.height() + GAP + t * ( 20 + GAP ), width, 20);

                    add(button);
                    listedButton.add(button);
                }

                update();

        }

        private class AttachmentDetailWindow extends Window {

            protected static final int WIDTH_P    = 120;
            protected static final int WIDTH_L    = 160;
            protected static final int GAP	= 2;

            public AttachmentDetailWindow(Attachment attachment, Attachment.Status condition, ExusiaiSkill skill, WndExusiaiSkill basement, AttachmentListWindow pointer, AttachmentSlot slot) {
                //super(attachment.icon(),attachment.name(),attachment.desc());

                super();

                int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

                Component titlebar = 	 new IconTitle( attachment.icon(), attachment.name() );

                titlebar.setRect( 0, 0, width, 0 );
                add(titlebar);

                RenderedTextBlock text = PixelScene.renderTextBlock( 6 );
                text.text( attachment.desc(), width );
                text.setPos( titlebar.left(), titlebar.bottom() + 2*GAP );
                add( text );

                //resize( width, (int)text.bottom() );

                String message = "";
                int size = 9;
                switch (condition){
                    case available:
                        message = Messages.get(WndExusiaiSkill.class,"available");break;
                    case using:
                        message = Messages.get(WndExusiaiSkill.class,"using");break;
                    case locked_by_pick:
                        message = Messages.get(WndExusiaiSkill.class,"locked_by_pick");break;
                    case locked_by_using:
                        message = Messages.get(WndExusiaiSkill.class,"locked_by_using",skill.getOccupied(attachment).name());size = 6;break;
                    case locked_by_talent:
                        message = Messages.get(WndExusiaiSkill.class,"locked_by_talent");break;
                    case locked_by_generic:
                        message = Messages.get(WndExusiaiSkill.class,"locked_by_generic");break;
                }

                RedButton confirmButton = new RedButton(message,size) {
                    @Override
                    protected void onClick() {
                        hide();
                        skill.switchAttachment(attachment,condition);
                        pointer.hide();
                        slot.updateIcon();
                        //basement.update();
                        //killAndErase();
                       /// parent.killAndErase();
                        //parent.parent.killAndErase();
                        //(parent.parent).parent.killAndErase();
                    }
                };
                confirmButton.enable(condition == Attachment.Status.available || condition == Attachment.Status.using || condition == Attachment.Status.locked_by_using);
                confirmButton.setRect(0,text.bottom() + 2 * GAP,width,20);
                add(confirmButton);

                resize( width, (int)confirmButton.bottom() );
            }
        }
    }

}
