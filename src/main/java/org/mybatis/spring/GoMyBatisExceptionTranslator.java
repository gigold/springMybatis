/**
 * Title: GoMyBatisExceptionTranslator.java<br/>
 * Description: <br/>
 * Copyright: Copyright (c) 2016<br/>
 * Company: gigold<br/>
 *
 */
package org.mybatis.spring;

/**
 * Title: GoMyBatisExceptionTranslator<br/>
 * Description: <br/>
 * Company: gigold<br/>
 * @author Devin
 * @date 2016年1月31日下午5:24:23
 *
 */
import com.gigold.pay.framework.core.exception.AbortException;
import com.gigold.pay.framework.core.exception.AbortExceptionLogger;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class GoMyBatisExceptionTranslator
  implements PersistenceExceptionTranslator, GOExceptionTranslator
{
  private final DataSource dataSource;
  private static AbortExceptionLogger logger = new AbortExceptionLogger();
  private SQLExceptionTranslator exceptionTranslator;

  public GoMyBatisExceptionTranslator(DataSource dataSource, boolean exceptionTranslatorLazyInit)
  {
    this.dataSource = dataSource;

    if (!exceptionTranslatorLazyInit)
      initExceptionTranslator();
  }

  public org.springframework.dao.DataAccessException translateExceptionIfPossible(RuntimeException e)
  {
    if ((e instanceof PersistenceException))
    {
      if ((e.getCause() instanceof PersistenceException)) {
        e = (PersistenceException)e.getCause();
      }
      if ((e.getCause() instanceof SQLException)) {
        initExceptionTranslator();
        return this.exceptionTranslator.translate(e.getMessage() + "\n", null, (SQLException)e.getCause());
      }
      return new MyBatisSystemException(e);
    }

    return null;
  }

  private synchronized void initExceptionTranslator()
  {
    if (this.exceptionTranslator == null)
      this.exceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
  }

  private com.gigold.pay.framework.core.exception.DataAccessException wrapException(RuntimeException exception)
  {
    AbortException aborEx = null;
    if (exception != null) {
      aborEx = new AbortException("89999", "mybatis has exception", getClass(), exception);
    }
    else {
      aborEx = new AbortException("89999", "mybatis has exception2", getClass());
    }

    return aborEx;
  }

  public com.gigold.pay.framework.core.exception.DataAccessException translateException(RuntimeException ex)
  {
    if ((ex instanceof PersistenceException))
    {
      if ((ex.getCause() instanceof PersistenceException)) {
        ex = (PersistenceException)ex.getCause();
      }
      if ((ex.getCause() instanceof SQLException)) {
        initExceptionTranslator();
        return wrapException(this.exceptionTranslator.translate(ex.getMessage() + "\n", null, (SQLException)ex.getCause()));
      }
      return wrapException(new MyBatisSystemException(ex));
    }

    return wrapException(null);
  }
}