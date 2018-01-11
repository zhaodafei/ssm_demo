package com.feiwww.dao.log;

import com.feiwww.mapper.log.LogMapper;
import com.feiwww.model.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * 日志DAO
 */
@Repository
public class LogDao {
    @Autowired
    private LogMapper logMapper;

    /******************注解********************/
    public boolean insertLog(Log log) {
        return logMapper.insertLog(log) == 1 ? true : false;
    }
}
