package com.github.azygous13.versionmonitor;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created on 5/2/2016 AD.
 */
@RunWith(AndroidJUnit4.class)
public class VersionMonitorTest {

    private VersionMonitor versionMonitor;

    @Before
    public void setUp() {
        versionMonitor = new VersionMonitor(InstrumentationRegistry.getContext());
    }

    @Test
    public void testIsHasPatchVersion() {
        boolean isHasPatch = versionMonitor.isHasPatchVersion("1.2.3");
        assertThat(isHasPatch, is(true));
    }

    @Test
    public void testIsHasPatchVersion_withNoPatch() {
        boolean isHasPatch = versionMonitor.isHasPatchVersion("1.2");
        assertThat(isHasPatch, is(false));
    }

    @Test
    public void testIsHasPatchVersion_withOnlyMajor() {
        boolean isHasPatch = versionMonitor.isHasPatchVersion("1");
        assertThat(isHasPatch, is(false));
    }

    @Test
    public void testGetNoPatchVersion() {
        float version = versionMonitor.getNoPatchVersion("1.2");
        assertThat(version, is(1.2f));
    }

    @Test
    public void testGetNoPatchVersion_withOnlyMajor() {
        float version = versionMonitor.getNoPatchVersion("1");
        assertThat(version, is(1f));
    }

    @Test
    public void testGetRemovePatchVersion() {
        float noPatchVersion = versionMonitor.getRemovePatchVersion("1.2.3");
        assertThat(noPatchVersion, is(1.2f));
    }

    @Test
    public void testGetRemovePatchVersion_withNoPatch() {
        float noPatchVersion = versionMonitor.getRemovePatchVersion("1.2");
        assertThat(noPatchVersion, is(1.2f));
    }

    @Test
    public void testGetRemovePatchVersion_withOnlyMajor() {
        float noPatchVersion = versionMonitor.getRemovePatchVersion("1");
        assertThat(noPatchVersion, is(1f));
    }

    @Test
    public void testGetPatchVersion() {
        float patchVersion = versionMonitor.getPatchVersion("1.2.3");
        assertThat(patchVersion, is(3f));
    }

    @Test
    public void testGetPatchVersion_withNoPatch() {
        float patchVersion = versionMonitor.getPatchVersion("1.2");
        assertThat(patchVersion, is(0f));
    }

    @Test
    public void testGetPatchVersion_onlyMajor() {
        float patchVersion = versionMonitor.getPatchVersion("1");
        assertThat(patchVersion, is(0f));
    }
    
    @Test
    public void testGetCurrentVersion() {
        String version = versionMonitor.getCurrentVersion("com.github.azygous13.versionmonitor");
        assertThat(version, is("1.0"));
    }

    @Test
    public void testIsNewVersion_bothNoPatch() {
        boolean isNewVersion = versionMonitor.isNewVersion("1.1", "1.2");
        assertThat(isNewVersion, is(true));

        isNewVersion = versionMonitor.isNewVersion("1.2", "1.1");
        assertThat(isNewVersion, is(false));
    }

    @Test
    public void testIsNewVersion_bothHasPatch() {
        boolean isNewVersion = versionMonitor.isNewVersion("1.2.2", "1.2.3");
        assertThat(isNewVersion, is(true));

        isNewVersion = versionMonitor.isNewVersion("1.2.3", "1.2.2");
        assertThat(isNewVersion, is(false));
    }

    @Test
    public void testIsNewVersion_bothNewVersionHasPatch() {
        boolean isNewVersion = versionMonitor.isNewVersion("1.1", "1.1.1");
        assertThat(isNewVersion, is(true));

        isNewVersion = versionMonitor.isNewVersion("1.2", "1.1.1");
        assertThat(isNewVersion, is(false));
    }

    @Test
    public void testIsNewVersion_bothCurrentVersionHasPatch() {
        boolean isNewVersion = versionMonitor.isNewVersion("1.1.1", "1.1");
        assertThat(isNewVersion, is(false));

        isNewVersion = versionMonitor.isNewVersion("1.1.1", "1.2");
        assertThat(isNewVersion, is(true));
    }

    @Test
    public void testIsNewVersion_bothEqual() {
        boolean isNewVersion = versionMonitor.isNewVersion("1.1", "1.1.0");
        assertThat(isNewVersion, is(false));

        isNewVersion = versionMonitor.isNewVersion("1.1.0", "1.1");
        assertThat(isNewVersion, is(false));

        isNewVersion = versionMonitor.isNewVersion("1.1", "1.1");
        assertThat(isNewVersion, is(false));
    }


    @Test
    public void testIsNewVersion() {
        boolean isNewVersion = versionMonitor.isNewVersion("1.0.0", "1.0.1");
        assertThat(isNewVersion, is(true));

        isNewVersion = versionMonitor.isNewVersion("1.0.1", "1.0");
        assertThat(isNewVersion, is(false));

        isNewVersion = versionMonitor.isNewVersion("1", "1.0.0");
        assertThat(isNewVersion, is(false));
    }
}