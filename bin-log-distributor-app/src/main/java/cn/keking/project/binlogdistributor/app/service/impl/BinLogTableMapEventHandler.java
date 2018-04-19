package cn.keking.project.binlogdistributor.app.service.impl;

import cn.keking.project.binlogdistributor.app.model.ColumnsTableMapEventData;
import cn.keking.project.binlogdistributor.app.service.BinLogEventHandler;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.TableMapEventData;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理TableMapEvent，主要是映射表名和id
 *
 * @author zhenhui
 * @date Created in 2018/17/01/2018/5:28 PM
 * @modified by
 */
@Service
public class BinLogTableMapEventHandler extends BinLogEventHandler {

    private static final Logger log = LoggerFactory.getLogger(BinLogTableMapEventHandler.class);

    @Autowired
    DataSource dataSource;

    @Override
    public void handle(Event event) {
        TableMapEventData d = event.getData();
        log.debug("TableMapEventData:{}", d);
        ColumnsTableMapEventData tableMapEventData = TABLE_MAP_ID.get(d.getTableId());
        //如果表结构有变化，重新设置映射信息
        if (tableMapEventData == null || !ColumnsTableMapEventData.checkEqual(d, tableMapEventData)) {
            log.info("更新表映射：{}-{}", d.getDatabase(), d.getTable());
            ColumnsTableMapEventData data = new ColumnsTableMapEventData(d);
            String sql = "show columns from `" + d.getTable() + "` from `" + d.getDatabase() + "`";
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet resultSet = ps.executeQuery();) {
                while (resultSet.next()) {
                    data.addColumnName(resultSet.getString("Field"));
                }
            } catch (SQLException e) {
                log.error("获取表数据错误,sql语句为{}，异常如下:{}", sql, e);
            }
            //将表id和表映射
            TABLE_MAP_ID.put(d.getTableId(), data);
        }
    }
}
