package com;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Runtime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Main {
    public static void main(String[] args) {

        List<String> listOfIPsJunk = getIPsAsAList();
        List<String> listOfIPsClean = new ArrayList<String>();
        List<String> listOfMACs = new ArrayList<String>();


        //depracated parsing for arp -a. Now using arp -e
        //          returned list of ip will contain extra meta data(junk) which needs to be stripped to bare ip address
/*        for (String string : listOfIPsJunk) {
            listOfIPsClean.add(string.split(" ")[1].replaceAll("\\)|\\(", ""));
        }*/

        for (String string : listOfIPsJunk) {
            String firstString = string.split(" ")[0];
//            String macString = string.split(" /t")[2];

            if (!firstString.equals("_gateway") && !firstString.equals("Address")){
                listOfIPsClean.add(firstString);
               // listOfMACs.add(string.split(" ")[2]);
            }

        }

/*        for (String s: listOfMACs){
            System.out.println(s);
        }*/

        StringBuilder stringBuilder = new StringBuilder();
        for (String string : listOfIPsClean) {
            stringBuilder.append(string).append(" ");
        }

        System.out.println(scanIPForPorts(stringBuilder.toString()));
    }

    private static String scanIPForPorts(String ipAddress) {

        String s = null;

        try {

            // using the Runtime exec method:
            Process p = Runtime.getRuntime().exec("nmap " + ipAddress + "-p,80,515,443,3306,139");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));
            //standard output
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // standard error
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "check console for output";

    }

    private static List<String> getIPsAsAList() {
        String s = null;

        List<String> listOfIPs = new ArrayList<String>();

        try {

            Process p = Runtime.getRuntime().exec("arp -e");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            while ((s = stdInput.readLine()) != null) {
                listOfIPs.add(s);
            }

            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return listOfIPs;
    }


}
