package cn.keking.project.binlogdistributor.param.model;

import cn.keking.project.binlogdistributor.param.enums.DatabaseEvent;
import cn.keking.project.binlogdistributor.param.enums.LockLevel;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author zhenhui
 * @date Created in 2018/17/01/2018/4:02 PM
 * @modified by
 */
public class ClientInfo implements Serializable {
    /**
     * 客户端编号
     */
    private String clientId;

    /**
     * 关注的数据库名
     */
    private String databaseName;

    /**
     * 关注的表名
     */
    private String tableName;

    /**
     * 关注的表的事件
     */
    private DatabaseEvent databaseEvent;

    /**
     * 数据锁定级别
     */
    private LockLevel lockLevel;

    /**
     * 锁级别为列的时候，使用指定列名
     */
    private String columnName;

    /**
     * 拼接key，避免频繁拼接
     */
    private String key;

    public ClientInfo() {
    }

    public ClientInfo(String clientId, String databaseName, String tableName, DatabaseEvent databaseEvent, LockLevel lockLevel, String columnName) {
        this.clientId = clientId;
        this.databaseName = databaseName;
        this.tableName = tableName;
        this.databaseEvent = databaseEvent;
        this.lockLevel = lockLevel;
        this.columnName = columnName;
        switch (lockLevel) {
            case TABLE:
                key = clientId + "-" + lockLevel + "-" + databaseName + "-" + tableName;
                break;
            case COLUMN:
                key = clientId + "-" + lockLevel + "-" + databaseName + "-" + tableName + "-";
                break;
            case NONE:
            default:
                key = clientId + "-" + lockLevel + "-" + databaseName;
        }
    }

    public LockLevel getLockLevel() {
        return lockLevel;
    }

    public void setLockLevel(LockLevel lockLevel) {
        this.lockLevel = lockLevel;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DatabaseEvent getDatabaseEvent() {
        return databaseEvent;
    }

    public void setDatabaseEvent(DatabaseEvent databaseEvent) {
        this.databaseEvent = databaseEvent;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientInfo)) return false;
        ClientInfo that = (ClientInfo) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(databaseName, that.databaseName) &&
                Objects.equals(tableName, that.tableName) &&
                databaseEvent == that.databaseEvent &&
                lockLevel == that.lockLevel &&
                Objects.equals(columnName, that.columnName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(clientId, databaseName, tableName, databaseEvent, lockLevel, columnName);
    }

    @Override
    public String toString() {
        return "{" +
                "\"clientId\":\"" + clientId + '\"' +
                ", \"databaseName\":\"" + databaseName + '\"' +
                ", \"tableName\":\"" + tableName + '\"' +
                ", \"databaseEvent\":\"" + databaseEvent + '\"' +
                ", \"lockLevel\":\"" + lockLevel + '\"' +
                ", \"columnName\":\"" + columnName + '\"' +
                ", \"key\":\"" + key +
                "\"}";
    }
}
