package com.dh.msbills.security;

abstract class PrefixUtil {

    static String addRolePrefix(String role) {
        return String.format("ROLE_%s", role);
    }
    static String addAudPrefix(String role) {
        return String.format("AUD_%s", role);
    }
    static String addScopePrefix(String role) {
        return String.format("SCOPE_%s", role);
    }
    static String addGroupPrefix(String role) {
        return String.format("GROUP_%s", role);
    }
}
