package com.video.data.command.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReturnMsg {

    private Logger logger = LoggerFactory.getLogger(this.toString());

    public void success(String msg) {
        logger.info(msg);
        System.out.println(msg);
    }

    public void error(String msg) {
        logger.error(msg);
        System.err.println(msg);
    }
}
