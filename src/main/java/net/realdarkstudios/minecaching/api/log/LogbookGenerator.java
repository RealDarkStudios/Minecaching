package net.realdarkstudios.minecaching.api.log;

import net.realdarkstudios.minecaching.api.minecache.Minecache;
import net.realdarkstudios.minecaching.api.MinecachingAPI;
import net.realdarkstudios.minecaching.api.util.MCUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LogbookGenerator {
    private final Minecache minecache;
    private String currentPageText = "";
    private ArrayList<String> pages = new ArrayList<>();
    private int numPages = 0;

    ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
    BookMeta bookMeta = (BookMeta) book.getItemMeta();

    public LogbookGenerator(Minecache minecache) {
        this.minecache = minecache;
        if (MinecachingAPI.get().hasLogbook(minecache.id()) && !MinecachingAPI.get().getLogbook(minecache.id()).getLogs().isEmpty()) {
            for (Log log : MinecachingAPI.get().getLogbook(minecache.id()).getLogsSorted(Comparator.comparing(Log::time))) addEntry(log);
        }
    }

    private void addEntry(Log log) {
        String top = (log.author().equals(minecache.owner()) ? ChatColor.GOLD : ChatColor.RESET) + (log.author().equals(MCUtils.EMPTY_UUID) ? "[CONSOLE]" : Bukkit.getOfflinePlayer(log.author()).getName()) + ChatColor.RESET + (log.isFTF() ? " | FTF" : "");
        String middle = log.type().toLogFormat() + " " + log.time().format(DateTimeFormatter.ofPattern("M/d/yy"));
        String bottom = log.log();

        String logStr = top + "\n" + middle + "\n" + bottom + "\n\n";

        int totalLength = logStr.length();

        currentPageText += logStr;

        nextPage();
    }

    private void nextPage() {
        pages.add(currentPageText);
        numPages++;
        currentPageText = "";
    }

    public LogbookGenerator update() {
        return new LogbookGenerator(minecache);
    }

    public ItemStack getLogbook(int bookNumber) {
        // For book 1: Pages 100 * (1 - 1) to (100 * 1) - 1 = 0 to 99
        // For book 2: Pages 100 * (2 - 1) to (100 * 2) - 1 = 100 to 199
        // and on.
        int lPage = Math.max(100 * (bookNumber - 1), 0);

        if (!pages.isEmpty()) bookMeta.setPages(pages.subList(lPage, Math.min(lPage + 100, numPages)));
        else bookMeta.setPages(List.of("No logs to show."));
        bookMeta.setTitle(minecache.id() + " Logbook");
        bookMeta.setAuthor("Minecaching Log");
        book.setItemMeta(bookMeta);

        return book;
    }
}
