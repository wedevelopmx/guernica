package mx.wedevelop.guernica;

import org.junit.Test;

import java.util.Date;

import mx.wedevelop.guernica.sqlite.model.Shift;
import mx.wedevelop.guernica.utils.simulation.DayElapseSimulation;
import mx.wedevelop.guernica.utils.simulation.ShiftElapseSimulation;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void monthSimulation() {
        DayElapseSimulation sim = new DayElapseSimulation(new Date(), 1);
        while(sim.tomorrow()) {
            System.out.println(sim.today());
        }
    }

    @Test
    public void montShiftSimulation() {
        ShiftElapseSimulation sim = new ShiftElapseSimulation(new Date(), 1);
        System.out.println("---------- Shift -----------------");
        while(sim.tomorrow()) {
            System.out.println(sim.today());
            System.out.println(sim.afternoonShift());
        }
    }
}