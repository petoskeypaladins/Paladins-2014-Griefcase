/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Jackson
 */
public class ReloadKickerCommand extends CommandBase {
    private boolean isKickerLimitSwitchTriggered;
    private double timeout;
    private Timer timer;
    
    public ReloadKickerCommand() {
        requires(shooter);
    }
    
    protected void initialize() {
        
        resetIsKickerLimitSwtichTriggered();
        
        shooter.reload(shooter.DRAW_KICKER_BACK);
        
        if (RobotMap.DOES_USE_AUTO_RELOAD == true) {
            Timer.delay(RobotMap.KICKER_RELOAD_DELAY_SECONDS);
        }
//        timeout = System.currentTimeMillis() + 1500; // 1.5 seconds 1000 mill = 1 second
        timeout = 2.0;
        timer = new Timer();
        timer.start();
    }
    
    protected void execute() {
        if (isKickerLimitSwitchTriggered == false && ((shooter.isKickerLimitSwitchTriggered()) || timer.get() >= timeout + .5)) {
            isKickerLimitSwitchTriggered = true;
            SmartDashboard.putNumber("Retract Time:", timer.get());
            timer.reset();
            timer.start();
        }
        
        if(isKickerLimitSwitchTriggered == false) {
            shooter.reload(shooter.DRAW_KICKER_BACK);
        } else {
            shooter.reload(shooter.ROTATE_KICKER_FORWARD);
        }
                
    }
    
    protected boolean isFinished() {
        return ((shooter.isKickerMotorReturnSwitchTriggered() || timer.get() >= timeout) && isKickerLimitSwitchTriggered);
    }
    
    protected void end() {
        shooter.stopMotor();
        resetIsKickerLimitSwtichTriggered();
    }
    
    protected void interrupted() {
        end();
    }
    
    private void resetIsKickerLimitSwtichTriggered() {
        isKickerLimitSwitchTriggered = false;
    }
}