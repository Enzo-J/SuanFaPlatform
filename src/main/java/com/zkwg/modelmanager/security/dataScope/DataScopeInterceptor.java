package com.zkwg.modelmanager.security.dataScope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.zkwg.modelmanager.core.BaseContextHandler;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.util.SelectUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;


/**
 * mybatis 数据权限拦截器
 * <p>
 * <p>
 * 1，全部：没有createUser权限
 * 2，本级：当前用户的orgId
 * 3，本级以及子级
 * 4，自定义：
 * 5，个人：createUser = 1
 *
 * @author zuihou
 * @date 2019/2/1
 */
@Slf4j
@AllArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

    final private Function<Integer, Map<String, Object>> function;

    @Override
    @SneakyThrows
    public Object intercept(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();
        Object parameterObject = boundSql.getParameterObject();

        //查找参数中包含DataScope类型的参数
        DataScope dataScope = findDataScopeObject(parameterObject);

        if (dataScope == null) {
            return invocation.proceed();
        }
        String scopeName = dataScope.getScopeName();
        String selfScopeName = dataScope.getSelfScopeName();
        Integer userId = dataScope.getUserId() == null ? BaseContextHandler.getUserId() : dataScope.getUserId();
        List<Integer> orgIds = dataScope.getOrgIds();
        DataScopeType dsType = DataScopeType.SELF;
        if (CollectionUtil.isEmpty(orgIds)) {
            //查询当前用户的 角色 最小权限
            //userId
            //dsType orgIds
            Map<String, Object> result = function.apply(userId);
            if (result == null) {
                return invocation.proceed();
            }

            Integer type = (Integer) result.get("dsType");
            dsType = DataScopeType.get(type);
            orgIds = (List<Integer>) result.get("orgIds");
        }

        //查全部
        if (DataScopeType.ALL.eq(dsType)) {
            return invocation.proceed();
        }
        //查个人
        if (DataScopeType.SELF.eq(dsType)) {

            if(SqlCommandType.SELECT == mappedStatement.getSqlCommandType()) {

                Select select = (Select) CCJSqlParserUtil.parse(originalSql);
                //
                FromItem fromItem = ((PlainSelect) select.getSelectBody()).getFromItem();
                //
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column(getAliasColumn(fromItem, selfScopeName)));
                equalsTo.setRightExpression(new Column(userId+""));
                //
                Expression where =((PlainSelect) select.getSelectBody()).getWhere();
                ((PlainSelect) select.getSelectBody()).setWhere(builderExpression(equalsTo, where));
                //
                originalSql = select.toString();

//                ((PlainSelect) select.getSelectBody()).getWhere().accept(new ExpressionVisitorAdapter() {
//                    @Override
//                    public void visit(Column column) {
//                        column.setColumnName(column.getColumnName().replace("_", ""));
//                    }
//                });

            }
//            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + selfScopeName + " = " + userId;
        }
        //查其他
        else if (StrUtil.isNotBlank(scopeName) && CollUtil.isNotEmpty(orgIds)) {
            String join = CollectionUtil.join(orgIds, ",");
//            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeName + " in (" + join + ")";

        }

        metaObject.setValue("delegate.boundSql.sql", originalSql);
        return invocation.proceed();

    }

    protected String getAliasColumn(FromItem fromItem, String selfScopeName) {

        Alias alias = fromItem.getAlias();

        return alias.toString().replace("AS", "") + "." + selfScopeName;
    }

    protected Expression builderExpression(EqualsTo equalsTo, Expression currentExpression) {

        if (currentExpression == null) {
            return equalsTo;
        } else {
            if (currentExpression instanceof BinaryExpression) {
                BinaryExpression binaryExpression = (BinaryExpression)currentExpression;
//                this.doExpression(binaryExpression.getLeftExpression());
//                this.doExpression(binaryExpression.getRightExpression());
            } else if (currentExpression instanceof InExpression) {
                InExpression inExp = (InExpression)currentExpression;
                ItemsList rightItems = inExp.getRightItemsList();
                if (rightItems instanceof SubSelect) {
//                    this.processSelectBody(((SubSelect)rightItems).getSelectBody());
                }
            }

            return currentExpression instanceof OrExpression ? new AndExpression(new Parenthesis(currentExpression), equalsTo) : new AndExpression(currentExpression, equalsTo);
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 查找参数是否包括DataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    private DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }

}
