package com.fulaan;

/**
 * Created by moslpc on 2016/12/23.
 */
public class User {
    private String id;
    private String user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof User && ((User) o).getId().equals(this.getId());
    }

}
