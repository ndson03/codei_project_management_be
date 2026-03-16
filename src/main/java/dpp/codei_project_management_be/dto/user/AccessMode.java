package dpp.codei_project_management_be.dto.user;

public enum AccessMode {
    ADMIN,  // Can create, update, delete parts and projects
    PIC,    // Part In Charge - can update own part, assign other PICs, create/update/delete projects
    USER    // Regular user - can view all projects
}