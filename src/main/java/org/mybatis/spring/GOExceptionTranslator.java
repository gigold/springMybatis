/**
 * Title: GOExceptionTranslator.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2016<br/>
 * Company: gigold<br/>
 *
 */
package org.mybatis.spring;
import com.gigold.pay.framework.core.exception.DataAccessException;
/**
 * Title: GOExceptionTranslator<br/>
 * Description: <br/>
 * Company: gigold<br/>
 * @author Devin
 * @date 2016年1月31日下午5:19:20
 *
 */
public abstract interface GOExceptionTranslator
{
  public abstract DataAccessException translateException(RuntimeException paramRuntimeException);
}
