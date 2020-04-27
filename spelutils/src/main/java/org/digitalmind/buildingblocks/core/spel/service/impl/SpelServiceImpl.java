package org.digitalmind.buildingblocks.core.spel.service.impl;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.digitalmind.buildingblocks.core.spel.service.SpelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class SpelServiceImpl implements SpelService {

    private final ExpressionParser parser;
    private final ApplicationContext applicationContext;
    private final BeanFactoryResolver beanFactoryResolver;
    private final LoadingCache<String, Expression> cacheExpressionParser;
    private final CacheLoader<String, Expression> loaderExpressionParser;

    @Autowired
    public SpelServiceImpl(ApplicationContext applicationContext) {
        this.parser = new SpelExpressionParser();
        this.applicationContext = applicationContext;
        this.beanFactoryResolver = new BeanFactoryResolver(applicationContext);
        this.loaderExpressionParser = new CacheLoader<String, Expression>() {
            @Override
            public Expression load(String expression) {
                return parser.parseExpression(expression);
            }
        };
        this.cacheExpressionParser = CacheBuilder.from("").build(loaderExpressionParser);

    }

    public EvaluationContext getContext(Object rootObject, Map<String, Object> variables) {
        StandardEvaluationContext context = new StandardEvaluationContext();
        if (rootObject != null) {
            context.setRootObject(rootObject);
        }
        if (variables != null) {
            context.setVariables(variables);
        }
        context.setBeanResolver(this.beanFactoryResolver);

        return context;
    }

    public Expression getExpression(String expression) throws ExecutionException {
        return this.cacheExpressionParser.get(expression);
    }

    @Override
    public Object getValue(String expression, EvaluationContext context) throws ExecutionException {
        Expression compiledExpression = getExpression(expression);
        return getValue(compiledExpression, context);
    }

    @Override
    public Object getValue(Expression expression, EvaluationContext context) {

        return expression.getValue(context);
    }

    @Override
    public void setValue(String expression, EvaluationContext context, Object value) throws ExecutionException {
        Expression compiledExpression = getExpression(expression);
        setValue(compiledExpression, context, value);
    }

    @Override
    public void setValue(Expression expression, EvaluationContext context, Object value) {
        expression.setValue(context, value);
    }

    @Override
    public void evictCache() {
        this.cacheExpressionParser.invalidateAll();
    }

}
