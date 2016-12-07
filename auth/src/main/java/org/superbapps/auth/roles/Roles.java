package org.superbapps.auth.roles;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author д06ри
 */
public enum Roles {

    // GENERAL MF
    //
    R_ADMIN("R_ADMIN"),
    R_POWERUSER("R_POWERUSER"),
    R_USER("R_USER"),
    //
    // PERMISSIONS
    //
    P_ADMIN("p:admin:*"),
    P_POWERUSER("p:poweruser:*"),
    P_USER("p:user:*");
    //

    //<editor-fold defaultstate="collapsed" desc="Support..">
    //<editor-fold defaultstate="collapsed" desc="System Support">
    private final String value;

    private Roles(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static List<Roles> getRoles() {
        return filterByName("R_");
    }

    public static List<Roles> getPermissions() {
        return filterByName("P_");
    }

    private static List<Roles> filterByName(String what) {
        return Arrays.asList(Roles.values())
                .stream()
                .filter((Roles r) -> r.name().startsWith(what))
                .collect(Collectors.toList());

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Test">
    public static void main(String[] args) {
        System.err.println("test 1");
        System.err.println(Roles.getRoles());
    }
    //</editor-fold>
    //</editor-fold>

}
