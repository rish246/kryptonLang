package com.Krypton.Syntax.PrimaryExpressions;

import com.Krypton.EvalResult;
import com.Krypton.ExpressionType;
import com.Krypton.Syntax.Expression;
import com.Krypton.Token;
import com.Krypton.Utilities.Environment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberAccessExpression extends Expression {
    public Token _memberName;
    public Expression _object;
    public List<String> _diagnostics = new ArrayList<>();
    public MemberAccessExpression(Expression object, Token memberName, int lineNumber) {
        super(ExpressionType.MemberAccessExpression, lineNumber);
        _object = object;
        _memberName = memberName;
    }

    public EvalResult evaluate(Environment env) throws Exception {
        try {
            String memberName = _memberName._lexeme;
            EvalResult objectRes = _object.evaluate(env);
            return getMemberFromObject(memberName, objectRes);
        } catch (Exception e) {
            _diagnostics.addAll(_object.getDiagnostics());
            _diagnostics.add(e.getMessage());
            throw e;
        }
    }
    /*
    write some tests for this too
     */

    private EvalResult getMemberFromObject(String memberName, EvalResult objectRes) {
        Map<String, EvalResult> objectMap = (HashMap) objectRes.getValue();
        EvalResult result = objectMap.get(memberName);
        if (result == null)
            result = objectMap.put(memberName, new EvalResult(null, "null"));
        return result;
    }

    public void prettyPrint(String indent) {
        System.out.println(_memberName._lexeme);
    }

    public List<String> getDiagnostics() {
        return _diagnostics;
    }

}
/*
def newNode(data, next) {
    return { data, next };
}

tail = newNode(3, null);
middle = newNode(2, tail);
head = newNode(1, middle);

def reverse(head) {
		[prev, cur] = [null, head];
		while (cur != null) {
		    print(cur.data);
			next = cur.next;
			cur.next = prev;
			prev = cur;
			cur = next;
		}
		return prev;
	}




 */