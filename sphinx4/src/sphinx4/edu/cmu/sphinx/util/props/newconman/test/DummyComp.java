package edu.cmu.sphinx.util.props.newconman.test;

import edu.cmu.sphinx.util.props.PropertyException;
import edu.cmu.sphinx.util.props.Registry;
import edu.cmu.sphinx.util.props.newconman.*;
import junit.framework.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


public class DummyComp implements SimpleConfigurable {

    /** doc of beamWidth. */
    @S4Integer(defaultValue = 4)
    public static final String PROP_BEAM_WIDTH = "beamWidth";

    /** doc of frontend. */
    @S4Component(type = DummyFrontEnd.class)
    public static final String PROP_FRONTEND = "frontend";

    @S4Double(defaultValue = 1.3, range = {-1, 15})
    public static final String PROP_ALPHA = "alpha";

    /** doc of the string. */
    @S4String(defaultValue = "sphinx4", range = {"sphinx4", "htk"})
    public static final String PROP_BEST_ASR = "bestAsrSystem";


    private int beamWidth;
    private DummyFrontEnd frontEnd;
    private String bestAsr;
    private double alpha;

    private Logger logger;


    public int getBeamWidth() {
        return beamWidth;
    }


    public DummyFrontEnd getFrontEnd() {
        return frontEnd;
    }


    public String getBestASR() {
        return bestAsr;
    }


    public double getAlpha() {
        return alpha;
    }


    public Logger getLogger() {
        return logger;
    }


    public void register(String name, Registry registry) throws PropertyException {
        // :-) we do not need this one anymore
    }


    public void newProperties(PropSheet ps) throws PropertyException {
        frontEnd = (DummyFrontEnd) ps.getComponent(PROP_FRONTEND);
        beamWidth = ps.getInt(PROP_BEAM_WIDTH);
        bestAsr = ps.getString(PROP_BEST_ASR);
        alpha = ps.getDouble(PROP_ALPHA);

        logger = ps.getLogger();
    }


    public String getName() {
        return "lalala";
    }


    /** Use the all defaults defined by the annotations to instantiate a Configurable. */
    @Test
    public void testGetDefaultInstance() throws PropertyException {
        Map<String, Object> defaultProps = new HashMap<String, Object>();
        defaultProps.put(DummyComp.PROP_FRONTEND, new DummyFrontEnd());

        DummyComp dc = (DummyComp) ConMan.getDefaultInstance(DummyComp.class, defaultProps);

        Assert.assertEquals(dc.getBeamWidth(), 4);
        Assert.assertEquals(1.3, dc.getAlpha(), 1E-10);
        Assert.assertTrue(dc.getFrontEnd() != null);
        Assert.assertTrue(dc.getBestASR().equals("sphinx4"));
        Assert.assertTrue(dc.getLogger() != null);
    }


    @Test
    public void testUseXmlConfig() throws IOException, PropertyException, InstantiationException {
        // probably you need to adpat this path. testconfig is located in {trunkroot}/sphinx4/tests/other
        File configFile = new File("../../sphinx4/tests/other/testconfig.xml");
        if (!configFile.exists())
            Assert.fail("can not find configuration file to be used for test");

        ConMan cm = new ConMan(configFile.toURI().toURL());

        DummyComp dc = (DummyComp) cm.lookup("duco");

        Assert.assertEquals(dc.getBeamWidth(), 123);
        Assert.assertEquals(9.99, dc.getAlpha(), 1E-10);

        Assert.assertTrue(dc.getFrontEnd() != null);
        Assert.assertTrue(dc.getFrontEnd().isUseMfccs());
        Assert.assertTrue(dc.getFrontEnd().getDataProcs().size() == 2);

        Assert.assertTrue(dc.getBestASR().equals("sphinx4"));
        Assert.assertTrue(dc.getLogger() != null);
    }
}

