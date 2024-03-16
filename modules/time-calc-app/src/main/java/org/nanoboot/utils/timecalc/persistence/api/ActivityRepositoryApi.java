package org.nanoboot.utils.timecalc.persistence.api;

import org.nanoboot.utils.timecalc.entity.Activity;

import java.util.List;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface ActivityRepositoryApi {

    void create(Activity activity);

    List<Activity> list(int year, int month, int day);

    List<Activity> list(String ticket);

    void update(Activity activity);

    Activity read(String id);
    
    void delete(String id);
    
    public List<String> getYears();

    public void putToClipboard(Activity activity);

    public Activity getFromClipboard();

    public int getLargestSortkey(int year, int month, int day);

}
