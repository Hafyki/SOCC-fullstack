package com.project.socc.entities;


import com.project.socc.enums.ProfileRole;

import java.util.UUID;

public class Profile {

    private UUID id;
    private ProfileRole role;
    private String description;
    private Boolean protectedProfile;
}
