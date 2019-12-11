package com.zhuzhou.framework.utils.stomp;

import java.net.InetAddress;


public class IPv4Util {
    private static final int INADDRSZ = 4;

    public static byte[] ipToBytesByInternet(String ipAddr) {
        try {
            return InetAddress.getByName(ipAddr).getAddress();
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }

    public static byte[] ipToBytesByReg(String ipAddr) {
        byte[] ret = new byte[4];
        try {
            String[] ipArr = ipAddr.split("\\.");
            ret[0] = ((byte) (Integer.parseInt(ipArr[0]) & 0xFF));
            ret[1] = ((byte) (Integer.parseInt(ipArr[1]) & 0xFF));
            ret[2] = ((byte) (Integer.parseInt(ipArr[2]) & 0xFF));
            ret[3] = ((byte) (Integer.parseInt(ipArr[3]) & 0xFF));
            return ret;
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }


    public static String bytesToIp(byte[] bytes) {
        return (bytes[0] & 0xFF) + "." + (bytes[1] & 0xFF) + "." + (bytes[2] & 0xFF) + "." + (bytes[3] & 0xFF);
    }


    public static int bytesToInt(byte[] bytes) {
        int addr = bytes[3] & 0xFF;
        addr |= bytes[2] << 8 & 0xFF00;
        addr |= bytes[1] << 16 & 0xFF0000;
        addr |= bytes[0] << 24 & 0xFF000000;
        return addr;
    }


    public static int ipToInt(String ipAddr) {
        try {
            return bytesToInt(ipToBytesByInternet(ipAddr));
        } catch (Exception e) {
            throw new IllegalArgumentException(ipAddr + " is invalid IP");
        }
    }


    public static byte[] intToBytes(int ipInt) {
        byte[] ipAddr = new byte[4];
        ipAddr[0] = ((byte) (ipInt >>> 24 & 0xFF));
        ipAddr[1] = ((byte) (ipInt >>> 16 & 0xFF));
        ipAddr[2] = ((byte) (ipInt >>> 8 & 0xFF));
        ipAddr[3] = ((byte) (ipInt & 0xFF));
        return ipAddr;
    }


    public static String intToIp(int ipInt) {
        return (ipInt >> 24 & 0xFF) + "." + (ipInt >> 16 & 0xFF) + "." + (ipInt >> 8 & 0xFF) + "." + (ipInt & 0xFF);
    }


    public static int[] getIPIntScope(String ipAndMask) {
        String[] ipArr = ipAndMask.split("/");
        if (ipArr.length != 2) {
            throw new IllegalArgumentException("invalid ipAndMask with: " + ipAndMask);
        }

        int netMask = Integer.valueOf(ipArr[1].trim()).intValue();
        if ((netMask < 0) || (netMask > 31)) {
            throw new IllegalArgumentException("invalid ipAndMask with: " + ipAndMask);
        }

        int ipInt = ipToInt(ipArr[0]);
        int netIP = ipInt & -1 << 32 - netMask;
        int hostScope = -1 >>> netMask;
        return new int[]{netIP, netIP + hostScope};
    }


    public static String[] getIPAddrScope(String ipAndMask) {
        int[] ipIntArr = getIPIntScope(ipAndMask);
        return new String[]{intToIp(ipIntArr[0]), intToIp(ipIntArr[0])};
    }


    public static int[] getIPIntScope(String ipAddr, String mask) {
        int netMaskInt = 0;
        int ipcount = 0;
        try {
            int ipInt = ipToInt(ipAddr);
            if ((null == mask) || ("".equals(mask))) {
                return new int[]{ipInt, ipInt};
            }
            netMaskInt = ipToInt(mask);
            ipcount = ipToInt("255.255.255.255") - netMaskInt;
            int netIP = ipInt & netMaskInt;
            int hostScope = netIP + ipcount;
            return new int[]{netIP, hostScope};
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid ip scope express  ip:" + ipAddr + "  mask:" + mask);
        }
    }

    public static String[] getIPStrScope(String ipAddr, String mask) {
        int[] ipIntArr = getIPIntScope(ipAddr, mask);
        return new String[]{intToIp(ipIntArr[0]), intToIp(ipIntArr[0])};
    }
}