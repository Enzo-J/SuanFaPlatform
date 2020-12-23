package com.zkwg.modelmanager.core;

public interface Watch {

    void update(IMessage message);

    void rollback(IMessage message);

}
