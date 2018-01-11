package com.feiwww.vo.userManagement;

/**
 * guava cache的key
 */
public class AdminCacheKey {
    private String username;
    private String password;
    private int start;
    private int limit;

    public AdminCacheKey() {

    }

    public AdminCacheKey(String username, String password, int start, int limit) {
        this.username = username;
        this.password = password;
        this.start = start;
        this.limit = limit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    //TODO(这个在什么时候使用????)
    @Override
    public boolean equals(Object o) {
        // 自动生成
        // if (this == o) return true;
        // if (o == null || getClass() != o.getClass()) return false;
        //
        // AdminCacheKey that = (AdminCacheKey) o;
        //
        // if (start != that.start) return false;
        // if (limit != that.limit) return false;
        // if (!username.equals(that.username)) return false;
        // return password.equals(that.password);

        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        AdminCacheKey other = (AdminCacheKey) o;
        if (limit != other.limit)
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (start != other.start)
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

    //TODO(这个在什么时候使用????)
    @Override
    public int hashCode() {
        //自动生成
        // int result = username.hashCode();
        // result = 31 * result + password.hashCode();
        // result = 31 * result + start;
        // result = 31 * result + limit;
        // return result;

        final int prime = 31;
        int result = 1;
        result = prime * result + limit;
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result + start;
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "AdminCacheKey{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", start=" + start +
                ", limit=" + limit +
                '}';
    }

}
