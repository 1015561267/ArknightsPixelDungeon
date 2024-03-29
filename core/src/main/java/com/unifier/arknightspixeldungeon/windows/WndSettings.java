/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2018 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.unifier.arknightspixeldungeon.windows;

import com.unifier.arknightspixeldungeon.ArknightsPixelDungeon;
import com.unifier.arknightspixeldungeon.Assets;
import com.unifier.arknightspixeldungeon.Chrome;
import com.unifier.arknightspixeldungeon.PDSettings;
import com.unifier.arknightspixeldungeon.messages.Languages;
import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scenes.GameScene;
import com.unifier.arknightspixeldungeon.scenes.PixelScene;
import com.unifier.arknightspixeldungeon.sprites.CharSprite;
import com.unifier.arknightspixeldungeon.ui.CheckBox;
import com.unifier.arknightspixeldungeon.ui.GameLog;
import com.unifier.arknightspixeldungeon.ui.Icons;
import com.unifier.arknightspixeldungeon.ui.OptionSlider;
import com.unifier.arknightspixeldungeon.ui.RedButton;
import com.unifier.arknightspixeldungeon.ui.RenderedTextBlock;
import com.unifier.arknightspixeldungeon.ui.Toolbar;
import com.unifier.arknightspixeldungeon.ui.Window;
import com.watabou.input.ControllerHandler;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class WndSettings extends WndTabbed {

	private static final int WIDTH		    = 112;
	private static final int HEIGHT         = 138;

    private static final int WIDTH_P	    = 122;
    private static final int WIDTH_L	    = 223;

    private static final int SLIDER_HEIGHT	= 21;
	private static final int BTN_HEIGHT	    = 16;
    private static final float GAP          = 2;

    private static final int GAP_TINY 		= 2;
	private static final int GAP_SML 		= 6;
	private static final int GAP_LRG 		= 18;

	private DisplayTab display;
	private UITab ui;
    private InputTab  input;
    private AudioTab audio;
    private LangsTab langs;

    //Moved langs settings into setting interface.By Teller,in 2021.8.9

	private static int last_index = 0;

	public WndSettings() {
	    super();

        float height;

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        display = new DisplayTab();
        display.setSize(width, 0);
        height = display.height();
        add( display );


        add( new IconTab(Icons.get(Icons.DISPLAY)){
            @Override
            protected void select(boolean value) {
                super.select(value);
                display.visible = display.active = value;
                if (value) last_index = 0;
            }
        });

        ui = new UITab();
        ui.setSize(width, 0);
        height = Math.max(height, ui.height());
        add( ui );

        add( new IconTab(Icons.get(Icons.SETTINGS)){
            @Override
            protected void select(boolean value) {
                super.select(value);
                ui.visible = ui.active = value;
                if (value) last_index = 1;
            }
        });

        input = new InputTab();
        input.setSize(width, 0);
        height = Math.max(height, input.height());

        if (DeviceCompat.hasHardKeyboard() || ControllerHandler.isControllerConnected()) {
            add(input);
            Image icon;
            if (ControllerHandler.controllerActive || !DeviceCompat.hasHardKeyboard()) {
                icon = Icons.get(Icons.CONTROLLER);
            } else {
                icon = Icons.get(Icons.INPUT);
            }
            add(new IconTab(icon) {
                @Override
                protected void select(boolean value) {
                    super.select(value);
                    input.visible = input.active = value;
                    if (value) last_index = 2;
                }
            });
        }

        audio = new AudioTab();
        audio.setSize(width, 0);
        height = Math.max(height, audio.height());
        add( audio );

        add( new IconTab(Icons.get(Icons.AUDIO)){
            @Override
            protected void select(boolean value) {
                super.select(value);
                audio.visible = audio.active = value;
                if (value) last_index = 3;
            }
        });

        langs = new LangsTab();
        langs.setSize(width, 0);
        height = Math.max(height, langs.height());
        add( langs );


        IconTab langsTab = new IconTab(Icons.get(Icons.LANGUAGE)){
            @Override
            protected void select(boolean value) {
                super.select(value);
                langs.visible = langs.active = value;
                if (value) last_index = 4;
            }

            @Override
            protected void createChildren() {
                super.createChildren();
                switch(Messages.lang().status()){
                    case INCOMPLETE:
                        icon.hardlight(1.5f, 0, 0);
                        break;
                    case UNREVIEWED:
                        icon.hardlight(1.5f, 0.75f, 0f);
                        break;
                }
            }

        };
        add( langsTab );

        resize(width, (int)Math.ceil(height));

        layoutTabs();

        if (tabs.size() == 4 && last_index >= 3){
            //input tab isn't visible
            select(last_index-1);
        } else {
            select(last_index);
        }

	}

    @Override
    public void hide() {
        super.hide();
        //resets generators because there's no need to retain chars for languages not selected
        ArknightsPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
            @Override
            public void beforeCreate() {
                Game.platform.resetGenerators();
            }
            @Override
            public void afterCreate() {
                //do nothing
            }
        });
    }

    private static class DisplayTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;

        CheckBox chkFullscreen;

        //OptionSlider optScale;
        CheckBox chkSaver;
        RedButton btnOrientation;

        ColorBlock sep2;
        OptionSlider optBrightness;
        OptionSlider optVisGrid;
        OptionSlider optFollowIntensity;

        CheckBox flyingSheep;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            chkFullscreen = new CheckBox( Messages.get(this, "fullscreen") ) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PDSettings.fullscreen(checked());
                }
            };
            if (DeviceCompat.supportsFullScreen()){
                chkFullscreen.checked(PDSettings.fullscreen());
            } else {
                chkFullscreen.checked(true);
                chkFullscreen.enable(false);
            }
            add(chkFullscreen);


            /*if (int)Math.ceil(2* Game.density) < PixelScene.maxDefaultZoom) {
                optScale = new OptionSlider(Messages.get(this, "scale"),
                        (int)Math.ceil(2* Game.density)+ "X",
                        PixelScene.maxDefaultZoom + "X",
                        (int)Math.ceil(2* Game.density),
                        PixelScene.maxDefaultZoom ) {
                    @Override
                    protected void onChange() {
                        if (getSelectedValue() != PDSettings.scale()) {
                            PDSettings.scale(getSelectedValue());
                            ArknightsPixelDungeon.seamlessResetScene();
                        }
                    }
                };
                optScale.setSelectedValue(PixelScene.defaultZoom);
                add(optScale);
            }*/

            if (DeviceCompat.isAndroid() && PixelScene.maxScreenZoom >= 2) {
                chkSaver = new CheckBox(Messages.get(this, "saver")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        if (checked()) {
                            checked(!checked());
                            ArknightsPixelDungeon.scene().add(new WndOptions(Icons.get(Icons.DISPLAY),
                                    Messages.get(DisplayTab.class, "saver"),
                                    Messages.get(DisplayTab.class, "saver_desc"),
                                    Messages.get(DisplayTab.class, "okay"),
                                    Messages.get(DisplayTab.class, "cancel")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        checked(!checked());
                                        PDSettings.powerSaver(checked());
                                    }
                                }
                            });
                        } else {
                            PDSettings.powerSaver(checked());
                        }
                    }
                };
                chkSaver.checked( PDSettings.powerSaver() );
                add( chkSaver );
            }

            if (DeviceCompat.isAndroid()) {
                Boolean landscape = PDSettings.landscape();
                if (landscape == null){
                    landscape = Game.width > Game.height;
                }
                Boolean finalLandscape = landscape;
                btnOrientation = new RedButton(finalLandscape ?
                        Messages.get(this, "portrait")
                        : Messages.get(this, "landscape")) {
                    @Override
                    protected void onClick() {
                        PDSettings.landscape(!finalLandscape);
                    }
                };
                add(btnOrientation);
            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            optBrightness = new OptionSlider(Messages.get(this, "brightness"),
                    Messages.get(this, "dark"), Messages.get(this, "bright"), -1, 1) {
                @Override
                protected void onChange() {
                    PDSettings.brightness(getSelectedValue());
                }
            };
            optBrightness.setSelectedValue(PDSettings.brightness());
            add(optBrightness);

            optVisGrid = new OptionSlider(Messages.get(this, "visual_grid"),
                    Messages.get(this, "off"), Messages.get(this, "high"), -1, 2) {
                @Override
                protected void onChange() {
                    PDSettings.visualGrid(getSelectedValue());
                }
            };
            optVisGrid.setSelectedValue(PDSettings.visualGrid());
            add(optVisGrid);

            optFollowIntensity = new OptionSlider(Messages.get(this, "camera_follow"),
                    Messages.get(this, "low"), Messages.get(this, "high"), 1, 4) {
                @Override
                protected void onChange() {
                    PDSettings.cameraFollow(getSelectedValue());
                }
            };
            optFollowIntensity.setSelectedValue(PDSettings.cameraFollow());
            add(optFollowIntensity);

            flyingSheep = new CheckBox(Messages.get(this, "flying_sheep")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    if (checked()) {
                        checked(!checked());
                        ArknightsPixelDungeon.scene().add(new WndOptions(Icons.get(Icons.FLYING_SHEEP),
                                Messages.get(DisplayTab.class, "flying_sheep"),
                                Messages.get(DisplayTab.class, "flying_sheep_desc"),
                                Messages.get(DisplayTab.class, "flying_sheep_okay"),
                                Messages.get(DisplayTab.class, "flying_sheep_cancel")) {
                            @Override
                            protected void onSelect(int index) {
                                if (index == 0) {
                                    checked(!checked());
                                    PDSettings.flyingSheep(checked());
                                }
                            }
                        });
                    } else {
                        PDSettings.flyingSheep(checked());
                    }
                }
            };
            flyingSheep.checked( PDSettings.flyingSheep() );
            add( flyingSheep );
        }

        @Override
        protected void layout() {

            float bottom = y;

            title.setPos((width - title.width())/2, bottom + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2*GAP;

            bottom = sep1.y + 1;

            if (width > 200 && chkSaver != null) {
                chkFullscreen.setRect(0, bottom + GAP, width/2-1, BTN_HEIGHT);
                chkSaver.setRect(chkFullscreen.right()+ GAP, bottom + GAP, width/2-1, BTN_HEIGHT);
                bottom = chkFullscreen.bottom();
            } else {
                chkFullscreen.setRect(0, bottom + GAP, width, BTN_HEIGHT);
                bottom = chkFullscreen.bottom();

                if (chkSaver != null) {
                    chkSaver.setRect(0, bottom + GAP, width, BTN_HEIGHT);
                    bottom = chkSaver.bottom();
                }
            }

            if (btnOrientation != null) {
                btnOrientation.setRect(0, bottom + GAP, width, BTN_HEIGHT);
                bottom = btnOrientation.bottom();
            }


           // if (optScale != null){
            //    optScale.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
           //     bottom = optScale.bottom();
            //}

            sep2.size(width, 1);
            sep2.y = bottom + GAP;
            bottom = sep2.y + 1;

            if (width > 200){
                optBrightness.setRect(0, bottom + GAP, width/2-GAP/2, SLIDER_HEIGHT);
                optVisGrid.setRect(optBrightness.right() + GAP, optBrightness.top(), width/2-GAP/2, SLIDER_HEIGHT);
            } else {
                optBrightness.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
                optVisGrid.setRect(0, optBrightness.bottom() + GAP, width, SLIDER_HEIGHT);
            }

            optFollowIntensity.setRect(0, optVisGrid.bottom() + GAP, width, SLIDER_HEIGHT);
            flyingSheep.setRect(0, optFollowIntensity.bottom() + GAP, width, BTN_HEIGHT);

            height = flyingSheep.bottom();
        }

    }

    private static class UITab extends Component {

        RenderedTextBlock title;

        ColorBlock sep1;
        OptionSlider optUIMode;
        OptionSlider optUIScale;

        RedButton btnToolbarSettings;

        //RenderedTextBlock barDesc;
        //RedButton btnSplit; RedButton btnGrouped; RedButton btnCentered;

        //CheckBox chkFlipToolbar;
        //CheckBox chkFlipTags;

        ColorBlock sep2;

        CheckBox chkFont;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            //add slider for UI size only if device has enough space to support it
            float wMin = Game.width / PixelScene.MIN_WIDTH_FULL;
            float hMin = Game.height / PixelScene.MIN_HEIGHT_FULL;
            if (Math.min(wMin, hMin) >= 2 * Game.density) {
                optUIMode = new OptionSlider(
                        Messages.get(this, "ui_mode"),
                        Messages.get(this, "mobile"),
                        Messages.get(this, "full"),
                        0,
                        2
                ) {
                    @Override
                    protected void onChange() {
                        PDSettings.interfaceSize(getSelectedValue());
                        ArknightsPixelDungeon.seamlessResetScene();
                    }
                };
                optUIMode.setSelectedValue(PDSettings.interfaceSize());
                add(optUIMode);
            }

            if ((int) Math.ceil(2 * Game.density) < PixelScene.maxDefaultZoom) {
                optUIScale = new OptionSlider(Messages.get(this, "scale"),
                        (int) Math.ceil(2 * Game.density) + "X",
                        PixelScene.maxDefaultZoom + "X",
                        (int) Math.ceil(2 * Game.density),
                        PixelScene.maxDefaultZoom) {
                    @Override
                    protected void onChange() {
                        if (getSelectedValue() != PDSettings.scale()) {
                            PDSettings.scale(getSelectedValue());
                            ArknightsPixelDungeon.seamlessResetScene();
                        }
                    }
                };
                optUIScale.setSelectedValue(PixelScene.defaultZoom);
                add(optUIScale);
            }

            if (PDSettings.interfaceSize() == 0) {
                btnToolbarSettings = new RedButton(Messages.get(this, "toolbar_settings"), 9) {
                    @Override
                    protected void onClick() {
                        ArknightsPixelDungeon.scene().addToFront(new Window() {

                            RenderedTextBlock barDesc;
                            RedButton btnSplit;
                            RedButton btnGrouped;
                            RedButton btnCentered;
                            //CheckBox chkQuickSwapper;
                            //RenderedTextBlock swapperDesc;
                            CheckBox chkFlipToolbar;
                            CheckBox chkFlipTags;

                            {
                                barDesc = PixelScene.renderTextBlock(Messages.get(WndSettings.UITab.this, "mode"), 9);
                                add(barDesc);

                                btnSplit = new RedButton(Messages.get(WndSettings.UITab.this, "split")) {
                                    @Override
                                    protected void onClick() {
                                        textColor(TITLE_COLOR);
                                        btnGrouped.textColor(WHITE);
                                        btnCentered.textColor(WHITE);
                                        PDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
                                        Toolbar.updateLayout();
                                    }
                                };
                                if (PDSettings.toolbarMode().equals(Toolbar.Mode.SPLIT.name())) {
                                    btnSplit.textColor(TITLE_COLOR);
                                }
                                add(btnSplit);

                                btnGrouped = new RedButton(Messages.get(WndSettings.UITab.this, "group")) {
                                    @Override
                                    protected void onClick() {
                                        btnSplit.textColor(WHITE);
                                        textColor(TITLE_COLOR);
                                        btnCentered.textColor(WHITE);
                                        PDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
                                        Toolbar.updateLayout();
                                    }
                                };
                                if (PDSettings.toolbarMode().equals(Toolbar.Mode.GROUP.name())) {
                                    btnGrouped.textColor(TITLE_COLOR);
                                }
                                add(btnGrouped);

                                btnCentered = new RedButton(Messages.get(WndSettings.UITab.this, "center")) {
                                    @Override
                                    protected void onClick() {
                                        btnSplit.textColor(WHITE);
                                        btnGrouped.textColor(WHITE);
                                        textColor(TITLE_COLOR);
                                        PDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
                                        Toolbar.updateLayout();
                                    }
                                };
                                if (PDSettings.toolbarMode().equals(Toolbar.Mode.CENTER.name())) {
                                    btnCentered.textColor(TITLE_COLOR);
                                }
                                add(btnCentered);

                                /*
                                chkQuickSwapper = new CheckBox(Messages.get(WndSettings.UITab.this, "quickslot_swapper")) {
                                    @Override
                                    protected void onClick() {
                                        super.onClick();
                                        PDSettings.quickSwapper(checked());
                                        Toolbar.updateLayout();
                                    }
                                };
                                chkQuickSwapper.checked(PDSettings.quickSwapper());
                                add(chkQuickSwapper);*/

                                //swapperDesc = PixelScene.renderTextBlock(Messages.get(WndSettings.UITab.this, "swapper_desc"), 5);
                                //swapperDesc.hardlight(0x888888);
                                //add(swapperDesc);

                                chkFlipToolbar = new CheckBox(Messages.get(WndSettings.UITab.this, "flip_toolbar")) {
                                    @Override
                                    protected void onClick() {
                                        super.onClick();
                                        PDSettings.flipToolbar(checked());
                                        Toolbar.updateLayout();
                                    }
                                };
                                chkFlipToolbar.checked(PDSettings.flipToolbar());
                                add(chkFlipToolbar);

                                chkFlipTags = new CheckBox(Messages.get(WndSettings.UITab.this, "flip_indicators")) {
                                    @Override
                                    protected void onClick() {
                                        super.onClick();
                                        PDSettings.flipTags(checked());
                                        GameScene.layoutTags();
                                    }
                                };
                                chkFlipTags.checked(PDSettings.flipTags());
                                add(chkFlipTags);

                                //layout
                                resize(WIDTH_P, 0);

                                barDesc.setPos((width - barDesc.width()) / 2f, GAP);
                                PixelScene.align(barDesc);

                                int btnWidth = (int) (width - 2 * GAP) / 3;
                                btnSplit.setRect(0, barDesc.bottom() + GAP, btnWidth, BTN_HEIGHT - 2);
                                btnGrouped.setRect(btnSplit.right() + GAP, btnSplit.top(), btnWidth, BTN_HEIGHT - 2);
                                btnCentered.setRect(btnGrouped.right() + GAP, btnSplit.top(), btnWidth, BTN_HEIGHT - 2);

                                //chkQuickSwapper.setRect(0, btnGrouped.bottom() + GAP, width, BTN_HEIGHT);

                                //swapperDesc.maxWidth(width);
                                ///swapperDesc.setPos(0, chkQuickSwapper.bottom()+1);
                                //swapperDesc.setPos(0, btnCentered.bottom() + 1);

                                if (width > 200) {
                                    chkFlipToolbar.setRect(0, btnCentered.bottom() + 2*GAP, width / 2 - 1, BTN_HEIGHT);
                                    chkFlipTags.setRect(chkFlipToolbar.right() + GAP, chkFlipToolbar.top(), width / 2 - 1, BTN_HEIGHT);
                                } else {
                                    chkFlipToolbar.setRect(0, btnCentered.bottom() + 2*GAP, width, BTN_HEIGHT);
                                    chkFlipTags.setRect(0, chkFlipToolbar.bottom() + 2*GAP, width, BTN_HEIGHT);
                                }

                                resize(WIDTH_P, (int) chkFlipTags.bottom());

                            }
                        });
                    }
                };
                add(btnToolbarSettings);



            /*chkFullscreen = new CheckBox( Messages.get(this, "fullscreen") ) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PDSettings.fullscreen(checked());
                }
            };
            chkFullscreen.checked(PDSettings.fullscreen());
            chkFullscreen.enable(DeviceCompat.supportsFullScreen());
            add(chkFullscreen);*/

            }else {

                ///chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")) {
                //    @Override
                //    protected void onClick() {
                //        super.onClick();
                 //       SPDSettings.flipTags(checked());
                //        GameScene.layoutTags();
                //    }
                //};
                //chkFlipTags.checked(SPDSettings.flipTags());
                //add(chkFlipTags);

            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            chkFont = new CheckBox(Messages.get(this, "system_font")){
                @Override
                protected void onClick() {
                    super.onClick();
                    ArknightsPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
                        @Override
                        public void beforeCreate() {
                            PDSettings.systemFont(checked());
                        }

                        @Override
                        public void afterCreate() {
                            //do nothing
                        }
                    });
                }
            };
            chkFont.checked(PDSettings.systemFont());
            add(chkFont);
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width())/2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2*GAP;

            height = sep1.y + 1;

            if (optUIMode != null && optUIScale != null && width > 200){
                optUIMode.setRect(0, height + GAP, width/2-1, SLIDER_HEIGHT);
                optUIScale.setRect(width/2+1, height + GAP, width/2-1, SLIDER_HEIGHT);
                height = optUIScale.bottom();
            } else {
                if (optUIMode != null) {
                    optUIMode.setRect(0, height + GAP, width, SLIDER_HEIGHT);
                    height = optUIMode.bottom();
                }

                if (optUIScale != null) {
                    optUIScale.setRect(0, height + GAP, width, SLIDER_HEIGHT);
                    height = optUIScale.bottom();
                }
            }

            if (btnToolbarSettings != null) {
                btnToolbarSettings.setRect(0, height + GAP, width, BTN_HEIGHT);
                height = btnToolbarSettings.bottom();
            } else {
                //chkFlipTags.setRect(0, height + GAP, width, BTN_HEIGHT);
                //height = chkFlipTags.bottom();
            }

            sep2.size(width, 1);
            sep2.y = height + GAP;

            chkFont.setRect(0, sep2.y + 1 + GAP, width, BTN_HEIGHT);
            height = chkFont.bottom();

        }

    }

    private static class InputTab extends Component{

        RenderedTextBlock title;
        ColorBlock sep1;

        RedButton btnKeyBindings;
        RedButton btnControllerBindings;

        ColorBlock sep2;

        OptionSlider optControlSens;
        OptionSlider optHoldMoveSens;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            if (DeviceCompat.hasHardKeyboard()){

                btnKeyBindings = new RedButton(Messages.get(this, "key_bindings")){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        ArknightsPixelDungeon.scene().addToFront(new WndKeyBindings(false));
                    }
                };

                add(btnKeyBindings);
            }

            if (ControllerHandler.isControllerConnected()){
                btnControllerBindings = new RedButton(Messages.get(this, "controller_bindings")){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        ArknightsPixelDungeon.scene().addToFront(new WndKeyBindings(true));
                    }
                };

                add(btnControllerBindings);
            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);


            optControlSens = new OptionSlider(
                    Messages.get(this, "controller_sensitivity"),
                    "1",
                    "10",
                    1,
                    10
            ) {
                @Override
                protected void onChange() {
                    PDSettings.controllerPointerSensitivity(getSelectedValue());
                }
            };
            optControlSens.setSelectedValue(PDSettings.controllerPointerSensitivity());
            add(optControlSens);

            optHoldMoveSens = new OptionSlider(
                    Messages.get(this, "movement_sensitivity"),
                    Messages.get(this, "off"),
                    Messages.get(this, "high"),
                    0,
                    4
            ) {
                @Override
                protected void onChange() {
                    PDSettings.movementHoldSensitivity(getSelectedValue());
                }
            };
            optHoldMoveSens.setSelectedValue(PDSettings.movementHoldSensitivity());
            add(optHoldMoveSens);
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width())/2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 3*GAP;

            height = sep1.y+1;

            if (width > 200 && btnKeyBindings != null && btnControllerBindings != null){
                btnKeyBindings.setRect(0, height + GAP, width/2-1, BTN_HEIGHT);
                btnControllerBindings.setRect(width/2+1, height + GAP, width/2-1, BTN_HEIGHT);
                height = btnControllerBindings.bottom();
            } else {
                if (btnKeyBindings != null) {
                    btnKeyBindings.setRect(0, height + GAP, width, BTN_HEIGHT);
                    height = btnKeyBindings.bottom();
                }

                if (btnControllerBindings != null) {
                    btnControllerBindings.setRect(0, height + GAP, width, BTN_HEIGHT);
                    height = btnControllerBindings.bottom();
                }
            }

            sep2.size(width, 1);
            sep2.y = height+ GAP;

            if (width > 200){
                optControlSens.setRect(0, sep2.y + 1 + GAP, width/2-1, SLIDER_HEIGHT);
                optHoldMoveSens.setRect(width/2 + 1, optControlSens.top(), width/2 -1, SLIDER_HEIGHT);
            } else {
                optControlSens.setRect(0, sep2.y + 1 + GAP, width, SLIDER_HEIGHT);
                optHoldMoveSens.setRect(0, optControlSens.bottom() + GAP, width, SLIDER_HEIGHT);
            }

            height = optHoldMoveSens.bottom();

        }
    }

    private static class AudioTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        OptionSlider optMusic;
        CheckBox chkMusicMute;
        ColorBlock sep2;
        OptionSlider optSFX;
        CheckBox chkMuteSFX;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            optMusic = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    PDSettings.musicVol(getSelectedValue());
                }
            };
            optMusic.setSelectedValue(PDSettings.musicVol());
            add(optMusic);

            chkMusicMute = new CheckBox(Messages.get(this, "music_mute")){
                @Override
                protected void onClick() {
                    super.onClick();
                    PDSettings.music(!checked());
                }
            };
            chkMusicMute.checked(!PDSettings.music());
            add(chkMusicMute);

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            optSFX = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    PDSettings.SFXVol(getSelectedValue());
                    if (Random.Int(100) == 0){
                        Sample.INSTANCE.play(Assets.SND_MIMIC);
                    } else {
                        Sample.INSTANCE.play(Random.oneOf(Assets.SND_GOLD,
                                Assets.SND_HIT,
                                Assets.SND_ITEM,
                                Assets.SND_SHATTER,
                                Assets.SND_EVOKE,
                                Assets.SND_SECRET));
                    }
                }
            };
            optSFX.setSelectedValue(PDSettings.SFXVol());
            add(optSFX);

            chkMuteSFX = new CheckBox( Messages.get(this, "sfx_mute") ) {
                @Override
                protected void onClick() {
                    super.onClick();
                    PDSettings.soundFx(!checked());
                    Sample.INSTANCE.play( Assets.SND_CLICK );
                }
            };
            chkMuteSFX.checked(!PDSettings.soundFx());
            add( chkMuteSFX );

            /*if (DeviceCompat.isiOS() && Messages.lang() == Languages.ENGLISH){

                sep3 = new ColorBlock(1, 1, 0xFF000000);
                add(sep3);

                chkIgnoreSilent = new CheckBox( Messages.get(this, "ignore_silent") ){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        SPDSettings.ignoreSilentMode(checked());
                    }
                };
                chkIgnoreSilent.checked(PDSettings.ignoreSilentMode());
                add(chkIgnoreSilent);
            }*/
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width())/2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2*GAP;

            optMusic.setRect(0, sep1.y + 1 + GAP, width, SLIDER_HEIGHT);
            chkMusicMute.setRect(0, optMusic.bottom() + GAP, width, BTN_HEIGHT);

            sep2.size(width, 1);
            sep2.y = chkMusicMute.bottom() + GAP;

            optSFX.setRect(0, sep2.y + 1 + GAP, width, SLIDER_HEIGHT);
            chkMuteSFX.setRect(0, optSFX.bottom() + GAP, width, BTN_HEIGHT);

            height = chkMuteSFX.bottom();
        }

    }

    private static class LangsTab extends Component{

        final static int COLS_P = 3;
        final static int COLS_L = 4;

        final static int BTN_HEIGHT = 11;

        RenderedTextBlock title;
        ColorBlock sep1;
        RenderedTextBlock txtLangName;
        RenderedTextBlock txtLangInfo;
        ColorBlock sep2;
        RedButton[] lanBtns;
        ColorBlock sep3;
        RenderedTextBlock txtTranifex;
        RedButton btnCredits;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            final ArrayList<Languages> langs = new ArrayList<>(Arrays.asList(Languages.values()));

            Languages nativeLang = Languages.matchLocale(Locale.getDefault());
            langs.remove(nativeLang);
            //move the native language to the top.
            langs.add(0, nativeLang);

            final Languages currLang = Messages.lang();

            txtLangName = PixelScene.renderTextBlock( Messages.titleCase(currLang.nativeName()) , 9 );
            if (currLang.status() == Languages.Status.REVIEWED) txtLangName.hardlight(TITLE_COLOR);
            else if (currLang.status() == Languages.Status.UNREVIEWED) txtLangName.hardlight(CharSprite.WARNING);
            else if (currLang.status() == Languages.Status.INCOMPLETE) txtLangName.hardlight(CharSprite.NEGATIVE);
            add(txtLangName);

            txtLangInfo = PixelScene.renderTextBlock(6);
            if (currLang == Languages.ENGLISH) txtLangInfo.text("This is the source language, written by the developer.");
            else if (currLang.status() == Languages.Status.REVIEWED) txtLangInfo.text(Messages.get(this, "completed"));
            else if (currLang.status() == Languages.Status.UNREVIEWED) txtLangInfo.text(Messages.get(this, "unreviewed"));
            else if (currLang.status() == Languages.Status.INCOMPLETE) txtLangInfo.text(Messages.get(this, "unfinished"));
            if (currLang.status() == Languages.Status.UNREVIEWED) txtLangInfo.setHightlighting(true, CharSprite.WARNING);
            else if (currLang.status() == Languages.Status.INCOMPLETE) txtLangInfo.setHightlighting(true, CharSprite.NEGATIVE);
            add(txtLangInfo);

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            lanBtns = new RedButton[langs.size()];
            for (int i = 0; i < langs.size(); i++){
                final int langIndex = i;
                RedButton btn = new RedButton(Messages.titleCase(langs.get(i).nativeName()), 8){
                    @Override
                    protected void onClick() {
                        super.onClick();
                        Messages.setup(langs.get(langIndex));
                        ArknightsPixelDungeon.seamlessResetScene(new Game.SceneChangeCallback() {
                            @Override
                            public void beforeCreate() {
                                PDSettings.language(langs.get(langIndex));
                                GameLog.wipe();
                                Game.platform.resetGenerators();
                            }
                            @Override
                            public void afterCreate() {
                                //do nothing
                            }
                        });
                    }
                };
                if (currLang == langs.get(i)){
                    btn.textColor(TITLE_COLOR);
                } else {
                    switch (langs.get(i).status()) {
                        case INCOMPLETE:
                            btn.textColor(0x888888);
                            break;
                        case UNREVIEWED:
                            btn.textColor(0xBBBBBB);
                            break;
                    }
                }
                lanBtns[i] = btn;
                add(btn);
            }

            sep3 = new ColorBlock(1, 1, 0xFF000000);
            add(sep3);

            txtTranifex = PixelScene.renderTextBlock(6);
            txtTranifex.text(Messages.get(this, "transifex"));
            add(txtTranifex);

            if (currLang != Languages.ENGLISH) {
                String credText = Messages.titleCase(Messages.get(this, "credits"));
                btnCredits = new RedButton(credText, credText.length() > 9 ? 6 : 9) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        String creds = "";
                        String creds2 = "";
                        String[] reviewers = currLang.reviewers();
                        String[] translators = currLang.translators();

                        ArrayList<String> total = new ArrayList<>();
                        total.addAll(Arrays.asList(reviewers));
                        total.addAll(Arrays.asList(reviewers));
                        total.addAll(Arrays.asList(translators));
                        int translatorIdx = reviewers.length;

                        //we have 2 columns in wide mode
                        boolean wide = (2 * reviewers.length + translators.length) > (PixelScene.landscape() ? 15 : 30);

                        int i;
                        if (reviewers.length > 0) {
                            creds += Messages.titleCase(Messages.get(LangsTab.this, "reviewers"));
                            creds2 += "";
                            boolean col2 = false;
                            for (i = 0; i < total.size(); i++) {
                                if (i == translatorIdx){
                                    creds += "\n\n" + Messages.titleCase(Messages.get(LangsTab.this, "translators"));
                                    creds2 += "\n\n";
                                    if (col2) creds2 += "\n";
                                    col2 = false;
                                }
                                if (wide && col2) {
                                    creds2 += "\n-" + total.get(i);
                                } else {
                                    creds += "\n-" + total.get(i);
                                }
                                col2 = !col2 && wide;
                            }
                        }

                        Window credits = new Window(0, 0, Chrome.get(Chrome.Type.TOAST));

                        int w = wide ? 125 : 60;

                        RenderedTextBlock title = PixelScene.renderTextBlock(6);
                        title.text(Messages.titleCase(Messages.get(LangsTab.this, "credits")), w);
                        title.hardlight(TITLE_COLOR);
                        title.setPos((w - title.width()) / 2, 0);
                        credits.add(title);

                        RenderedTextBlock text = PixelScene.renderTextBlock(5);
                        text.setHightlighting(false);
                        text.text(creds, 65);
                        text.setPos(0, title.bottom() + 2);
                        credits.add(text);

                        if (wide) {
                            RenderedTextBlock rightColumn = PixelScene.renderTextBlock(5);
                            rightColumn.setHightlighting(false);
                            rightColumn.text(creds2, 65);
                            rightColumn.setPos(65, title.bottom() + 6);
                            credits.add(rightColumn);
                        }

                        credits.resize(w, (int) text.bottom() + 2);
                        ArknightsPixelDungeon.scene().addToFront(credits);
                    }
                };
                add(btnCredits);
            }

        }

        @Override
        protected void layout() {
            title.setPos((width - title.width())/2, y + GAP);

            sep1.size(width, 1);
            sep1.y = title.bottom() + 2*GAP;

            txtLangName.setPos( (width - txtLangName.width())/2f, sep1.y + 1 + GAP );
            PixelScene.align(txtLangName);

            txtLangInfo.setPos(0, txtLangName.bottom() + 2*GAP);
            txtLangInfo.maxWidth((int)width);

            y = txtLangInfo.bottom() + GAP;
            int x = 0;

            sep2.size(width, 1);
            sep2.y = y;
            y += 2;

            int cols = PixelScene.landscape() ? COLS_L : COLS_P;
            int btnWidth = (int)Math.floor((width - (cols-1)) / cols);
            for (RedButton btn : lanBtns){
                btn.setRect(x, y, btnWidth, BTN_HEIGHT);
                btn.setPos(x, y);
                x += btnWidth+1;
                if (x + btnWidth > width){
                    x = 0;
                    y += BTN_HEIGHT+1;
                }
            }
            if (x > 0){
                y += BTN_HEIGHT+1;
            }

            sep3.size(width, 1);
            sep3.y = y;
            y += 2;

            if (btnCredits != null){
                btnCredits.setSize(btnCredits.reqWidth() + 2, 16);
                btnCredits.setPos(width - btnCredits.width(), y);

                txtTranifex.setPos(0, y);
                txtTranifex.maxWidth((int)btnCredits.left());

                height = Math.max(btnCredits.bottom(), txtTranifex.bottom());
            } else {
                txtTranifex.setPos(0, y);
                txtTranifex.maxWidth((int)width);

                height = txtTranifex.bottom();
            }

        }
    }
}
