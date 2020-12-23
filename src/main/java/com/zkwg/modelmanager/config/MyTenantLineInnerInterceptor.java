package com.zkwg.modelmanager.config;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.zkwg.modelmanager.core.BaseContextHandler;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;

public class MyTenantLineInnerInterceptor extends TenantLineInnerInterceptor {

    private TenantLineHandler tenantLineHandler;

    public MyTenantLineInnerInterceptor(final TenantLineHandler tenantLineHandler) {
        super(tenantLineHandler);
        this.tenantLineHandler = tenantLineHandler;
    }

    protected Expression builderExpression(Expression currentExpression, Table table) {
        if(BaseContextHandler.getTenant() != null && BaseContextHandler.getTenant() == 1) return currentExpression;
        if(!BaseContextHandler.getIgnoreTenantId()) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(this.getAliasColumn(table));
            equalsTo.setRightExpression(this.tenantLineHandler.getTenantId());
            if (currentExpression == null) {
                return equalsTo;
            } else {
                if (currentExpression instanceof BinaryExpression) {
                    BinaryExpression binaryExpression = (BinaryExpression)currentExpression;
                    this.doExpression(binaryExpression.getLeftExpression());
                    this.doExpression(binaryExpression.getRightExpression());
                } else if (currentExpression instanceof InExpression) {
                    InExpression inExp = (InExpression)currentExpression;
                    ItemsList rightItems = inExp.getRightItemsList();
                    if (rightItems instanceof SubSelect) {
                        this.processSelectBody(((SubSelect)rightItems).getSelectBody());
                    }
                }

                return currentExpression instanceof OrExpression ? new AndExpression(new Parenthesis(currentExpression), equalsTo) : new AndExpression(currentExpression, equalsTo);
            }
        }
        return currentExpression;
    }

    // 修改update语句
    protected void processUpdate(Update update, int index, Object obj) {
        Table table = update.getTable();
        if (!this.tenantLineHandler.ignoreTable(table.getName()) && !BaseContextHandler.getIgnoreTenantId()) {
            update.setWhere(this.andExpression(table, update.getWhere()));
        }
    }

}
