package me.boxadactle.coordinatesdisplay.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.JsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ModVersion {

    private static ModVersion version;

    boolean isMostRecent;
    String mostRecentVer;
    static String thisVer = "2.1.2";

    public ModVersion(boolean isMostRecent, String mostRecentVer) {
        this.isMostRecent = isMostRecent;
        this.mostRecentVer = mostRecentVer;
    }

    public boolean isMostRecent() {
        return isMostRecent;
    }

    public String thisVersion() {
        return thisVer;
    }

    public String getMostRecentVer() {
        return mostRecentVer != null ? mostRecentVer : "unknown version";
    }

    public Text getUpdateText() {
        Text link = Text.literal("here").styled((style -> style
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Click here to open in browser")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, CoordinatesDisplay.UPDATE_MOD_URL))
                .withColor(ModUtils.getColorDecimal("aqua"))
        ));

        return mostRecentVer != null ? Text.literal(String.format("There is a new version of CoordinatesDisplay available for Minecraft %s! You are currently on %s (newest version is %s). ", CoordinatesDisplay.MINECRAFT_VERSION, this.thisVersion(), this.getMostRecentVer()) +
                "Click ").append(link).append(" to download the newest version.") :
                Text.literal(String.format("There is a new version of CoordinatesDisplay available for Minecraft %s! Click ", CoordinatesDisplay.MINECRAFT_VERSION)).append(link).append(" to download the newest version.");
    }

    public String toString() {
        return CoordinatesDisplay.MOD_ID + " v" + this.thisVersion();
    }

    public static ModVersion getVersion() {
        if (version != null)
            return version;

        CoordinatesDisplay.LOGGER.info("Checking mod version...");

        CoordinatesDisplay.MINECRAFT_VERSION = MinecraftClient.getInstance().getGame().getVersion().getId();
        CoordinatesDisplay.LOGGER.info(CoordinatesDisplay.MINECRAFT_VERSION);
        try {
            URL url = new URL(CoordinatesDisplay.UPDATE_URL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");

            c.setRequestProperty("Content-Type", "application/json");

            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);

            if (c.getResponseCode() == 200) {
                BufferedReader b = new BufferedReader(new InputStreamReader(c.getInputStream()));
                String i;
                StringBuilder content = new StringBuilder();

                while ((i = b.readLine()) != null) {
                    content.append(i + "\n");
                }

                b.close();

                JsonObject response = JsonHelper.deserialize(content.toString().trim());

                JsonElement v = response.get(CoordinatesDisplay.MINECRAFT_VERSION);
                JsonElement u = response.get("update-url");

                if (v != null) {

                    CoordinatesDisplay.UPDATE_MOD_URL = u != null ? u.toString().replaceAll("\"", "") : null;

                    ModVersion ver = new ModVersion(v.toString().replaceAll("\"", "").equals(thisVer), v.toString().replaceAll("\"", ""));
                    version = ver;
                    return ver;

                } else {
                    CoordinatesDisplay.LOGGER.error("Minecraft version not specified on update URL!");
                    ModVersion ver = new ModVersion(false, null);
                    version = ver;
                    return ver;
                }

            } else {
                CoordinatesDisplay.LOGGER.error("Request to update URL failed");
                ModVersion ver = new ModVersion(false, null);
                version = ver;
                return ver;
            }

        } catch (IOException e) {
            CoordinatesDisplay.LOGGER.error("Unable to send request to server!");
            CoordinatesDisplay.LOGGER.printStackTrace(e);
            return new ModVersion(false, null);
        }
    }
}