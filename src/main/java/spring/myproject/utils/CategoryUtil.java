package spring.myproject.utils;

import java.util.Arrays;
import java.util.List;

public class CategoryUtil {
    public static String[] names = {
//            "book", "society", "travel", "exercise", "music", "job", "festival",
//            "language", "game", "art", "dance", "service", "picture",
//            "development", "watching", "animal", "cook", "car"
            "category1", "category2", "category3", "category4", "category5",
            "category6", "category7", "category8", "category9", "category10",
            "category11", "category12", "category13", "category14", "category15",
            "category16", "category17", "category18", "category19", "category20"
    };;
    public static List<String> list = Arrays.stream(names).map(name -> name).toList();
    public static boolean existCategory(String name){
        return list.contains(name);

    }
}
