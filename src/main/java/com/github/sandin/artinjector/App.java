package com.github.sandin.artinjector;

import org.apache.commons.cli.*;

import java.io.File;

/**
 * Application
 *
 * @author san
 */
public class App {

    private static final String USAGE_INJECT = "artinjector -i <injecto_so> -p <package_name>";
    private static final String USAGE_GETABI = "artinjector -p <package_name> -a";

    public static void main(String[] args) throws ArtInjectException {
        System.out.println("[Success] Android VM Injector v1.0");
        DefaultParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption(
                Option.builder("p")
                        .longOpt("package")
                        .argName("package")
                        .desc("package name")
                        .hasArg(true)
                        .required(true)
                        .build());

        options.addOption(
                Option.builder("i")
                        .longOpt("injectso")
                        .argName("injectso")
                        .desc("inject so")
                        .hasArg(true)
                        .required(false)
                        .build());

        options.addOption(
                Option.builder("s")
                        .longOpt("serial")
                        .argName("serial")
                        .desc("device serial")
                        .hasArg(true)
                        .required(false)
                        .build());

        options.addOption(
                Option.builder("a")
                        .longOpt("abi")
                        .argName("abi")
                        .desc("application abi")
                        .hasArg(false)
                        .required(false)
                        .build());

        options.addOption(
                Option.builder("l")
                        .longOpt("launch")
                        .argName("launch")
                        .desc("launch application")
                        .hasArg(false)
                        .required(false)
                        .build());

        options.addOption(
                Option.builder("ac")
                        .longOpt("activity")
                        .argName("activityName")
                        .desc("activity name")
                        .hasArg(true)
                        .required(false)
                        .build());

        options.addOption(
                Option.builder("adb")
                        .longOpt("adbPath")
                        .argName("adbPath")
                        .desc("inject so")
                        .hasArg(true)
                        .required(false)
                        .build());

        CommandLine cl;
        try {
            cl = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(USAGE_INJECT + "\n or \n" + USAGE_GETABI, options);
            System.exit(-1);
            return;
        }


        String adbPath = "adb";
        if (cl.hasOption("adb")) {
            adbPath = cl.getOptionValue("adbPath");
        }

        String packageName = cl.getOptionValue("package");
        String injectSo = cl.getOptionValue("injectso");
        String serial = cl.getOptionValue("serial");
        String activityName = cl.getOptionValue("ac");
        ArtInjector artInjector;

        if (!AdbUtils.checkAdbProcess() && !cl.hasOption("adb")) {
            System.out.println("[Success] Adb Path is : " + adbPath);
            artInjector = new ArtInjector(adbPath);
        } else {
            artInjector = new ArtInjector();
        }

        if (cl.hasOption("a")) {
            try {
                String appAbi = artInjector.getAppAbi(serial, packageName, 30 * 1000);
                System.out.println("[Success] Application abi is : " + appAbi);
            } catch (ArtInjectException e) {
                System.err.println("[Error] ErrorInfo: " + e.getMessage());
                System.exit(-1);
            }
        } else {
            if (cl.hasOption("l")) {
                artInjector.launchApplication(serial, packageName, activityName, 10 * 1000);
            }
            if (!cl.hasOption("i")) {
                System.exit(0);
            } else {
                File soFile = new File(injectSo);
                if (!soFile.exists()) {
                    System.out.println("[ErrorCode]: " + ErrorCodes.SOFILE_NOT_EXIST);
                    System.err.println(
                            "[Error] ErrorInfo: inject so file is not exists, " + soFile.getAbsolutePath());
                    return;
                }

                try {
                    artInjector.inject(serial, packageName, soFile, 30 * 1000);
                } catch (ArtInjectException e) {
                    System.err.println("[Error] ErrorInfo: " + e.getMessage());
                    System.exit(-1);
                }
                //artInjector.dispose();
                System.out.println("[Success] Inject: OK");
            }

        }
        System.exit(0);
    }
}
