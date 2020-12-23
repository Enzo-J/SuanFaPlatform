package com.zkwg.modelmanager.core.plugin;

import com.zkwg.modelmanager.core.plugin.param.IParameter;

/**
 * Created by lyc on 2017/9/30.
 */
public abstract class AbstractServerPlugin<R> extends AbstractPlugin{

    public abstract R server(IParameter parameter);

}
