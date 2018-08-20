/*
 *
 * @author Arnold & Ian
 */

package rainbowreef;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyControl extends KeyAdapter {

    private GameEvents gameE;
    
    public KeyControl(GameEvents ge){ this.gameE = ge; }
    
    @Override
    public void keyPressed(KeyEvent e){ gameE.setValue(e); }
    
    @Override
    public void keyReleased(KeyEvent e) { gameE.setValue(e); }
}