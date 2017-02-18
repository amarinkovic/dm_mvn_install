package pro.documentum.persistence.common.query.expression.functions;

import org.datanucleus.query.expression.InvokeExpression;

import pro.documentum.persistence.common.query.IDQLEvaluator;
import pro.documentum.persistence.common.query.IInvokeEvaluator;
import pro.documentum.persistence.common.query.expression.DQLExpression;

/**
 * @author Andrey B. Panfilov <andrey@panfilov.tel>
 */
public final class DQLUpper extends DQLFunc {

    public static final String FUNC = "UPPER";

    public static final String RIGHT_FUNC = "toUpperCase";

    private DQLUpper(final String field) {
        super(String.format("%s(%s)", FUNC, field));
    }

    private static DQLExpression evaluate(final InvokeExpression invokeExpr,
            final IDQLEvaluator<?> evaluator) {
        DQLExpression expression = processField(invokeExpr, evaluator, FUNC,
                RIGHT_FUNC);
        if (expression == null) {
            expression = processLiteralOrParameter(invokeExpr, evaluator, FUNC,
                    RIGHT_FUNC);
        }
        if (expression == null) {
            return null;
        }
        return new DQLUpper(expression.getText());
    }

    public static IInvokeEvaluator getInvokeEvaluator() {
        return new IInvokeEvaluator() {
            @Override
            public DQLExpression evaluate(final InvokeExpression expression,
                    final IDQLEvaluator<?> evaluator) {
                return DQLUpper.evaluate(expression, evaluator);
            }
        };
    }

}
