package dev.simplyamazing.jonkcore.Utilities;

import dev.simplyamazing.jonkcore.JonkCORE;
import dev.simplyamazing.jonkcore.Objects.Interfaces.IJonkPlugin;

import java.io.*;
import java.net.URL;

public class UpdateChecker implements Runnable {
    private final IJonkPlugin plugin;
    private boolean upToDate;
    private String latestVersion;

    public UpdateChecker(final IJonkPlugin plugin) {
        this.plugin = plugin;
        this.latestVersion = plugin.getVersion();
        this.upToDate = true;
    }

    /**
     * Run the update checker to check if the plugin is up to date.
     */
    @Override
    public void run() {
        System.out.println(plugin.getPrefix() + "Checking for updates...");
        InputStream in = null;
        try {
            in = new URL(plugin.getRepository().getPath() + "main/resources/plugin.yml").openStream();
        } catch(IOException e) {
            System.out.println(plugin.getPrefix() + "Failed to check for updates!");
            e.printStackTrace();
        }
        if(in == null) return;
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        try {
            String line = null;
            while(line == null || line.startsWith("version: ")) {
                line = br.readLine();
            }
            latestVersion = line.replaceFirst("version: ", "");
            upToDate = JonkCORE.getInstance().getDescription().getVersion().equals(latestVersion);
            if(upToDate) {
                System.out.println(plugin.getPrefix() + "Plugin is up to date!");
            } else {
                System.out.println(plugin.getPrefix() + "Plugin is outdated! The latest version is " + latestVersion + ".");
            }
        } catch(IOException e) {
            System.out.println(plugin.getPrefix() + "Failed to read latest version!");
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if the plugin is up to date.
     * @return whether the plugin is up to date.
     */
    public boolean isUpToDate() {
        return upToDate;
    }

    /**
     * Get the latest version of the plugin.
     * @return the latest version of the plugin.
     */
    public String getLatestVersion() {
        return latestVersion;
    }
}
