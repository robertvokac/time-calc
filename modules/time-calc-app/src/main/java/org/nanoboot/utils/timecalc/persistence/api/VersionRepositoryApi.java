package org.nanoboot.utils.timecalc.persistence.api;

/**
 * @author Robert Vokac
 * @since 23.02.2024
 */
public interface VersionRepositoryApi {

    int read();

    void update(int newVersion);

}
