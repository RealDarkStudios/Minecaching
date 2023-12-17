package net.realdarkstudios.minecaching;

import net.realdarkstudios.minecaching.data.Minecache;

import java.util.*;

public class Utils {
    public static final String EMPTY_UUID_STRING = "00000000-0000-0000-0000-000000000000";
    public static final UUID EMPTY_UUID = UUID.fromString(EMPTY_UUID_STRING);

    public static String generateID(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int n = alphabet.length();

        StringBuilder result = new StringBuilder("MC-");
        Random r = new Random();

        for (int i=0; i<length; i++)
            result.append(alphabet.charAt(r.nextInt(n)));

        return result.toString();
    }

    public static String generateCode(int length){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int n = alphabet.length();

        StringBuilder result = new StringBuilder();
        Random r = new Random();

        for (int i=0; i<length; i++)
            result.append(alphabet.charAt(r.nextInt(n)));

        return result.toString();
    }

    public static HashMap<String, Minecache> sortByMinecache(HashMap<String, Minecache> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Minecache> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, Comparator.comparing(m -> (m.getValue().hidden())));

        // put data from sorted list to hashmap
        HashMap<String, Minecache> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Minecache> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
