package com.unifier.arknightspixeldungeon.scripts.FormalPlot;

import com.unifier.arknightspixeldungeon.messages.Messages;
import com.unifier.arknightspixeldungeon.scripts.Plot;
import com.unifier.arknightspixeldungeon.scripts.Script;
import com.unifier.arknightspixeldungeon.windows.WndDialog;

public class SewerPlot extends Plot {

    public static final String PLOT_NAME = "sewer";

    private final static int maxprocess = 10;

    {
        process = 1 ;
    }

    protected String getPlotName() {
        return SEWER_NAME;
    }

    @Override
    public void reachProcess(WndDialog wndDialog) {
        diagulewindow = wndDialog;

        while(this.process < needed_process )
        {
            this.process();
        }
    }

    @Override
    public void process() {
        if(diagulewindow!=null) {
            switch (process) {
                default:
                case 1:
                    process_to_1();//Mostly process to 1 is made directly when creating,it might not be used,just in case
                    break;
                case 2:
                    process_to_2();
                    break;
                case 3:
                    process_to_3();
                    break;
                case 4:
                    process_to_4();
                    break;
                case 5:
                    process_to_5();
                    break;
                case 6:
                    process_to_6();
                    break;
                case 7:
                    process_to_7();
                    break;
                case 8:
                    process_to_8();
                    break;
                case 9:
                    process_to_9();
                    break;
                case 10:
                    process_to_10();
                    break;
            }
            diagulewindow.update();
            process ++;
        }
    }

    @Override
    public void initial(WndDialog wndDialog) {
            diagulewindow = wndDialog;
            process = 2;
            process_to_1();
    }

    @Override
    public boolean end() {
        return process > maxprocess;
    }

    @Override
    public void skip() {
        diagulewindow.cancel();
        WndDialog.settedPlot = null;
    }

    private void process_to_1()
    {
        diagulewindow.hideAll();
        diagulewindow.showBackground(Messages.get(this, "background1"));
    }

    private void process_to_2()
    {
        diagulewindow.showBackground(Messages.get(this, "background2"));
    }

    private void process_to_3()
    {
        diagulewindow.showBackground(Messages.get(this, "background3"));
    }

    private void process_to_4()
    {
        diagulewindow.setMainAvatar(Script.Portrait(Script.Character.CHEN));
        diagulewindow.setLeftName(Script.Name(Script.Character.CHEN));
        diagulewindow.changeText(Messages.get(this, "txt1"));
    }

    private void process_to_5()
    {
        diagulewindow.darkenMainAvatar();

        diagulewindow.setSecondAvatar(Script.Portrait(Script.Character.RED));
        diagulewindow.setRightName(Script.Name(Script.Character.RED));
        diagulewindow.changeText(Messages.get(this, "txt2"));
    }
    private void process_to_6()
    {
        diagulewindow.darkenSecondAvatar();

        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.EXUSIAI));
        diagulewindow.lightenThirdAvatar();
        diagulewindow.setRightName(Script.Name(Script.Character.EXUSIAI));

        diagulewindow.changeText(Messages.get(this, "txt3"));


    }
    private void process_to_7()
    {
        diagulewindow.darkenThirdAvatar();

        diagulewindow.lightenMainAvatar();
        diagulewindow.setLeftName(Script.Name(Script.Character.CHEN));

        diagulewindow.changeText(Messages.get(this, "txt4"));

        process = 7;
    }
    private void process_to_8()
    {
        diagulewindow.darkenMainAvatar();

        diagulewindow.lightenThirdAvatar();
        diagulewindow.setRightName(Script.Name(Script.Character.EXUSIAI));

        diagulewindow.changeText(Messages.get(this, "txt5"));


    }
    private void process_to_9()
    {
        diagulewindow.setThirdAvatar(Script.Portrait(Script.Character.AMIYA));
        diagulewindow.lightenThirdAvatar();

        diagulewindow.setRightName(Script.Name(Script.Character.AMIYA));
        //diagulewindow.thirdAvaratToFront();

        diagulewindow.changeText(Messages.get(this, "txt6"));


    }
    private void process_to_10()
    {
        diagulewindow.darkenThirdAvatar();
        diagulewindow.lightenMainAvatar();

        diagulewindow.setLeftName(Script.Name(Script.Character.CHEN));

        diagulewindow.changeText(Messages.get(this, "txt7"));

    }
}
