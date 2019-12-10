package com.video.data.command;

import com.video.data.command.model.ReturnMsg;

public interface BaseCommandI {

    ReturnMsg run(String... args);


}
