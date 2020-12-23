package com.zkwg.modelmanager.exception;

import com.zkwg.modelmanager.exception.base.BaseException;
import com.zkwg.modelmanager.exception.base.BusinessException;

import java.text.MessageFormat;

public interface BusinessExceptionAssert extends IResponseEnum, Assert {

    @Override
    default BaseException newException() {

        String msg = this.getMessage();

        return new BusinessException(this, new Object[]{},  msg);
    }

    @Override
    default BaseException newException(Object... args) {

        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {

        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg, t);
    }

}