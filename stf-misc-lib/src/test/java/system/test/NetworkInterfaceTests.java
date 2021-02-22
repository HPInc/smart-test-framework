package system.test;

import com.github.jeansantos38.stf.framework.network.NetworkHelper;
import org.testng.Assert;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import system.base.MainTestBase;


public class NetworkInterfaceTests extends MainTestBase {

    @Test
    @Parameters({"networkInterface"})
    public void NetworkTests(@Optional("eth1") String networkInterface) throws Exception {
        String ipV4example = "192.168.0.100";
        String ipV6example = "2804:14d:4c88:88c9:50a9:1074:3cb0:d7e6";

        Assert.assertTrue(NetworkHelper.isValidIpv4(ipV4example));
        Assert.assertTrue(NetworkHelper.isValidIpv6(ipV6example));
        Assert.assertFalse(NetworkHelper.listAllNetworkInterfacesAvailable().isEmpty());

        testLog.logIt(NetworkHelper.retrieveLocalIPv4Address(networkInterface));
        testLog.logIt(NetworkHelper.retrieveLocalIPv6Address(networkInterface));
    }
}