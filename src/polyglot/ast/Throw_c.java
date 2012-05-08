/*******************************************************************************
 * This file is part of the Polyglot extensible compiler framework.
 *
 * Copyright (c) 2000-2008 Polyglot project group, Cornell University
 * Copyright (c) 2006-2008 IBM Corporation
 * All rights reserved.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This program and the accompanying materials are made available under
 * the terms of the Lesser GNU Public License v2.0 which accompanies this
 * distribution.
 * 
 * The development of the Polyglot project has been supported by a
 * number of funding sources, including DARPA Contract F30602-99-1-0533,
 * monitored by USAF Rome Laboratory, ONR Grant N00014-01-1-0968, NSF
 * Grants CNS-0208642, CNS-0430161, and CCF-0133302, an Alfred P. Sloan
 * Research Fellowship, and an Intel Research Ph.D. Fellowship.
 *
 * See README for contributors.
 ******************************************************************************/

package polyglot.ast;

import polyglot.ast.*;

import polyglot.util.*;
import polyglot.types.*;
import polyglot.visit.*;
import java.util.*;

/**
 * A <code>Throw</code> is an immutable representation of a <code>throw</code>
 * statement. Such a statement contains a single <code>Expr</code> which
 * evaluates to the object being thrown.
 */
public class Throw_c extends Stmt_c implements Throw {
	protected Expr expr;

	public Throw_c(Position pos, Expr expr) {
		super(pos);
		assert (expr != null);
		this.expr = expr;
	}

	/** Get the expression to throw. */
	public Expr expr() {
		return this.expr;
	}

	/** Set the expression to throw. */
	public Throw expr(Expr expr) {
		Throw_c n = (Throw_c) copy();
		n.expr = expr;
		return n;
	}

	/** Reconstruct the statement. */
	protected Throw_c reconstruct(Expr expr) {
		if (expr != this.expr) {
			Throw_c n = (Throw_c) copy();
			n.expr = expr;
			return n;
		}

		return this;
	}

	/** Visit the children of the statement. */
	public Node visitChildren(NodeVisitor v) {
		Expr expr = (Expr) visitChild(this.expr, v);
		return reconstruct(expr);
	}

	/** Type check the statement. */
	public Node typeCheck(TypeChecker tc) throws SemanticException {
		if (!expr.type().isThrowable()) {
			throw new SemanticException("Can only throw subclasses of \""
					+ tc.typeSystem().Throwable() + "\".", expr.position());
		}

		return this;
	}

	public Type childExpectedType(Expr child, AscriptionVisitor av) {
		TypeSystem ts = av.typeSystem();

		if (child == expr) {
			return ts.Throwable();
		}

		return child.type();
	}

	public String toString() {
		return "throw " + expr + ";";
	}

	/** Write the statement to an output file. */
	public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
		w.write("throw ");
		print(expr, w, tr);
		w.write(";");
	}

	public Term firstChild() {
		return expr;
	}

	public List acceptCFG(CFGBuilder v, List succs) {
		v.visitCFG(expr, this, EXIT);

		// Throw edges will be handled by visitor.
		return Collections.EMPTY_LIST;
	}

	public List throwTypes(TypeSystem ts) {
		// if the exception that a throw statement is given to throw is null,
		// then a NullPointerException will be thrown.
		return CollectionUtil.list(expr.type(), ts.NullPointerException());
	}

	public Node copy(NodeFactory nf) {
		return nf.Throw(this.position, this.expr);
	}

}
