package org.digitalmind.buildingblocks.core.spel.service;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface SpelService {

    public EvaluationContext getContext(Object payload, Map<String, Object> variables);

    public Object getValue(String expression, EvaluationContext context) throws ExecutionException;

    public Object getValue(Expression expression, EvaluationContext context);

    public Object getValueNewTransaction(String expression, EvaluationContext context) throws ExecutionException;

    public void setValue(String expression, EvaluationContext context, Object value) throws ExecutionException;

    public void setValue(Expression expression, EvaluationContext context, Object value);

    public void evictCache();
}
